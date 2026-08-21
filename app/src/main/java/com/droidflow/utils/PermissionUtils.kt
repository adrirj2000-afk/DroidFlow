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

    fun checkSpecialPermissions(context: android.content.Context, actionsJson: String) {
        val notificationManager = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        if (actionsJson.contains("\"DND\"") && !notificationManager.isNotificationPolicyAccessGranted) {
            android.widget.Toast.makeText(context, "Falta permiso: 'Acceso a No Molestar'", android.widget.Toast.LENGTH_LONG).show()
            val intent = android.content.Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        if (actionsJson.contains("\"BRIGHTNESS\"") && !android.provider.Settings.System.canWrite(context)) {
            android.widget.Toast.makeText(context, "Falta permiso: 'Modificar ajustes del sistema'", android.widget.Toast.LENGTH_LONG).show()
            val intent = android.content.Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = android.net.Uri.parse("package:${context.packageName}")
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
