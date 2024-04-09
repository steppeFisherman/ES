package com.example.es.utils

import android.content.Context
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability

interface ApiCheck {
    fun apiCheck(): ApiType
    class Base(private val context: Context) : ApiCheck {
        override fun apiCheck(): ApiType {

            val googleApi = GoogleApiAvailability.getInstance()
            val resultCodeGoogle = googleApi.isGooglePlayServicesAvailable(context)
            val connectionGoogleSuccess = com.google.android.gms.common.ConnectionResult.SUCCESS

            val huaweiApi = HuaweiApiAvailability.getInstance()
            val resultCodeHuawei = huaweiApi.isHuaweiMobileServicesAvailable(context)
            val connectionHuaweiSuccess = com.huawei.hms.api.ConnectionResult.SUCCESS

           return when {
                resultCodeGoogle == connectionGoogleSuccess -> ApiType.GOOGLE
                resultCodeHuawei == connectionHuaweiSuccess -> ApiType.HUAWEI
                else -> ApiType.OTHERS
            }
        }
    }
}