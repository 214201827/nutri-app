package com.example.nutricionapp.ui.theme
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.example.nutricionapp.patient.InicioPatient
import com.google.firebase.auth.FirebaseAuth

fun signOut(navController: NavHostController) {
    // Cerrar sesión de Firebase
    FirebaseAuth.getInstance().signOut()

    // Redirigir a la pantalla de inicio de sesión y limpiar el back stack
    navController.navigate("login") {
        // Limpia el back stack de todas las pantallas previas
        popUpTo(navController.graph.startDestinationId) { inclusive = true }

        // Asegura que no se pueda navegar hacia atrás
        launchSingleTop = true
        restoreState = false
    }
}