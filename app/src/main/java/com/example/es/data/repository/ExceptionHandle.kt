package com.example.es.data.repository

import com.example.es.domain.model.ErrorType
import com.example.es.domain.model.ResultUser
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import java.net.UnknownHostException

interface ExceptionHandle {

    fun handle(exception: Exception?): ResultUser

    class Base : ExceptionHandle {
        override fun handle(exception: Exception?): ResultUser = ResultUser.Fail(
            when (exception) {
                is UnknownHostException -> ErrorType.NO_CONNECTION
                is FirebaseException -> ErrorType.FIREBASE_EXCEPTION
                is HttpException -> ErrorType.HTTP_EXCEPTION
                is FirebaseAuthException -> ErrorType.USER_NOT_REGISTERED
                else -> ErrorType.GENERIC_ERROR
            }
        )
    }
}
