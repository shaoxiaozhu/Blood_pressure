package com.example.bpmonitor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bpmonitor.models.Measurement;

import java.util.Date;
import java.util.List;

@Dao
public interface MeasurementDao {
    @Insert
    long insert(Measurement measurement);

    @Update
    void update(Measurement measurement);

    @Delete
    void delete(Measurement measurement);

    @Query("SELECT * FROM measurements WHERE userId = :userId ORDER BY measureTime DESC")
    LiveData<List<Measurement>> getAllMeasurements(long userId);

    @Query("SELECT * FROM measurements WHERE userId = :userId AND measureTime BETWEEN :startDate AND :endDate ORDER BY measureTime DESC")
    LiveData<List<Measurement>> getMeasurementsByDateRange(long userId, Date startDate, Date endDate);

    @Query("SELECT * FROM measurements WHERE id = :id")
    LiveData<Measurement> getMeasurementById(long id);

    @Query("SELECT AVG(systolic) as avgSystolic, AVG(diastolic) as avgDiastolic, AVG(heartRate) as avgHeartRate FROM measurements WHERE userId = :userId AND measureTime BETWEEN :startDate AND :endDate")
    LiveData<MeasurementStats> getAverageStats(long userId, Date startDate, Date endDate);

    class MeasurementStats {
        public double avgSystolic;
        public double avgDiastolic;
        public double avgHeartRate;
    }
}