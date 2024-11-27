package com.example.nutricionapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.util.recuperarContrasena
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    //var password by remember { mutableStateOf("") }
    var showErrorLoginDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }
    val patientId = email




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
                painter = painterResource(id = R.drawable.manzana_regla2),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 32.dp)
                    //.border(1.dp, Color.White)
            )

            Text(
                text = "NUTRI",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            var email by remember { mutableStateOf("saulrivera@gmail.com") }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor =  Color.White,
                ),
            )

            var password by remember { mutableStateOf("hola123") }
            OutlinedTextField(

                value = password,
                onValueChange = {
                    if (it.length <= 16) password = it // Limita la contraseña a 16 caracteres
                },
                label = { Text("Contraseña") },
                placeholder = { Text("Ingrese su contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Elimina KeyboardType.Email
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor =  Color.White,
                )
            )






            Button(
                onClick = {

                    FirestoreRepository.getemail(email)
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        if (email == "admin@gmail.com" && password == "hola123") {

                            navController.navigate("AdminRequest") // Navega directamente a la pantalla de administrador

                        }else {
                            val db = FirebaseFirestore.getInstance()
                            FirebaseAuth.getInstance()
                                .signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    var verif: String? = null
                                    if (task.isSuccessful) {
                                        db.collection("nutriologos").whereEqualTo("email",email)
                                            .get()
                                            .addOnSuccessListener { result ->
                                                for (document in result) {
                                                    verif = document.getString("procesoVerificacion")
                                                }
                                                if (verif == null || verif!!.isEmpty()) {
                                                    // Redirige al flujo de pacientes solo si no hay verificación
                                                   // val patientId = email

                                                    navController.navigate("MainPatient/$email")

                                                } else {
                                                    navController.navigate("UserTypeSelector/$email")
                                                }
                                            }
                                    }
                                    else {

                                        showErrorLoginDialog = true
                                    // Muestra diálogo de error en caso de fallo
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
            TextButton(onClick = { navController.navigate("resetPassword")}){
                Text(
                    text = "Olvide mi contraseña",
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