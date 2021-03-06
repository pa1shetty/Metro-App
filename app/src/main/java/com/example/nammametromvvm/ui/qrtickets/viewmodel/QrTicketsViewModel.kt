package com.example.nammametromvvm.ui.qrtickets.viewmodel

import androidx.lifecycle.ViewModel
import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.database.module.QrTicket
import com.example.nammametromvvm.utility.ApiException
import com.example.nammametromvvm.utility.ErrorException
import com.example.nammametromvvm.utility.NoInternetException
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class QrTicketsViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val loggerClass: LoggerClass,
    private val dataBaseRepository: DataBaseRepository,
    private val dateMethods: DateMethods,
) : ViewModel() {

    private val _stationFetchResponse = MutableSharedFlow<Boolean>()
    val stationFetchResponse: SharedFlow<Boolean> = _stationFetchResponse

    fun getAllTickets(): Flow<List<QrTicket>> = dataBaseRepository.getAllTickets()
    fun getUnusedTickets(): Flow<List<QrTicket>> = dataBaseRepository.getUnusedTickets()
    fun getOtherTickets(): Flow<List<QrTicket>> = dataBaseRepository.getOtherTickets()
    fun getTicketCount(): Flow<Int> = dataBaseRepository.getTicketCount()

    suspend fun fetchTicketList() {
        try {
            networkRepository.fetchTicketList()
        } catch (e: ApiException) {
            loggerClass.error(e)
        } catch (e: NoInternetException) {
            loggerClass.error(e)
        } catch (e: ErrorException) {
            loggerClass.error(e)
        } catch (e: SocketTimeoutException) {
            loggerClass.error(e)
        }
    }

    fun getDateMethods() = dateMethods
    fun getTicketDetails(ticketId: String) = dataBaseRepository.getTicketDetails(ticketId)


}