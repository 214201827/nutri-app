package com.example.nutricionapp.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.PacienteDb
import com.example.nutricionapp.notificaciones.Notifications
import com.example.nutricionapp.notificaciones.listenToNotifications


@Composable
fun MainPatient (patientId: String ,navController: NavHostController) {

   // var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    var selectedItem by remember { mutableStateOf(1) }
    var currentScreen by remember { mutableStateOf("dietas") }
    var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    val NutId = paciente?.Nid ?: ""
    val notifications = remember { mutableStateListOf<Map<String, Any>>() }

    //obtner datos de nutriologo asginado
    LaunchedEffect(patientId) {
        FirestoreRepository.getPatientData(patientId) { data ->
            paciente = data
        }
    }
    LaunchedEffect(patientId) {
        listenToNotifications(patientId) { newNotifications ->
            notifications.clear()
            notifications.addAll(newNotifications)
        }
    }
    // Conteo de notificaciones no leídas
    val unreadCount = notifications.count { !(it["read"] as Boolean) }

    // Utilizar Scaffold para integrar SnackbarHost y BottomBar
    Scaffold(
       // snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF4B3D6E)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        currentScreen = "inicio"
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Dietas") },
                    label = { Text("Dietas") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        currentScreen = "dietas"
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
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
                "inicio" -> { // pantalla de inicio
                    PerfilPatient(patientId, navController)
                }
                "dietas" -> { // pantalla de vista dieta
                    DietaPatient(patientId, navController, NutId)
                }
                "notificaciones" -> {
                    // Pantalla de notificaciones
                    Notifications(patientId, navController)

                }
                // Otras pantallas...
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPatientIncio() {
    val navController = rememberNavController()
    MainPatient(navController = navController ,patientId = "1")
}
