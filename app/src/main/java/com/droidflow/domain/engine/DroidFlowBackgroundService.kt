/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.domain.engine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.bluetooth.BluetoothDevice
import android.content.pm.ServiceInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DroidFlowBackgroundService : Service() {

    @Inject lateinit var flowEngine: FlowEngine

    private val batteryReceiver = BatteryReceiver()
    private val bluetoothReceiver = BluetoothReceiver()
    
    private val screenHeadsetReceiver = object : android.content.BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            CoroutineScope(Dispatchers.IO).launch {
                when (action) {
                    Intent.ACTION_SCREEN_ON -> flowEngine.processTrigger("SCREEN_ON")
                    Intent.ACTION_SCREEN_OFF -> flowEngine.processTrigger("SCREEN_OFF")
                    Intent.ACTION_HEADSET_PLUG -> {
                        val state = intent.getIntExtra("state", 0)
                        if (state == 1) {
                            flowEngine.processTrigger("HEADPHONES_CONNECTED")
                        }
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        registerDynamicReceivers()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Sticky to restart if the system kills it
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
        unregisterReceiver(bluetoothReceiver)
        unregisterReceiver(screenHeadsetReceiver)
    }

    private fun startForegroundService() {
        val channelId = "droidflow_background_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Motor de DroidFlow",
                NotificationManager.IMPORTANCE_LOW // Low to not make sound
            ).apply {
                description = "Mantiene las automatizaciones activas"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("DroidFlow activo")
            .setContentText("Tus automatizaciones están ejecutándose.")
            .setSmallIcon(android.R.drawable.sym_def_app_icon) // We'll change this when the real icon is imported
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(1, notification)
        }
    }

    private fun registerDynamicReceivers() {
        // Battery Intents
        val batteryFilter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        registerReceiver(batteryReceiver, batteryFilter)

        // Bluetooth Intents
        val bluetoothFilter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }
        registerReceiver(bluetoothReceiver, bluetoothFilter)

        val screenHeadsetFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_HEADSET_PLUG)
        }
        registerReceiver(screenHeadsetReceiver, screenHeadsetFilter)
    }
}
