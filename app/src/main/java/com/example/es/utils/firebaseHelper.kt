package com.example.es.utils

import androidx.room.Database
import com.example.es.ui.model.DataUi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var USER: DataUi

const val DATABASE_LOCATION =
    "//https://user-location-71197-default-rtdb.europe-west1.firebasedatabase.app/"

const val NODE_USERS = "users"
const val CHILD_ID = "id"
const val CHILD_ID_CACHE = "idCache"
const val CHILD_FULL_NAME = "fullName"
const val CHILD_PHONE_USER = "phoneUser"
const val CHILD_PHONE_OPERATOR = "phoneOperator"
const val CHILD_PHOTO = "photo"
const val CHILD_TIME = "time"
const val CHILD_TIME_LONG = "timeLong"
const val CHILD_LATITUDE = "latitude"
const val CHILD_LONGITUDE = "longitude"
const val CHILD_LOCATION_ADDRESS = "locationAddress"
const val CHILD_HOME_ADDRESS = "homeAddress"
const val CHILD_COMPANY = "company"
const val CHILD_ALARM = "alarm"
const val CHILD_NOTIFY = "notify"
const val CHILD_COMMENT = "comment"
const val CHILD_LOCATION_FLAG_ONLY = "locationFlagOnly"

const val APP_PREFERENCES = "APP_PREFERENCES"
const val PREF_BOOLEAN_VALUE = "PREF_BOOLEAN_VALUE"
const val PREF_PHONE_VALUE = "PREF_PHONE_VALUE"
const val PREF_FULL_NAME_VALUE = "PREF_FULL_NAME_VALUE"
const val PREF_ID_VALUE = "PREF_ID_VALUE"
const val PREF_COMPANY_VALUE = "PREF_COMPANY_VALUE"
const val PREF_HOME_ADDRESS_VALUE = "PREF_HOME_ADDRESS_VALUE"
const val PREF_URI_VALUE = "PREF_URI_VALUE"

fun  initFirebase(){
//    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
}
