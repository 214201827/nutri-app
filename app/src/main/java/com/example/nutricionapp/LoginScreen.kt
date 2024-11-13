package com.example.nutricionapp


import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.max
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import com.example.nutricionapp.ProviderType
import com.example.nutricionapp.HomeNutritionist
import com.example.nutricionapp.UserTypeSelectorScreen
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrorLoginDialog by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F)

            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 32.dp)
                    .border(1.dp, Color.White)
            )

            Text(
                text = "NUTRI",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            var email by remember { mutableStateOf("davidyeaah@gmail.com") }
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    //.border(1.dp, Color(0xFF4B3D6E), shape = RoundedCornerShape(16.dp))
                    .background(Color(0xFF4B3D6E)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                ),
                //shape = RoundedCornerShape(16.dp)
            )

            var password by remember { mutableStateOf("hola123") }
            TextField(

                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    //.border(1.dp, Color(0xFF4B3D6E), shape = RoundedCornerShape(16.dp))
                    .background(Color(0xFF4B3D6E)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Button(
                onClick = {

                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        if (email == "admin@gmail.com" && password == "hola123") {
                                        navController.navigate("AdminRequest") // Navega directamente a la pantalla de administrador

                                }else {
                            FirebaseAuth.getInstance()
                                .signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        navController.navigate("UserTypeSelector") // Navega para usuarios regulares
                                    } else {
                                        showErrorLoginDialog =
                                            true // Muestra diálogo de error en caso de fallo
                                    }
                                }
                        }
                        }else {
                            showErrorLoginDialog = true
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Text(text = "Iniciar sesión", color = Color(0xFF65558F), fontSize = 16.sp)
            }


            TextButton(onClick = { navController.navigate("RegisterOptions")}) {
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Diálogo que se muestra en caso de error
    if (showErrorLoginDialog) {
        AlertDialog(
            onDismissRequest = { showErrorLoginDialog = false },
            confirmButton = {
                Button(
                    onClick = { showErrorLoginDialog = false } // Cerrar el diálogo
                ) {
                    Text("Aceptar")
                }
            },
            title = { Text("Error") },
            text = { Text("Error al iniciar sesión. Verifica tus credenciales e inténtalo de nuevo.") }
        )
    }

}


// Preview del Login, solamente
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NutricionAppTheme {
        LoginScreen(navController = rememberNavController())
    }
}