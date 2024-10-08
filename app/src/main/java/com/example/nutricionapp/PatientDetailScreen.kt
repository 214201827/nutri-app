package com.example.nutricionapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutricionapp.ui.theme.NutricionAppTheme

@Composable
fun PatientDetailScreen(navController: NavHostController, patient: Patient, onBackClick: () -> Unit) {
    var selectedTab by remember { mutableStateOf("Dieta") } // Controlar la pestaña seleccionada
    var selectedItem by remember { mutableStateOf(1) } // Mantener el ítem seleccionado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
    ) {
        // Información del paciente con foto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Foto del paciente
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Foto del paciente",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 16.dp),
                    tint = Color.White
                )

                // Datos del paciente
                Column {
                    Text(text = patient.name, fontSize = 20.sp, color = Color.White)
                    Text(text = "Edad: ${patient.age}", fontSize = 16.sp, color = Color.LightGray)
                    Text(text = "Dieta: ${patient.diet}", fontSize = 16.sp, color = Color.LightGray)
                }
                Button(
                    onClick = {
                        navController.navigate("CreateAppoitment") // Regresar a la pantalla principal
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E))
                ){
                    Text("Crear cita",color = Color.White)
                }
            }
        }

        // Barra de navegación de pestañas
        NavigationBar(
            containerColor = Color(0xFF4B3D6E),
            modifier = Modifier.fillMaxWidth()
        ) {
            NavigationBarItem(
                icon = { Text("Dieta", color = Color.White) },
                selected = selectedTab == "Dieta",
                onClick = { selectedTab = "Dieta" }
            )
            NavigationBarItem(
                icon = { Text("Progreso", color = Color.White) },
                selected = selectedTab == "Progreso",
                onClick = { selectedTab = "Progreso" }
            )
            NavigationBarItem(
                icon = { Text("Historial", color = Color.White) },
                selected = selectedTab == "Historial",
                onClick = { selectedTab = "Historial" }
            )
        }

        // Mostrar contenido según la pestaña seleccionada
        when (selectedTab) {
            "Dieta" -> DietContent(navController = rememberNavController())
            "Progreso" -> ProgressContent()
            "Historial" -> HistoryContent()
        }


        // Este Spacer empuja la barra de navegación inferior hacia el fondo
        Spacer(modifier = Modifier.weight(1f))

        // Barra de navegación inferior





    }
}
@Composable
fun DietContent(navController: NavHostController) {
    // Aquí va el contenido relacionado con la dieta del paciente
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Dieta del paciente", fontSize = 18.sp, color = Color.White)
        // Añade aquí más detalles sobre la dieta

    }

}

@Composable
fun ProgressContent() {
    // Aquí va el contenido relacionado con el progreso del paciente
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Progreso del paciente", fontSize = 18.sp, color = Color.White)
        // Añade aquí más detalles sobre el progreso
    }
}

@Composable
fun HistoryContent() {
    // Aquí va el contenido relacionado con el historial del paciente
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Historial del paciente", fontSize = 18.sp, color = Color.White)
        // Añade aquí más detalles sobre el historial
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPatientDetailScreen() {
    // Crear un paciente de ejemplo
    val samplePatient = Patient(
        name = "Juan Pérez",
        age = 35,
        diet = "Alta en proteínas"
    )

    // Crear un NavController simulado
    val navController = rememberNavController()

    // Llamar a la pantalla con los datos de ejemplo
    PatientDetailScreen(
        navController = navController,
        patient = samplePatient,
        onBackClick = { /* Acción de ejemplo para el botón de atrás */ }
    )
}