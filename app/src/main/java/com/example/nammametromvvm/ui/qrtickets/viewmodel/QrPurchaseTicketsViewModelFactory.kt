package com.example.nammametromvvm.ui.qrtickets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import javax.inject.Inject

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */

@Suppress("UNCHECKED_CAST")
class QrPurchaseTicketsViewModelFactory @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val configurations: Configurations,
    private val loggerClass: LoggerClass,
    private val dataBaseRepository: DataBaseRepository,
    private val dateMethods: DateMethods
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QrPurchaseTicketsViewModel(
            networkRepository,
            configurations,
            loggerClass,
            dataBaseRepository,
            dateMethods
        ) as T
    }
}