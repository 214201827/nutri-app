package com.example.nutricionapp

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserTypeSelectorScreen(NutId: String,navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Si no hay usuario autenticado
    if (NutId == null) {
        Toast.makeText(context, "Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        return
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Paciente Option
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 96.dp)
                    .clickable {
                        navController.navigate("MainPatient/$NutId")
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tazon_verduras),
                    contentDescription = "Paciente logo"
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Paciente",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Nutriólogo Option with Firestore Validation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    scope.launch {
                        try {
                            val db = FirebaseFirestore.getInstance()
                            val documentSnapshot = db.collection("nutriologos")
                                .document(NutId)
                                .get()
                                .await()

                            if (documentSnapshot.exists()) {
                                val procesoVerificacion = documentSnapshot.getString("procesoVerificacion")
                                when (procesoVerificacion) {
                                    "No verificado" -> navController.navigate("NoVerificado")
                                    "En proceso" -> navController.navigate("ProcesoVerificacion")
                                    "Verificado" -> navController.navigate("MainNutritionist/$NutId")
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
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vaso_agua_manzana),
                    contentDescription = "Nutriólogo logo"
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