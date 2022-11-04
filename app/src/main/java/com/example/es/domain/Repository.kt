package com.example.es.domain

import com.example.es.domain.model.ResultUser

interface Repository {
   suspend fun executeAuth(id: String, phone: String): ResultUser
   suspend fun fetchExisted(id: String): ResultUser
}