package com.example.nutricionapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
