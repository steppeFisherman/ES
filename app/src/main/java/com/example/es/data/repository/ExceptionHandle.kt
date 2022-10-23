package com.example.es.data.repository

import com.example.es.domain.model.ErrorType
import com.google.firebase.FirebaseException
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import java.net.UnknownHostException

interface ExceptionHandle {

    fun handle(e: Exception): com.example.es.domain.model.Result

    class Base : ExceptionHandle {
        override fun handle(e: Exception): com.example.es.domain.model.Result =
            com.example.es.domain.model.Result.Fail(
            when (e) {
                is UnknownHostException -> ErrorType.NO_CONNECTION
                is FirebaseException -> ErrorType.FIREBASE_EXCEPTION
                is HttpException -> ErrorType.HTTP_EXCEPTION
                else -> ErrorType.GENERIC_ERROR
            }
        )
    }
}
