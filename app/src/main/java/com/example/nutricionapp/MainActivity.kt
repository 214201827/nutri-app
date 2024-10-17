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
import com.example.nutricionapp.AuthenticationManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear instancia de AuthenticationManager
        val authenticationManager = AuthenticationManager(context = this)

        setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController, authenticationManager = authenticationManager)
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, authenticationManager: AuthenticationManager) {
    NavHost(navController = navController, startDestination = "login") {
        composable("registerOptions") { RegisterOptionsScreen(navController) }
        composable("registerPatient") { RegisterPatientScreen(navController) }
        composable("registerNutritionist") { RegisterNutScreen(navController) }
        composable("HomeNutritionist") { HomeNutritionist(navController) }
        composable("UserTypeSelector") { UserTypeSelectorScreen(navController) }
        composable("CreateAppoitment") {
            CreateAppointmentScreen(navController, onBackClick = { navController.popBackStack() })
        }
        composable("login") {
            // Pasa el authenticationManager al LoginScreen
            LoginScreen(navController = navController, authenticationManager = authenticationManager)
        }
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
   // NutricionAppTheme { LoginScreen(navController = rememberNavController()) }


}