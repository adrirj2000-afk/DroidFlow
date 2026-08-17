package com.droidflow.utils

import android.Manifest
import android.os.Build

object PermissionUtils {
    fun getRequiredPermissions(triggerType: String, actionsJson: String): List<String> {
        val permissions = mutableSetOf<String>()

        // Triggers
        when (triggerType) {
            "SMS_RECEIVED" -> permissions.add(Manifest.permission.RECEIVE_SMS)
            "CALL_INCOMING" -> permissions.add(Manifest.permission.READ_PHONE_STATE)
            "BLUETOOTH" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
                    permissions.add(Manifest.permission.BLUETOOTH_SCAN)
                } else {
                    permissions.add(Manifest.permission.BLUETOOTH)
                }
            }
            "LOCATION_ARRIVE" -> {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }

        // Actions
        if (actionsJson.contains("\"SMS_SEND\"")) {
            permissions.add(Manifest.permission.SEND_SMS)
        }
        if (actionsJson.contains("\"BLUETOOTH_ON\"") || actionsJson.contains("\"BLUETOOTH_OFF\"")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                permissions.add(Manifest.permission.BLUETOOTH)
                permissions.add(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }
        if (actionsJson.contains("\"NOTIFICATION\"")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (actionsJson.contains("\"SCREEN_OFF\"")) {
            permissions.add("ACCESSIBILITY_SCREEN_OFF")
        }

        return permissions.toList()
    }
}
