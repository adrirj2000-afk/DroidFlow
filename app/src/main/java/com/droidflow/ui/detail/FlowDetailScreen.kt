/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.detail

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.droidflow.data.local.FlowEntity
import com.droidflow.data.local.HistoryEntity
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowDetailScreen(
    viewModel: FlowDetailViewModel = hiltViewModel(),
    onNavigateToBuilder: (Long) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val flow by viewModel.flow.collectAsState()
    val historyList by viewModel.history.collectAsState()

    if (flow == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle del Flujo",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            HeaderSection(flow = flow!!)
            LogicVisualizerSection(flow = flow!!)
            ActionButtonsSection(
                onEdit = { onNavigateToBuilder(flow!!.id) },
                onTest = { viewModel.testFlow() }
            )
            ExecutionHistorySection(historyList = historyList)
        }
    }
}

@Composable
private fun HeaderSection(flow: FlowEntity) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = flow.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
            StatusBadge(isEnabled = flow.isEnabled)
        }
        Text(
            text = flow.description ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun StatusBadge(isEnabled: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_scale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        val iconColor = if (isEnabled) Color(0xFF22C55E) else MaterialTheme.colorScheme.outlineVariant
        Box(
            modifier = Modifier
                .size(8.dp)
                .drawBehind {
                    if (isEnabled) {
                        drawCircle(
                            color = iconColor.copy(alpha = alpha * 0.4f),
                            radius = size.width / 2 * scale
                        )
                    }
                }
                .background(iconColor, CircleShape)
        )
        Text(
            text = if (isEnabled) "Activado" else "Desactivado",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LogicVisualizerSection(flow: FlowEntity) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // CUANDO Card
        TriggerCard(flow = flow)

        // Flow Line
        Box(
            modifier = Modifier
                .padding(start = 23.dp)
                .width(2.dp)
                .height(24.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
        )

        // ENTONCES Card
        ActionCard(flow = flow)
    }
}

@Composable
private fun TriggerCard(flow: FlowEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when(flow.triggerType) {
                        "TIME" -> Icons.Default.Schedule
                        "BATTERY", "BATTERY_FULL" -> Icons.Default.BatteryAlert
                        "BLUETOOTH" -> Icons.Default.Bluetooth
                        "APP_OPENED" -> Icons.Default.Apps
                        "WIFI_CONNECTED", "WIFI_DISCONNECTED" -> Icons.Default.Wifi
                        "CHARGER_CONNECTED", "CHARGER_DISCONNECTED" -> Icons.Default.Power
                        "AIRPLANE_MODE_ON", "AIRPLANE_MODE_OFF" -> Icons.Default.AirplanemodeActive
                        "HEADPHONES_CONNECTED" -> Icons.Default.Headphones
                        "NFC_SCANNED" -> Icons.Default.Nfc
                        "SCREEN_ON", "SCREEN_OFF" -> Icons.Default.PhoneAndroid
                        "CALL_INCOMING" -> Icons.Default.Phone
                        else -> Icons.Default.PlayArrow
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Column {
                Text(
                    text = "CUANDO",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                
                val conditionText = try {
                    val array = if (flow.conditionsJson.isNotBlank()) JSONArray(flow.conditionsJson) else JSONArray()
                    when (flow.triggerType) {
                        "TIME" -> "Todos los días a las ${array.optString(0, "00:00")}"
                        "BATTERY" -> "Batería está ${array.optString(0, "baja")}"
                        "BLUETOOTH" -> "Conectado a Bluetooth"
                        "APP_OPENED" -> "Al abrir la app ${array.optString(1)}"
                        "MANUAL" -> "Ejecución manual"
                        "BATTERY_FULL" -> "Batería llena (100%)"
                        "CHARGER_CONNECTED" -> "Al conectar el cargador"
                        "CHARGER_DISCONNECTED" -> "Al desconectar el cargador"
                        "WIFI_CONNECTED" -> "Al conectar al Wi-Fi"
                        "WIFI_DISCONNECTED" -> "Al desconectar del Wi-Fi"
                        "AIRPLANE_MODE_ON" -> "Al activar el Modo Avión"
                        "AIRPLANE_MODE_OFF" -> "Al desactivar el Modo Avión"
                        "HEADPHONES_CONNECTED" -> "Al conectar los auriculares"
                        "NFC_SCANNED" -> "Al escanear etiqueta NFC"
                        "SCREEN_ON" -> "Al encender la pantalla"
                        "SCREEN_OFF" -> "Al apagar la pantalla"
                        "CALL_INCOMING" -> "Al recibir una llamada"
                        else -> "Condición desconocida (${flow.triggerType})"
                    }
                } catch (e: Exception) {
                    "Error leyendo condición (${flow.triggerType})"
                }

                Text(
                    text = conditionText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ActionCard(flow: FlowEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    drawLine(
                        color = Color(0xFF7C4DFF),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(0f, size.height),
                        strokeWidth = 4.dp.toPx()
                    )
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Column {
                    Text(
                        text = "ENTONCES",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Ejecutar acciones del sistema",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Action Details
            Column(
                modifier = Modifier
                    .padding(start = 64.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val parsedActions = remember(flow.actionsJson) {
                    val list = mutableListOf<Triple<ImageVector, String, String?>>()
                    try {
                        val actionsArray = JSONArray(flow.actionsJson)
                        for (i in 0 until actionsArray.length()) {
                            val actionObj = actionsArray.getJSONObject(i)
                            val type = actionObj.getString("type")
                            val item = when (type) {
                                "VIBRATE" -> Triple(Icons.Default.Vibration, "Vibrar dispositivo", null)
                                "NOTIFICATION" -> Triple(Icons.Default.Notifications, "Enviar notificación", null)
                                "VOLUME" -> Triple(Icons.Default.VolumeUp, "Ajustar Volumen", "${actionObj.optInt("level")}%")
                                "BRIGHTNESS" -> Triple(Icons.Default.BrightnessMedium, "Ajustar Brillo", "${actionObj.optInt("level")}")
                                "OPEN_APP" -> Triple(Icons.Default.Launch, "Abrir Aplicación", actionObj.optString("packageName"))
                                "TTS" -> Triple(Icons.Default.RecordVoiceOver, "Texto a voz", actionObj.optString("text"))
                                "SMS" -> Triple(Icons.Default.Sms, "Enviar SMS", actionObj.optString("phoneNumber"))
                                "WIFI" -> Triple(if(actionObj.optBoolean("enable")) Icons.Default.Wifi else Icons.Default.WifiOff, if(actionObj.optBoolean("enable")) "Activar Wi-Fi" else "Desactivar Wi-Fi", null)
                                "BLUETOOTH" -> Triple(if(actionObj.optBoolean("enable")) Icons.Default.Bluetooth else Icons.Default.BluetoothDisabled, if(actionObj.optBoolean("enable")) "Activar Bluetooth" else "Desactivar Bluetooth", null)
                                "DND" -> Triple(Icons.Default.DoNotDisturbOn, if(actionObj.optBoolean("enable")) "Activar No Molestar" else "Desactivar No Molestar", null)
                                else -> Triple(Icons.Default.Extension, type, null)
                            }
                            list.add(item)
                        }
                    } catch (e: Exception) {
                        list.add(Triple(Icons.Default.Error, "Error leyendo acciones", null))
                    }
                    list
                }

                parsedActions.forEachIndexed { index, actionData ->
                    ActionDetailRow(icon = actionData.first, title = actionData.second, value = actionData.third)
                    if (index < parsedActions.size - 1) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionDetailRow(icon: ImageVector, title: String, value: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun ActionButtonsSection(onEdit: () -> Unit, onTest: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onEdit,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Editar", style = MaterialTheme.typography.titleMedium)
        }

        Button(
            onClick = onTest,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C4DFF), // gradient equivalent for simplicity
                contentColor = Color.White
            )
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Probar ahora", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ExecutionHistorySection(historyList: List<HistoryEntity>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Historial de ejecuciones",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
        )

        val dateFormat = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())

        if (historyList.isEmpty()) {
            Text(
                text = "Sin ejecuciones previas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 4.dp)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                historyList.take(10).forEach { history ->
                    HistoryItem(
                        isSuccess = history.isSuccess, 
                        time = dateFormat.format(Date(history.timestamp))
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(isSuccess: Boolean, time: String) {
    val containerColor = if (isSuccess) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surfaceContainerLow
    val borderColor = if (isSuccess) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f) else MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
    val iconBgColor = if (isSuccess) Color(0xFF22C55E).copy(alpha = 0.1f) else MaterialTheme.colorScheme.errorContainer
    val iconColor = if (isSuccess) Color(0xFF4ADE80) else MaterialTheme.colorScheme.onErrorContainer
    val textColor = if (isSuccess) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
    val icon = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = if (isSuccess) "Ejecutado correctamente" else "Error al ejecutar",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
        Text(
            text = time,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
