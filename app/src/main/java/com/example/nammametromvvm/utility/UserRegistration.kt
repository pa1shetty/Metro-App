package com.example.nammametromvvm.utility

import android.content.Context
import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.data.repositaries.network.responses.login.otpVerification.OtpVerificationData

class UserRegistration(
    private val dataBaseRepository: DataBaseRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun userLoggedIn(otpVerificationData: OtpVerificationData) {
        dataStoreRepository.saveCKey(otpVerificationData.cKey)
        dataStoreRepository.saveUserName(otpVerificationData.name)
        dataStoreRepository.saveUserEmail(otpVerificationData.email)
        dataStoreRepository.userLoggedIn()
    }

    suspend fun userLoggedOut(context: Context): Boolean {
        dataStoreRepository.clearDatastore()
        dataBaseRepository.clearDb(context)
        return true
    }
}