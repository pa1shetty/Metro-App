package com.example.nammametromvvm.ui.homescreen.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.utility.theme.HandleTheme
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(val handleTheme: HandleTheme) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(handleTheme) as T

    }
}