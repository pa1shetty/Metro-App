package com.example.nammametromvvm.utility.di

import android.app.Application
import com.example.nammametromvvm.ui.homescreen.viewModels.SettingsViewModelFactory
import com.example.nammametromvvm.utility.UserRegistration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object SettingsScreenModule {
    @Provides
    @ActivityScoped
    fun provideSettingsScreenViewModelFactory(
        app: Application,
        userRegistration: UserRegistration,
    ) = SettingsViewModelFactory(
        app,
        userRegistration
    )
}