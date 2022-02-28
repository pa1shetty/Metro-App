package com.example.nammametromvvm.utility.di

import android.app.Application
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.homescreen.viewModels.SettingsViewModelFactory
import com.example.nammametromvvm.utility.UserRegistration
import com.example.nammametromvvm.utility.language.LocaleManager
import com.example.nammametromvvm.utility.theme.HandleTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(ViewModelComponent::class)
object SettingsScreenModule {
    @Provides
    @FragmentScoped
    fun provideSettingsScreenViewModelFactory(
        provideApplication: Application,
        userRegistration: UserRegistration,
        dataStoreRepository: DataStoreRepository,
        handleTheme: HandleTheme,
        localeManager: LocaleManager
    ) = SettingsViewModelFactory(
        provideApplication,
        userRegistration,
        dataStoreRepository,
        handleTheme,
        localeManager
    )


}