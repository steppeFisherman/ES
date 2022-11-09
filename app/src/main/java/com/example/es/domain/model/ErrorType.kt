package com.example.es.domain.model

enum class ErrorType {
    NO_CONNECTION,
    FIREBASE_EXCEPTION,
    HTTP_EXCEPTION,
    USER_NOT_REGISTERED,
    SQL_EXCEPTION,
    GENERIC_ERROR
}