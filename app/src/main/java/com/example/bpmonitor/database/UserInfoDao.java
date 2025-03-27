package com.example.bpmonitor.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bpmonitor.models.UserInfo;

@Dao
public interface UserInfoDao {
    @Insert
    long insert(UserInfo userInfo);

    @Update
    void update(UserInfo userInfo);

    @Delete
    void delete(UserInfo userInfo);

    @Query("SELECT * FROM user_info WHERE id = :id")
    UserInfo getUserInfoById(long id);

    @Query("SELECT * FROM user_info LIMIT 1")
    UserInfo getFirstUserInfo();
}