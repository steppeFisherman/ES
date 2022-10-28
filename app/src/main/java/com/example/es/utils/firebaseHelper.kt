package com.example.es.utils

import com.google.firebase.database.DatabaseReference

//lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference

const val NODE_USERS = "users"
const val CHILD_FULL_NAME = "full_name"
const val CHILD_ID = "id"
const val CHILD_TIME = "time"
const val CHILD_PHONE = "phone"
const val CHILD_LATITUDE = "latitude"
const val CHILD_LONGITUDE = "longitude"
const val APP_PREFERENCES = "APP_PREFERENCES"
const val PREF_BOOLEAN_VALUE = "PREF_BOOLEAN_VALUE"
const val PREF_PHONE_VALUE = "PREF_PHONE_VALUE"
const val PREF_ID_VALUE = "PREF_ID_VALUE"

//fun  initFirebase(){
//    AUTH = FirebaseAuth.getInstance()
//    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
//}