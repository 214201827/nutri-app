package com.example.myapplication000.db

data class Dieta(
    val Did: Int,         // ID del paciente
    val desayuno: String, // Nombre del desayuno
    val comida: String,   // Nombre de la comida
    val cena: String,     // Nombre de la cena
    val dia: String
)

data class Record(
    val nid: Int? = null,
    val titulo : String = "",
    val descripcion : String = ""
)
data class notify(
    val nid: Int? = null,
    val titulo : String = "",
    val descripcion : String = ""
)