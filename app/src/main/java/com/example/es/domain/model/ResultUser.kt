package com.example.es.domain.model

import androidx.lifecycle.LiveData

sealed class ResultUser {
    data class Success(val user: DataDomain) : ResultUser()
    data class SuccessList(val users: List<DataDomain>) : ResultUser()
    data class SuccessLiveData(val usersLiveData: LiveData<List<DataDomain>>) : ResultUser()
    data class Fail(val error: ErrorType) : ResultUser()
    data class Loading(val loading: Boolean) : ResultUser()
}
