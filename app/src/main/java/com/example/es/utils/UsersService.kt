package com.example.es.utils

import com.example.es.data.model.cloudModel.DataCloud

//
//typealias UsersListener = (list: List<DataCloud>) -> Unit
//
//class UsersService {
//
//    private var users = mutableListOf<DataCloud>()
//    private val listeners = mutableSetOf<UsersListener>()
//
//    init {
//        users = (1..10).map {
//            DataCloud(
//                id = it,
//                phone = (it + 10).toString(),
//                full_name = "",
//                time = "",
//                latitude = "",
//                longitude = ""
//            )
//        }.toMutableList()
//    }
//
//    fun addListener(listener: UsersListener) {
//        listeners.add(listener)
//        listener.invoke(users)
//    }
//
//    fun removeListener(listener: UsersListener) {
//        listeners.remove(listener)
//    }
//
//    private fun notifyChanges() {
//        listeners.forEach { it.invoke(users) }
//    }
//}