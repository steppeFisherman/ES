package com.example.es.domain

import javax.inject.Inject

interface FetchDataUseCase {

    fun execute(): com.example.es.domain.model.Result

    class Base @Inject constructor(private val repository: Repository) : FetchDataUseCase {
        override fun execute(): com.example.es.domain.model.Result = repository.allItems
    }
}