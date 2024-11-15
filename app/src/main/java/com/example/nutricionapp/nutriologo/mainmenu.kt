package com.example.nutricionapp.nutriologo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.navigation.NavController
import com.example.nutricionapp.ReminderItem
import com.example.nutricionapp.db.FirestoreRepository.getRecords
import com.example.nutricionapp.db.Record

@Composable
fun RecordatorioScreen(navController: NavController) {
    val records = remember { mutableStateListOf<Record>() }

    // Llama a la función getRecords para obtener los datos de Firestore
    getRecords { newRecords ->
        records.clear()
        records.addAll(newRecords)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B3D6E)) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = true, // Aquí puedes usar una variable para controlar la selección
                    onClick = { /* Ya estamos en la pantalla de pacientes */ }, // Navegar a pantalla de inicio
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Pacientes") },
                    label = { Text("Pacientes") },
                    selected = false,
                    onClick = { navController.navigate("pacien") },
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
                    selected = false,
                    onClick = { navController.navigate("notifications") }, // Navegar a pantalla de notificaciones
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
        // Muestra los registros en Cards
        Box(modifier = Modifier.background(Color(0xFF65558F))){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Recordatorios",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de recordatorios
            LazyColumn {
                items(records.toList()) { record ->
                    ReminderItem(record)
                }
            }
        }
    }
    }
}
