package com.example.nutricionapp

import ListPatNutritionist
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.nutricionapp.ResetPassword.RecuperarContrasenaScreen
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.notificaciones.Notifications
import com.example.nutricionapp.nutritionist.CitasScreen
import com.example.nutricionapp.nutritionist.EditCitaScreen
import com.example.nutricionapp.nutritionist.updaesScreen
import com.example.nutricionapp.util.CheckUserTypeScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentUser = FirebaseAuth.getInstance().currentUser

            // Determina la pantalla inicial dinámicamente
            val startDestination = if (currentUser != null) {
                "checkUserType" // Pantalla que verifica el tipo de usuario
            } else {
                "login" // Pantalla de inicio de sesión
            }

            // Pasa el destino inicial dinámico a AppNavHost
            AppNavHost(navController = navController, startDestination = startDestination)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = "login",enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            tween(durationMillis = 500)
        )
    },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(durationMillis = 500)
            )
        }) {
        composable("registerOptions") { RegisterOptionsScreen(navController) }
        composable("registerPatient") { RegisterPatientScreen(navController) }
        composable("registerNutritionist") { RegisterNutScreen(navController) }
        composable("NoVerificado") { NoAutorizado(navController) }
        composable("ProcesoVerificacion") { ProcesoVerificacionScreen(navController) }
        composable("citas") { CitasScreen(navController) }
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
            com.example.nutricionapp.patient.PerfilPatient(
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
            Notifications(
                nutId = backStackEntry.arguments?.getString("NutId") ?: "",
                navController = navController
            )
        }
        composable("checkUserType") { CheckUserTypeScreen(navController) }
        composable("resetPassword") { RecuperarContrasenaScreen(navController) }
        composable(
            "editCita/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            EditCitaScreen(
                email = email,
                onConfirm = { date, hour ->
                    FirestoreRepository.createCitaAndUpdateAppointment(
                        pid = email, // Email del paciente
                        day = date,
                        hour = hour
                    ) { success ->
                        if (success) {
                            navController.popBackStack()
                        } else {
                            Log.e("CitasScreen", "Error al crear la cita")
                        }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    NutricionAppTheme { LoginScreen(navController = rememberNavController() ) }


}
