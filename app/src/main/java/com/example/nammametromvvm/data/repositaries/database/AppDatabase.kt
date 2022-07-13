package com.example.nammametromvvm.data.repositaries.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nammametromvvm.data.repositaries.database.dao.ConfigDao
import com.example.nammametromvvm.data.repositaries.database.dao.QrTicketsDao
import com.example.nammametromvvm.data.repositaries.database.dao.StationDao
import com.example.nammametromvvm.data.repositaries.database.dao.UserDao
import com.example.nammametromvvm.data.repositaries.database.module.Config
import com.example.nammametromvvm.data.repositaries.database.module.QrTicket
import com.example.nammametromvvm.data.repositaries.database.module.User
import com.example.nammametromvvm.data.repositaries.network.responses.stationLists.Station

@Database(
    entities = [User::class, Config::class, QrTicket::class, Station::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getConfigDao(): ConfigDao
    abstract fun getQrTickets(): QrTicketsDao
    abstract fun getStationDao(): StationDao
}