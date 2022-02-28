package com.example.nammametromvvm.utility.di

import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.homescreen.viewModels.HomeFragmentViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(ViewModelComponent::class)
object HomeScreenModule {
    @Provides
    @FragmentScoped
    fun provideHomeScreenViewModelFactory(
        dataStoreRepository: DataStoreRepository
    ) = HomeFragmentViewModelFactory(
        dataStoreRepository
    )
}