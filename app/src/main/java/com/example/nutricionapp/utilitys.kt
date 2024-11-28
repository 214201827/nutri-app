package com.example.nutricionapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutricionapp.db.Record
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import java.util.Date

fun calcularEdad(fechaNacimiento: Timestamp?): Int? {
    if (fechaNacimiento == null) return null

    val fechaActual = Calendar.getInstance().time
    val nacimiento = fechaNacimiento.toDate()

    val calendarioNacimiento = Calendar.getInstance().apply { time = nacimiento }
    val calendarioActual = Calendar.getInstance().apply { time = fechaActual }

    var edad = calendarioActual.get(Calendar.YEAR) - calendarioNacimiento.get(Calendar.YEAR)
    if (calendarioActual.get(Calendar.DAY_OF_YEAR) < calendarioNacimiento.get(Calendar.DAY_OF_YEAR)) {
        edad--
    }

    return edad
}
//calcular edad

@RequiresApi(Build.VERSION_CODES.O)
fun calculateAge(selectedDate: Date?): Int {
    if (selectedDate == null) return 0

    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    val today = LocalDate.now()
    val birthDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))

    return Period.between(birthDate, today).years
}





@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(onDateSelected: (Date?) -> Unit) { // Cambia el tipo del callback a Date
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedDateText by remember { mutableStateOf("") } // Mostrar fecha seleccionada

    OutlinedTextField(
        value = selectedDateText, // Fecha seleccionada
        onValueChange = {}, // Solo lectura
        readOnly = true,
        label = { Text("Fecha de nacimiento") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Abrir Selector de Fecha",
                tint = Color.LightGray,
                modifier = Modifier.clickable { showDatePicker = true }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }, // Abre el DatePicker al clic
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.LightGray,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            focusedIndicatorColor = Color.White,
        )
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDate: Date? = datePickerState.selectedDateMillis?.let {
                            Date(it) // Convierte directamente a Date
                        }
                        // Actualiza el texto del campo
                        selectedDateText = selectedDate?.let {
                            val calendar = Calendar.getInstance()
                            calendar.time = it
                            "${calendar.get(Calendar.DAY_OF_MONTH)+ 1}/" +
                                    "${calendar.get(Calendar.MONTH) + 1}/" +
                                    "${calendar.get(Calendar.YEAR)}"
                        } ?: "Fecha no seleccionada"

                        // Llama al callback con el objeto Date o null si no se seleccionó
                        onDateSelected(selectedDate)
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun ReminderItem(record: Record) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = record.titulo,
                    fontSize = 18.sp,
                    color = Color.White
                )
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar notif",
                        tint = Color.Red
                    )
                }

            }

            Text(
                text = record.descr,
                fontSize = 14.sp,
                color = Color.LightGray
            )

        }
    }
}