package com.example.bpmonitor.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bpmonitor.models.Measurement;
import com.example.bpmonitor.database.MeasurementDao;
import com.example.bpmonitor.database.AppDatabase;
import com.example.bpmonitor.database.MeasurementDao.MeasurementStats;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeasurementViewModel extends AndroidViewModel {
    private final MeasurementDao measurementDao;
    private final ExecutorService executorService;
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private long currentUserId = 1; // 临时使用固定用户ID，后续需要从登录系统获取

    public MeasurementViewModel(Application application) {
        super(application);
        measurementDao = AppDatabase.getInstance(application).measurementDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void saveMeasurement(Measurement measurement) {
        if (!validateMeasurement(measurement)) {
            return;
        }

        measurement.setUserId(currentUserId);
        measurement.setMeasureTime(new Date());

        executorService.execute(() -> {
            try {
                measurementDao.insert(measurement);
            } catch (Exception e) {
                errorMessage.postValue("保存测量数据失败：" + e.getMessage());
            }
        });
    }

    public void updateMeasurement(Measurement measurement) {
        if (!validateMeasurement(measurement)) {
            return;
        }

        executorService.execute(() -> {
            try {
                measurementDao.update(measurement);
            } catch (Exception e) {
                errorMessage.postValue("更新测量数据失败：" + e.getMessage());
            }
        });
    }

    public void deleteMeasurement(Measurement measurement) {
        executorService.execute(() -> {
            try {
                measurementDao.delete(measurement);
            } catch (Exception e) {
                errorMessage.postValue("删除测量数据失败：" + e.getMessage());
            }
        });
    }

    public LiveData<List<Measurement>> getAllMeasurements() {
        return measurementDao.getAllMeasurements(currentUserId);
    }

    public LiveData<List<Measurement>> getMeasurementsByDateRange(Date startDate, Date endDate) {
        return measurementDao.getMeasurementsByDateRange(currentUserId, startDate, endDate);
    }

    public LiveData<MeasurementStats> getAverageStats(Date startDate, Date endDate) {
        return measurementDao.getAverageStats(currentUserId, startDate, endDate);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private boolean validateMeasurement(Measurement measurement) {
        if (measurement.getSystolic() < 60 || measurement.getSystolic() > 250) {
            errorMessage.setValue("收缩压数值异常（60-250mmHg）");
            return false;
        }
        if (measurement.getDiastolic() < 40 || measurement.getDiastolic() > 150) {
            errorMessage.setValue("舒张压数值异常（40-150mmHg）");
            return false;
        }
        if (measurement.getHeartRate() < 40 || measurement.getHeartRate() > 200) {
            errorMessage.setValue("心率数值异常（40-200次/分）");
            return false;
        }
        return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}