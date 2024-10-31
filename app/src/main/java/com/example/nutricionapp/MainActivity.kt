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
import com.example.myapplication000.NotificationListScreen
import com.example.myapplication000.NotificationListScreenpac
import com.example.myapplication000.PatientDetailScreenP
import com.example.myapplication000.RecordatorioScreen
import com.example.myapplication000.RecordatorioScreenpac

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
    NavHost(navController = navController, startDestination = "reminders") {
        composable("registerOptions") { RegisterOptionsScreen(navController) }
        composable("registerPatient") { RegisterPatientScreen(navController) }
        composable("registerNutritionist") { RegisterNutScreen(navController)  }
        composable("NoVerificado") { NoAutorizado(navController) }
        composable("ProcesoVerificacion") { ProcesoVerificacionScreen(navController) }
        composable("PatientHomeScreen") { PatientHomeScreen(navController, patient = patientData) }
        composable("UserTypeSelector") { UserTypeSelectorScreen(navController) }
        composable("CreateAppoitment") { CreateAppointmentScreen(navController, onBackClick = { navController.popBackStack()}) }
        composable("pacien") { com.example.myapplication000.PatientListScreen(navController) }
        composable("patientDetail/{patientId}") { backStackEntry ->
            com.example.myapplication000.PatientDetailScreen(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }
        composable("reminders") { RecordatorioScreen(navController) }
        composable("Pnotifications") { NotificationListScreenpac(navController) }
        composable("Pmenu") { RecordatorioScreenpac(navController) }
        composable("Pperfil") { PatientDetailScreenP(navController) }
        composable("notifications") { NotificationListScreen(navController) }
//        composable("PatientListScreen") { PatientListScreen(navController,onBackClick = {})  }
//        composable("NotificationScreen") { NotificationScreen(navController = navController,
//            notifications = listOf(
//                Notification("Recordatorio de Cita", "Juan Pérez tiene una cita el 30 de septiembre."),
//                Notification("Dieta Actualizada", "La dieta de María López ha sido actualizada.")
//            ),
//            onBackClick = {}
//        )  }
        composable("login") { LoginScreen(navController) }



    }

}


// Preview de la aplicación
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    NutricionAppTheme { LoginScreen(navController = rememberNavController()) }


}