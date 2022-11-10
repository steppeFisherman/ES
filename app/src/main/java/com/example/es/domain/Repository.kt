package com.example.es.domain

import com.example.es.domain.model.DataDomain
import com.example.es.domain.model.ResultUser
import com.example.es.domain.model.ResultUserLiveData

interface Repository {
    suspend fun executeAuth(id: String, phone: String): ResultUser
    suspend fun fetchExisted(id: String): ResultUser
    suspend fun postLocation(id: String, map: MutableMap<String,Any>): ResultUser
    val usersCached: ResultUser
}