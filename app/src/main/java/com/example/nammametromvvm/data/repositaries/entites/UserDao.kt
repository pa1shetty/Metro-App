package com.example.nammametromvvm.data.repositaries.entites

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
@Entity
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun upsert(user: User): Long



    @Query("SELECT * FROM user WHERE uid=$CURRENT_USER_ID")
    @JvmSuppressWildcards
    fun getUser(): LiveData<User>
}