package com.example.es.domain

import com.example.es.domain.model.ResultUser

interface Repository {
    suspend fun executeAuth(id: String, phone: String): ResultUser
    suspend fun fetchExisted(id: String): ResultUser
    suspend fun postLocationUpdates(id: String, map: MutableMap<String, Any>): ResultUser
    suspend fun postAlarmUpdates(id: String, map: MutableMap<String, Any>): ResultUser
    suspend fun fetchCachedByDate(timeStart: Long, timeEnd: Long): ResultUser
    val usersCached: ResultUser
}