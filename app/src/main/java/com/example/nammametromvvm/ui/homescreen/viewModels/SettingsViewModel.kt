package com.example.nammametromvvm.ui.homescreen.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.nammametromvvm.utility.UserRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class SettingsViewModel(
    private val userRegistration: UserRegistration,
) : ViewModel() {

    suspend fun logOut(context: Context): Boolean =
        withContext(Dispatchers.IO) {
            Log.d("test11", "logOut: ")
            userRegistration.userLoggedOut(context) }
}