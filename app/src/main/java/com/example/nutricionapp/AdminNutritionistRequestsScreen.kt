package com.example.nutricionapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.nutricionapp.ProfileImagen.ImageSelector
import com.example.nutricionapp.ProfileImagen.uploadImageToFirebase
import com.example.nutricionapp.ui.theme.NutricionAppTheme

// Data class for Nutritionist requests
data class NutritionistRequest(val fullName: String, val email: String, val procesoVerificacion: String, val ineUrl: String) {
    // Constructor sin argumentos requerido por Firebase
    constructor() : this("", "", "", "")
}


// Composable function for the admin requests screen
@Composable
fun AdminNutritionistRequestsScreen(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val requests = remember { mutableStateOf<List<NutritionistRequest>>(emptyList()) }

    // Escucha en tiempo real las solicitudes desde Firestore
    LaunchedEffect(Unit) {
        try {
        db.collection("nutriologos")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    Log.w("Firestore", "Error al escuchar solicitudes", e)
                    return@addSnapshotListener
                }

                val newRequests = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(NutritionistRequest::class.java)
                } ?: emptyList()
                requests.value = newRequests
            }
        } catch (e: Exception) {
            Log.e("AdminNutritionistRequestsScreen", "Error al cargar las solicitudes: ", e)
        }
    }

    // Interfaz de la lista de solicitudes
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F)) // Fondo del Box
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Solicitudes de Nutriólogos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White, // Color del texto
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(requests.value) { request ->
                    RequestCard(request = request, onStatusChange = { newStatus ->
                        // Actualiza el campo "status" del documento usando el email
                        db.collection("nutriologos")
                            .whereEqualTo("email", request.email)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    val document = documents.first()
                                    document.reference.update("procesoVerificacion", newStatus)
                                        .addOnSuccessListener {
                                            Log.d("Firestore", "Estado actualizado para ${request.email}")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w("Firestore", "Error al actualizar estado", e)
                                        }
                                } else {
                                    Log.w("Firestore", "Documento no encontrado para el email ${request.email}")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error al obtener el documento", e)
                            }
                    })
                }
            }
        }
    }
}

// Composable para cada solicitud individual
@Composable
fun RequestCard(request: NutritionistRequest, onStatusChange: (String) -> Unit) {

    var showDialog by remember { mutableStateOf(false) }
    var ineUrl by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(request) {
        // Obtener la URL de la imagen INE desde Firestore
        getImageUrlFromFirebase(request.email) { url ->
            ineUrl = url
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB8A8D9)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nombre: ${request.fullName}",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize // Tamaño de fuente adaptado
            )
            Text(
                text = "Email: ${request.email}",
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
            Text(
                text = "Estado actual: ${request.procesoVerificacion}",
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón para "En Proceso"
                Button(
                    onClick = { onStatusChange("En proceso") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E)),
                    contentPadding = PaddingValues(horizontal = 4.dp) // Margen interno reducido
                ) {
                    Text(
                        text = "En proceso",
                        color = Color.White,
                        maxLines = 1,
                        fontSize = 10.sp, // Tamaño de texto reducido aún más
                        modifier = Modifier.wrapContentWidth()
                    )
                }
                // Botón para "Verificado"
                Button(
                    onClick = { onStatusChange("Verificado") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E)),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text = "Verificado",
                        color = Color.White,
                        maxLines = 1,
                        fontSize = 10.sp,
                        modifier = Modifier.wrapContentWidth()
                    )
                }
                // Botón para "Denegado"
                Button(
                    onClick = { onStatusChange("No verificado") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E)),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text = "No verificado",
                        color = Color.White,
                        maxLines = 1,
                        fontSize = 10.sp,
                        modifier = Modifier.wrapContentWidth()
                    )
                }
                // boton para ver foto subida

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E)),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text = "Ver INE",
                        color = Color.White,
                        maxLines = 1,
                        fontSize = 10.sp,
                        modifier = Modifier.wrapContentWidth()
                    )
                }

                // Mostrar el diálogo cuando el botón sea presionado
                if (showDialog && ineUrl != null) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Imagen de INE") },
                        text = {
                            Column {
                                // Mostrar la imagen de la INE
                                if (ineUrl != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(ineUrl),
                                        contentDescription = "Foto de INE",
                                        modifier = Modifier.fillMaxWidth(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text("Cargando imagen...")
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }
            }
        }
    }
}
fun getImageUrlFromFirebase(email: String, onResult: (String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    // Buscar el documento del nutriologo usando el correo
    db.collection("nutriologos")
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                onResult(null)
            } else {
                // Obtener la URL de la INE
                val document = documents.first()
                val ineUrl = document.getString("ineUrl")
                onResult(ineUrl)
            }
        }
        .addOnFailureListener {
            onResult(null)
        }
}


// Vista previa de la pantalla de solicitudes de nutriólogos
@Preview(showBackground = true)
@Composable
fun AdminNutritionistRequestsScreenPreview() {
    // Crear una lista de solicitudes de nutriologos de ejemplo
    val sampleRequests = listOf(
        NutritionistRequest("Nutriólogo A", "nutriologoA@example.com", "En proceso",""),
        NutritionistRequest("Nutriólogo B", "nutriologoB@example.com", "Verificado",""),
        NutritionistRequest("Nutriólogo C", "nutriologoC@example.com", "Denegado",""),
    )

    // Usar tema de la aplicación
    NutricionAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF65558F)) // Fondo del Box
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Solicitudes de Nutriólogos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White, // Color del texto
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(sampleRequests) { request ->
                        RequestCard(request = request, onStatusChange = {})
                    }
                }
            }
        }
    }
}

