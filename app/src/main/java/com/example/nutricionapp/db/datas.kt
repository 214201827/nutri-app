package com.example.nutricionapp.db

import com.google.firebase.Timestamp

data class PacienteDb(
    val correo: String = "",
    val fecha: Timestamp?,
    val imc: Double? = null,
    val imcI: Double? = null,
    val imm: Double? = null,
    val immI: Double? = null,
    val nombre: String = "",
    val Nid: String = "",
    val peso: Double? = null,
    val pesoI: Double? = null,
    val PesoMeta: Double? = null,
    val dir: String = "",
    val cel: Long? = null,
    val diet: String = "",
    val nextAppointment: String = "",
    val profileImage: String = "",

)
data class Paciented(
    val email: String = "",
    val nombre: String = ""
)
data class Comida(
    val comida: String,
    val descr: String,
    val hora: Int,
    var comentario: String
)
data class Dieta(
    val email: String,
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
    val destId: String? = null,
    val remId: String? = null,
    val descr: String,
    val titulo: String,
    val id: Int? = null
)