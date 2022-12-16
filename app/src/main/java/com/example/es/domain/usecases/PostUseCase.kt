package com.example.es.domain.usecases

import com.example.es.domain.Repository
import com.example.es.domain.model.ResultUser
import javax.inject.Inject

interface PostUseCase {

    suspend fun postUpdates(id: String, map: MutableMap<String,Any>): ResultUser

    class Base @Inject constructor(private val repository: Repository) : PostUseCase {
        override suspend fun postUpdates(id: String, map: MutableMap<String, Any>)
                : ResultUser =
            repository.postUpdates(id = id, map = map)
    }
}
