package com.example.nammametromvvm.ui.homescreen.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@SuppressLint("CustomSplashScreen")
class HomeActivityViewModelFactory @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeActivityViewModel(
            dataStoreRepository
        ) as T
    }
}