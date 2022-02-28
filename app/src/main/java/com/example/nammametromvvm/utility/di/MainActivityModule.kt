package com.example.nammametromvvm.utility.di

import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.homescreen.viewModels.MainViewModelFactory
import com.example.nammametromvvm.utility.theme.HandleTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ViewModelComponent::class)
object MainActivityModule {
    @Provides
    @ActivityScoped
    fun provideHomeScreenViewModelFactory(
        handleTheme: HandleTheme
    ) = MainViewModelFactory(
        handleTheme
    )

    @Provides
    @ActivityScoped
    fun provideHandleTheme(dataStoreRepository: DataStoreRepository) =
        HandleTheme(dataStoreRepository)
}