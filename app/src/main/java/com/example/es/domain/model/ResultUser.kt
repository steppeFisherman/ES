package com.example.es.domain.model

sealed class ResultUser {
    data class Success(val user: DataDomain) : ResultUser()
    data class Fail(val error: ErrorType) : ResultUser()
    data class Loading(val loading: Boolean) : ResultUser()
}
