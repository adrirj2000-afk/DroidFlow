/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.builder

import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.droidflow.core.theme.*
import java.util.Calendar

enum class AppSelectorMode {
    TRIGGER, ACTION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuilderScreen(
    viewModel: BuilderViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val packageManager = context.packageManager

    var triggerExpanded by remember { mutableStateOf(false) }
    var triggerSelected by remember { mutableStateOf<String?>(null) }
    var showAccessibilityDialog by remember { mutableStateOf(false) }
    var triggerLabel by remember { mutableStateOf("Elige qu茅 debe activar tu flujo") }
    var conditionsJson by remember { mutableStateOf("[]") }
    var flowName by remember { mutableStateOf("Flujo Personalizado") }

    var actionExpanded by remember { mutableStateOf(false) }
    val selectedActions = remember { mutableStateListOf<ActionItem>() }

    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { grantedPermissions ->
            val allGranted = grantedPermissions.values.all { it }
            if (allGranted && triggerSelected != null) {
                val combinedJson = "[" + selectedActions.map { it.json.removePrefix("[").removeSuffix("]") }.joinToString(",") + "]"
                viewModel.saveFlow(
                    name = flowName,
                    triggerType = triggerSelected!!,
                    conditionsJson = conditionsJson,
                    actionsJson = combinedJson
                )
                com.droidflow.utils.PermissionUtils.checkSpecialPermissions(context, combinedJson)
                    onNavigateBack()
            }
        }
    )

    var showAppSelector by remember { mutableStateOf(false) }
        var showTextDialog by remember { mutableStateOf<String?>(null) }
        var tempTextValue by remember { mutableStateOf("") }
    var installedApps by remember { mutableStateOf<List<ResolveInfo>>(emptyList()) }
    var appSelectorMode by remember { mutableStateOf(AppSelectorMode.TRIGGER) }

    var showBatteryDialog by remember { mutableStateOf(false) }
    var batterySliderValue by remember { mutableStateOf(20f) }

    var showActionSliderDialog by remember { mutableStateOf(false) }
    var actionSliderType by remember { mutableStateOf("") }
    var actionSliderValue by remember { mutableStateOf(50f) }

    var showActionTextDialog by remember { mutableStateOf(false) }
    var actionTextType by remember { mutableStateOf("") }
    var actionTextValue by remember { mutableStateOf("") }

    var showActionSmsDialog by remember { mutableStateOf(false) }
    var actionSmsPhone by remember { mutableStateOf("") }
    var actionSmsMessage by remember { mutableStateOf("") }

        val triggers = listOf(
        Triple("NOTIFICATION", "Notificaci髇 recibida", ""),
        Triple("CALL_RECEIVED", "Llamada entrante", ""),
        Triple("SMS_RECEIVED", "SMS recibido", ""),
        Triple("TIME", "Hora exacta", ""),
        Triple("TIME", "Hora exacta", ""),
        Triple("BATTERY", "Bater铆a baja", ""),
        Triple("BLUETOOTH", "Conexi贸n Bluetooth", "[\"Car Audio\"]"),
        Triple("BATTERY_FULL", "Bater铆a llena (100%)", ""),
        Triple("CHARGER_CONNECTED", "Cargador conectado", ""),
        Triple("CHARGER_DISCONNECTED", "Cargador desconectado", ""),
        Triple("APP_OPENED", "Aplicaci贸n abierta", ""),
        Triple("WIFI_CONNECTED", "WiFi conectado", ""),
        Triple("WIFI_DISCONNECTED", "WiFi desconectado", ""),
        Triple("AIRPLANE_MODE_ON", "Modo Avi贸n activado", ""),
        Triple("AIRPLANE_MODE_OFF", "Modo Avi贸n desactivado", ""),
        Triple("HEADPHONES_CONNECTED", "Auriculares conectados", ""),
        Triple("SCREEN_ON", "Pantalla encendida", ""),
        Triple("SCREEN_OFF", "Pantalla apagada", "")
    )

