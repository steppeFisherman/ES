package com.example.es.domain.model

import androidx.lifecycle.LiveData

sealed class ResultUserLiveData {
    data class Success<T>(val user: LiveData<List<T>>) : ResultUserLiveData()
    data class Fail(val error: ErrorType) : ResultUserLiveData()
    data class Loading(val loading: Boolean) : ResultUserLiveData()
}
