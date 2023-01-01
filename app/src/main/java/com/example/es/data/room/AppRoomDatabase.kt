package com.example.es.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.es.data.model.cacheModel.DataCache
import com.example.es.utils.DateLongConverter

@Database(entities = [DataCache::class], version = 2, exportSchema = false)
@TypeConverters(DateLongConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun getAppRoomDao(): AppRoomDao

    companion object {
        @Volatile
        private var database: AppRoomDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppRoomDatabase {
            return if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    AppRoomDatabase::class.java,
                    "database"
                ).fallbackToDestructiveMigration()
                    .build()
                database as AppRoomDatabase
            } else database as AppRoomDatabase
        }
    }
}