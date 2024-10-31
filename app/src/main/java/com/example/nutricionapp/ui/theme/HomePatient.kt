package com.example.nutricionapp.ui.theme

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import com.example.nutricionapp.HomeNutritionist
import com.example.nutricionapp.Notification
import com.example.nutricionapp.Patient
import com.example.nutricionapp.R
import com.example.nutricionapp.Reminder


data class PatientData(
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val assignedNutritionist: String,
    val nextAppointment: String? = null
)

@Composable
fun HomePatient(navController: NavHostController) {
    val patientData = PatientData(
        name = "Carlos Ramírez",
        email = "carlos.ramirez@example.com",
        phone = "555-1234-567",
        address = "Av. Ejemplo 123, Ciudad",
        assignedNutritionist = "Dra. Martínez",
        nextAppointment = "5 de Noviembre, 10:00 AM")
    Log.d("HomeNutrition", "Navigated to HomeNutrition")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Bienvenido, ${patientData.name}",
            fontSize = 28.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Foto del paciente
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Foto del paciente",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f))
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de información del paciente
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFB8A8D9)),
            elevation = CardDefaults.elevatedCardElevation(6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Datos del paciente",
                    fontSize = 20.sp,
                    color = Color(0xFF4B3D6E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                PatientInfoRow(label = "Nombre", info = patientData.name)
                PatientInfoRow(label = "Correo", info = patientData.email)
                PatientInfoRow(label = "Teléfono", info = patientData.phone)
                PatientInfoRow(label = "Dirección", info = patientData.address)

                Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

                PatientInfoRow(label = "Nutriólogo asignado", info = patientData.assignedNutritionist)

                if (patientData.nextAppointment != null) {
                    Text(
                        text = "Próxima cita: ${patientData.nextAppointment}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
                    )
                } else {
                    Text(
                        text = "No tiene citas programadas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Empuja la NavigationBar hacia el fondo


        NavigationBar(
            containerColor = Color(0xFF4B3D6E)
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") },
                selected = false,
                onClick = { /* Acciones para Inicio */ },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = "Dieta") },
                label = { Text("Dieta") },
                selected = false,
                onClick = { /* Acciones para Dieta */ },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones") },
                label = { Text("Notificaciones") },
                selected = false,
                onClick = { /* Acciones para Notificaciones */ },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}


@Composable
fun PatientInfoRow(label: String, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = "$label:",
            color = Color(0xFF4B3D6E),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp)
        )
        Text(text = info, color = Color(0xFF616161))
    }
}
@Composable
fun PatientItem(patient: Patient, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }, // Llamada a la acción de clic
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
fun NotificationScreen(navController: NavHostController, notifications: List<Notification>, onBackClick: () -> Unit) {
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
fun NotificationScreenPreview() {
    NutricionAppTheme {
        NotificationScreen(
            navController = rememberNavController(),
            notifications = listOf(
                Notification("Recordatorio de Cita", "Juan Pérez tiene una cita el 30 de septiembre."),
                Notification("Dieta Actualizada", "La dieta de María López ha sido actualizada.")
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PatientHomeScreenPreview() {
    val patientData = PatientData(
        name = "Carlos Ramírez",
        email = "carlos.ramirez@example.com",
        phone = "555-1234-567",
        address = "Av. Ejemplo 123, Ciudad",
        assignedNutritionist = "Dra. Martínez",
        nextAppointment = "5 de Noviembre, 10:00 AM"
    )
    NutricionAppTheme {
        HomePatient(navController = rememberNavController())
    }
}