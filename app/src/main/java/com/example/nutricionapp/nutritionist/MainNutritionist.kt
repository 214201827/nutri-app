package com.example.nutricionapp.nutritionist


import ListPatNutritionist
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


@Composable
fun MainPatient (patientId: String ,navController: NavHostController) {

    // var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    var selectedItem by remember { mutableStateOf(0) }
    var currentScreen by remember { mutableStateOf("pacientes") }


    // Utilizar Scaffold para integrar SnackbarHost y BottomBar
    Scaffold(
        // snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF4B3D6E)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Pacientes") },
                    label = { Text("Pacientes") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        currentScreen = "pacientes"
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Recordatorios") },
                    label = { Text("Recordatorios") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        currentScreen = "recordatorios"
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones") },
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
                "pacientes" -> { // pantalla de inicio
                    ListPatNutritionist(navController)
                }
                "recordatorios" -> { // pantalla de vista dieta

                }
                "notificaciones" -> {
                    // Pantalla de notificaciones
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