package com.example.es.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.es.data.model.cacheModel.DataCache

@Dao
interface AppRoomDao {

    @Query("SELECT * FROM item_table")
    fun fetchAllUsers(): LiveData<List<DataCache>>

    @Query("SELECT * FROM item_table")
    suspend fun fetchAllUsersBySuspend(): List<DataCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(dataCache: DataCache)

    @Query("DELETE FROM item_table")
    suspend fun deleteUser()

}