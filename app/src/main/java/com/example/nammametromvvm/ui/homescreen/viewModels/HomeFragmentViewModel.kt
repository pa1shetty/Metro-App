package com.example.nammametromvvm.ui.homescreen.viewModels

import androidx.lifecycle.ViewModel
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HomeFragmentViewModel(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    suspend fun getUserName(): Flow<String> =
        withContext(Dispatchers.IO) { dataStoreRepository.getUserNameFlow() }

    suspend fun isUserLoggedIn(): Boolean =
        withContext(Dispatchers.IO) { dataStoreRepository.isUserLoggedIn() }
}