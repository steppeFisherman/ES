package com.example.es.data.repository

import com.example.es.domain.Repository
import com.example.es.domain.model.Result
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val cloudSource: CloudSource,
    private val exceptionHandle: ExceptionHandle
) : Repository {

    //    override val allItems: Result
//        get() = try {
//            Result.Success(cloudSource.fetchCloud())
//        } catch (e: Exception) {
//            exceptionHandle.handle(e)
//        }
    override fun execute(phone: String): Result =
        try {
            Result.Success(cloudSource.fetchCloud(phone))
        } catch (e: Exception) {
            exceptionHandle.handle(e)
        }
}