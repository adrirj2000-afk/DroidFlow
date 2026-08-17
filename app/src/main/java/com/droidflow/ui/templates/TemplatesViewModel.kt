/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.templates

import android.content.Context
import android.net.wifi.WifiManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidflow.data.local.FlowDao
import com.droidflow.data.local.FlowEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Template(
    val name: String,
    val description: String,
    val triggerType: String,
    val conditionsJson: String,
    val actionsJson: String,
    val icon: String // Just an emoji or identifier
)

@HiltViewModel
class TemplatesViewModel @Inject constructor(
    private val flowDao: FlowDao
) : ViewModel() {

    val templates = listOf(
        Template(
            name = "Modo Noche",
            description = "Silencia el teléfono y baja el brillo a las 23:00",
            triggerType = "TIME",
            conditionsJson = "[\"23:00\"]",
            actionsJson = "[{\"type\":\"VOLUME\", \"level\":0}, {\"type\":\"BRIGHTNESS\", \"level\":10}]",
            icon = "🌙"
        ),
        Template(
            name = "Ahorro Batería",
            description = "Apaga Bluetooth cuando la batería baja del 20%",
            triggerType = "BATTERY",
            conditionsJson = "[\"<20%\"]",
            actionsJson = "[{\"type\":\"BLUETOOTH_OFF\"}]",
            icon = "🔋"
        ),
        Template(
            name = "Modo Coche",
            description = "Abre Google Maps al conectar el Bluetooth del coche",
            triggerType = "BLUETOOTH",
            conditionsJson = "[\"Car Audio\"]",
            actionsJson = "[{\"type\":\"OPEN_APP\", \"packageName\":\"com.google.android.apps.maps\"}]",
            icon = "🚗"
        )
    )

    fun getAvailableWifiNetworks(context: Context): List<String> {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        val networks = mutableListOf<String>()
        try {
            val configuredNetworks = wifiManager?.configuredNetworks
            configuredNetworks?.forEach { config ->
                // Remove quotes from SSID
                val ssid = config.SSID?.replace("\"", "")
                if (!ssid.isNullOrBlank()) {
                    networks.add(ssid)
                }
            }
        } catch (e: SecurityException) {
            // Permission missing
        }
        return networks.distinct()
    }

    fun applyTemplate(template: Template, customConditionsJson: String? = null) {
        viewModelScope.launch {
            val flow = FlowEntity(
                name = template.name,
                description = template.description,
                isEnabled = false,
                triggerType = template.triggerType,
                conditionsJson = customConditionsJson ?: template.conditionsJson,
                actionsJson = template.actionsJson
            )
            flowDao.insertFlow(flow)
        }
    }
}
