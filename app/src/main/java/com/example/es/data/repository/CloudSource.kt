package com.example.es.data.repository

import com.example.es.data.model.MapCacheToDomain
import com.example.es.data.model.MapCloudToCache
import com.example.es.data.model.MapCloudToDomain
import com.example.es.data.model.MapDomainToCloud
import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.data.room.AppRoomDao
import com.example.es.domain.model.DataDomain
import com.example.es.domain.model.ErrorType
import com.example.es.domain.model.ResultUser
import com.example.es.utils.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CloudSource {

    suspend fun fetchAuth(id: String, phone: String): ResultUser
    suspend fun fetchExisted(id: String): ResultUser
    suspend fun postLocation(id: String, dataDomain: DataDomain): ResultUser

    class Base @Inject constructor(
        private val appDao: AppRoomDao,
        private val mapperCacheToDomain: MapCacheToDomain,
        private val mapperCloudToCache: MapCloudToCache,
        private val mapperCloudToDomain: MapCloudToDomain,
        private val mapperDomainToCloud: MapDomainToCloud,
        private val dispatchers: ToDispatch,
        private val exceptionHandle: ExceptionHandle
    ) : CloudSource {

        private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
        private val scope = CoroutineScope(Job() + exceptionHandler)

        private lateinit var result: ResultUser

        override suspend fun fetchAuth(id: String, phone: String): ResultUser {
            REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
                .addOnCompleteListener() { task ->
                    val dataCloud = task.result.getValue(DataCloud::class.java) ?: DataCloud()
                    val isPhoneExists = dataCloud.phone_user == phone
                    result = if (isPhoneExists) {
                        val mapCloudToDomain = mapperCloudToDomain.mapCloudToDomain(dataCloud)
                        ResultUser.Success(mapCloudToDomain)
                    } else ResultUser.Fail(ErrorType.USER_NOT_REGISTERED)
                }.await()
            return result
        }

        override suspend fun fetchExisted(id: String): ResultUser {

            REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
                .addOnCompleteListener() { task ->
                    result = if (task.isSuccessful) {
                        val dataCloud = task.result.getValue(DataCloud::class.java) ?: DataCloud()
                        val dataDomain = mapperCloudToDomain.mapCloudToDomain(dataCloud)
                        ResultUser.Success(dataDomain)
                    } else exceptionHandle.handle(exception = task.exception)
                }.await()
            return result
        }

        override suspend fun postLocation(id: String, dataDomain: DataDomain): ResultUser {
            val dataCloud = mapperDomainToCloud.mapDomainToCloud(dataDomain)
            val dataMap = mutableMapOf<String, Any>()

//            dataMap[CHILD_ID] = dataCloud.id
//            dataMap[CHILD_FULL_NAME] = dataCloud.full_name
//            dataMap[CHILD_PHONE_USER] = dataCloud.phone_user
//            dataMap[CHILD_PHONE_OPERATOR] = dataCloud.phone_operator
//            dataMap[CHILD_PHOTO] = dataCloud.photo
            dataMap[CHILD_TIME] = dataCloud.time_location
            dataMap[CHILD_LATITUDE] = dataCloud.latitude
            dataMap[CHILD_LONGITUDE] = dataCloud.longitude
//            dataMap[CHILD_ALARM] = dataCloud.alarm
//            dataMap[CHILD_NOTIFY] = dataCloud.notify

            REF_DATABASE_ROOT.child(NODE_USERS).child(id).updateChildren(dataMap)
                .addOnCompleteListener { task ->
                    result = if (task.isSuccessful) {
                        val dataCache = mapperCloudToCache.mapCloudToCache(dataCloud)
                        dispatchers.launchIO(scope = scope) {
                            appDao.insertUser(dataCache)
                        }
                        val data = mapperCacheToDomain.mapCacheToDomain(dataCache)
                        ResultUser.Success(data)
                    } else {
                        ResultUser.Fail(ErrorType.GENERIC_ERROR)
                    }
                }
                .addOnFailureListener { exception ->
                    result = exceptionHandle.handle(exception = exception)
                }.await()
            return result
        }

//                CHILD_ID [dataCloud.id],
//                CHILD_ID [dataCloud.id],
//                CHILD_FULL_NAME to  dataCloud.full_name
//                CHILD_PHONE_USER to  dataCloud.phone_user
//                CHILD_PHONE_OPERATOR to  dataCloud.phone_operator
//                CHILD_PHOTO to  dataCloud.photo
//                CHILD_TIME to  dataCloud.time
//                CHILD_LATITUDE to  dataCloud.latitude
//                CHILD_LONGITUDE to  dataCloud.longitude
//                CHILD_ALARM to  dataCloud.alarm
//                CHILD_NOTIFY to  dataCloud.notify

//            REF_DATABASE_ROOT.child(NODE_USERS).child(id).updateChildren()


//
//            REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
//                .addOnCompleteListener { getValue ->
//                    if (getValue.isSuccessful) {
//                        val dataCloud =
//                            getValue.result.getValue(DataCloud::class.java)
//                                ?: DataCloud()
//                        dataCloud.latitude = latitude
//                        REF_DATABASE_ROOT.child(NODE_USERS).child(id).child(CHILD_LATITUDE)
//                            .setValue(latitude)
//                            .addOnCompleteListener { setValue ->
//                                result = if (setValue.isSuccessful) {
//                                    val dataCache = mapperCloudToCache.mapCloudToCache(dataCloud)
//                                    dispatchers.launchIO(scope = scope) {
//                                        appDao.insertUser(dataCache)
//                                    }
//                                    val dataDomain = mapperCloudToDomain.mapCloudToDomain(dataCloud)
//                                    ResultUser.Success(dataDomain)
//                                }else {
//                                    exceptionHandle.handle(exception = setValue.exception)
//                                }
//                            }
//                            .addOnFailureListener { exception ->
////                                result = exceptionHandle.handle(exception = exception)
//                       result = ResultUser.Fail(ErrorType.GENERIC_ERROR)
//
//                            }
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    result = exceptionHandle.handle(exception = exception)
//                }
//            return result


//            REF_DATABASE_ROOT.child(NODE_USERS).child(id).child(CHILD_LATITUDE).setValue(latitude)
//                .addOnCompleteListener { setValue ->
//                    if (setValue.isSuccessful) {
//                        REF_DATABASE_ROOT.child(NODE_USERS).child(id).get()
//                            .addOnCompleteListener { getValue ->
//                                if (getValue.isSuccessful) {
//                                    val dataCloud =
//                                        getValue.result.getValue(DataCloud::class.java)
//                                            ?: DataCloud()
//                                    val dataCache = mapperCloudToCache.mapCloudToCache(dataCloud)
//
//                                    dispatchers.launchIO(scope = scope) {
//                                        appDao.insertUser(dataCache)
//                                    }
//                                    val dataDomain = mapperCloudToDomain.mapCloudToDomain(dataCloud)
//                                    result = ResultUser.Success(dataDomain)
//                                } else result =
//                                    exceptionHandle.handle(exception = getValue.exception)
//                            }
//                    } else result = exceptionHandle.handle(exception = setValue.exception)
//                }.await()
//            return result
    }
}
