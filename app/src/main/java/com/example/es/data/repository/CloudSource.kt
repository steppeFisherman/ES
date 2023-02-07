package com.example.es.data.repository

import com.example.es.data.model.MapCacheToDomain
import com.example.es.data.model.MapCloudToCache
import com.example.es.data.model.MapCloudToDomain
import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.data.room.AppRoomDao
import com.example.es.domain.model.ErrorType
import com.example.es.domain.model.ResultUser
import com.example.es.utils.NODE_USERS
import com.example.es.utils.REF_DATABASE_ROOT
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CloudSource {

    suspend fun fetchAuth(id: String, phone: String): ResultUser
    suspend fun fetchExisted(id: String): ResultUser
    suspend fun postLocationUpdates(id: String, map: MutableMap<String, Any>): ResultUser
    suspend fun postAlarmUpdates(id: String, map: MutableMap<String, Any>): ResultUser

    class Base @Inject constructor(
        private val appDao: AppRoomDao,
        private val mapperCacheToDomain: MapCacheToDomain,
        private val mapperCloudToCache: MapCloudToCache,
        private val mapperCloudToDomain: MapCloudToDomain,
        private val dispatchers: ToDispatch,
        private val exceptionHandle: ExceptionHandle
    ) : CloudSource {

        private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
        private val scope = CoroutineScope(Job() + exceptionHandler)

        private lateinit var result: ResultUser

        override suspend fun fetchAuth(id: String, phone: String): ResultUser {
            REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
                .addOnCompleteListener { task ->
                    val dataCloud = task.result.getValue(DataCloud::class.java) ?: DataCloud()
                    val isPhoneExists = dataCloud.phoneUser == phone
                    result = if (isPhoneExists) {
                        val mapCloudToDomain = mapperCloudToDomain.mapCloudToDomain(dataCloud)
                        ResultUser.Success(mapCloudToDomain)
                    } else ResultUser.Fail(ErrorType.USER_NOT_REGISTERED)
                }.await()
            return result
        }

        override suspend fun fetchExisted(id: String): ResultUser {
            REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
                .addOnCompleteListener { task ->
                    result = if (task.isSuccessful) {
                        val dataCloud = task.result.getValue(DataCloud::class.java) ?: DataCloud()
                        val dataDomain = mapperCloudToDomain.mapCloudToDomain(dataCloud)
                        ResultUser.Success(dataDomain)
                    } else exceptionHandle.handle(exception = task.exception)
                }.await()
            return result
        }

        override suspend fun postLocationUpdates(
            id: String,
            map: MutableMap<String, Any>
        ): ResultUser {
             REF_DATABASE_ROOT.child(NODE_USERS).child(id).updateChildren(map).await()
            REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
                .addOnCompleteListener { task ->
                    result = if (task.isSuccessful) {
                        val dataCloud =
                            task.result.getValue(DataCloud::class.java) ?: DataCloud()
                        val dataCache = mapperCloudToCache.mapCloudToCache(dataCloud)
                        dispatchers.launchIO(scope = scope) { appDao.insertUser(dataCache) }
                        val dataDomain = mapperCacheToDomain.mapCacheToDomain(dataCache)
                        ResultUser.Success(dataDomain)
                    } else {
                        exceptionHandle.handle(exception = task.exception)
                    }
                }.await()
            return result
        }

        override suspend fun postAlarmUpdates(
            id: String,
            map: MutableMap<String, Any>
        ): ResultUser {
            REF_DATABASE_ROOT.child(NODE_USERS).child(id).updateChildren(map).await()
            REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
                .addOnCompleteListener { task ->
                    result = if (task.isSuccessful) {
                        val dataCloud =
                            task.result.getValue(DataCloud::class.java) ?: DataCloud()
                        val dataCache = mapperCloudToCache.mapCloudToCache(dataCloud)
                        dispatchers.launchIO(scope = scope) { appDao.insertUser(dataCache) }
                        val dataDomain = mapperCacheToDomain.mapCacheToDomain(dataCache)
                        ResultUser.Success(dataDomain)
                    } else {
                        exceptionHandle.handle(exception = task.exception)
                    }
                }.await()
            return result
        }
    }
}

