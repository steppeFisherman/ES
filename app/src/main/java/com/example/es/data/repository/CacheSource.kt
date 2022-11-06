package com.example.es.data.repository

import androidx.lifecycle.map
import com.example.es.data.model.MapCacheToDomain
import com.example.es.data.model.MapCloudToCache
import com.example.es.data.model.MapCloudToDomain
import com.example.es.data.room.AppRoomDao
import com.example.es.domain.model.ResultUser
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
            ResultUser.Success(domain)
        } catch (e: Exception) {
            exceptionHandle.handle(exception = e)
        }
    }
}