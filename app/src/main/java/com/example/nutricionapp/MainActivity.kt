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
import com.example.nutricionapp.nutriologo.RecordatorioScreen
import com.example.nutricionapp.paciente.PatientDetailScreenP
import com.example.nutricionapp.paciente.RecordatorioScreenpac
import com.example.nutricionapp.nutriologo.PatientListScreen

data class PatientData(
    var fullName: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var assignedNutritionist: String? = null,
    var nextAppointment: String? = null
    // Agrega los demás campos necesarios aquí
) {
    // Constructor sin argumentos requerido por Firebase
    constructor() : this(null, null, null, null, null)
}




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
        composable("PatientHomeScreen") { HomePatient(navController) }
        composable("UserTypeSelector") { UserTypeSelectorScreen(navController) }
        composable("CreateAppoitment") { CreateAppointmentScreen(navController, onBackClick = { navController.popBackStack() }) }

        composable("pacien") { PatientListScreen(navController) }

        // Detalle del paciente por `patientId`
        composable("patientDetail/{patientId}") { backStackEntry ->
            com.example.nutricionapp.nutriologo.PatientDetailScreen(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }

        composable("updDiet/{patientId}") { backStackEntry ->
            com.example.nutricionapp.nutriologo.PatientDetailScreen3(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }

        composable("reminders") { RecordatorioScreen(navController) }

        composable("patientDetailP") { PatientDetailScreenP(navController) }

        composable("RecordatorioScreenpac") { RecordatorioScreenpac(navController) }

        composable("login") { LoginScreen(navController) }
    }
}


// Preview de la aplicación
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    NutricionAppTheme { LoginScreen(navController = rememberNavController()) }


}