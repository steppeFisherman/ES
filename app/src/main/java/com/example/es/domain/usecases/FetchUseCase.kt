package com.example.es.domain.usecases

import com.example.es.domain.Repository
import com.example.es.domain.model.ResultUser
import javax.inject.Inject

interface FetchUseCase {

    suspend fun executeAuth(id: String, phone: String): ResultUser
    suspend fun fetchExisted(id: String): ResultUser
    fun fetchCached(): ResultUser

    class Base @Inject constructor(private val repository: Repository) : FetchUseCase {

        override suspend fun executeAuth(id: String, phone: String): ResultUser =
            repository.executeAuth(id = id, phone = phone)

        override suspend fun fetchExisted(id: String): ResultUser = repository.fetchExisted(id = id)
        override fun fetchCached(): ResultUser = repository.usersCached
    }
}