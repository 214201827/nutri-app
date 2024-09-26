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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.input.PasswordVisualTransformation




@Composable
fun RegisterPatientScreen(navController: NavHostController) {
    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Paciente",fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = birthDate,
            singleLine = true,
            onValueChange = { birthDate = it },
            label = { Text("Fecha de Nacimiento (DD/MM/AAAA)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = { /* Aquí hacer registro */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { /* Aquí hacer registro con Google */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) // Cambia a containerColor
        ) {
            Text("Registrarse con Google", color = MaterialTheme.colorScheme.onPrimary) // Cambia a colorScheme
        }

        Spacer(modifier = Modifier.height(16.dp))


        TextButton(onClick = { navController.navigate("login") }) {
            Text("Ya tienes cuanta? Inicia sesion")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterPatientScreen() {
    RegisterPatientScreen(navController = rememberNavController())
}
