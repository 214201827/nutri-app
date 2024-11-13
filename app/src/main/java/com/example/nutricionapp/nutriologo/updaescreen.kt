package com.example.nutricionapp.nutriologo


import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nutricionapp.db.Dieta
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.FirestoreRepository.upd
import com.example.nutricionapp.db.PacienteDb
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen3(patientId: String, navController: NavController) {
    var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    var dieta by remember { mutableStateOf<List<Dieta>?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var editedValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val daysOfWeek = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
    var selectedDayIndex by remember { mutableStateOf(0) } // Índice del día seleccionado

    LaunchedEffect(patientId) {
        // Obtener datos del paciente
        FirestoreRepository.getPatientData(patientId) { data ->
            paciente = data
            data?.id?.let { id ->
                // Obtener la dieta para el paciente
                FirestoreRepository.getDietData(id.toString()) { dietData ->
                    dieta = dietData
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B3D6E)) {
                // Botones de navegación (igual que antes)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF65558F))
                .padding(10.dp)
                .systemBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Aumentar espacio entre elementos
            ) {
                DaySelector3(
                    daysOfWeek = daysOfWeek,
                    selectedDayIndex = selectedDayIndex,
                    onDayChange = { index -> selectedDayIndex = index }
                )

                // Lógica para mostrar la dieta según el día seleccionado
                dieta?.let { dietList ->
                    if (dietList.isNotEmpty()) {
                        val selectedDay = daysOfWeek[selectedDayIndex]
                        val dailyDiet = dietList.filter { it.dia == selectedDay }

                        if (dailyDiet.isNotEmpty()) {
                            dailyDiet.forEach { diet ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    // Card para Desayuno
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            TextField(
                                                value = editedValues["desayuno-${diet.id}"] ?: diet.desayuno.comida,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + ("desayuno-${diet.id}" to newValue)
                                                },
                                                label = { Text("Desayuno") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues["descr-desayuno-${diet.id}"] ?: diet.desayuno.descr,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + ("descr-desayuno-${diet.id}" to newValue)
                                                },
                                                label = { Text("Descripción") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }

                                    // Card para Comida
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            TextField(
                                                value = editedValues["comida-${diet.id}"] ?: diet.comida.comida,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + ("comida-${diet.id}" to newValue)
                                                },
                                                label = { Text("Comida") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues["descr-comida-${diet.id}"] ?: diet.comida.descr,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + ("descr-comida-${diet.id}" to newValue)
                                                },
                                                label = { Text("Descripción") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }

                                    // Card para Cena
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            TextField(
                                                value = editedValues["cena-${diet.id}"] ?: diet.cena.comida,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + ("cena-${diet.id}" to newValue)
                                                },
                                                label = { Text("Cena") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues["descr-cena-${diet.id}"] ?: diet.cena.descr,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + ("descr-cena-${diet.id}" to newValue)
                                                },
                                                label = { Text("Descripción") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            Text("No hay dieta disponible para el día seleccionado", fontSize = 16.sp, color = Color.LightGray)
                        }

                        // Botón para actualizar la dieta solo para el día seleccionado
                        Button(
                            onClick = {
                                // Aquí corregimos el uso de editedValues para cada comida
                                dailyDiet.forEach { diet ->
                                    // Asegurándonos de que los valores sean del tipo Any
                                    val desayunoData: Map<String, Any> = mapOf(
                                        "comida" to (editedValues["desayuno-${diet.id}"] ?: ""),
                                        "descr" to (editedValues["descr-desayuno-${diet.id}"] ?: ""),
                                        "hora" to 8 // Hora de ejemplo, asegurándonos de que sea del tipo adecuado (Int)
                                    )
                                    val comidaData: Map<String, Any> = mapOf(
                                        "comida" to (editedValues["comida-${diet.id}"] ?: ""),
                                        "descr" to (editedValues["descr-comida-${diet.id}"] ?: ""),
                                        "hora" to 13 // Hora de ejemplo
                                    )
                                    val cenaData: Map<String, Any> = mapOf(
                                        "comida" to (editedValues["cena-${diet.id}"] ?: ""),
                                        "descr" to (editedValues["descr-cena-${diet.id}"] ?: ""),
                                        "hora" to 20 // Hora de ejemplo
                                    )

                                    // Asegúrate de pasar todos los parámetros a la función upd
                                    upd(patientId.toInt(), daysOfWeek[selectedDayIndex], desayunoData, comidaData, cenaData)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Actualizar Dieta")
                        }
                    }
                }
            }
        }
    }
}



//---------------------------------------------------------------------------------------------------
@Composable
fun DaySelector3(
    daysOfWeek: List<String>,
    selectedDayIndex: Int,
    onDayChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
                // Cambiar al día anterior, asegurándose de que no salga de los límites
                val newIndex = if (selectedDayIndex > 0) selectedDayIndex - 1 else daysOfWeek.lastIndex
                onDayChange(newIndex)
            }
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Día anterior", tint = Color.White)
        }

        // Mostrar el día seleccionado
        Text(
            text = daysOfWeek[selectedDayIndex],
            color = Color.White,
            fontSize = 16.sp
        )

        IconButton(
            onClick = {
                // Cambiar al siguiente día, asegurándose de que no salga de los límites
                val newIndex = if (selectedDayIndex < daysOfWeek.lastIndex) selectedDayIndex + 1 else 0
                onDayChange(newIndex)
            }
        ) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Siguiente día", tint = Color.White)
        }
    }
}

@Composable
fun DatePickerDialog3(onDismissRequest: () -> Unit, onDateSelected: (Date) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Muestra el selector de fecha
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Seleccionar Fecha") },
        text = {
            TextField(
                value = calendar.time.toString(), // Muestra la fecha actual
                onValueChange = { },
                readOnly = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    // Aquí puedes agregar la lógica para manejar la fecha seleccionada
                    onDateSelected(calendar.time) // Pasa la fecha seleccionada
                }
            ) {
                Text("Seleccionar")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}
@Composable
fun HistorialScreen3(patientId: String) {

}