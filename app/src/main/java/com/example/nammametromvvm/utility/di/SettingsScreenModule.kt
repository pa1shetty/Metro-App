package com.example.nammametromvvm.utility.di

import com.example.nammametromvvm.ui.homescreen.viewModels.SettingsViewModelFactory
import com.example.nammametromvvm.utility.UserRegistration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SettingsScreenModule {
    @Provides
    @ViewModelScoped
    fun provideSettingsScreenViewModelFactory(
        userRegistration: UserRegistration,
    ) = SettingsViewModelFactory(
        userRegistration
    )
}