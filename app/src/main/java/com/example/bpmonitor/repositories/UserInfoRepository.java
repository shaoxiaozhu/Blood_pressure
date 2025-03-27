package com.example.bpmonitor.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bpmonitor.database.AppDatabase;
import com.example.bpmonitor.database.UserInfoDao;
import com.example.bpmonitor.models.UserInfo;

public class UserInfoRepository {
    private UserInfoDao userInfoDao;
    private static UserInfoRepository INSTANCE;

    private UserInfoRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userInfoDao = db.userInfoDao();
    }

    public static UserInfoRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UserInfoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserInfoRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    public void insertUserInfo(UserInfo userInfo, OnUserInfoSavedCallback callback) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return userInfoDao.insert(userInfo);
            }

            @Override
            protected void onPostExecute(Long id) {
                if (callback != null) {
                    callback.onUserInfoSaved(id);
                }
            }
        }.execute();
    }

    public void updateUserInfo(UserInfo userInfo, OnUserInfoUpdatedCallback callback) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userInfoDao.update(userInfo);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (callback != null) {
                    callback.onUserInfoUpdated();
                }
            }
        }.execute();
    }

    public void getUserInfo(long id, OnUserInfoLoadedCallback callback) {
        new AsyncTask<Void, Void, UserInfo>() {
            @Override
            protected UserInfo doInBackground(Void... voids) {
                return userInfoDao.getUserInfoById(id);
            }

            @Override
            protected void onPostExecute(UserInfo userInfo) {
                if (callback != null) {
                    callback.onUserInfoLoaded(userInfo);
                }
            }
        }.execute();
    }

    public interface OnUserInfoSavedCallback {
        void onUserInfoSaved(long id);
    }

    public interface OnUserInfoUpdatedCallback {
        void onUserInfoUpdated();
    }

    public interface OnUserInfoLoadedCallback {
        void onUserInfoLoaded(UserInfo userInfo);
    }
}