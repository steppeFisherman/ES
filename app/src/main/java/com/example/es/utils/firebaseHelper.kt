package com.example.es.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference

const val NODE_USERS = "users"
const val CHILD_FULL_NAME = "full_name"
const val CHILD_ID = "id"
const val CHILD_TIME = "time"
const val CHILD_PHONE = "phone"
const val CHILD_LATITUDE = "latitude"
const val CHILD_LONGITUDE = "longitude"

//fun  initFirebase(){
//    AUTH = FirebaseAuth.getInstance()
//    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
//}