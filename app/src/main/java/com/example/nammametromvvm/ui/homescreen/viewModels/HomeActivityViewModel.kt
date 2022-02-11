package com.example.nammametromvvm.ui.homescreen.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mymvvmsample.data.repositaries.DataBaseRepository
import com.example.mymvvmsample.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HomeActivityViewModel(
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
) : AndroidViewModel(application) {

    suspend fun getUserName(): Flow<String> =
        withContext(Dispatchers.IO) { dataStoreRepository.getUserNameFlow() }
}