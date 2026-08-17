package com.droidflow.domain.engine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SystemStateReceiver : BroadcastReceiver() {

    @Inject
    lateinit var flowEngine: FlowEngine

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        
        CoroutineScope(Dispatchers.IO).launch {
            when (action) {
                Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                    val isAirplaneOn = intent.getBooleanExtra("state", false)
                    flowEngine.processTrigger(if (isAirplaneOn) "AIRPLANE_MODE_ON" else "AIRPLANE_MODE_OFF")
                }
                WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                    val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                    flowEngine.processTrigger(if (wifiState == WifiManager.WIFI_STATE_ENABLED) "WIFI_CONNECTED" else "WIFI_DISCONNECTED")
                }
            }
        }
    }
}
