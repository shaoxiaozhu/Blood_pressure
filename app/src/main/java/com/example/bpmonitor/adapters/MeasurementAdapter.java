package com.example.bpmonitor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpmonitor.R;
import com.example.bpmonitor.models.Measurement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.ViewHolder> {
    private List<Measurement> measurements = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_measurement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Measurement measurement = measurements.get(position);
        holder.tvTime.setText(dateFormat.format(measurement.getMeasureTime()));
        holder.tvSystolic.setText(String.format(Locale.getDefault(), "%d", measurement.getSystolic()));
        holder.tvDiastolic.setText(String.format(Locale.getDefault(), "%d", measurement.getDiastolic()));
        holder.tvHeartRate.setText(String.format(Locale.getDefault(), "%d", measurement.getHeartRate()));
        holder.tvNote.setText(measurement.getNote());
    }

    @Override
    public int getItemCount() {
        return measurements.size();
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvSystolic, tvDiastolic, tvHeartRate, tvNote;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSystolic = itemView.findViewById(R.id.tvSystolic);
            tvDiastolic = itemView.findViewById(R.id.tvDiastolic);
            tvHeartRate = itemView.findViewById(R.id.tvHeartRate);
            tvNote = itemView.findViewById(R.id.tvNote);
        }
    }
}