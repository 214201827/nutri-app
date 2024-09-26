package com.example.nutri_app.ui.theme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController



@Composable
fun RegisterOptionsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Elige tu tipo de registro", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.navigate("registerPatient") }) {
            Text("Registrarse como Paciente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("registerNutritionist") }) {
            Text("Registrarse como Nutriólogo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Ya teienes cuenta? Inicia sesion")
        }
    }
}
@Preview
@Composable
fun PreviewRegisterOptionsScreen() {
    RegisterOptionsScreen(navController = rememberNavController())
}
