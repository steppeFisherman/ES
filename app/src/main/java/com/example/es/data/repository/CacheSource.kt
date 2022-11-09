package com.example.es.data.repository

import android.database.sqlite.SQLiteException
import androidx.lifecycle.map
import com.example.es.data.model.MapCacheToDomain
import com.example.es.data.model.MapCloudToCache
import com.example.es.data.model.MapCloudToDomain
import com.example.es.data.room.AppRoomDao
import com.example.es.domain.model.ResultUser
import com.example.es.domain.model.ResultUserLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.database.DatabaseException
import java.net.UnknownHostException
import javax.inject.Inject

interface CacheSource {

    fun fetchCached(): ResultUser

    class Base @Inject constructor(
        private val appDao: AppRoomDao,
        private val mapperCacheToDomain: MapCacheToDomain,
        private val mapperCloudToCache: MapCloudToCache,
        private val mapperCloudToDomain: MapCloudToDomain,
        private val dispatchers: ToDispatch,
        private val exceptionHandle: ExceptionHandle
    ) : CacheSource {
        override fun fetchCached(): ResultUser = try {
            val users = appDao.fetchAllUsers()
            val domain = users.map { listDataCache ->
                listDataCache.map {
                    mapperCacheToDomain.mapCacheToDomain(it)
                }
            }
            ResultUser.SuccessLiveDAta(domain)
        } catch (e: Exception) {
            exceptionHandle.handle(exception = e)
        }
    }
}