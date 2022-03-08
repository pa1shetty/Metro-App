package com.example.nammametromvvm.ui.qrtickets.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.data.repositaries.entites.QrTicket
import com.example.nammametromvvm.utility.*
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.date.DateMethods.DateConstants.date_format_from_server
import com.example.nammametromvvm.utility.date.DateMethods.DateConstants.date_format_previous_year
import com.example.nammametromvvm.utility.logs.LoggerClass
import kotlinx.coroutines.flow.Flow
import java.net.SocketTimeoutException

class QrTicketsViewModel(
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val configurations: Configurations,
    private val loggerClass: LoggerClass,
    private val userRegistration: UserRegistration,
    private val genericMethods: GenericMethods,
    private val dataBaseRepository: DataBaseRepository,
    private val dateMethods: DateMethods
) : ViewModel() {

    fun getAllTickets(): Flow<List<QrTicket>> = dataBaseRepository.getAllTickets()
    fun getUnusedTickets(): Flow<List<QrTicket>> = dataBaseRepository.getUnusedTickets()
    fun getOtherTickets(): Flow<List<QrTicket>> = dataBaseRepository.getOtherTickets()
    fun getTicketCount(): Flow<Int> = dataBaseRepository.getTicketCount()

    suspend fun fetchTicketList() {

        try {
            val returnS = networkRepository.fetchTicketList()

            Log.d("test79", "requestForOtp: $returnS")
        } catch (e: ApiException) {
            loggerClass.error(e)
        } catch (e: NoInternetException) {
            loggerClass.error(e)
        } catch (e: ErrorException) {
            Log.d("test79", "requestForOtp: ${e.code}")
            loggerClass.error(e)
        } catch (e: SocketTimeoutException) {
            loggerClass.error(e)
        }
        fun convertDateFormat(
            originalDate: String,
            originalDateFormat: String = date_format_from_server,
            requiredDateFormat: String = date_format_previous_year
        ): String =
            dateMethods.convertDateFormat(originalDate, originalDateFormat, requiredDateFormat)
    }

    fun getDateMethods() = dateMethods

}