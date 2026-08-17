/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.permissions

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droidflow.core.theme.*
import com.droidflow.domain.engine.PermissionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val permissionManager = remember { PermissionManager(context) }

    var hasNotifications by remember { mutableStateOf(permissionManager.hasPostNotificationsPermission()) }
    var hasExactAlarm by remember { mutableStateOf(permissionManager.hasExactAlarmPermission()) }
    var hasWriteSettings by remember { mutableStateOf(permissionManager.hasWriteSettingsPermission()) }
    var hasBluetooth by remember { mutableStateOf(permissionManager.hasBluetoothConnectPermission()) }
    var hasAccessibility by remember { mutableStateOf(permissionManager.hasAccessibilityPermission()) }
    var hasDnd by remember { mutableStateOf(permissionManager.hasDndPermission()) }
    var hasLocation by remember { mutableStateOf(permissionManager.hasLocationPermission()) }
    var hasSms by remember { mutableStateOf(permissionManager.hasSmsPermission()) }

    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasNotifications = isGranted }

    val bluetoothLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasBluetooth = isGranted }

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasLocation = isGranted }

    val smsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> hasSms = isGranted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Permisos de DroidFlow", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background, titleContentColor = Color.White)
            )
        },
        containerColor = Background
    ) { padding ->
        // Note: Using LazyColumn because the list of permissions is getting long and might scroll on smaller screens
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("DroidFlow solo solicita permisos cuando configuras una acción o evento que los requiera.", color = OnSurfaceVariant, fontSize = 14.sp)
            }
            
            item {
                PermissionItem(
                    title = "Servicio de Accesibilidad",
                    description = "Crítico para detectar cuándo abres o cierras otras aplicaciones.",
                    isGranted = hasAccessibility,
                    onClick = {
                        context.startActivity(permissionManager.getAccessibilityIntent())
                    }
                )
            }

            item {
                PermissionItem(
                    title = "No Molestar (DND)",
                    description = "Necesario para la acción de silenciar el teléfono.",
                    isGranted = hasDnd,
                    onClick = {
                        context.startActivity(permissionManager.getDndIntent())
                    }
                )
            }

            item {
                PermissionItem(
                    title = "Notificaciones",
                    description = "Necesario para la acción de Mostrar Notificación.",
                    isGranted = hasNotifications,
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                )
            }

            item {
                PermissionItem(
                    title = "Bluetooth",
                    description = "Necesario para detectar cuando conectas dispositivos.",
                    isGranted = hasBluetooth,
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            bluetoothLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                        }
                    }
                )
            }

            item {
                PermissionItem(
                    title = "Ubicación",
                    description = "Necesario para detectar si llegas a casa o a una zona Wi-Fi.",
                    isGranted = hasLocation,
                    onClick = {
                        locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                )
            }

            item {
                PermissionItem(
                    title = "Enviar SMS",
                    description = "Necesario para automatizar el envío de mensajes de texto.",
                    isGranted = hasSms,
                    onClick = {
                        smsLauncher.launch(Manifest.permission.SEND_SMS)
                    }
                )
            }

            item {
                PermissionItem(
                    title = "Modificar ajustes",
                    description = "Necesario para la acción de Cambiar Brillo.",
                    isGranted = hasWriteSettings,
                    onClick = {
                        context.startActivity(permissionManager.getWriteSettingsIntent())
                    }
                )
            }
            
            item {
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer)
                ) {
                    Text("Volver", color = OnPrimaryContainer)
                }
            }
        }
    }
}

@Composable
fun PermissionItem(title: String, description: String, isGranted: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
        modifier = Modifier.fillMaxWidth().border(1.dp, if (isGranted) PrimaryContainer.copy(alpha=0.5f) else OutlineVariant, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text(description, color = OnSurfaceVariant, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onClick,
                enabled = !isGranted,
                colors = ButtonDefaults.buttonColors(containerColor = if (isGranted) Color.Transparent else PrimaryContainer)
            ) {
                Text(if (isGranted) "✓ Concedido" else "Conceder")
            }
        }
    }
}
