/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.domain.engine

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.droidflow.data.local.FlowDao
import com.droidflow.data.local.HistoryDao
import com.droidflow.data.local.HistoryEntity
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date
import javax.inject.Singleton

@Singleton
class FlowEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val flowDao: FlowDao,
    private val historyDao: HistoryDao
) {
    suspend fun processTrigger(triggerType: String, data: String = "") = withContext(Dispatchers.IO) {
        val flows = flowDao.getAllFlows().firstOrNull() ?: return@withContext
        val matchingFlows = flows.filter { it.triggerType == triggerType && it.isEnabled }
        
        for (flow in matchingFlows) {
            var shouldExecute = true
            if (triggerType == "APP_OPENED" && data.isNotEmpty()) {
                if (!flow.conditionsJson.contains(data)) {
                    shouldExecute = false
                }
            }
            if (shouldExecute) {
                evaluateAndExecute(flow.id.toString())
            }
        }
    }

    suspend fun evaluateAndExecute(triggerId: String) = withContext(Dispatchers.IO) {
        val flows = flowDao.getAllFlows().firstOrNull() ?: return@withContext
        val flow = flows.find { it.id.toString() == triggerId || it.name == triggerId } ?: return@withContext
        
        if (!flow.isEnabled) return@withContext
        val startTime = System.currentTimeMillis()
        var isSuccess = true
        var errorMessage: String? = null

        try {
            val actionsArray = JSONArray(flow.actionsJson)
            for (i in 0 until actionsArray.length()) {
                val actionObj = actionsArray.getJSONObject(i)
                val type = actionObj.getString("type")
                
                when (type) {
                    "VIBRATE" -> VibrationAction().execute(context)
                    "NOTIFICATION" -> NotificationAction("DroidFlow", "Ejecución de flujo: ${flow.name}").execute(context)
                    "VOLUME" -> {
                        val level = actionObj.optInt("level", 50)
                        VolumeAction(level).execute(context)
                    }
                    "BRIGHTNESS" -> {
                        val level = actionObj.optInt("level", 128)
                        BrightnessAction(level).execute(context)
                    }
                    "OPEN_APP" -> {
                        val packageName = actionObj.optString("packageName", "")
                        if (packageName.isNotEmpty()) {
                            OpenAppAction(packageName).execute(context)
                        }
                    }
                    "FLASHLIGHT_ON" -> FlashlightAction(true).execute(context)
                    "FLASHLIGHT_OFF" -> FlashlightAction(false).execute(context)
                    "DND_ON" -> DndAction(true).execute(context)
                    "DND_OFF" -> DndAction(false).execute(context)
                    "WALLPAPER" -> WallpaperAction().execute(context)
                    "SOUND_PLAY" -> SoundPlayAction().execute(context)
                    "BATTERY_SAVER" -> BatterySaverAction().execute(context)
                    "SCREEN_OFF" -> ScreenOffAction().execute(context)
                    "MEDIA_PLAY_PAUSE" -> MediaAction("PLAY_PAUSE").execute(context)
                    "MEDIA_NEXT" -> MediaAction("NEXT").execute(context)
                    "TTS" -> {
                        val text = actionObj.optString("text", "Ejecutando flujo de DroidFlow")
                        TtsAction(text).execute(context)
                    }
                    "SMS_SEND" -> {
                        val phone = actionObj.optString("phoneNumber", "123456789")
                        val message = actionObj.optString("message", "Mensaje automático de DroidFlow")
                        SmsAction(phone, message).execute(context)
                    }
                    "HTTP_REQUEST" -> {
                        val url = actionObj.optString("url", "https://www.google.com")
                        val method = actionObj.optString("method", "GET")
                        HttpRequestAction(url, method).execute(context)
                    }
                    "WIFI_ON" -> WifiAction(true).execute(context)
                    "WIFI_OFF" -> WifiAction(false).execute(context)
                    "BLUETOOTH_ON" -> BluetoothAction(true).execute(context)
                    "BLUETOOTH_OFF" -> BluetoothAction(false).execute(context)
                    // Legacy support for older flows
                    "WIFI" -> {
                        val enable = actionObj.optBoolean("enable", false)
                        WifiAction(enable).execute(context)
                    }
                    "BLUETOOTH" -> {
                        val enable = actionObj.optBoolean("enable", false)
                        BluetoothAction(enable).execute(context)
                    }
                    "WHATSAPP_SEND" -> WhatsAppAction(actionObj.getString("phoneNumber"), actionObj.getString("message")).execute(context)
                }
            }
        } catch (e: Exception) {
            isSuccess = false
            errorMessage = e.message
        } finally {
            val duration = System.currentTimeMillis() - startTime
            historyDao.insertHistory(
                HistoryEntity(
                    flowId = flow.id,
                    timestamp = Date().time,
                    isSuccess = isSuccess,
                    durationMs = duration,
                    errorMessage = errorMessage
                )
            )
        }
    }
}
