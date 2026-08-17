/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.templates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.hilt.navigation.compose.hiltViewModel
import com.droidflow.ui.home.FlowViewModel
import com.droidflow.data.local.FlowEntity

@Composable
fun TemplatesScreen(
    viewModel: FlowViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Populares") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Header Section
        Text(
            text = "Descubrir Plantillas",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 40.sp,
            letterSpacing = (-0.5).sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Automatizaciones preconfiguradas diseñadas para optimizar tu dispositivo. Selecciona una plantilla para comenzar.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Categories Filter
        val filters = listOf(
            FilterData("Populares", Icons.Filled.Star, selectedFilter == "Populares"),
            FilterData("Casa", Icons.Outlined.Home, selectedFilter == "Casa"),
            FilterData("Coche", Icons.Outlined.DirectionsCar, selectedFilter == "Coche"),
            FilterData("Audio", Icons.Outlined.Headphones, selectedFilter == "Audio"),
            FilterData("Batería", Icons.Outlined.BatteryStd, selectedFilter == "Batería"),
            FilterData("Redes", Icons.Outlined.Wifi, selectedFilter == "Redes")
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filters) { filter ->
                FilterChipCustom(filter, onClick = { selectedFilter = filter.label })
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        val templates = listOf(
            TemplateData(
                title = "Modo noche",
                description = "Reduce el brillo, activa no molestar y desactiva el Wi-Fi automáticamente según tu horario.",
                icon = Icons.Outlined.Bedtime,
                iconColor = Color(0xFF7C4DFF),
                actionsCount = 3,
                previewIcons = listOf(Icons.Outlined.Schedule, Icons.Outlined.DoNotDisturbOn, Icons.Outlined.WifiOff),
                glowColor = Color(0xFF7C4DFF),
                isPrimary = true,
                category = "Casa",
                triggerType = "TIME",
                conditionsJson = "[\"23:00\"]",
                actionsJson = "[{\"type\":\"BRIGHTNESS\", \"level\":10}, {\"type\":\"DND\", \"enable\":true}, {\"type\":\"WIFI\", \"enable\":false}]"
            ),
            TemplateData(
                title = "Ahorro extremo",
                description = "Si la batería baja del 15%, desactiva servicios innecesarios y reduce el tiempo de pantalla.",
                icon = Icons.Outlined.BatterySaver,
                iconColor = Color(0xFFD4BBFF),
                actionsCount = 3,
                previewIcons = listOf(Icons.Outlined.BatteryAlert, Icons.Outlined.BluetoothDisabled, Icons.Outlined.BrightnessLow),
                glowColor = Color(0xFFD4BBFF),
                isPrimary = false,
                category = "Batería",
                triggerType = "BATTERY",
                conditionsJson = "[\"< 15%\"]",
                actionsJson = "[{\"type\":\"BLUETOOTH\", \"enable\":false}, {\"type\":\"WIFI\", \"enable\":false}, {\"type\":\"BRIGHTNESS\", \"level\":10}]"
            ),
            TemplateData(
                title = "Modo conducción",
                description = "Al conectar al Bluetooth del coche, abre mapas, sube el volumen y lee mensajes en voz alta.",
                icon = Icons.Outlined.DirectionsCar,
                iconColor = Color(0xFFCCC2E0),
                actionsCount = 3,
                previewIcons = listOf(Icons.Outlined.BluetoothConnected, Icons.Outlined.Map, Icons.Outlined.VolumeUp),
                glowColor = Color(0xFFCCC2E0),
                isPrimary = false,
                category = "Coche",
                triggerType = "BLUETOOTH",
                conditionsJson = "[]",
                actionsJson = "[{\"type\":\"OPEN_APP\", \"packageName\":\"com.google.android.apps.maps\"}, {\"type\":\"VOLUME\", \"level\":100}, {\"type\":\"TTS\", \"text\":\"Modo conducción activado\"}]"
            ),
            TemplateData(
                title = "Silenciar en reuniones",
                description = "Pone el teléfono en vibración rápidamente.",
                icon = Icons.Outlined.EventBusy,
                iconColor = Color(0xFF7C4DFF),
                actionsCount = 1,
                previewIcons = listOf(Icons.Outlined.DateRange, Icons.Outlined.Vibration),
                glowColor = Color(0xFF7C4DFF),
                isPrimary = false,
                category = "Populares",
                triggerType = "MANUAL",
                conditionsJson = "[]",
                actionsJson = "[{\"type\":\"VIBRATE\"}]"
            ),
            TemplateData(
                title = "Llegar a casa",
                description = "Conecta el Wi-Fi y ajusta el volumen al llegar a casa.",
                icon = Icons.Outlined.Home,
                iconColor = Color(0xFF4CAF50),
                actionsCount = 2,
                previewIcons = listOf(Icons.Outlined.LocationOn, Icons.Outlined.Wifi),
                glowColor = Color(0xFF4CAF50),
                isPrimary = true,
                category = "Casa",
                triggerType = "MANUAL",
                conditionsJson = "[]",
                actionsJson = "[{\"type\":\"WIFI\", \"enable\":true}, {\"type\":\"VOLUME\", \"level\":60}]"
            ),
            TemplateData(
                title = "Audio Bluetooth",
                description = "Abre Spotify automáticamente al conectar tus auriculares.",
                icon = Icons.Outlined.Headphones,
                iconColor = Color(0xFF03A9F4),
                actionsCount = 1,
                previewIcons = listOf(Icons.Outlined.BluetoothConnected, Icons.Outlined.PlayArrow),
                glowColor = Color(0xFF03A9F4),
                isPrimary = false,
                category = "Audio",
                triggerType = "BLUETOOTH",
                conditionsJson = "[]",
                actionsJson = "[{\"type\":\"OPEN_APP\", \"packageName\":\"com.spotify.music\"}]"
            ),
            TemplateData(
                title = "Desconexión Total",
                description = "Desactiva todas las conexiones de red con un solo toque.",
                icon = Icons.Outlined.WifiOff,
                iconColor = Color(0xFFF44336),
                actionsCount = 2,
                previewIcons = listOf(Icons.Outlined.WifiOff, Icons.Outlined.BluetoothDisabled),
                glowColor = Color(0xFFF44336),
                isPrimary = false,
                category = "Redes",
                triggerType = "MANUAL",
                conditionsJson = "[]",
                actionsJson = "[{\"type\":\"WIFI\", \"enable\":false}, {\"type\":\"BLUETOOTH\", \"enable\":false}]"
            ),
            TemplateData(
                title = "Optimización de Batería",
                description = "Activa el ahorro de energía durante la noche para evitar consumos inesperados.",
                icon = Icons.Outlined.BatteryAlert,
                iconColor = Color(0xFFFFC107),
                actionsCount = 2,
                previewIcons = listOf(Icons.Outlined.Schedule, Icons.Outlined.BatterySaver),
                glowColor = Color(0xFFFFC107),
                isPrimary = false,
                category = "Batería",
                triggerType = "TIME",
                conditionsJson = "[\"23:30\"]",
                actionsJson = "[{\"type\":\"WIFI\", \"enable\":false}, {\"type\":\"BRIGHTNESS\", \"level\":20}]"
            )
        )
        
        var showNameDialog by remember { mutableStateOf<TemplateData?>(null) }
        var customName by remember { mutableStateOf("") }

        val displayedTemplates = if (selectedFilter == "Populares") {
            templates
        } else {
            templates.filter { it.category == selectedFilter }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(displayedTemplates) { template ->
                TemplateCard(template, onUseTemplate = {
                    customName = template.title
                    showNameDialog = template
                })
            }
        }

        if (showNameDialog != null) {
            AlertDialog(
                onDismissRequest = { showNameDialog = null },
                title = { Text("Nombre del Flujo") },
                text = {
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        val template = showNameDialog!!
                        val newFlow = FlowEntity(
                            name = customName.ifBlank { template.title },
                            description = template.description,
                            isEnabled = false,
                            triggerType = template.triggerType,
                            conditionsJson = template.conditionsJson,
                            actionsJson = template.actionsJson
                        )
                        viewModel.insertFlow(newFlow)
                        showNameDialog = null
                        onNavigateHome()
                    }) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNameDialog = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun FilterChipCustom(data: FilterData, onClick: () -> Unit) {
    val backgroundColor = if (data.isActive) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    val borderColor = if (data.isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    val textColor = if (data.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    val iconColor = if (data.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        shape = CircleShape,
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = data.label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun TemplateCard(template: TemplateData, onUseTemplate: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Action */ }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(template.glowColor.copy(alpha = 0.15f))
                    .blur(32.dp)
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header (Icon & Action count)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = template.icon,
                            contentDescription = null,
                            tint = template.iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "${template.actionsCount} Acciones",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Title and description
                Text(
                    text = template.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = template.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Visual Logic Preview
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
                ) {
                    template.previewIcons.forEachIndexed { index, icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = template.iconColor
                        )
                        if (index < template.previewIcons.size - 1) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .width(16.dp)
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Use Template Button
                if (template.isPrimary) {
                    Button(
                        onClick = onUseTemplate,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C4DFF),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Usar Plantilla",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = onUseTemplate,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "Usar Plantilla",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

data class FilterData(
    val label: String,
    val icon: ImageVector,
    val isActive: Boolean
)

data class TemplateData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color,
    val actionsCount: Int,
    val previewIcons: List<ImageVector>,
    val glowColor: Color,
    val isPrimary: Boolean,
    val category: String,
    val triggerType: String,
    val conditionsJson: String,
    val actionsJson: String
)
