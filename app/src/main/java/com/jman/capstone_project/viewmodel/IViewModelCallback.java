package com.jman.capstone_project.viewmodel;

import com.jman.capstone_project.remoteDataSource.models.WeatherInfoModel;

public interface IViewModelCallback {

    void passToViewModel(WeatherInfoModel weatherInfoModel);
}
