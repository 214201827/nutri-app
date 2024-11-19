package com.example.nutricionapp.nutritionist


import ListPatNutritionist
import PatientItem
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.Paciented
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainNutritionist (navController: NavHostController) {

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

            }
        }
    }
}