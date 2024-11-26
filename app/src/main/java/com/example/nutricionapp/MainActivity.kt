package com.example.nutricionapp

import ListPatNutritionist
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.example.nutricionapp.nutritionist.updaesScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
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
        composable("UserTypeSelector/{NutId}") { backStackEntry ->
            UserTypeSelectorScreen(
                NutId = backStackEntry.arguments?.getString("NutId") ?: "",
                navController = navController
            ) }
        composable("MainNutritionist/{NutId}") { backStackEntry ->
            com.example.nutricionapp.nutritionist.MainNutritionist(
                NutId = backStackEntry.arguments?.getString("NutId") ?: "",
                navController
            ) }
        composable("CreateAppoitment/{patientId}") { backStackEntry -> CreateAppointmentScreen(navController,
            patientId = backStackEntry.arguments?.getString("patientId") ?: "",
            onBackClick = { navController.popBackStack() }) }

       // composable("pacien") { PatientListScreen(navController) }

        // Detalle del paciente por `patientId`
        composable("inicioPatient/{patientId}") { backStackEntry ->
            com.example.nutricionapp.patient.InicioPatient(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }
        composable( "dietaPatient/{patientId}" ) {  }
        composable("MainPatient/{patientId}") { backStackEntry ->
            com.example.nutricionapp.patient.MainPatient(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }
        composable("AdminRequest") { AdminNutritionistRequestsScreen(navController) }
        composable("updDiet/{patientId}") { backStackEntry ->
            updaesScreen(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController
            )
        }
        composable("comentario/{patientId}/{NutId}") { backStackEntry ->
            com.example.nutricionapp.patient.Comentario(
                patientId = backStackEntry.arguments?.getString("patientId") ?: "",
                navController = navController,
                NutId = backStackEntry.arguments?.getString("NutId") ?: ""
            )
        }
        composable("login") { LoginScreen(navController) }
        composable("dietaNutritionist/{patientId}") { backStackEntry ->
            com.example.nutricionapp.nutritionist.DietaNutritionist(
                patientId = backStackEntry.arguments?.getString("patientId") ?: ""
                ,navController = navController)
        }
        composable("listPatNutritionist") { ListPatNutritionist(navController) }

        composable("notificaciones/{NutId}") { backStackEntry ->
            com.example.nutricionapp.nutritionist.NotificationsNutritionist(
                nutId = backStackEntry.arguments?.getString("NutId") ?: "",
                navController = navController
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    NutricionAppTheme { LoginScreen(navController = rememberNavController() ) }


}