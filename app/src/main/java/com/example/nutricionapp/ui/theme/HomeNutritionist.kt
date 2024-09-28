package com.example.nutricionapp

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

data class Reminder(val patientName: String, val message: String)

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

    var selectedItem by remember { mutableStateOf(0) } //mantener el intem sleccionado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
    ) {
        Box(
            modifier = Modifier
                .weight(1f) 
        ) {
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

        NavigationBar(
            containerColor = Color(0xFF4B3D6E)
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") },
                selected = selectedItem == 0,
                onClick = {
                    selectedItem = 0
                    //clic Home
                }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "Pacientes") },
                label = { Text("Pacientes") },
                selected = selectedItem == 1,
                onClick = {
                    selectedItem = 1
                    // clic en Pacientes
                }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones") },
                label = { Text("Notificaciones") },
                selected = selectedItem == 2,
                onClick = {
                    selectedItem = 2
                    // clic notificaciones
                }
            )
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E)) // Color de las tarjetas
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

@Preview(showBackground = true)
@Composable
fun HomeNutritionistPreview() {
    NutricionAppTheme {
        HomeNutritionist(navController = rememberNavController())
    }
}
