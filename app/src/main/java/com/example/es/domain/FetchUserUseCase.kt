package com.example.es.domain

import com.example.es.domain.model.ResultUser
import javax.inject.Inject

interface FetchUserUseCase {

    suspend fun executeAuth(id: String, phone: String): ResultUser
    suspend fun fetchExisted(id: String): ResultUser

    class Base @Inject constructor(private val repository: Repository) : FetchUserUseCase {
        override suspend fun executeAuth(id: String, phone: String): ResultUser =
            repository.executeAuth(id = id, phone = phone)

        override suspend fun fetchExisted(id: String): ResultUser =
            repository.fetchExisted(id = id)
    }
}