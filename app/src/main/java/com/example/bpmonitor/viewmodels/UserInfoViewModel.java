package com.example.bpmonitor.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bpmonitor.models.UserInfo;
import com.example.bpmonitor.repositories.UserInfoRepository;

public class UserInfoViewModel extends AndroidViewModel {
    private UserInfoRepository repository;
    private MutableLiveData<UserInfo> userInfo;
    private MutableLiveData<String> errorMessage;

    public UserInfoViewModel(Application application) {
        super(application);
        repository = UserInfoRepository.getInstance(application);
        userInfo = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<UserInfo> getUserInfo() {
        return userInfo;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadUserInfo(long id) {
        repository.getUserInfo(id, new UserInfoRepository.OnUserInfoLoadedCallback() {
            @Override
            public void onUserInfoLoaded(UserInfo info) {
                userInfo.setValue(info);
            }
        });
    }

    public void saveUserInfo(UserInfo info) {
        if (!validateUserInfo(info)) {
            return;
        }

        if (info.getId() == 0) {
            repository.insertUserInfo(info, new UserInfoRepository.OnUserInfoSavedCallback() {
                @Override
                public void onUserInfoSaved(long id) {
                    info.setId(id);
                    userInfo.setValue(info);
                }
            });
        } else {
            repository.updateUserInfo(info, new UserInfoRepository.OnUserInfoUpdatedCallback() {
                @Override
                public void onUserInfoUpdated() {
                    userInfo.setValue(info);
                }
            });
        }
    }

    private boolean validateUserInfo(UserInfo info) {
        if (info.getAge() <= 0 || info.getAge() > 120) {
            errorMessage.setValue("年龄必须在1-120岁之间");
            return false;
        }
        if (info.getHeight() <= 0 || info.getHeight() > 250) {
            errorMessage.setValue("身高必须在1-250厘米之间");
            return false;
        }
        if (info.getWeight() <= 0 || info.getWeight() > 300) {
            errorMessage.setValue("体重必须在1-300千克之间");
            return false;
        }
        if (info.getGlucose() < 0 || info.getGlucose() > 30) {
            errorMessage.setValue("血糖值必须在0-30mmol/L之间");
            return false;
        }
        return true;
    }
}