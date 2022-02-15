package com.example.nammametromvvm.ui.homescreen.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.nammametromvvm.utility.UserRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsViewModel(
    application: Application,
    private val userRegistration: UserRegistration,
) : AndroidViewModel(application) {

    suspend fun logOut(context: Context): Boolean =
        withContext(Dispatchers.IO) {
            Log.d("test11", "logOut: ")
            userRegistration.userLoggedOut(context) }
}