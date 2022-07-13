package com.example.nammametromvvm.data.repositaries

import com.example.nammametromvvm.data.repositaries.database.AppDatabase
import com.example.nammametromvvm.data.repositaries.database.module.Config
import com.example.nammametromvvm.data.repositaries.database.module.QrTicket
import com.example.nammametromvvm.data.repositaries.database.module.User
import com.example.nammametromvvm.data.repositaries.network.responses.stationLists.Station
import com.example.nammametromvvm.utility.AppConstants.configurations
import com.example.nammametromvvm.utility.TicketType.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataBaseRepository @Inject constructor(
    private val db: AppDatabase,
) {

    fun saveConfig(config: List<Config>) {
        db.getConfigDao().nukeConfigurable()
        db.getConfigDao().saveConfig(config)
        configurations = getConfigs()
    }

    fun getConfigs() = db.getConfigDao().getConfigs()

    @Suppress("unused")
    fun getConfig(key: String) = db.getConfigDao().getConfig(key)
    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    @Suppress("unused")
    fun getUser() = db.getUserDao().getUser()


    fun saveTickets(ticket: List<QrTicket>) {
        db.getQrTickets().nukeTickets()
        db.getQrTickets().saveTickets(ticket)
    }


    fun getTicketCount(): Flow<Int> {
        return db.getQrTickets().getTicketsCount()
    }

    fun getAllTickets(): Flow<List<QrTicket>> {
        return db.getQrTickets().getTickets(
            listOf(
                EXPIRED.ticketType,
                PENDING.ticketType,
                FAILED.ticketType,
                CANCELLED.ticketType,
                USED.ticketType,
                UNUSED.ticketType
            )
        )
    }

    fun getUnusedTickets(): Flow<List<QrTicket>> {
        return db.getQrTickets().getTickets(listOf(UNUSED.ticketType))
    }

    fun getOtherTickets(): Flow<List<QrTicket>> {
        return db.getQrTickets().getTickets(
            listOf(
                EXPIRED.ticketType,
                PENDING.ticketType,
                FAILED.ticketType,
                CANCELLED.ticketType,
                USED.ticketType
            )
        )
    }

    fun saveStationList(stations: List<Station>) {
        db.getStationDao().nukeStations()
        db.getStationDao().saveStations(stations)
    }

    fun getStationList(searchQuery: String) =
        db.getStationDao().getStations(searchQuery)

    fun getTicketDetails(ticketId: String): Flow<QrTicket> {
        return db.getQrTickets().getTicketDetails(
            ticketId
        )
    }

    fun clearDb() = db.clearAllTables()

}