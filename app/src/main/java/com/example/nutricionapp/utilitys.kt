package com.example.nutricionapp

import com.google.firebase.Timestamp
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
