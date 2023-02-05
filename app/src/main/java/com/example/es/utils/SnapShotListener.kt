package com.example.es.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


typealias Listener = (snapshot: DataSnapshot) -> Unit

class SnapShotListener(private val listener: Listener) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        listener(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {}
}