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
import com.example.nutricionapp.nutriologo.RecordatorioScreen
import com.example.nutricionapp.nutriologo.PatientListScreen

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
        composable("registerNutritionist") { RegisterNutScreen(navController) }
        composable("HomeNutritionist") { HomeNutritionist(navController) }
        composable("NoVerificado") { NoAutorizado(navController) }
        composable("ProcesoVerificacion") { ProcesoVerificacionScreen(navController) }
        //composable("PatientHomeScreen") { HomePatient(navController) }
        composable("UserTypeSelector") { UserTypeSelectorScreen(navController) }
        ///////////////////////////////////////////////////////////////
        // rama forte

        composable("CreateAppoitment") { CreateAppointmentScreen(navController, onBackClick = { navController.popBackStack() }) }

        composable("pacien") { PatientListScreen(navController) }

        // Detalle del paciente por `patientId`
        composable("inicioPatient/{patientId}") { backStackEntry ->
            com.example.nutricionapp.patient.InicioPatient(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }
        composable("MainPatient/{patientId}") { backStackEntry ->
            com.example.nutricionapp.patient.MainPatient(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }
        composable("AdminRequest") { AdminNutritionistRequestsScreen(navController) }
        composable("updDiet/{patientId}") { backStackEntry ->
            com.example.nutricionapp.nutriologo.PatientDetailScreen3(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }
        composable("reminders") { RecordatorioScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("patientDetail/{patientId}") { backStackEntry ->
            com.example.nutricionapp.nutriologo.PatientDetailScreen(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }
    }

}


// Preview de la aplicación
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    NutricionAppTheme { LoginScreen(navController = rememberNavController() ) }


}