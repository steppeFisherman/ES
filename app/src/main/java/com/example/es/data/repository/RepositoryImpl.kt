package com.example.es.data.repository

import com.example.es.domain.Repository
import com.example.es.domain.model.DataDomain
import com.example.es.domain.model.ResultUser
import com.google.firebase.FirebaseException
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val cloudSource: CloudSource,
    private val cacheSource: CacheSource,
    private val exceptionHandle: ExceptionHandle,
    private val dispatchers: ToDispatch
) : Repository {

//    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> }
//    private val scope = CoroutineScope(Job() + exceptionHandler)

    override suspend fun executeAuth(id: String, phone: String): ResultUser = try {
        cloudSource.fetchAuth(id = id, phone = phone)
    } catch (e: Exception) {
        exceptionHandle.handle(exception = e)
    }

    override suspend fun fetchExisted(id: String): ResultUser = try {
        cloudSource.fetchExisted(id = id)
    } catch (e: Exception) {
        exceptionHandle.handle(exception = e)
    }

    override suspend fun postLocation(id: String, map: MutableMap<String,Any>): ResultUser =
        try {
            cloudSource.postLocation(id, map)
        } catch (e: Exception) {
            exceptionHandle.handle(exception = e)
        }

    override val usersCached: ResultUser
        get() = try {
            cacheSource.fetchCached()
        } catch (e: Exception) {
            exceptionHandle.handle(exception = e)
        }
}

