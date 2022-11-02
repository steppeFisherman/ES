package com.example.es.domain

import com.example.es.domain.model.ResultUser
import javax.inject.Inject

interface FetchUserUseCase {

     suspend fun execute(id: String, phone: String): ResultUser

    class Base @Inject constructor(private val repository: Repository) : FetchUserUseCase {
        override suspend fun execute(id: String, phone: String): ResultUser =
            repository.execute(id = id, phone = phone)
    }
}