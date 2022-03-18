package com.example.nammametromvvm.data.repositaries.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "MyDb"
            ).build()


    }

    fun deleteDb(context: Context) {
        context.deleteDatabase("MyDb")
    }
}