package com.example.nammametromvvm.utility.di

import android.content.Context
import androidx.room.Room
import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.data.repositaries.entites.AppDatabase
import com.example.nammametromvvm.data.repositaries.network.MyApi
import com.example.nammametromvvm.data.repositaries.network.NetworkConnectionInterceptor
import com.example.nammametromvvm.utility.AesLibrary
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.GenericMethods
import com.example.nammametromvvm.utility.UserRegistration
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import com.example.nammametromvvm.utility.logs.Logs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideData(): String {
        return "test"
    }

    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context) = app


    @Provides
    @Singleton
    fun provideLog() = Logs()

    @Provides
    @Singleton
    fun provideLoggerClass(provideLog: Logs) = LoggerClass(provideLog)

    @Provides
    @Singleton
    fun provideAesLibrary() = AesLibrary()

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext app: Context,
        provideAesLibrary: AesLibrary
    ) = DataStoreRepository(app, provideAesLibrary)

    @Provides
    @Singleton
    fun provideNetworkConnectionInterceptor(
        @ApplicationContext app: Context,
    ) = NetworkConnectionInterceptor(app)

    @Provides
    @Singleton
    fun provideDateMethods(
    ) = DateMethods()

    @Provides
    @Singleton
    fun provideMyApi(
        provideNetworkConnectionInterceptor: NetworkConnectionInterceptor
    ) = MyApi(provideNetworkConnectionInterceptor)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "MyDb"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDataBaseRepository(appDatabase: AppDatabase): DataBaseRepository {
        return DataBaseRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun provideGenericMethods(
    ) = GenericMethods()

    @Provides
    @Singleton
    fun provideNetworkRepository(
        provideMyApi: MyApi,
        provideDataBaseRepository: DataBaseRepository,
        provideDataStoreRepository: DataStoreRepository,
        configurations: Configurations
    ) = NetworkRepository(
        provideMyApi,
        provideDataBaseRepository,
        provideDataStoreRepository,
        configurations
    )

    @Provides
    @Singleton
    fun provideConfiguration(
        provideDataBaseRepository: DataBaseRepository,
    ) = Configurations(provideDataBaseRepository)

    @Provides
    @Singleton
    fun provideUserRegistration(
        dataBaseRepository: DataBaseRepository,
        dataStoreRepository: DataStoreRepository,
        networkRepository: NetworkRepository
    ) = UserRegistration(dataBaseRepository, dataStoreRepository, networkRepository)
}