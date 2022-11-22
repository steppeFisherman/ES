package com.example.es.utils

import com.example.es.data.model.cloudModel.DataCloud


typealias UsersListener = (list: List<DataCloud>) -> Unit

class UsersService {

    private var users = mutableListOf<DataCloud>()
    private val listeners = mutableSetOf<UsersListener>()

    init {
        users = (1..100).map {
            DataCloud(
                id = it.toString(),
                idCache = 0,
                fullName = "",
                phoneUser = (it + 10).toString(),
                phoneOperator = "+74955802688",
                photo = "",
                time = "",
                timeLong = 0,
                latitude = "",
                longitude = "",
                locationAddress = "",
                homeAddress = "",
                company = "",
                alarm = false,
                notify = false
            )
        }.toMutableList()
    }

    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        listener.invoke(users)
    }

    fun removeListener(listener: UsersListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(users) }
    }
}