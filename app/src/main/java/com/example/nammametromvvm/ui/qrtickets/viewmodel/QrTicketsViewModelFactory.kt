package com.example.nammametromvvm.ui.qrtickets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.GenericMethods
import com.example.nammametromvvm.utility.UserRegistration
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import javax.inject.Inject

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */

@Suppress("UNCHECKED_CAST")
class QrTicketsViewModelFactory @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val configurations: Configurations,
    private val loggerClass: LoggerClass,
    private val userRegistration: UserRegistration,
    private val genericMethods: GenericMethods,
    private val dataBaseRepository: DataBaseRepository,
    private val dateMethods: DateMethods
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QrTicketsViewModel(
            networkRepository,
            dataStoreRepository,
            configurations,
            loggerClass,
            userRegistration,
            genericMethods,
            dataBaseRepository,
            dateMethods
        ) as T
    }
}