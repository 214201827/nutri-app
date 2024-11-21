package com.example.nutricionapp.nutritionist


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nutricionapp.db.Dieta
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.FirestoreRepository.createAndUploadExcelFile
import com.example.nutricionapp.db.FirestoreRepository.upd
import com.example.nutricionapp.db.PacienteDb
import java.util.Calendar
import java.util.Date
import androidx.compose.ui.platform.LocalContext
import com.example.nutricionapp.db.FirestoreRepository.uploadHist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun updaesScreen(patientId: String, navController: NavController) {
    val context = LocalContext.current
    var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    var dieta by remember { mutableStateOf<List<Dieta>?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var editedValues by remember { mutableStateOf<Map<String, Map<String, String>>>(emptyMap()) } // Cambiar a un mapa anidado
    val daysOfWeek = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
    var selectedDayIndex by remember { mutableStateOf(0) } // Índice del día seleccionado

    LaunchedEffect(patientId) {
        // Obtener datos del paciente
        FirestoreRepository.getPatientData(patientId) { data ->
            paciente = data
            data?.correo?.let { email ->
                // Obtener la dieta para el paciente
                FirestoreRepository.getDietData(email) { dietData ->
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
                                                value = editedValues[selectedDay]?.get("desayuno-${diet.email}")
                                                    ?: diet.desayuno.comida,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + (selectedDay to
                                                            (editedValues[selectedDay] ?: emptyMap()) + ("desayuno-${diet.email}" to newValue))
                                                },
                                                label = { Text("Desayuno") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues[selectedDay]?.get("descr-desayuno-${diet.email}")
                                                    ?: diet.desayuno.descr,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + (selectedDay to
                                                            (editedValues[selectedDay] ?: emptyMap()) + ("descr-desayuno-${diet.email}" to newValue))
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
                                                value = editedValues[selectedDay]?.get("comida-${diet.email}")
                                                    ?: diet.comida.comida,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + (selectedDay to
                                                            (editedValues[selectedDay] ?: emptyMap()) + ("comida-${diet.email}" to newValue))
                                                },
                                                label = { Text("Comida") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues[selectedDay]?.get("descr-comida-${diet.email}")
                                                    ?: diet.comida.descr,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + (selectedDay to
                                                            (editedValues[selectedDay] ?: emptyMap()) + ("descr-comida-${diet.email}" to newValue))
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
                                                value = editedValues[selectedDay]?.get("cena-${diet.email}")
                                                    ?: diet.cena.comida,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + (selectedDay to
                                                            (editedValues[selectedDay] ?: emptyMap()) + ("cena-${diet.email}" to newValue))
                                                },
                                                label = { Text("Cena") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues[selectedDay]?.get("descr-cena-${diet.email}")
                                                    ?: diet.cena.descr,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + (selectedDay to
                                                            (editedValues[selectedDay] ?: emptyMap()) + ("descr-cena-${diet.email}" to newValue))
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


                        Button(
                            onClick = {
                                uploadHist(context, patientId) { isSuccessful ->
                                    if (isSuccessful) {
                                        // Mostrar mensaje o realizar alguna acción en caso de éxito
                                        Log.d("Upload", "El archivo fue subido correctamente.")
                                    } else {
                                        // Mostrar mensaje en caso de error
                                        Log.e("Upload", "Hubo un error al subir el archivo.")
                                    }
                                }

                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Nueva Dieta")
                        }
                        // Botón para actualizar la dieta en todos los días de la semana
                        Button(
                            onClick = {

                                // Actualizar dieta para todos los días de la semana
                                daysOfWeek.forEach { day ->
                                    val dayDiet = dietList.filter { it.dia == day }
                                    dayDiet.forEach { diet ->
                                        // Datos modificados o valores por defecto si no han sido editados
                                        val desayunoData: Map<String, Any> = mapOf(
                                            "comida" to (editedValues[day]?.get("desayuno-${diet.email}") ?: diet.desayuno.comida),
                                            "descr" to (editedValues[day]?.get("descr-desayuno-${diet.email}") ?: diet.desayuno.descr),
                                            "hora" to 8 // Ejemplo de hora
                                        )
                                        val comidaData: Map<String, Any> = mapOf(
                                            "comida" to (editedValues[day]?.get("comida-${diet.email}") ?: diet.comida.comida),
                                            "descr" to (editedValues[day]?.get("descr-comida-${diet.email}") ?: diet.comida.descr),
                                            "hora" to 13
                                        )
                                        val cenaData: Map<String, Any> = mapOf(
                                            "comida" to (editedValues[day]?.get("cena-${diet.email}") ?: diet.cena.comida),
                                            "descr" to (editedValues[day]?.get("descr-cena-${diet.email}") ?: diet.cena.descr),
                                            "hora" to 20
                                        )

                                        // Llama a la función `upd` para actualizar Firestore
                                        upd(patientId, day, desayunoData, comidaData, cenaData)
                                    }
                                }

                                // Crear y subir el archivo Excel después de actualizar las dietas
                                createAndUploadExcelFile(context, patientId, dietList, editedValues) { success ->
                                    if (success) {
                                        Log.d("ExcelUpload", "Archivo Excel subido correctamente.")
                                    } else {
                                        Log.e("ExcelUpload", "Error al subir el archivo Excel.")
                                    }

                                    // Regresar al estado anterior después de intentar subir el archivo
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Guardar")
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