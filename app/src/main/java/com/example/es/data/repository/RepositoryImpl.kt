package com.example.es.data.repository

import com.example.es.domain.Repository
import com.example.es.domain.model.ResultUser
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val cloudSource: CloudSource,
    private val exceptionHandle: ExceptionHandle,
    private val dispatchers: ToDispatch
) : Repository {

//    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> }
//    private val scope = CoroutineScope(Job() + exceptionHandler)

    override suspend fun execute(id: String, phone: String): ResultUser =
        cloudSource.fetchCloud(id = id, phone = phone)
}