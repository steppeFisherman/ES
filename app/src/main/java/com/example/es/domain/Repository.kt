package com.example.es.domain

interface Repository {
    //    val allItems: Result
    fun execute(phone: String): com.example.es.domain.model.Result
}