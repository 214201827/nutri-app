package com.example.myapplication000.db

import com.google.firebase.Timestamp


data class Pacient(
    val correo: String = "",
    val fecha: Timestamp? = null, // Campo fecha como Timestamp
    val foto: String? = null,
    val id: Int = 0,
    val imc: Double? = null,
    val imcI: Double? = null,
    val imm: Double? = null,
    val immI: Double? = null,
    val nombre: String = "",
    val nid: Int? = null,
    val peso: Double? = null,
    val pesoI: Double? = null,
    val cita: Timestamp? = null // Campo cita como Timestamp
)
data class Dietass(
    val did: Int = 0,
    val tipoComida: String = "",
    val desayuno: String = "",
    val comida: String = "",
    val cena: String = ""
)
