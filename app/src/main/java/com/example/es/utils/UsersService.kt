package com.example.es.utils

import com.example.es.data.model.cloudModel.DataCloud


typealias UsersListener = (list: List<DataCloud>) -> Unit

class UsersService {

    private var users = mutableListOf<DataCloud>()
    private val listeners = mutableSetOf<UsersListener>()

    init {
        users = (1..100).map {
            DataCloud(
                id = it,
                full_name = "",
                phone_user = (it + 10).toString(),
                phone_operator = "+74955802688",
                photo = "",
                time_location = "",
                latitude = "",
                longitude = "",
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