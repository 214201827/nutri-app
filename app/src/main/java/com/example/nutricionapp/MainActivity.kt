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
import com.example.nutricionapp.ui.theme.LoginScreen
import com.example.nutricionapp.ui.theme.RegisterNutScreen
import com.example.nutricionapp.ui.theme.RegisterOptionsScreen
import com.example.nutricionapp.ui.theme.RegisterPatientScreen

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
        composable("login") { LoginScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NutricionAppTheme { LoginScreen(navController = rememberNavController()) }


}
