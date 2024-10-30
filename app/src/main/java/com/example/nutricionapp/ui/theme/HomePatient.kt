package com.example.nutricionapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.nutricionapp.R


data class PatientData(
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val assignedNutritionist: String,
    val nextAppointment: String? = null
)

@Composable
fun PatientHomeScreen(navController: NavHostController, patient: PatientData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Bienvenido, ${patient.name}",
            fontSize = 28.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Foto del paciente
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Cambia este recurso según la imagen del paciente
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

                PatientInfoRow(label = "Nombre", info = patient.name)
                PatientInfoRow(label = "Correo", info = patient.email)
                PatientInfoRow(label = "Teléfono", info = patient.phone)
                PatientInfoRow(label = "Dirección", info = patient.address)

                Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

                PatientInfoRow(label = "Nutriólogo asignado", info = patient.assignedNutritionist)

                if (patient.nextAppointment != null) {
                    Text(
                        text = "Próxima cita: ${patient.nextAppointment}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).
                        align(Alignment.CenterHorizontally)
                    )
                } else {
                    Text(
                        text = "No tiene citas programadas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).
                        align(Alignment.CenterHorizontally)
                    )
                }
            }
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
        PatientHomeScreen(navController = rememberNavController(), patient = patientData)
    }
}