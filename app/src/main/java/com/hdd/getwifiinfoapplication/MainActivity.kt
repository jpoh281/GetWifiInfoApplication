package com.hdd.getwifiinfoapplication

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
        )

        val networkRequest: NetworkRequest =
            NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()

        val connectivityManager = getSystemService(ConnectivityManager::class.java)

        connectivityManager.registerNetworkCallback(networkRequest, CustomNetworkCallback())

    }

    fun getWifiInfo() {
        val wifiManager: WifiManager =
            applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        Log.d("AAAAAAAA", wifiInfo.bssid.toString())
    }
}

@RequiresApi(Build.VERSION_CODES.S)
class CustomNetworkCallback : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
    override fun onAvailable(network: Network) {}

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        val wifiInfo = networkCapabilities.transportInfo as WifiInfo
        Log.d("AAAAAAAA", wifiInfo.bssid.toString())
    }
}