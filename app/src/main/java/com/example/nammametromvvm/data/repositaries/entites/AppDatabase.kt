package com.example.nammametromvvm.data.repositaries.entites

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mymvvmsample.data.db.entites.UserDao

@Database(
    entities = [User::class, Config::class],
    version = 1
)
 abstract class AppDatabase   : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getConfigDao(): ConfigDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke  (context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase (context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "MyDb"
            ).build()


    }
}