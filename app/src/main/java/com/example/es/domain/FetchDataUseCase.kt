package com.example.es.domain

import javax.inject.Inject

interface FetchDataUseCase {

    fun execute(phone: String): com.example.es.domain.model.Result

    class Base @Inject constructor(private val repository: Repository) : FetchDataUseCase {
        override fun execute(phone: String): com.example.es.domain.model.Result =
            repository.execute(phone)
    }
}