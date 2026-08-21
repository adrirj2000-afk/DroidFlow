/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.domain.engine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.telephony.TelephonyManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.media.AudioManager
import android.provider.Settings
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.speech.tts.TextToSpeech
import android.telephony.SmsManager
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

interface Trigger {
    val id: String
}

interface Action {
    suspend fun execute(context: Context)
}

class NotificationAction(
    private val title: String,
    private val message: String
) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val channelId = "flow_engine_channel"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Flow Engine Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }
}

class VibrationAction(
    private val durationMs: Long = 500L
) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(durationMs)
                }
            }
        }
    }
}

class VolumeAction(val level: Int) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, level, 0)
        }
    }
}

class BrightnessAction(val level: Int) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            if (Settings.System.canWrite(context)) {
                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS,
                    level
                )
            }
        }
    }
}

class OpenAppAction(val packageName: String) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            val pm = context.packageManager
            val intent = pm.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}

class FlashlightAction(val enable: Boolean) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                val cameraId = cameraManager.cameraIdList.firstOrNull()
                if (cameraId != null) {
                    cameraManager.setTorchMode(cameraId, enable)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class DndAction(val enable: Boolean) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (notificationManager.isNotificationPolicyAccessGranted) {
                    val filter = if (enable) NotificationManager.INTERRUPTION_FILTER_NONE else NotificationManager.INTERRUPTION_FILTER_ALL
                    notificationManager.setInterruptionFilter(filter)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class TtsAction(val text: String) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                var tts: TextToSpeech? = null
                tts = TextToSpeech(context) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        tts?.language = Locale.getDefault()
                        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class SmsAction(val phoneNumber: String, val message: String) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    context.getSystemService(SmsManager::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    SmsManager.getDefault()
                }
                smsManager?.sendTextMessage(phoneNumber, null, message, null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class HttpRequestAction(val urlString: String, val method: String = "GET") : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = method
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.responseCode
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class WifiAction(val enable: Boolean) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                @Suppress("DEPRECATION")
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager
                wifiManager.isWifiEnabled = enable
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class BluetoothAction(val enable: Boolean) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                @Suppress("DEPRECATION")
                val bluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter()
                if (enable) bluetoothAdapter?.enable() else bluetoothAdapter?.disable()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class WallpaperAction : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                val wallpaperManager = android.app.WallpaperManager.getInstance(context)
                // Just clear wallpaper to default as a safe implementation
                wallpaperManager.clear()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class SoundPlayAction : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                val notification = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)
                val r = android.media.RingtoneManager.getRingtone(context, notification)
                r.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class BatterySaverAction : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                val intent = Intent(android.provider.Settings.ACTION_BATTERY_SAVER_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                android.widget.Toast.makeText(context, "Activa el ahorro de batería desde este panel.", android.widget.Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class ScreenOffAction : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                val service = DroidFlowAccessibilityService.instance
                if (service != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    service.performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN)
                } else {
                    android.widget.Toast.makeText(context, "Se requiere el servicio de Accesibilidad activado (o Android 9+).", android.widget.Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class MediaAction(val action: String) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val keyCode = when (action) {
                    "PLAY_PAUSE" -> android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                    "NEXT" -> android.view.KeyEvent.KEYCODE_MEDIA_NEXT
                    else -> android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                }
                val eventDown = android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, keyCode)
                val eventUp = android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, keyCode)
                audioManager.dispatchMediaKeyEvent(eventDown)
                audioManager.dispatchMediaKeyEvent(eventUp)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class WhatsAppAction(val phoneNumber: String, val message: String) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                // Remove any non-numeric characters from the phone number
                val cleanPhone = phoneNumber.replace(Regex("[^0-9]"), "")
                // Create a URI that opens WhatsApp directly on that chat
                val uri = android.net.Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${android.net.Uri.encode(message)}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.whatsapp")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class SystemButtonAction(val button: String) : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                val service = DroidFlowAccessibilityService.instance
                if (service != null) {
                    val actionCode = when (button) {
                        "HOME" -> android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME
                        "BACK" -> android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK
                        "RECENTS" -> android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_RECENTS
                        else -> return@withContext
                    }
                    service.performGlobalAction(actionCode)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class RejectCallAction : Action {
    override suspend fun execute(context: Context) {
        withContext(Dispatchers.Main) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as android.telecom.TelecomManager
                    telecomManager.endCall()
                } else {
                    // Reflection fallback for older Android versions
                    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    val clazz = Class.forName(telephonyManager.javaClass.name)
                    val method = clazz.getDeclaredMethod("getITelephony")
                    method.isAccessible = true
                    val telephonyService = method.invoke(telephonyManager)
                    val telephonyServiceClass = Class.forName(telephonyService.javaClass.name)
                    val endCallMethod = telephonyServiceClass.getDeclaredMethod("endCall")
                    endCallMethod.isAccessible = true
                    endCallMethod.invoke(telephonyService)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
