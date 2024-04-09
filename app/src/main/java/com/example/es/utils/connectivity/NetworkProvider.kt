package com.example.es.utils.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import java.util.HashSet
import javax.inject.Inject

/**
 * Save all available networks with an internet connection to a set (@validNetworks).
 * As long as the size of the set > 0, this LiveData emits true.
 * MinSdk = 21.
 */
class NetworkProvider @Inject constructor(val context: Context) {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()

    val liveData: LiveData<Boolean>
        get() = netWorkData

    private var netWorkData = object : MutableLiveData<Boolean>(true) {

        override fun onActive() {
            postNetworkStatus()
            networkCallback = createNetworkCallback()
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }

        override fun onInactive() {
            connectivityManager.unregisterNetworkCallback(networkCallback)
            validNetworks.clear()
        }
    }

    private fun postNetworkStatus() {
        netWorkData.postValue(validNetworks.isNotEmpty())
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        /**
         Called when a network is detected. If that network has internet, save it in the Set.
         Source: https://developer.android.com/reference/android/net/ConnectivityManager
         .NetworkCallback#onAvailable(android.net.Network)
         */
        override fun onAvailable(network: Network) {
            /**
             * On Build.VERSION_CODES.O and higher to avoid an extra call onAvailable() method
             * when the app is initially started with an internet connection, thus we don't call
             * checkTheRealConnection(network) twice.
             */
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                val hasInternetCapability =
                    networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

                val isValidated =
                    networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

                if (hasInternetCapability == true && isValidated == true) {
                    checkConnection(network)
                }
            }
        }

        /**
         * Only starting from version Build.VERSION_CODES.O onAvailable() will always
         * immediately be followed by a call to onCapabilitiesChanged().
         * On versions below Build.VERSION_CODES.O, when the app is started with an internet connection,
         * nothing apart from onAvailable() is being called.
         */

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities,
        ) {
            val isInternet =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            val isValidated =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            if (isInternet && isValidated) {
                checkConnection(network)
            }
        }

        /**
         If the callback was registered with registerNetworkCallback() it will be called for each network
         which no longer satisfies the criteria of the callback.
         Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onLost(android.net.Network)
         */
        override fun onLost(network: Network) {
            validNetworks.remove(network)
            postNetworkStatus()
        }

        private fun checkConnection(network: Network) {
            validNetworks.add(network)
            postNetworkStatus()
        }
    }
}
