package com.example.nammametromvvm.utility.di

import android.app.Application
import com.example.mymvvmsample.data.repositaries.DataBaseRepository
import com.example.mymvvmsample.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.splashscreen.SplashScreenViewModelFactory
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object SplashScreenModule {
    @Provides
    @ActivityScoped
    fun provideSplashScreenViewModelFactory(
        app: Application,
        networkRepository: NetworkRepository,
        dataStoreRepository: DataStoreRepository,
        dateMethods: DateMethods,
        loggerClass: LoggerClass,
        dataBaseRepository: DataBaseRepository,
        configurations: Configurations
    ) = SplashScreenViewModelFactory(
        app,
        networkRepository,
        dataStoreRepository,
        dateMethods,
        loggerClass,
        dataBaseRepository,
        configurations
    )
}