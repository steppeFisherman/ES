package com.example.es.domain.usecases

import com.example.es.domain.Repository
import com.example.es.domain.model.ResultUser
import javax.inject.Inject

interface PostUseCase {

    suspend fun postLocation(id: String, latitude: String, longitude: String): ResultUser

    class Base @Inject constructor(private val repository: Repository) : PostUseCase {
        override suspend fun postLocation(id: String, latitude: String, longitude: String)
                : ResultUser =
            repository.postLocation(id = id, latitude = latitude, longitude = longitude)
    }
}
