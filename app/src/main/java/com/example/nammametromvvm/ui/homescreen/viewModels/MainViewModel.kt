package com.example.nammametromvvm.ui.homescreen.viewModels


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    fun setUpTheme() {
        // viewModelScope.launch { handleTheme.init() }
    }
}