package com.example.nutri_app.ui.theme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Clear






@Composable
fun RegisterNutScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var photoINE by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Nutriologo", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = birthDate,
            singleLine = true,
            onValueChange = { birthDate = it },
            label = { Text("Fecha de Nacimiento (DD/MM/AAAA)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = licenseNumber,
            onValueChange = { licenseNumber = it },
            label = { Text("Número de cédula") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Toggle password visibility")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Lógica para abrir el selector de imágenes
                // implementar seleccionar la imagen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (photoINE == null) "Agregar Foto del INE" else "Foto Agregada")
        }

        // Botón de registro
        Button(
            onClick = { /*onRegister(name, licenseNumber, email, password)*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrar")
        }
        TextButton(onClick = { navController.navigate("login") }) {
            Text("Ya tienes cuanta? Inicia sesion")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewRegisterNutScreen() {
    RegisterNutScreen(navController = rememberNavController())
}
//@Preview(showBackground = true)
//@Composable
//fun RegisterNutritionistScreenPreview() {
//    RegisterNutritionistScreen { name, licenseNumber, email, password ->
//        // Acciones de registro, como imprimir en consola o navegar a otra pantalla
//        println("Registered: $name, $licenseNumber, $email")
//    }
//}