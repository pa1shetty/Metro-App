package com.example.mymvvmsample.data.db.entites

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nammametromvvm.data.repositaries.entites.CURRENT_USER_ID
import com.example.nammametromvvm.data.repositaries.entites.User

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