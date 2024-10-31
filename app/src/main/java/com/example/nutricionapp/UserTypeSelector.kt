package com.example.nutricionapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun UserTypeSelectorScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Paciente Option (unchanged)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 96.dp)
                    .clickable {
                        navController.navigate("PatientHomeScreen")
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Paciente logo",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Paciente",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Nutriólogo Option with Validation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    val userEmail = FirebaseAuth.getInstance().currentUser?.email
                    if (userEmail != null) {
                        scope.launch {
                            try {
                                val db = FirebaseFirestore.getInstance()
                                val documentSnapshot = db.collection("/nutriologos")
                                    .document(userEmail)
                                    .get()


                                if (documentSnapshot.result != null) {
                                    val procesoVerificacion = documentSnapshot.result.get("procesoVerificacion")
                                    Log.d("DEBUG", procesoVerificacion.toString())
                                    when (procesoVerificacion) {
                                        "No verificado" -> navController.navigate("NoVerificado")
                                        "En proceso" -> navController.navigate("ProcesoVerificacion")
                                        "Verificado" -> navController.navigate("HomeNutritionist")
                                        else -> {
                                            Toast.makeText(
                                                context,
                                                "Estado de verificación desconocido.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Documento no encontrado.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Error al obtener datos: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Usuario no autenticado.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Nutriólogo logo",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Nutriólogo",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}




/*@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    NutricionAppTheme {
        UserTypeSelectorScreen(navController = rememberNavController(), email = "fdsfasdf")
    }
}*/