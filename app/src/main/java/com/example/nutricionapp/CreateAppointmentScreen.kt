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
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter


import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity


import androidx.compose.runtime.*
import androidx.compose.ui.unit.toSize


import androidx.navigation.NavHostController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerAppoiment() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    //Fecha


    }

@Composable
fun CreateAppointmentScreen(navController: NavHostController, onBackClick: () -> Unit) {
    var patientName by remember { mutableStateOf("") }
    var appointmentDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }


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
    //combo box hora
    val hours = generateHourList()
    var selectedHour by remember { mutableStateOf(hours.first()) }


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
        TextField(
            value = patientName,
            onValueChange = { patientName = it },
            label = { Text("Nombre del Paciente") },
            modifier = Modifier
                .fillMaxWidth()
               // .padding(vertical = 8.dp)
            .background(Color(0xFF4B3D6E)),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White
            )
        )
        HourComboBox(
            label = "Hora",
            hours = hours,
            selectedHour = selectedHour,
            onHourSelected = { selectedHour = it }
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

        //combo para fecha
        //DatePickerAppoiment()
        var selectedDay by remember { mutableStateOf("") }
        var selectedMonth by remember { mutableStateOf("") }
        var selectedYear by remember { mutableStateOf("") }

        val days = (1..31).map { it.toString() }
        val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
        val years = (1900..2024).map { it.toString() }

        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Espacio entre cada ComboBox
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComboBox(
                label = "Día",
                items = days,
                selectedItem = selectedDay,
                onItemSelected = { selectedDay = it },
            )


            ComboBox(
                label = "Mes",
                items = months,
                selectedItem = selectedMonth,
                onItemSelected = { selectedMonth = it },
            )



            ComboBox(
                label = "Año",
                items = years,
                selectedItem = selectedYear,
                onItemSelected = { selectedYear = it },
            )
        }


        TextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notas adicionales") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp).background(Color(0xFF4B3D6E)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )



        Button(
            onClick = {
                navController.navigate("HomeNutritionist")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        ) {
            Text("Guardar Cita", color = Color(0xFF65558F))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourComboBox(
    label: String,
    hours: List<String>,
    selectedHour: String,
    onHourSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedHour,
            onValueChange = { },
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle Dropdown",
                    tint = Color.White // Cambiar color del icono a blanco
                )
            },
            modifier = modifier
                .menuAnchor()
                .padding(vertical = 8.dp)
                .background(Color(0xFF4B3D6E))
                .width(110.dp)
                .height(56.dp),
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )


        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Cierra el menú si se hace clic fuera
        ) {
            hours.forEach { hour -> // Recorrer la lista de horas
                DropdownMenuItem(
                    text = { Text(hour) }, // Mostrar cada hora
                    onClick = {
                        onHourSelected(hour) // Actualizar la hora seleccionada
                        expanded = false // Cerrar el menú después de la selección
                    }
                )
            }
        }
    }
}


// Función que genera la lista de horas desde las 8:00 AM hasta las 6:00 PM cada media hora
fun generateHourList(): List<String> {
    val hours = mutableListOf<String>()
    var currentTime = LocalTime.of(8, 0)
    val endTime = LocalTime.of(18, 0)

    while (currentTime <= endTime) {
        val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
        hours.add(formattedTime)
        currentTime = currentTime.plusMinutes(30)
    }
    return hours
}


@Preview(showBackground = true)
@Composable
fun CreateAppointmentScreenPreview() {
    // Si tu pantalla requiere el navController, puedes usar un dummy o un controlador de navegación vacío para la vista previa
    CreateAppointmentScreen(navController = rememberNavController(), onBackClick = {})
}