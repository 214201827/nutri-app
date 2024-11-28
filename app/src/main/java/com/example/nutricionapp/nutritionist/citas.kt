package com.example.nutricionapp.nutritionist

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.Paciented
import androidx.navigation.NavHostController
import com.example.nutricionapp.db.FirestoreRepository.getOccupiedHours
import com.google.firebase.auth.FirebaseAuth
import java.util.*

@Composable
fun CitasScreen(navController: NavHostController) {
    var patientDataList by remember { mutableStateOf(listOf<Paciented>()) }
    var isLoading by remember { mutableStateOf(true) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val emailNut = currentUser?.email.toString()

    // Cargar los pacientes asociados al nutriólogo
    LaunchedEffect(emailNut) {
        // Verificar que el emailNut no sea vacío
        if (emailNut.isNotBlank()) {
            FirestoreRepository.getCityDataForNutritionist(emailNut) { data ->
                patientDataList = data
                isLoading = false
            }
        }
    }

    if (isLoading) {
        CircularProgressIndicator() // Indicador de carga mientras obtenemos los datos
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(32.dp)
    ) {
        Text(
            text = "Lista de Pacientes - Citas",
            fontSize = 24.sp,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(patientDataList.size) { index ->
                PatientItemCita(
                    patient = patientDataList[index],
                    onClick = {
                        navController.navigate("editCita/${patientDataList[index].email}")
                    }
                )
            }
        }
    }
}

    // Composable para mostrar un paciente en una Card (redirigido al formulario de cita)
    @Composable
    fun PatientItemCita(patient: Paciented, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFB8A8D9)),
            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = patient.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B3D6E),
                    )
                    Text(
                        text = "Email: ${patient.email}",
                        fontSize = 14.sp,
                        color = Color(0xFF616161)
                    )
                    Text(
                        text = "Cita: ${patient.nextAppointment}",
                        fontSize = 14.sp,
                        color = Color(0xFF616161)
                    )
                }
            }
        }
    }

@Composable
fun EditCitaScreen(
    email: String,
    onConfirm: (String, Int) -> Unit,
    onCancel: () -> Unit
) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedHour by remember { mutableStateOf<Int?>(null) }
    var occupiedHours by remember { mutableStateOf<List<Int>>(emptyList()) }
    val context = LocalContext.current

    // Función para obtener las horas ocupadas para una fecha y nid
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp),
        contentAlignment = Alignment.Center // Alineación centralizada para todo el contenido
    ) {
        // Título de la pantalla
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Centrado horizontal
            verticalArrangement = Arrangement.spacedBy(16.dp), // Espaciado entre los elementos
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Crear Cita para $email", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            // Selector de fecha con Date Picker
            Button(onClick = {
                showDatePicker(context) { date ->
                    selectedDate = date
                    // Obtener horas ocupadas cuando se selecciona la fecha
                    getOccupiedHours(date) { occupied ->
                        occupiedHours = occupied // Actualizar las horas ocupadas
                    }
                }
            }) {
                Text(if (selectedDate.isEmpty()) "Seleccionar Fecha" else selectedDate)
            }

            // Mostrar el selector de hora solo si se ha seleccionado una fecha
            var expanded by remember { mutableStateOf(false) }

            if (selectedDate.isNotEmpty()) {
                Button(onClick = { expanded = true }) {
                    Text(if (selectedHour == null) "Seleccionar Hora" else "$selectedHour:00")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // Mostrar horas de 9 a 16, pero solo las no ocupadas
                    (9..16).forEach { hour ->
                        if (!occupiedHours.contains(hour)) {
                            DropdownMenuItem(
                                text = { Text("$hour:00") },
                                onClick = {
                                    selectedHour = hour
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Espacio entre el DropdownMenu y los botones de acción
            Spacer(modifier = Modifier.height(16.dp))

            // Botones de "Guardar" y "Cancelar"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center // Centrado horizontal
            ) {
                Button(onClick = {
                    if (selectedDate.isNotEmpty() && selectedHour != null) {
                        onConfirm(selectedDate, selectedHour!!) // Confirmar la cita
                    }
                }) {
                    Text("Guardar")
                }
                Spacer(modifier = Modifier.width(16.dp)) // Espacio entre los botones
                Button(onClick = { onCancel() }) {
                    Text("Cancelar")
                }
            }
        }
    }
}



// Función para mostrar el DatePicker
private fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSelected(formattedDate)
        },
        year,
        month,
        day
    ).show()
}