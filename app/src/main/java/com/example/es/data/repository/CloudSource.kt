package com.example.es.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.es.data.model.MapCacheToDomain
import com.example.es.data.model.MapCloudToCache
import com.example.es.data.model.cloudModel.DataCloud
import com.example.es.data.room.AppRoomDao
import com.example.es.domain.model.DataDomain
import com.example.es.utils.NODE_USERS
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import javax.inject.Inject

interface CloudSource {

    fun fetchCloud(): MutableLiveData<List<DataDomain>>

    class InitialFetchFromCache @Inject constructor(
        private val appDao: AppRoomDao,
        private val mapperCacheToDomain: MapCacheToDomain,
        private val mapperCloudToCache: MapCloudToCache,
        private val dispatchers: ToDispatch,
    ) : CloudSource {

        private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
        private val scope = CoroutineScope(Job() + exceptionHandler)

        override fun fetchCloud(): MutableLiveData<List<DataDomain>> {

            val item = MutableLiveData<List<DataDomain>>()
            val phone = "+7 916 800 00 20"

            FirebaseDatabase.getInstance().reference
                .child(NODE_USERS).child(phone)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val dataCloud =
                            snapshot.getValue(DataCloud::class.java) ?: DataCloud()
                        val dataCache = mapperCloudToCache.mapCloudToCache(dataCloud)
                        item.value = listOf(mapperCacheToDomain.mapCacheToDomain(dataCache))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            return item
        }
    }
}