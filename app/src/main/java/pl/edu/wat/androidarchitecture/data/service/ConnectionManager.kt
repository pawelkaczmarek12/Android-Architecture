package pl.edu.wat.androidarchitecture.data.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

class ConnectionManager {

    fun registerNetworkCallback(context: Context, onLost: () -> Unit, onAvailable: () -> Unit) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(NetworkRequest.Builder().build(), object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                onLost()
            }

            override fun onAvailable(network: Network) {
                onAvailable()
            }
        })
    }

}
