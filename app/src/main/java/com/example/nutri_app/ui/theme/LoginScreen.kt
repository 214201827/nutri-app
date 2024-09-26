package com.example.nutri_app.ui.theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.nutri_app.ui.theme.NutriappTheme
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutri_app.ui.theme.RegisterOptionsScreen
import com.example.nutri_app.ui.theme.RegisterPatientScreen
//
//@Composable
//fun LoginScreen(navController: NavHostController) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF65558F)),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(32.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            // Logo Image
////            Image(
////                painter = painterResource(),
////                contentDescription = null,
////                contentScale = ContentScale.Crop,
////                modifier = Modifier
////                    .size(120.dp)
////                    .padding(bottom = 32.dp)
////            )
//
//            Text(
//                text = "Bienvenido",
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            TextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Correo electrónico") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                singleLine = true,
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    focusedTextColor = Color.White,
//                    focusedLabelColor = Color.White
//                )
//            )
//
//            TextField(
//                value = "",
//                onValueChange = {},
//                placeholder = {Text(text = "Constrasena") },
//                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                singleLine = true,
//                maxLines = 1,
//                visualTransformation = PasswordVisualTransformation(),
//                colors = TextFieldDefaults.colors(
//                    unfocusedTextColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                )
//            )
//
//            Button(
//                onClick = { /* Handle login action */ },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White
//                )
//            ) {
//                Text(text = "Iniciar sesión", color = Color(0xFF65558F), fontSize = 16.sp)
//            }
//
//            TextButton(onClick = { navController.navigate("registerOptions") }) {
//                Text(
//                    text = "¿No tienes cuenta? Regístrate",
//                    color = Color.White,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
//    }
//}
//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    NutriappTheme {
//        LoginScreen(navController = rememberNavController())
//    }
//}

import androidx.compose.foundation.border
import com.example.nutri_app.R
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF5A4A8A), Color(0xFF65558F)),
                    startY = 0f,
                    endY = 1000f
                )
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
                text = "Bienvenido",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color(0xFF4B3D6E), shape = RoundedCornerShape(16.dp))
                    .background(Color(0xFF4B3D6E)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
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

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color(0xFF4B3D6E), shape = RoundedCornerShape(16.dp))
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
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Text(text = "Iniciar sesión", color = Color(0xFF6200EE), fontSize = 16.sp)
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
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NutriappTheme {
        LoginScreen(navController = rememberNavController())
    }
}
