package com.example.nutricionapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutricionapp.ui.theme.NutricionAppTheme

// Data class for Nutritionist requests
data class NutritionistRequest(val name: String, val email: String, val status: String, val id: String = "")

// Composable function for the admin requests screen
@Composable
fun AdminNutritionistRequestsScreen() {
    val db = FirebaseFirestore.getInstance()
    val requests = remember { mutableStateOf<List<NutritionistRequest>>(emptyList()) }

    // Escucha en tiempo real las solicitudes desde Firestore
    LaunchedEffect(Unit) {
        db.collection("nutritionistRequests")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    Log.w("Firestore", "Error al escuchar solicitudes", e)
                    return@addSnapshotListener
                }

                // Mapea los documentos de Firestore a objetos NutritionistRequest
                val newRequests = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(NutritionistRequest::class.java)?.copy(id = doc.id)
                }
                requests.value = newRequests
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
                        db.collection("nutritionistRequests").document(request.id)
                            .update("status", newStatus)
                            .addOnSuccessListener { Log.d("Firestore", "Estado actualizado") }
                            .addOnFailureListener { e -> Log.w("Firestore", "Error al actualizar estado", e) }
                    })
                }
            }
        }
    }
}

// Composable para cada solicitud individual
@Composable
fun RequestCard(request: NutritionistRequest, onStatusChange: (String) -> Unit) {
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
                text = "Nombre: ${request.name}",
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
                text = "Estado actual: ${request.status}",
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
                    onClick = { onStatusChange("en proceso") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E)),
                    contentPadding = PaddingValues(horizontal = 4.dp) // Margen interno reducido
                ) {
                    Text(
                        text = "En Proceso",
                        color = Color.White,
                        maxLines = 1,
                        fontSize = 10.sp, // Tamaño de texto reducido aún más
                        modifier = Modifier.wrapContentWidth()
                    )
                }
                // Botón para "Verificado"
                Button(
                    onClick = { onStatusChange("verificado") },
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
                    onClick = { onStatusChange("denegado") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E)),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text = "Denegado",
                        color = Color.White,
                        maxLines = 1,
                        fontSize = 10.sp,
                        modifier = Modifier.wrapContentWidth()
                    )
                }
            }
        }
    }



}

// Vista previa de la pantalla de solicitudes de nutriólogos
@Preview(showBackground = true)
@Composable
fun AdminNutritionistRequestsScreenPreview() {
    // Crear una lista de solicitudes de nutriologos de ejemplo
    val sampleRequests = listOf(
        NutritionistRequest("Nutriólogo A", "nutriologoA@example.com", "En proceso"),
        NutritionistRequest("Nutriólogo B", "nutriologoB@example.com", "Verificado"),
        NutritionistRequest("Nutriólogo C", "nutriologoC@example.com", "Denegado")
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

