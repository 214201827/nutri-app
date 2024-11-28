package com.example.nutricionapp.ResetPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.nutricionapp.util.recuperarContrasena

//recuperar contraseña screen
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RecuperarContrasenaScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var emailErrorMessage by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Recuperar Contraseña",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp) // Asegura que no se solape con la columna
        )

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp), // Espacio suficiente para evitar superposición con el título
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (success) {
                Text(
                    "Se ha enviado un correo para recuperar tu contraseña",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                OutlinedTextField(
                    value = email,
                    onValueChange = { newValue ->
                        email = newValue
                        emailError = false
                    },
                    label = { Text("Correo") },
                    isError = emailError,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color.White,
                    )
                )
                if (emailError) {
                    Text(
                        text = emailErrorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start).padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (email.isEmpty()) {
                            emailError = true
                            emailErrorMessage = "El correo no puede estar vacío"
                        } else {
                            loading = true
                            recuperarContrasena(email) { result ->
                                loading = false
                                if (result) {
                                    success = true
                                } else {
                                    emailError = true
                                    emailErrorMessage = "No se pudo enviar el correo"
                                }
                            }
                        }
                    },
                    enabled = !loading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            color = Color(0xFF65558F),
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Text("Recuperar contraseña", color = Color(0xFF65558F))
                    }
                }
            }
        }
    }
}
