package com.example.es.domain.usecases

import com.example.es.domain.Repository
import com.example.es.domain.model.ResultUser
import javax.inject.Inject

interface PostUseCase {

    suspend fun postLocationUpdates(id: String, map: MutableMap<String,Any>): ResultUser
    suspend fun postAlarmUpdates(id: String, map: MutableMap<String,Any>): ResultUser

    class Base @Inject constructor(private val repository: Repository) : PostUseCase {

        override suspend fun postLocationUpdates(id: String, map: MutableMap<String, Any>)
                : ResultUser = repository.postLocationUpdates(id = id, map = map)

        override suspend fun postAlarmUpdates(id: String, map: MutableMap<String, Any>)
        : ResultUser = repository.postAlarmUpdates(id = id, map = map)
    }
}
