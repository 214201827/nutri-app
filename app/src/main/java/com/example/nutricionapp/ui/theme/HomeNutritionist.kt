package com.example.nutricionapp

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

data class Reminder(val patientName: String, val message: String)
data class Patient(val name: String, val age: Int, val diet: String)
data class Notification(val title: String, val message: String)

@Composable
fun HomeNutritionist(navController: NavHostController) {
    val reminders = remember {
        listOf(
            Reminder("Juan Pérez", "Actualizar dieta"),
            Reminder("María López", "Cita el 30 de septiembre"),
            Reminder("Carlos García", "Enviar resultados de análisis"),
            Reminder("Laura Martínez", "Cita el 5 de octubre")
        )
    }

    val notifications = remember {
        listOf(
            Notification("Mensaje de Maria", "xxxxxxx."),
            Notification("Dieta Actualizada", "La dieta de María López ha sido actualizada."),
            Notification("Resultados de Análisis", "Los resultados de análisis de Carlos García están disponibles.")
        )
    }

    var selectedItem by remember { mutableStateOf(0) } // Mantener el ítem seleccionado
    var currentScreen by remember { mutableStateOf("home") } // Controlar qué pantalla mostrar

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f) // Permite que la Box tome el espacio disponible
                .background(Color(0xFF65558F))
        ) {
            when (currentScreen) {
                "home" -> {
                    Column(
                        modifier = Modifier
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
                            items(reminders.size) { index ->
                                ReminderItem(reminder = reminders[index])
                            }
                        }
                    }
                }
                "patients" -> {
                    PatientListScreen(onBackClick = { currentScreen = "home" })
                }
                "notifications" -> {
                    NotificationScreen(notifications, onBackClick = { currentScreen = "home" })
                }
            }
        }

        NavigationBar(
            containerColor = Color(0xFF4B3D6E)
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") },
                selected = selectedItem == 0,
                onClick = {
                    selectedItem = 0
                    currentScreen = "home"
                }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "Pacientes") },
                label = { Text("Pacientes") },
                selected = selectedItem == 1,
                onClick = {
                    selectedItem = 1
                    currentScreen = "patients" // Mostrar la lista de pacientes
                }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones") },
                label = { Text("Notificaciones") },
                selected = selectedItem == 2,
                onClick = {
                    selectedItem = 2
                    currentScreen = "notifications" // Mostrar la pantalla de notificaciones
                }
            )
        }
    }
}

@Composable
fun PatientListScreen(onBackClick: () -> Unit) {
    val patients = remember {
        listOf(
            Patient("Juan Pérez", 30, "Dieta balanceada"),
            Patient("María López", 25, "Dieta keto"),
            Patient("Carlos García", 40, "Dieta vegetariana"),
            Patient("Laura Martínez", 28, "Dieta mediterránea")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp),
    ) {

        Text(
            text = "Lista de Pacientes",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de pacientes
        LazyColumn {
            items(patients.size) { index ->
                PatientItem(patient = patients[index])
            }
        }
    }
}

@Composable
fun NotificationScreen(notifications: List<Notification>, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp),
    ) {
        Text(
            text = "Notificaciones",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de notificaciones
        LazyColumn {
            items(notifications.size) { index ->
                NotificationItem(notification = notifications[index])
            }
        }
    }
}

@Composable
fun ReminderItem(reminder: Reminder) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = reminder.patientName,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = reminder.message,
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun PatientItem(patient: Patient) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = patient.name,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = "Edad: ${patient.age}",
                fontSize = 14.sp,
                color = Color.LightGray
            )
            Text(
                text = "Dieta: ${patient.diet}",
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = notification.title,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeNutritionistPreview() {
    NutricionAppTheme {
        HomeNutritionist(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PatientListScreenPreview() {
    NutricionAppTheme {
        PatientListScreen(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NutricionAppTheme {
        NotificationScreen(
            notifications = listOf(
                Notification("Recordatorio de Cita", "Juan Pérez tiene una cita el 30 de septiembre."),
                Notification("Dieta Actualizada", "La dieta de María López ha sido actualizada.")
            ),
            onBackClick = {}
        )
    }
}