    val actions = listOf(
        Triple("REJECT_CALL", "Rechazar Llamada", "[{\"type\":\"REJECT_CALL\"}]"),
        Triple("SYSTEM_BUTTON", "Bot髇 del Sistema", ""),
        Triple("BACKGROUND_SMS", "SMS en Segundo Plano", ""),
        Triple("VIBRATE", "Vibraci贸n", "[{\"type\":\"VIBRATE\"}]"),
        Triple("NOTIFICATION", "Notificaci贸n", "[{\"type\":\"NOTIFICATION\"}]"),
        Triple("VOLUME", "Bajar volumen", "[{\"type\":\"VOLUME\", \"level\":0}]"),
        Triple("BRIGHTNESS", "Bajar brillo", "[{\"type\":\"BRIGHTNESS\", \"level\":10}]"),
        Triple("OPEN_APP", "Abrir App", ""),
        Triple("BLUETOOTH_ON", "Activar Bluetooth", "[{\"type\":\"BLUETOOTH_ON\"}]"),
        Triple("BLUETOOTH_OFF", "Desactivar Bluetooth", "[{\"type\":\"BLUETOOTH_OFF\"}]"),
        Triple("WIFI_ON", "Activar WiFi", "[{\"type\":\"WIFI_ON\"}]"),
        Triple("WIFI_OFF", "Desactivar WiFi", "[{\"type\":\"WIFI_OFF\"}]"),
        Triple("DND_ON", "Activar No Molestar", "[{\"type\":\"DND_ON\"}]"),
        Triple("DND_OFF", "Desactivar No Molestar", "[{\"type\":\"DND_OFF\"}]"),
        Triple("FLASHLIGHT_ON", "Encender Linterna", "[{\"type\":\"FLASHLIGHT_ON\"}]"),
        Triple("FLASHLIGHT_OFF", "Apagar Linterna", "[{\"type\":\"FLASHLIGHT_OFF\"}]"),
        Triple("WALLPAPER", "Cambiar Fondo de Pantalla", "[{\"type\":\"WALLPAPER\"}]"),
        Triple("SOUND_PLAY", "Reproducir Sonido", "[{\"type\":\"SOUND_PLAY\"}]"),
        Triple("TTS", "Leer Texto (TTS)", "[{\"type\":\"TTS\"}]"),
        Triple("BATTERY_SAVER", "Ahorro de Bater铆a", "[{\"type\":\"BATTERY_SAVER\"}]"),
        Triple("SMS_SEND", "Enviar SMS", "[{\"type\":\"SMS_SEND\"}]"),
        Triple("SCREEN_OFF", "Bloquear Pantalla", "[{\"type\":\"SCREEN_OFF\"}]"),
        Triple("MEDIA_PLAY_PAUSE", "Reproducir/Pausar Multimedia", "[{\"type\":\"MEDIA_PLAY_PAUSE\"}]"),
        Triple("MEDIA_NEXT", "Siguiente Canci贸n", "[{\"type\":\"MEDIA_NEXT\"}]"),
        Triple("HTTP_REQUEST", "Petici贸n HTTP", "[{\"type\":\"HTTP_REQUEST\"}]"),
        Triple("WHATSAPP_SEND", "Enviar WhatsApp", "")
    )

    val loadedFlow by viewModel.loadedFlow.collectAsState()

