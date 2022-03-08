package com.example.nammametromvvm.utility.di

import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrTicketsViewModelFactory
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.GenericMethods
import com.example.nammametromvvm.utility.UserRegistration
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(ViewModelComponent::class)
object QrTicketsScreenModule {
    @Provides
    @FragmentScoped
    fun provideQrTicketViewModelFactory(
        networkRepository: NetworkRepository,
        dataStoreRepository: DataStoreRepository,
        configurations: Configurations,
        loggerClass: LoggerClass,
        userRegistration: UserRegistration,
        genericMethods: GenericMethods,
        dataBaseRepository: DataBaseRepository,
        dateMethods: DateMethods
    ) = QrTicketsViewModelFactory(
        networkRepository,
        dataStoreRepository,
        configurations,
        loggerClass,
        userRegistration,
        genericMethods,
        dataBaseRepository,
        dateMethods
    )
}