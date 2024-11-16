package com.example.nutricionapp.paciente

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nutricionapp.R
import com.example.nutricionapp.db.FirestoreRepository.getmyData
import com.example.nutricionapp.db.PacienteDb
//import com.example.nutricionapp.db.notify
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun NotificationListScreenpac(navController: NavController) {
    var paciente by remember { mutableStateOf<PacienteDb?>(null) }

    // Llama a la función getnotf para obtener los datos de Firestore
    LaunchedEffect(Unit) {
        getmyData { data ->
            paciente = data
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B3D6E)) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { navController.navigate("Pmenu") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Mis datos") },
                    label = { Text("Mis datos") },
                    selected = false,
                    onClick = { navController.navigate("Pperfil") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones") },
                    label = { Text("Notificaciones") },
                    selected = true,
                    onClick = { /* Ya estamos en la pantalla de notificaciones */ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    ) { innerPadding ->
        // Muestra las notificaciones en Cards
        paciente?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF65558F))
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Bienvenido, ${it.nombre ?: "Usuario"}",
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Foto del paciente
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Foto del paciente",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.3f))
                        .padding(4.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tarjeta de información del paciente
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB8A8D9)),
                    elevation = CardDefaults.elevatedCardElevation(6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Datos del paciente",
                            fontSize = 20.sp,
                            color = Color(0xFF4B3D6E),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Muestra información con valores por defecto si es nula
                        PatientInfoRow(label = "Nombre", info = it.nombre ?: "No disponible")
                        PatientInfoRow(label = "Correo", info = it.correo ?: "No disponible")
                        PatientInfoRow(label = "Teléfono", info = it.cel.toString() ?: "No disponible")
                        PatientInfoRow(label = "Dirección", info = it.dir ?: "No disponible")
                    }
                }
            }
        }
    }
}

@Composable
fun PatientInfoRow(label: String, info: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B3D6E)
        )
        Text(
            text = info,
            color = Color.Black
        )
    }
}

// Composable para mostrar una notificación
/*@Composable
fun NotificationItem1(notification: notify) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E)),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = notification.titulo,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = notification.descripcion,
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }

// Dialogo de confirmación para eliminar la notificación
                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Confirmar Eliminación") },
                        text = { Text("¿Estás seguro de que deseas eliminar esta notificación?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    deleteNotificationFromFirestore(12345)
                                    showDeleteDialog =
                                        false // Cierra el diálogo después de eliminar
                                }
                            ) {
                                Text("Eliminar")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDeleteDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}
// Función para eliminar la notificación en Firestore
fun deleteNotificationFromFirestore(pid: Int) {
    val db = FirebaseFirestore.getInstance()

    // Asumiendo que las notificaciones están almacenadas en una colección `notificaciones` con documentos que tienen el `Pid`
    db.collection("notif").document(pid.toString())
        .delete()
        .addOnSuccessListener {
            Log.d("Firestore", "Notificación con Pid $pid eliminada correctamente.")
            // Puedes agregar un Toast para notificar al usuario
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error al eliminar la notificación", e)
        }
}
*/