    LaunchedEffect(loadedFlow) {
        loadedFlow?.let { flow ->
            flowName = flow.name
            triggerSelected = flow.triggerType
            conditionsJson = flow.conditionsJson
            
            val triggerMatch = triggers.find { it.first == flow.triggerType }
            if (flow.triggerType == "APP_OPENED") {
                val pkgRegex = """\["APP_OPENED",\s*"([^"]+)"\]""".toRegex()
                val pkgName = pkgRegex.find(flow.conditionsJson)?.groupValues?.get(1)
                if (pkgName != null) {
                    try {
                        val appInfo = packageManager.getApplicationInfo(pkgName, 0)
                        val name = packageManager.getApplicationLabel(appInfo).toString()
                        triggerLabel = "Al abrir $name"
                    } catch (e: Exception) {
                        triggerLabel = "Al abrir $pkgName"
                    }
                } else {
                    triggerLabel = triggerMatch?.second ?: flow.triggerType
                }
            } else if (flow.triggerType == "TIME") {
                val timeRegex = """\["([^"]+)"\]""".toRegex()
                val timeStr = timeRegex.find(flow.conditionsJson)?.groupValues?.get(1)
                triggerLabel = "Hora exacta: ${timeStr ?: ""}"
            } else if (flow.triggerType == "BATTERY") {
                val batRegex = """\["<([^%]+)%"\]""".toRegex()
                val batVal = batRegex.find(flow.conditionsJson)?.groupValues?.get(1)
                if (batVal != null) {
                    triggerLabel = "Bater铆a < $batVal%"
                    batterySliderValue = batVal.toFloatOrNull() ?: 20f
                } else {
                    triggerLabel = triggerMatch?.second ?: flow.triggerType
                }
            } else {
                triggerLabel = triggerMatch?.second ?: flow.triggerType
            }

            selectedActions.clear()
            val objRegex = """\{[^}]+\}""".toRegex()
            val typeRegex = """"type"\s*:\s*"([^"]+)"""".toRegex()
            val objects = objRegex.findAll(flow.actionsJson).map { it.value }.toList()
            
            objects.forEach { obj ->
                val type = typeRegex.find(obj)?.groupValues?.get(1)
                if (type != null) {
                    val actionMatch = actions.find { it.first == type }
                    if (type == "OPEN_APP") {
                        val pkgRegex = """"packageName"\s*:\s*"([^"]+)"""".toRegex()
                        val pkgName = pkgRegex.find(obj)?.groupValues?.get(1)
                        var name = pkgName ?: "App"
                        if (pkgName != null) {
                            try {
                                val appInfo = packageManager.getApplicationInfo(pkgName, 0)
                                name = packageManager.getApplicationLabel(appInfo).toString()
                            } catch (e: Exception) {}
                        }
                        selectedActions.add(ActionItem(type, "Abrir $name", "[$obj]"))
                    } else {
                        selectedActions.add(ActionItem(type, actionMatch?.second ?: type, "[$obj]"))
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { 
                        Text("DroidFlow", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) 
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    actions = {
                        if (loadedFlow != null) {
                            IconButton(onClick = { 
                                viewModel.deleteFlow()
                                onNavigateBack()
                            }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        } else {
                            IconButton(onClick = { }) {
                                Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f))
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            
            // Flow Line MaterialTheme.colorScheme.background
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 100.dp, bottom = 100.dp)
                    .width(2.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                
                // Header text
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Crear nuevo flujo", 
                            fontSize = 28.sp, 
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 36.sp
                        )
                        Text(
                            "Configura el comportamiento autom谩tico.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                OutlinedTextField(
                    value = flowName,
                    onValueChange = { flowName = it },
                    label = { Text("Nombre del Flujo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )

                // Block 1: Trigger
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(20.dp, RoundedCornerShape(12.dp), ambientColor = MaterialTheme.colorScheme.primaryContainer, spotColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.05f))
                        .border(1.dp, MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Bolt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("CUANDO", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 0.15.sp)
                                Text("CONDICI脫N INICIAL", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, letterSpacing = 0.5.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = triggerLabel,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontStyle = FontStyle.Italic,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Box {
                            OutlinedButton(
                                onClick = { triggerExpanded = true },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("A帽adir condici贸n", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                selectedActions.forEachIndexed { index, action ->
                    // Connector Node (Icon overlay)
                    Box(
                        modifier = Modifier
                            .offset(y = (-16).dp)
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.background, CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape)
                            .zIndex(2f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.ArrowDownward, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }

                    // Block 2: Action
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-32).dp)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("ENTONCES", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 0.15.sp)
                                        Text("ACCI脫N A EJECUTAR", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, letterSpacing = 0.5.sp)
                                    }
                                }
                                IconButton(onClick = { selectedActions.removeAt(index) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = action.label,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // Final Connector before Add button
                Box(
                    modifier = Modifier
                        .offset(y = (-16).dp)
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape)
                        .zIndex(2f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.ArrowDownward, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                }

                // Add Action Button
                Box(modifier = Modifier.fillMaxWidth().offset(y = (-32).dp)) {
                    Button(
                        onClick = { actionExpanded = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("A帽adir acci贸n", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Floating Action Save
                Button(
                    onClick = {
                        if (triggerSelected != null && selectedActions.isNotEmpty()) {
                            val combinedJson = "[" + selectedActions.map { it.json.removePrefix("[").removeSuffix("]") }.joinToString(",") + "]"
                            val requiredPerms = com.droidflow.utils.PermissionUtils.getRequiredPermissions(triggerSelected!!, combinedJson)
                            
                            if (requiredPerms.contains("ACCESSIBILITY_SCREEN_OFF")) {
                                showAccessibilityDialog = true
                            } else {
                                val normalPerms = requiredPerms.filter { it != "ACCESSIBILITY_SCREEN_OFF" }.toTypedArray()
                                if (normalPerms.isNotEmpty()) {
                                    permissionLauncher.launch(normalPerms)
                                } else {
                                    viewModel.saveFlow(
                                        name = flowName,
                                        triggerType = triggerSelected!!,
                                        conditionsJson = conditionsJson,
                                        actionsJson = combinedJson
                                    )
                                    com.droidflow.utils.PermissionUtils.checkSpecialPermissions(context, combinedJson)
                    onNavigateBack()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Flujo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (showAccessibilityDialog) {
                AlertDialog(
                    onDismissRequest = { showAccessibilityDialog = false },
                    title = { Text("Permiso Requerido") },
                    text = { Text("Para apagar la pantalla necesitas habilitar el Servicio de Accesibilidad.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showAccessibilityDialog = false
                            val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            context.startActivity(intent)
                        }) {
                            Text("Abrir Ajustes", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAccessibilityDialog = false }) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (triggerExpanded) {
                ModalBottomSheet(
                    onDismissRequest = { triggerExpanded = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    LazyColumn {
                        items(triggers) { (type, label, json) ->
                            ListItem(
                                headlineContent = { Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                modifier = Modifier.clickable {
                                    triggerExpanded = false
                                    triggerSelected = type
                                    if (type == "TIME") {
                                        val c = Calendar.getInstance()
                                        TimePickerDialog(
                                            context,
                                            { _, hour, min ->
                                                val formatted = String.format("%02d:%02d", hour, min)
                                                triggerLabel = "Hora exacta: $formatted"
                                                conditionsJson = "[\"$formatted\"]"
                                            },
                                            c.get(Calendar.HOUR_OF_DAY),
                                            c.get(Calendar.MINUTE),
                                            true
                                        ).show()
                                    } else if (type == "BATTERY") {
                                        showBatteryDialog = true
                                    } else if (type == "APP_OPENED") {
                                        appSelectorMode = AppSelectorMode.TRIGGER
                                        installedApps = packageManager.queryIntentActivities(
                                            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
                                            0
                                        )
                                        showAppSelector = true
                                    } else {
                                        triggerLabel = label
                                        conditionsJson = json
                                    }
                                },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                            )
                        }
                    }
                }
            }

            if (actionExpanded) {
                ModalBottomSheet(
                    onDismissRequest = { actionExpanded = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    LazyColumn {
                        items(actions) { (type, label, json) ->
                            ListItem(
                                headlineContent = { Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                modifier = Modifier.clickable {
                                    actionExpanded = false
                                    when (type) {
                                        "OPEN_APP" -> {
                                            appSelectorMode = AppSelectorMode.ACTION
                                            installedApps = packageManager.queryIntentActivities(
                                                Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
                                                0
                                            )
                                            showAppSelector = true
                                        }
                                        "VOLUME", "BRIGHTNESS" -> {
                                            actionSliderType = type
                                            actionSliderValue = if (type == "VOLUME") 50f else 128f
                                            showActionSliderDialog = true
                                        }
                                        "TTS", "NOTIFICATION" -> {
                                            actionTextType = type
                                            actionTextValue = ""
                                            showActionTextDialog = true
                                        }
                                        "SMS_SEND", "WHATSAPP_SEND" -> {
                                            actionSmsPhone = ""
                                            actionSmsMessage = ""
                                            showActionSmsDialog = true
                                            actionTextType = type // Re-using to know if it's SMS or WA
                                        }
                                        else -> {
                                            selectedActions.add(ActionItem(type, label, json))
                                        }
                                    }
                                },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                            )
                        }
                    }
                }
            }

            
        if (showTextDialog != null) {
            val type = showTextDialog!!
            val title = when (type) {
                "NOTIFICATION" -> "Filtro de Notificaci髇 (Opcional)"
                "CALL_RECEIVED" -> "N鷐ero de Tel閒ono (Opcional)"
                "SMS_RECEIVED" -> "N鷐ero o Nombre (Opcional)"
                else -> "Filtro"
            }
            val label = when (type) {
                "NOTIFICATION" -> "Palabra clave o Nombre"
                else -> "Dejar en blanco para cualquiera"
            }
            AlertDialog(
                onDismissRequest = { showTextDialog = null },
                title = { Text(title) },
                text = {
                    OutlinedTextField(
                        value = tempTextValue,
                        onValueChange = { tempTextValue = it },
                        label = { Text(label) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        conditionsJson = "[\"$tempTextValue\"]"
                        val prefix = when (type) {
                            "NOTIFICATION" -> "Notificaci髇 de"
                            "CALL_RECEIVED" -> "Llamada de"
                            "SMS_RECEIVED" -> "SMS de"
                            else -> type
                        }
                        triggerLabel = if (tempTextValue.isNotBlank()) "$prefix $tempTextValue" else "Cualquier $prefix"
                        showTextDialog = null
                    }) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTextDialog = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
        if (showBatteryDialog) {
                AlertDialog(
                    onDismissRequest = { showBatteryDialog = false },
                    title = { Text("Nivel de bater铆a") },
                    text = {
                        Column {
                            Text("Activar cuando la bater铆a baje al ${batterySliderValue.toInt()}%")
                            Slider(
                                value = batterySliderValue,
                                onValueChange = { batterySliderValue = it },
                                valueRange = 5f..100f,
                                steps = 19
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            triggerLabel = "Bater铆a < ${batterySliderValue.toInt()}%"
                            conditionsJson = "[\"<${batterySliderValue.toInt()}%\"]"
                            showBatteryDialog = false
                        }) {
                            Text("Aceptar", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showBatteryDialog = false }) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showAppSelector) {
                AlertDialog(
                    onDismissRequest = { showAppSelector = false },
                    title = { Text("Selecciona una aplicaci贸n") },
                    text = {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(installedApps) { resolveInfo ->
                                val appName = resolveInfo.loadLabel(packageManager).toString()
                                val packageName = resolveInfo.activityInfo.packageName
                                val icon = remember(resolveInfo) { 
                                    resolveInfo.loadIcon(packageManager).toBitmap(width = 96, height = 96).asImageBitmap() 
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (appSelectorMode == AppSelectorMode.TRIGGER) {
                                                triggerSelected = "APP_OPENED"
                                                triggerLabel = "Al abrir $appName"
                                                conditionsJson = "[\"APP_OPENED\", \"$packageName\"]"
                                            } else {
                                                selectedActions.add(ActionItem("OPEN_APP", "Abrir $appName", "[{\"type\":\"OPEN_APP\", \"packageName\":\"$packageName\"}]"))
                                            }
                                            showAppSelector = false
                                        }
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        bitmap = icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(appName, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showAppSelector = false }) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showActionSliderDialog) {
                val title = if (actionSliderType == "VOLUME") "Nivel de Volumen" else "Nivel de Brillo"
                val range = if (actionSliderType == "VOLUME") 0f..100f else 0f..255f
                AlertDialog(
                    onDismissRequest = { showActionSliderDialog = false },
                    title = { Text(title) },
                    text = {
                        Column {
                            Text("Valor: ${actionSliderValue.toInt()}")
                            Slider(
                                value = actionSliderValue,
                                onValueChange = { actionSliderValue = it },
                                valueRange = range
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val actionLabel = if (actionSliderType == "VOLUME") "Volumen al ${actionSliderValue.toInt()}%" else "Brillo a ${actionSliderValue.toInt()}"
                            val actionJson = "[{\"type\":\"$actionSliderType\", \"level\":${actionSliderValue.toInt()}}]"
                            selectedActions.add(ActionItem(actionSliderType, actionLabel, actionJson))
                            showActionSliderDialog = false
                        }) {
                            Text("Aceptar", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showActionSliderDialog = false }) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showActionTextDialog) {
                val title = if (actionTextType == "TTS") "Texto a voz" else "Mostrar Notificaci贸n"
                AlertDialog(
                    onDismissRequest = { showActionTextDialog = false },
                    title = { Text(title) },
                    text = {
                        OutlinedTextField(
                            value = actionTextValue,
                            onValueChange = { actionTextValue = it },
                            label = { Text("Mensaje") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val actionLabel = if (actionTextType == "TTS") "Leer: $actionTextValue" else "Notificar: $actionTextValue"
                            val actionJson = "[{\"type\":\"$actionTextType\", \"message\":\"$actionTextValue\"}]"
                            selectedActions.add(ActionItem(actionTextType, actionLabel, actionJson))
                            showActionTextDialog = false
                        }) {
                            Text("Aceptar", color = MaterialTheme.colorScheme.primary)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showActionTextDialog = false }) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showActionSmsDialog) {
                val isWhatsapp = actionTextType == "WHATSAPP_SEND"
                AlertDialog(
                    onDismissRequest = { showActionSmsDialog = false },
                    title = { Text(if (isWhatsapp) "Enviar WhatsApp" else "Enviar SMS") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = actionSmsPhone,
                                onValueChange = { actionSmsPhone = it },
                                label = { Text("N煤mero de tel茅fono") },
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                            OutlinedTextField(
                                value = actionSmsMessage,
                                onValueChange = { actionSmsMessage = it },
                                label = { Text("Mensaje") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val typeLabel = if (isWhatsapp) "WhatsApp" else "SMS"
                            val actionType = if (isWhatsapp) "WHATSAPP_SEND" else "SMS_SEND"
                            val actionJson = "[{\"type\":\"$actionType\", \"phoneNumber\":\"$actionSmsPhone\", \"message\":\"$actionSmsMessage\"}]"
                            selectedActions.add(ActionItem(actionType, "$typeLabel a $actionSmsPhone", actionJson))
                            showActionSmsDialog = false
                        }) {
                            Text("Aceptar", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showActionSmsDialog = false }) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.primaryContainer)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class ActionItem(val type: String, val label: String, val json: String)

