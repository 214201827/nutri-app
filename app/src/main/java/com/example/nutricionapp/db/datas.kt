package com.example.nutricionapp.db

import com.google.firebase.Timestamp

data class PacienteDb(
    val correo: String = "",
    val dietaId: Int = 0,
    val fecha: Timestamp?,
    val foto: String? = null,
    val id: Int? = null,
    val imc: Double? = null,
    val imcI: Double? = null,
    val imm: Double? = null,
    val immI: Double? = null,
    val nombre: String = "",
    val Nid: Int? = null,
    val peso: Double? = null,
    val pesoI: Double? = null,
    val PesoMeta: Double? = null
)
data class Paciented(
    val id: Int? = null,
    val nombre: String = ""
)
data class Comida(
    val comida: String,
    val descr: String,
    val hora: Int
)
data class Dieta(
    val id: Int,
    val desayuno: Comida,
    val comida: Comida,
    val cena: Comida,
    val dia: String
)
data class Dieta2(
    val desayuno: Comida,
    val comida: Comida,
    val cena: Comida,
    val dia: String
)

data class Record(
    val destId: Int? = null,
    val remId: Int? = null,
    val descr: String,
    val titulo: String,
    val id: Int? = null
)