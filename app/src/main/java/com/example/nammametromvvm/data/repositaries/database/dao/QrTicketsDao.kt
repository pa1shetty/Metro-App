package com.example.nammametromvvm.data.repositaries.database.dao

import androidx.room.*
import com.example.nammametromvvm.data.repositaries.database.module.QrTicket
import kotlinx.coroutines.flow.Flow

@Dao
@Entity
interface QrTicketsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTickets(ticket: List<QrTicket>)


    @Query("SELECT * FROM qrTicket Where txnStatus in (:transactionStatus)")
    @JvmSuppressWildcards
    fun getTickets(transactionStatus: List<Int>): Flow<List<QrTicket>>

    @Query("SELECT COUNT(*) FROM qrTicket")
    fun getTicketsCount(): Flow<Int>


    @Query("DELETE FROM qrTicket")
    fun nukeTickets()

}