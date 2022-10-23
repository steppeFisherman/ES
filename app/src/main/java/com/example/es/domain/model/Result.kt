package com.example.es.domain.model

import androidx.lifecycle.LiveData

sealed class Result {
    data class Success(val itemsDomain: LiveData<List<DataDomain>>) : Result()
    data class Fail(val errorType: ErrorType) : Result()
}
