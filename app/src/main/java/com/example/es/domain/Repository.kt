package com.example.es.domain

import com.example.es.domain.model.ResultUser

interface Repository {
   suspend fun execute(id: String): ResultUser
}