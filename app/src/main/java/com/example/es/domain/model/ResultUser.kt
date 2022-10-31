package com.example.es.domain.model

sealed class ResultUser {
    data class Success(val userDomain: DataDomain) : ResultUser()
    data class Fail(val errorType: ErrorType) : ResultUser()
}
