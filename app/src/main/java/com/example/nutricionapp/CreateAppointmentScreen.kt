package com.example.nutricionapp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import java.util.*


import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup

import androidx.navigation.NavHostController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerAppoiment() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Fecha de cita") },
            //colors = TextFieldDefaults(NutricionAppTheme { R.color }),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.White
            )
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}
@Composable
fun CreateAppointmentScreen(navController: NavHostController, onBackClick: () -> Unit) {
    var patientName by remember { mutableStateOf("") }
    var appointmentDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Lista de horas disponibles desde las 8 AM hasta las 8 PM
    val hours = (8..20).flatMap { hour ->
        listOf("${hour}:00", "${hour}:30").map {
            if (hour < 12) "$it AM" else if (hour == 12) "$it PM" else "${hour - 12}:00 PM"
        }
    }

    var selectedTime by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) } // Estado para mostrar el DropdownMenu

    // Contexto para el DatePickerDialog
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Dialog para seleccionar la fecha
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            appointmentDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        }, year, month, day
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp)
    ) {
        Text(
            text = "Crear Cita",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo para el nombre del paciente
        OutlinedTextField(
            value = patientName,
            onValueChange = { patientName = it },
            label = { Text("Nombre del Paciente") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.White
            )
        )


        // DropdownMenu para seleccionar la hora
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = if (selectedTime.isNotEmpty()) selectedTime else "Seleccionar Hora",
                onValueChange = {},
                readOnly = true, // Evita que el campo sea editable
               // label = { Text("Hora de la Cita") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White
                )
                )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                hours.forEach { hour ->
                    DropdownMenuItem(
                        text = { Text(text = hour) },
                        onClick = {
                            selectedTime = hour
                            expanded = false
                        }
                    )
                }
            }
        }
        DatePickerAppoiment()

        // Campo para notas adicionales
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notas adicionales") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.White
            )
        )


        // Botón para guardar la cita
        Button(
            onClick = {
                navController.navigate("HomeNutritionist") // Regresar a la pantalla principal
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E))
        ) {
            Text("Guardar Cita", color = Color.White)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateAppointmentScreenPreview() {
    // Si tu pantalla requiere el navController, puedes usar un dummy o un controlador de navegación vacío para la vista previa
    CreateAppointmentScreen(navController = rememberNavController(), onBackClick = {})
}