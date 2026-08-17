/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.droidflow.core.theme.*
import com.droidflow.data.local.FlowEntity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.draw.scale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: FlowViewModel = hiltViewModel(),
    onNavigateToBuilder: (Long?) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToTemplates: () -> Unit,
    onNavigateToPermissions: () -> Unit
) {
    val flows by viewModel.flows.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            PrimaryGradientButton(text = "+ Crear flujo", onClick = { onNavigateToBuilder(null) })
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (flows.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay flujos. Crea uno nuevo.", color = OnSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(flows) { flow ->
                        FlowCard(
                            flow = flow,
                            onToggle = { isEnabled -> viewModel.toggleFlow(flow, isEnabled) },
                            onDelete = { viewModel.deleteFlow(flow) },
                            onClick = { onNavigateToDetail(flow.id) },
                            onPlay = { viewModel.executeFlow(flow.id.toString()) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
    }
}

@Composable
fun PrimaryGradientButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PrimaryContainer, PrimaryGradientEnd)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = OnPrimaryContainer, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun FlowCard(flow: FlowEntity, onToggle: (Boolean) -> Unit, onDelete: () -> Unit, onClick: () -> Unit, onPlay: () -> Unit) {
    val alpha = if (flow.isEnabled) 1f else 0.7f
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(SecondaryContainer.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (flow.triggerType) {
                            "TIME" -> Icons.Default.Schedule
                            "BATTERY" -> Icons.Default.BatteryFull
                            else -> Icons.Default.Bolt
                        }
                        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = flow.name, color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha), fontWeight = FontWeight.Medium, fontSize = 16.sp)
                        Text(text = if (flow.isEnabled) "Activo" else "Desactivado", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha), fontSize = 11.sp)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (flow.triggerType == "MANUAL") {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable { onPlay() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                        }
                    }
                    Switch(
                        checked = flow.isEnabled,
                        onCheckedChange = { onToggle(it) },
                        modifier = Modifier.scale(0.8f),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.surface,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Logic Visualizer
            Box(modifier = Modifier.padding(start = 19.dp)) {
                // Vertical Line
                Box(modifier = Modifier
                    .width(1.dp)
                    .height(64.dp)
                    .absoluteOffset(x = 3.dp, y = 12.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.primary, CircleShape).border(4.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("SI", color = MaterialTheme.colorScheme.primary, fontSize = 11.sp, modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha=0.1f), RoundedCornerShape(4.dp)).border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha=0.2f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (flow.triggerType == "MANUAL") "Ejecución manual" else flow.triggerType, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.secondary, CircleShape).border(4.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("ENTONCES", color = MaterialTheme.colorScheme.secondary, fontSize = 11.sp, modifier = Modifier.background(MaterialTheme.colorScheme.secondary.copy(alpha=0.1f), RoundedCornerShape(4.dp)).border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha=0.2f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Acciones configuradas", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

