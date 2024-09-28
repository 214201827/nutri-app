package com.example.nutricionapp.ui.theme

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

data class Patient(val name: String, val age: Int, val diet: String)

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
        // Botón de retroceso
        IconButton(onClick = onBackClick) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
        }

        Text(
            text = "Lista de Pacientes",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
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


@Preview(showBackground = true)
@Composable
fun PatientListScreenPreview() {
    NutricionAppTheme {
        PatientListScreen(onBackClick = {})
    }
}
