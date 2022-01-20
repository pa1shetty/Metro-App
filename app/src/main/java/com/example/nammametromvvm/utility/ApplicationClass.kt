@file:Suppress("unused")

package com.example.nammametromvvm.utility

import android.app.Application
import com.example.mymvvmsample.data.db.entites.AppDatabase
import com.example.mymvvmsample.data.network.NetworkConnectionInterceptor
import com.example.mymvvmsample.data.repositaries.DataBaseRepository
import com.example.mymvvmsample.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.data.repositaries.network.MyApi
import com.example.nammametromvvm.ui.splashscreen.SplashScreenViewModelFactory
import com.example.nammametromvvm.utility.AppConstants.INTERNAL_LOG_PATH
import com.example.nammametromvvm.utility.AppConstants.LOG_FOLDER_NAME
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import com.example.nammametromvvm.utility.logs.Logs
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.io.File

class ApplicationClass : Application(), KodeinAware {
    override fun onCreate() {
        super.onCreate()
        INTERNAL_LOG_PATH =
            applicationContext.getExternalFilesDir(LOG_FOLDER_NAME).toString() + File.separator
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@ApplicationClass))

        bind() from singleton { LoggerClass(kodein) }
        bind() from singleton { Logs(kodein) }
        bind() from singleton { AesLibrary() }
        bind() from singleton { DataStoreRepository(instance(), instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { DateMethods() }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }

        bind() from singleton { DataBaseRepository(instance()) }
        bind() from singleton { NetworkRepository(instance(), instance(), instance()) }
        bind() from provider {
            SplashScreenViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(), instance(), instance()
            )
        }
    }
}