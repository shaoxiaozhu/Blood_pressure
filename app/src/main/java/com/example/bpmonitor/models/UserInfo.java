package com.example.bpmonitor.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_info")
public class UserInfo {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int age;
    private String gender;
    private float height;
    private float weight;
    private float glucose;
    private String province;
    private String ethnicity;
    private boolean hasHypertension;
    private boolean hasHyperlipidemia;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public float getGlucose() { return glucose; }
    public void setGlucose(float glucose) { this.glucose = glucose; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getEthnicity() { return ethnicity; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }

    public boolean isHasHypertension() { return hasHypertension; }
    public void setHasHypertension(boolean hasHypertension) { this.hasHypertension = hasHypertension; }

    public boolean isHasHyperlipidemia() { return hasHyperlipidemia; }
    public void setHasHyperlipidemia(boolean hasHyperlipidemia) { this.hasHyperlipidemia = hasHyperlipidemia; }
}