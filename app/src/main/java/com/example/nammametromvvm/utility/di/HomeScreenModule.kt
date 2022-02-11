package com.example.nammametromvvm.utility.di

import android.app.Application
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.homescreen.viewModels.HomeActivityViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object HomeScreenModule {
    @Provides
    @ActivityScoped
    fun provideHomeScreenViewModelFactory(
        app: Application,
        dataStoreRepository: DataStoreRepository
    ) = HomeActivityViewModelFactory(
        app,
        dataStoreRepository
    )
}