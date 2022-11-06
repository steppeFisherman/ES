package com.example.es.data.repository

import com.example.es.domain.Repository
import com.example.es.domain.model.ResultUser
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val cloudSource: CloudSource,
    private val cacheSource: CacheSource,
    private val exceptionHandle: ExceptionHandle,
    private val dispatchers: ToDispatch
) : Repository {

//    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> }
//    private val scope = CoroutineScope(Job() + exceptionHandler)

    override suspend fun executeAuth(id: String, phone: String): ResultUser =
        cloudSource.fetchAuth(id = id, phone = phone)

    override suspend fun fetchExisted(id: String): ResultUser =
        cloudSource.fetchExisted(id = id)

    override suspend fun postLocation(id: String,latitude: String, longitude: String): ResultUser =
        cloudSource.postLocation(id,latitude,longitude)

    override val usersCached: ResultUser
        get() = cacheSource.fetchCached()

}

