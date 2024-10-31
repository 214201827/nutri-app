package com.example.myapplication000

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.myapplication000.FirestoreRepository.getnotf
import com.example.myapplication000.db.notify


@Composable
fun NotificationListScreen(navController: NavController) {
    val notifications = remember { mutableStateListOf<notify>() }

    // Llama a la función getnotf para obtener los datos de Firestore
    getnotf { newNotifications ->
        notifications.clear()
        notifications.addAll(newNotifications)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B3D6E)) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { navController.navigate("reminders") }, // Navegar a pantalla de inicio
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
                    onClick = { navController.navigate("pacien") }, // Navegar a pantalla de pacientes
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
                    selected = true, // Marca esta pestaña como seleccionada
                    onClick = { /* Ya estamos en la pantalla de notificaciones */ },
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
        // Muestra las notificaciones en Cards
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF65558F))
                .padding(16.dp)
        ) {
            Text(
                text = "Notificaciones",
                fontSize = 24.sp,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(16.dp))
            notifications.forEach { notification ->
                NotificationItem(notification)
            }
        }
    }
}

// Composable para mostrar una notificación
@Composable
fun NotificationItem(notification: notify) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E)),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = notification.titulo,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = notification.descripcion,
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar Paciente",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}