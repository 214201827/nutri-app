package com.example.myapplication000

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
    val pesoI: Double? = null
)
data class Paciented(
    val id: Int? = null,
    val nombre: String = ""
)
