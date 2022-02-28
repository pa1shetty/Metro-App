package com.example.nammametromvvm.ui.homescreen.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammametromvvm.utility.theme.HandleTheme
import kotlinx.coroutines.launch

class MainViewModel(val handleTheme: HandleTheme) : ViewModel() {

    fun setUpTheme() {
        viewModelScope.launch { handleTheme.init() }
    }
}