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
            description = "Activa No Molestar, baja brillo y apaga WiFi a las 23:00",
            triggerType = "TIME",
            conditionsJson = "[\"23:00\"]",
            actionsJson = """[{"type":"DND_ON"}, {"type":"BRIGHTNESS", "level":10}, {"type":"WIFI_OFF"}]""",
            icon = "ðŸŒ™"
        ),
        Template(
            name = "Ahorro BaterÃ­a",
            description = "Apaga Bluetooth cuando la baterÃ­a baja del 20%",
            triggerType = "BATTERY",
            conditionsJson = "[\"<20%\"]",
            actionsJson = "[{\"type\":\"BLUETOOTH_OFF\"}]",
            icon = "ðŸ”‹"
        ),
        Template(
            name = "Modo Coche",
            description = "Abre Google Maps al conectar el Bluetooth del coche",
            triggerType = "BLUETOOTH",
            conditionsJson = "[\"Car Audio\"]",
            actionsJson = "[{\"type\":\"OPEN_APP\", \"packageName\":\"com.google.android.apps.maps\"}]",
            icon = "🚙"
        ),
        Template(
            name = "Bloqueador de Molestosos",
            description = "Rechaza la llamada automáticamente si es del número prohibido",
            triggerType = "CALL_RECEIVED",
            conditionsJson = "[\"600123456\"]",
            actionsJson = "[{\"type\":\"REJECT_CALL\"}]",
            icon = "🚫"
        ),
        Template(
            name = "Modo SOS",
            description = "Si recibes una notificación con 'Emergencia', sube volumen y enciende la linterna",
            triggerType = "NOTIFICATION",
            conditionsJson = "[\"Emergencia\"]",
            actionsJson = "[{\"type\":\"DND_OFF\"}, {\"type\":\"VOLUME\", \"level\":100}, {\"type\":\"FLASHLIGHT_ON\"}]",
            icon = "🚨"
        ),
        Template(
            name = "Auto-Respuesta Reunión",
            description = "Si te mandan un SMS importante, responde en segundo plano",
            triggerType = "SMS_RECEIVED",
            conditionsJson = "[\"600000000\"]",
            actionsJson = "[{\"type\":\"SMS_SEND\", \"phoneNumber\":\"600000000\", \"message\":\"Estoy reunido, luego hablamos.\"}]",
            icon = "💬"
        ),
        Template(
            name = "Filtro Anti-Mirones",
            description = "Si abren Instagram, pulsa el botón 'Inicio' mágicamente",
            triggerType = "APP_OPENED",
            conditionsJson = "[\"APP_OPENED\", \"com.instagram.android\"]",
            actionsJson = "[{\"type\":\"SYSTEM_BUTTON\", \"button\":\"HOME\"}]",
            icon = "🕵️"
        )
    )

    fun getAvailableWifiNetworks(context: Context): List<String> {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        val networks = mutableListOf<String>()
        try {
            @Suppress("DEPRECATION")
            val scanResults = wifiManager?.scanResults
            scanResults?.forEach { result ->
                val ssid = result.SSID?.replace("\"", "")
                if (!ssid.isNullOrBlank()) {
                    networks.add(ssid)
                }
            }
            
            @Suppress("DEPRECATION")
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
