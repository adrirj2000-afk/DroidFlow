/*
 * Developed by ZortVibes
 * Copyright (c) 2026. All rights reserved.
 */
package com.droidflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.droidflow.core.theme.DroidFlowTheme
import com.droidflow.ui.home.HomeScreen
import com.droidflow.ui.components.BottomNavBar
import com.droidflow.ui.detail.FlowDetailScreen
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import com.droidflow.core.preferences.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Iniciar servicio permanente
        try {
            val serviceIntent = android.content.Intent(this, com.droidflow.domain.engine.DroidFlowBackgroundService::class.java)
            androidx.core.content.ContextCompat.startForegroundService(this, serviceIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setContent {
            val themeMode by preferencesManager.themeMode.collectAsState()
            val systemTheme = isSystemInDarkTheme()
            val isDarkTheme = when (themeMode) {
                "Claro" -> false
                "Oscuro" -> true
                else -> systemTheme
            }

            DroidFlowTheme(darkTheme = isDarkTheme) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
                        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
                    ) { _ -> }
                    androidx.compose.runtime.LaunchedEffect(Unit) {
                        if (androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    bottomBar = {
                        // The user requested the Bottom Navigation to be visible in all screens 
                        // exactly like the screenshots (including Builder and Detail).
                        BottomNavBar(
                            currentRoute = currentRoute?.substringBefore("/"),
                            onNavigateToHome = { navController.navigate("home") },
                            onNavigateToTemplates = { navController.navigate("templates") },
                            onNavigateToSettings = { navController.navigate("settings") }
                        )
                    }
                ) { padding ->
                    NavHost(
                        navController = navController, 
                        startDestination = "home",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("home") {
                            HomeScreen(
                                onNavigateToBuilder = { flowId -> 
                                    if (flowId != null) navController.navigate("builder?flowId=$flowId")
                                    else navController.navigate("builder")
                                },
                                onNavigateToDetail = { flowId ->
                                    navController.navigate("detail/$flowId")
                                },
                                onNavigateToTemplates = { navController.navigate("templates") },
                                onNavigateToPermissions = { navController.navigate("permissions") }
                            )
                        }
                        composable(
                            route = "builder?flowId={flowId}",
                            arguments = listOf(androidx.navigation.navArgument("flowId") { 
                                type = androidx.navigation.NavType.StringType
                                nullable = true 
                            })
                        ) {
                            com.droidflow.ui.builder.BuilderScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = "detail/{flowId}",
                            arguments = listOf(androidx.navigation.navArgument("flowId") { 
                                type = androidx.navigation.NavType.StringType 
                            })
                        ) {
                            com.droidflow.ui.detail.FlowDetailScreen(
                                onNavigateToBuilder = { flowId ->
                                    navController.navigate("builder?flowId=$flowId")
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable("history") {
                            com.droidflow.ui.history.HistorySettingsScreen(
                                onNavigateToPermissions = { navController.navigate("permissions") }
                            )
                        }
                        composable("templates") {
                            com.droidflow.ui.templates.TemplatesScreen(
                                onNavigateHome = {
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                }
                            )
                        }
                        composable("permissions") {
                            com.droidflow.ui.permissions.PermissionsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("settings") {
                            // Routed to HistorySettingsScreen because it contains both
                            com.droidflow.ui.history.HistorySettingsScreen(
                                onNavigateToPermissions = { navController.navigate("permissions") }
                            )
                        }
                    }
                }
            }
        }
    }
}
