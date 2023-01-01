package com.example.es.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.es.ui.app.DoesNetworkHaveInternet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Save all available networks with an internet connection to a set (@validNetworks).
 * As long as the size of the set > 0, this LiveData emits true.
 * MinSdk = 21.
 */
class ConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val validNetworks: MutableSet<Network> = HashSet()

    fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        Log.d("AAAA", "onActive:")
        checkValidNetworks()
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        Log.d("AAAA", "onInactive:")
        cm.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            val isInternet =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            val isValidated =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            if (isValidated) {
                validNetworks.add(network)
                checkValidNetworks()
                Log.d(
                    "AAAA", "isValidated: $network $networkCapabilities\n" +
                            "________________________________________________________\n"
                )
            } else {
                validNetworks.remove(network)
                checkValidNetworks()
                Log.d(
                    "AAAA",
                    "Network has No Connection Capability: $network $networkCapabilities\n" +
                            "________________________________________________________\n"
                )
            }
            postValue(isInternet && isValidated)
            checkValidNetworks()
        }

        /*
          Called when a network is detected. If that network has internet, save it in the Set.
          Source: https://developer.android.com/reference/android/net/ConnectivityManager
          .NetworkCallback#onAvailable(android.net.Network)
         */
        @SuppressLint("MissingPermission")
        override fun onAvailable(network: Network) {

            Log.d(
                "AAAA", "onAvailable: $network\n" +
                        "___________________________________________________\n"
            )
            val networkCapabilities = cm.getNetworkCapabilities(network)
            val hasInternetCapability =
                networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            val isValidated =
                networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            Log.d(
                "AAAA", "onAvailable networkCapabilities: ${network}, $hasInternetCapability\n" +
                        "__________________________________________________________"
            )
            if (hasInternetCapability == true && isValidated == true) {
                // check if this network actually has internet
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet) {
                        withContext(Dispatchers.Main) {
                            Log.d(
                                "AAAA", "onAvailable: hasInternet google. $network\n" + "" +
                                        "______________________________________\n"
                            )
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        /*
          If the callback was registered with registerNetworkCallback() it will be called for each network which no longer satisfies the criteria of the callback.
          Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onLost(android.net.Network)
         */
        override fun onLost(network: Network) {
            Log.d("AAAA", "onLost: ${network}\n")
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }
}