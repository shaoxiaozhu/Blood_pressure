package com.example.bpmonitor.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "measurements")
public class Measurement {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private int systolic;      // 收缩压
    private int diastolic;     // 舒张压
    private int heartRate;     // 心率
    private Date measureTime;  // 测量时间
    private String note;       // 备注
    private long userId;       // 关联的用户ID

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getSystolic() { return systolic; }
    public void setSystolic(int systolic) { this.systolic = systolic; }

    public int getDiastolic() { return diastolic; }
    public void setDiastolic(int diastolic) { this.diastolic = diastolic; }

    public int getHeartRate() { return heartRate; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }

    public Date getMeasureTime() { return measureTime; }
    public void setMeasureTime(Date measureTime) { this.measureTime = measureTime; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
}