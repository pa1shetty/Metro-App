package com.example.nammametromvvm.ui.homescreen.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
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