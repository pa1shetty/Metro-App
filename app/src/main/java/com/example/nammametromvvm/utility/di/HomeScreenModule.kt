package com.example.nammametromvvm.utility.di

import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.homescreen.viewModels.HomeActivityViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object HomeScreenModule {
    @Provides
    @ViewModelScoped
    fun provideHomeScreenViewModelFactory(
        dataStoreRepository: DataStoreRepository
    ) = HomeActivityViewModelFactory(
        dataStoreRepository
    )
}