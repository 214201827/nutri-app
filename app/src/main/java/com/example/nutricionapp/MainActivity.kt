package com.example.nutricionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.nutricionapp.CreateAppointmentScreen

data class PatientData(
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val assignedNutritionist: String,
    val nextAppointment: String? = null
)

val patientData = PatientData(
    name = "Carlos Ramírez",
    email = "carlos.ramirez@example.com",
    phone = "555-1234-567",
    address = "Av. Ejemplo 123, Ciudad",
    assignedNutritionist = "Dra. Martínez",
    nextAppointment = "5 de Noviembre, 10:00 AM"
)


class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController)
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("registerOptions") { RegisterOptionsScreen(navController) }
        composable("registerPatient") { RegisterPatientScreen(navController) }
        composable("registerNutritionist") { RegisterNutScreen(navController)  }
        composable("HomeNutritionist") { HomeNutritionist(navController)  }
        composable("NoVerificado") { NoAutorizado(navController) }
        composable("ProcesoVerificacion") { ProcesoVerificacionScreen(navController) }
        composable("PatientHomeScreen") { PatientHomeScreen(navController, patient = patientData) }
        composable("UserTypeSelector") { UserTypeSelectorScreen(navController) }
        composable("CreateAppoitment") { CreateAppointmentScreen(navController, onBackClick = { navController.popBackStack()}) }
//        composable("PatientListScreen") { PatientListScreen(navController,onBackClick = {})  }
//        composable("NotificationScreen") { NotificationScreen(navController = navController,
//            notifications = listOf(
//                Notification("Recordatorio de Cita", "Juan Pérez tiene una cita el 30 de septiembre."),
//                Notification("Dieta Actualizada", "La dieta de María López ha sido actualizada.")
//            ),
//            onBackClick = {}
//        )  }
        composable("login") { LoginScreen(navController) }
        composable("patient_detail/{name}/{age}/{diet}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val age = backStackEntry.arguments?.getString("age")?.toInt() ?: 0
            val diet = backStackEntry.arguments?.getString("diet") ?: ""
            PatientDetailScreen(
                navController,
                Patient(name, age, diet),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}


// Preview de la aplicación
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    NutricionAppTheme { LoginScreen(navController = rememberNavController()) }


}