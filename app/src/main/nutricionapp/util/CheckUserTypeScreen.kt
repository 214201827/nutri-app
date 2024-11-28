package com.example.nutricionapp.util

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



@Composable

fun CheckUserTypeScreen(navController: NavHostController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    // Estado para controlar la navegación
    var navigationHandled by remember { mutableStateOf(false) }

    // Si ya se manejó la navegación, no realiza más operaciones
    if (!navigationHandled) {
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                db.collection("nutriologos")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { result ->
                        val userType = result.documents.firstOrNull()?.getString("procesoVerificacion")
                        if (userType.isNullOrEmpty()) {
                            // Redirige al flujo de pacientes
                            navController.navigate("MainPatient/$email") {
                                popUpTo("checkUserType") { inclusive = true }
                            }
                        } else {
                            // Redirige al flujo de nutricionistas
                            navController.navigate("UserTypeSelector/$email") {
                                popUpTo("checkUserType") { inclusive = true }
                            }
                        }
                        navigationHandled = true // Marca la navegación como completada
                    }
                    .addOnFailureListener {
                        navController.navigate("login") {
                            popUpTo("checkUserType") { inclusive = true }
                        }
                        navigationHandled = true // Marca la navegación como completada
                    }
            } else {
                navController.navigate("login") {
                    popUpTo("checkUserType") { inclusive = true }
                }
                navigationHandled = true // Marca la navegación como completada
            }
        } else {
            navController.navigate("login") {
                popUpTo("checkUserType") { inclusive = true }
            }
            navigationHandled = true // Marca la navegación como completada
        }
    }

    // Muestra un indicador de carga mientras se verifica
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

//recuperar constraseña
fun recuperarContrasena(correo: String, onComplete: (Boolean) -> Unit) {
    FirebaseAuth.getInstance().sendPasswordResetEmail(correo)
        .addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
}



