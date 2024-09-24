package com.example.nutriapp

data class Paciente(
    val nombres: String,
    val apellidos: String,
    val email: String,
    val fechaNacimiento: String, // Para simplicidad, usamos String en lugar de datetime
    val nutriologoAsign: String, // Referencia al nutriólogo
    val passwordHash: String,
    val tel: String,
    val sessionToken: String,
    val mediciones: List<String> = listOf(), // Inicializamos con una lista vacía
    val historia: List<String> = listOf()
)

data class Nutriologo(
    val nombres: String,
    val apellidos: String,
    val email: String,
    val direccion: String,
    val tel: String,
    val passwordHash: String,
    val verificado: Boolean,
    val sessionToken: String,
    val numCedula: String,
    val pacientesAssign: List<String> = listOf() // Referencia a los pacientes asignados
)
