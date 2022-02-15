package com.example.nammametromvvm.utility.di

import android.app.Application
import com.example.mymvvmsample.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.login.viewModel.LoginViewModelFactory
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.UserRegistration
import com.example.nammametromvvm.utility.logs.LoggerClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object LoginScreenModule {
    @Provides
    @ActivityScoped
    fun provideLoginScreenViewModelFactory(
        app: Application,
        networkRepository: NetworkRepository,
        dataStoreRepository: DataStoreRepository,
        configurations: Configurations,
        loggerClass: LoggerClass,
        userRegistration: UserRegistration,
    ) = LoginViewModelFactory(
        app,
        networkRepository,
        dataStoreRepository,
        configurations,
        loggerClass,
        userRegistration
    )
}