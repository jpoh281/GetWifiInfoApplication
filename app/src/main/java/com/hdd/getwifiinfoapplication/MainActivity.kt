package com.hdd.getwifiinfoapplication

import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.hdd.getwifiinfoapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher1 =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                button1Pressed()
            } else {
                // can't Job
            }
        }

    private val requestPermissionLauncher2 =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                button2Pressed()
            } else {
                // can't Job
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.first.setOnClickListener {
            button1Pressed()
        }
        binding.second.setOnClickListener {
            button2Pressed()
        }
    }

    private fun getWifiInfoByConnectivityManager() {
        val networkRequest: NetworkRequest =
            NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.requestNetwork(networkRequest, CustomNetworkCallback())
        connectivityManager.registerNetworkCallback(networkRequest, CustomNetworkCallback())
    }

    private fun getWifiInfoByWifiManager() {
        val wifiManager: WifiManager =
            applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        Log.d("button1Pressed", wifiInfo.bssid.toString())
    }

    private fun button1Pressed() {
        when {
            applicationContext.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                getWifiInfoByWifiManager()
            }
            else -> {
                requestPermissionLauncher1.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }

        }
    }

    private fun button2Pressed() {
        when {
            applicationContext.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                getWifiInfoByConnectivityManager()
            }
            else -> {
                requestPermissionLauncher2.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }

        }
    }

}

@RequiresApi(Build.VERSION_CODES.S)
class CustomNetworkCallback : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
    override fun onAvailable(network: Network) {}

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        val wifiInfo = networkCapabilities.transportInfo as WifiInfo
        Log.d("button2Pressed", wifiInfo.bssid.toString())
    }
}