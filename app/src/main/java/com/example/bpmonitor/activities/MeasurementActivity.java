package com.example.bpmonitor.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpmonitor.R;
import com.example.bpmonitor.models.Measurement;
import com.example.bpmonitor.viewmodels.MeasurementViewModel;

import java.util.Date;

public class MeasurementActivity extends AppCompatActivity {
    private EditText etSystolic, etDiastolic, etHeartRate, etNote;
    private RecyclerView rvMeasurements;
    private MeasurementViewModel viewModel;
    private MeasurementAdapter adapter;
    private LineChart lineChart;
    private Button btnStartDate, btnEndDate;
    private Date startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);

        viewModel = new ViewModelProvider(this).get(MeasurementViewModel.class);

        initViews();
        setupObservers();
    }

    private void initViews() {
        etSystolic = findViewById(R.id.etSystolic);
        etDiastolic = findViewById(R.id.etDiastolic);
        etHeartRate = findViewById(R.id.etHeartRate);
        etNote = findViewById(R.id.etNote);
        rvMeasurements = findViewById(R.id.rvMeasurements);
        lineChart = findViewById(R.id.lineChart);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);

        // 初始化RecyclerView
        rvMeasurements.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeasurementAdapter();
        rvMeasurements.setAdapter(adapter);

        // 初始化图表
        setupLineChart();

        // 设置按钮点击事件
        findViewById(R.id.btnSave).setOnClickListener(v -> saveMeasurement());
        btnStartDate.setOnClickListener(v -> showDatePicker(true));
        btnEndDate.setOnClickListener(v -> showDatePicker(false));
    }

    private void setupObservers() {
        viewModel.getAllMeasurements().observe(this, measurements -> {
            adapter.setMeasurements(measurements);
            updateLineChart(measurements);
        });

        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        // 观察日期范围内的统计数据
        if (startDate != null && endDate != null) {
            viewModel.getAverageStats(startDate, endDate).observe(this, stats -> {
                if (stats != null) {
                    String averageInfo = String.format(Locale.getDefault(),
                            "平均值 - 收缩压: %.1f, 舒张压: %.1f, 心率: %.1f",
                            stats.avgSystolic, stats.avgDiastolic, stats.avgHeartRate);
                    Toast.makeText(this, averageInfo, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void saveMeasurement() {
        try {
            Measurement measurement = new Measurement();
            measurement.setSystolic(Integer.parseInt(etSystolic.getText().toString()));
            measurement.setDiastolic(Integer.parseInt(etDiastolic.getText().toString()));
            measurement.setHeartRate(Integer.parseInt(etHeartRate.getText().toString()));
            measurement.setNote(etNote.getText().toString());

            viewModel.saveMeasurement(measurement);
            clearInputs();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数值", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        etSystolic.setText("");
        etDiastolic.setText("");
        etHeartRate.setText("");
        etNote.setText("");
        etSystolic.requestFocus();
    }

    private void setupLineChart() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
            @Override
            public String getFormattedValue(float value) {
                return dateFormat.format(new Date((long) value));
            }
        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisMinimum(0f);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setForm(Legend.LegendForm.LINE);
    }

    private void updateLineChart(List<Measurement> measurements) {
        ArrayList<Entry> systolicEntries = new ArrayList<>();
        ArrayList<Entry> diastolicEntries = new ArrayList<>();
        ArrayList<Entry> heartRateEntries = new ArrayList<>();

        for (Measurement measurement : measurements) {
            float timeInMillis = measurement.getMeasureTime().getTime();
            systolicEntries.add(new Entry(timeInMillis, measurement.getSystolic()));
            diastolicEntries.add(new Entry(timeInMillis, measurement.getDiastolic()));
            heartRateEntries.add(new Entry(timeInMillis, measurement.getHeartRate()));
        }

        LineDataSet systolicSet = new LineDataSet(systolicEntries, "收缩压");
        systolicSet.setColor(Color.RED);
        systolicSet.setCircleColor(Color.RED);

        LineDataSet diastolicSet = new LineDataSet(diastolicEntries, "舒张压");
        diastolicSet.setColor(Color.BLUE);
        diastolicSet.setCircleColor(Color.BLUE);

        LineDataSet heartRateSet = new LineDataSet(heartRateEntries, "心率");
        heartRateSet.setColor(Color.GREEN);
        heartRateSet.setCircleColor(Color.GREEN);

        LineData lineData = new LineData(systolicSet, diastolicSet, heartRateSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    if (isStartDate) {
                        startDate = calendar.getTime();
                        btnStartDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startDate));
                    } else {
                        endDate = calendar.getTime();
                        btnEndDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(endDate));
                    }
                    if (startDate != null && endDate != null) {
                        viewModel.getMeasurementsByDateRange(startDate, endDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}