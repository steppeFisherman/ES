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

    override suspend fun postUpdates(id: String, map: MutableMap<String, Any>): ResultUser =
        try {
            cloudSource.postUpdates(id, map)
        } catch (e: Exception) {
            exceptionHandle.handle(exception = e)
        }

    override suspend fun fetchCachedByDate(timeStart: Long, timeEnd: Long): ResultUser = try {
        cacheSource.fetchCachedByDate(timeStart, timeEnd)
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

