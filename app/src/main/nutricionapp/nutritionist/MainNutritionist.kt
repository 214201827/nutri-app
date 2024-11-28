package com.example.nutricionapp.nutritionist


import ListPatNutritionist
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.nutricionapp.notificaciones.Notifications
import com.example.nutricionapp.notificaciones.listenToNotifications

@Composable
fun MainNutritionist(NutId: String, navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(1) }
    var currentScreen by remember { mutableStateOf("pacientes") }
    val notifications = remember { mutableStateListOf<Map<String, Any>>() }

    // Escucha las notificaciones asociadas al nutricionista
    LaunchedEffect(NutId) {
        listenToNotifications(NutId) { newNotifications ->
            notifications.clear()
            notifications.addAll(newNotifications)
        }
    }

    // Conteo de notificaciones no leídas
    val unreadCount = notifications.count { !(it["read"] as Boolean) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF4B3D6E)
            ) {
                // Perfil Tab
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        currentScreen = "perfil"
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                // Pacientes Tab
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Pacientes") },
                    label = { Text("Pacientes") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        currentScreen = "pacientes"
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                // Notificaciones Tab con Badge
                NavigationBarItem(
                    icon = {
                        BadgedBox(
                            badge = {
                                if (unreadCount > 0) {
                                    Badge {
                                        Text(unreadCount.toString(), color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones")
                        }
                    },
                    label = { Text("Notificaciones") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        currentScreen = "notificaciones"
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.NoteAlt, contentDescription = "Citas") },
                    label = { Text("Citas") },
                    selected = selectedItem == 3,
                    onClick = {
                        selectedItem = 3
                        currentScreen = "Citas"
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    ) { innerPadding ->
        // Contenido principal
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFF65558F))
        ) {
            when (currentScreen) {
                "perfil" -> { // pantalla de perfil
                    PerfilNutritionist(NutId, navController)
                }
                "pacientes" -> { // pantalla de pacientes
                    ListPatNutritionist(navController)
                }
                "notificaciones" -> { // pantalla de notificaciones
                    Notifications(NutId, navController)
                }
                "Citas" -> {
                    // Pantalla de citas
                    CitasScreen(navController)
                }
            }
        }
    }
}
