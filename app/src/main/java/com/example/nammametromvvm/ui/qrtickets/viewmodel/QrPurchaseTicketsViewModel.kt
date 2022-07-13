package com.example.nammametromvvm.ui.qrtickets.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammametromvvm.data.module.StationSelection
import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.ui.qrtickets.ui.InputFor
import com.example.nammametromvvm.utility.ApiException
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.ErrorException
import com.example.nammametromvvm.utility.NoInternetException
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.date.DateMethods.DateConstants.date_format_dd_MMM_yyyy
import com.example.nammametromvvm.utility.logs.LoggerClass
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class QrPurchaseTicketsViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val configurations: Configurations,
    private val loggerClass: LoggerClass,
    private val dataBaseRepository: DataBaseRepository,
    private val dateMethods: DateMethods,
) : ViewModel() {
    private val _stationFetchResponse = MutableSharedFlow<Boolean>()
    val fareFetchResponse = MutableSharedFlow<Int>(0)

    private val stationSelection: MutableLiveData<StationSelection> =
        MutableLiveData<StationSelection>(StationSelection())
    private val totalFare: MutableLiveData<Float> =
        MutableLiveData<Float>(0f)

    fun getTicketDetails() = stationSelection
    private val travelDate: MutableLiveData<Long> =
        MutableLiveData<Long>(MaterialDatePicker.todayInUtcMilliseconds())

    private val passengerCount: MutableLiveData<Int> =
        MutableLiveData<Int>(1)

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
    suspend fun fetchStationList() {
        try {
            networkRepository.fetchStationList()
            _stationFetchResponse.emit(true)
        } catch (e: ApiException) {
            _stationFetchResponse.emit(false)
            loggerClass.error(e)
        } catch (e: NoInternetException) {
            _stationFetchResponse.emit(false)
            loggerClass.error(e)
        } catch (e: ErrorException) {
            loggerClass.error(e)
            _stationFetchResponse.emit(false)
        } catch (e: SocketTimeoutException) {
            loggerClass.error(e)
            _stationFetchResponse.emit(false)
        }
    }

    fun searchStation(searchQuery: String) =
        dataBaseRepository.getStationList(searchQuery)

    fun setValue(stationName: String, inputForValue: InputFor) {
        if (InputFor.FROM_STATION == inputForValue) {
            stationSelection.value!!.copy(fromStation = stationName)
                .also { stationSelection.value = it }
        } else {
            stationSelection.value!!.copy(toStation = stationName)
                .also { stationSelection.value = it }
        }
    }

    fun clearStationSelection() {
        stationSelection.value!!.copy(toStation = "", fromStation = "")
            .also { stationSelection.value = it }

    }

    fun clearToStationSelection() {
        stationSelection.value!!.copy(toStation = "").also { stationSelection.value = it }
    }

    fun getCurrentTicketDate() = this.travelDate
    fun setCurrentTicketDate(travelDate: Long) {
        this.travelDate.value = travelDate
    }

    fun getCurrentTicketDateFormatted(travelDate: Long = this.travelDate.value!!) =
        dateMethods.longToFormattedDate(travelDate, date_format_dd_MMM_yyyy)

    fun setPassengerCount(passengerCount: Int) {
        passengerCount.also { this.passengerCount.value = it }
    }

    fun getFromStation() =
        stationSelection.value!!.fromStation

    fun getToStation() =
        stationSelection.value!!.toStation

    fun getPassengerCount() = passengerCount

    fun getMaxPassengerCount() = configurations.getMaxPassengerCount()
    fun getMinPassengerCount() = configurations.getMinPassengerCount()
    fun getFare() {
        viewModelScope.launch {
            totalFare.value = networkRepository.getFare(
                stationSelection.value!!.fromStation,
                stationSelection.value!!.toStation, passengerCount.value!!, travelDate.value!!
            ).totalFare.toFloat()
            fareFetchResponse.emit(1)
        }
    }

    fun getTotalFare() = totalFare.value

}