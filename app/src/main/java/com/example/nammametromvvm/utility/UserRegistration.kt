package com.example.nammametromvvm.utility

import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.data.repositaries.network.responses.Login.otpVerification.OtpVerificationData
import javax.inject.Inject


class UserRegistration @Inject constructor(
    private val dataBaseRepository: DataBaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val networkRepository: NetworkRepository,
) {
    suspend fun userLoggedIn(otpVerificationData: OtpVerificationData) {
        dataStoreRepository.saveCKey(otpVerificationData.cKey)
        dataStoreRepository.saveUserName(otpVerificationData.name)
        dataStoreRepository.saveUserEmail(otpVerificationData.email)
        dataStoreRepository.userLoggedIn()
        userRegister()
    }

    suspend fun userLoggedOut(): Boolean {
        dataStoreRepository.clearDatastore()
        dataBaseRepository.clearDb()
        return true
    }

    private suspend fun userRegister() {
        dataStoreRepository.saveSplTkn(networkRepository.register().splTkn)
    }
}