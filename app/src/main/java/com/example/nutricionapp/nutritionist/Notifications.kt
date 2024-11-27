package com.example.nutricionapp.nutritionist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import java.util.Date
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notifications(nutId: String, navController: NavHostController) {
    val notifications = remember { mutableStateListOf<Map<String, Any>>() }

    // Escucha las notificaciones asociadas al nutricionista
    LaunchedEffect(nutId) {
        listenToNotifications(nutId) { newNotifications ->
            notifications.clear()
            notifications.addAll(newNotifications)
        }
    }

    //dialogo para responder notificaciones
    val openDialog = remember { mutableStateOf(false) }
    var newMessage by remember { mutableStateOf("") }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones",color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF65558F)),

                //modifier = Modifier.padding(8.dp)
            )
        },
        floatingActionButton = {
            if (notifications.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        notifications.forEach { notification ->
                            val notificationId = notification["id"] as String
                            markNotificationAsRead(notificationId)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text("Marcar todas")
                }
            }
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFF65558F))
            ) {
                if (notifications.isEmpty()) {
                    Text(
                        text = "No tienes notificaciones.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(notifications) { notification ->
                            val notificationId = notification["id"] as String
                            var message = notification["message"] as String
                            val remId = notification["RemId"] as String
                            val type = notification["type"] as String
                            val timestamp = Date(notification["timestamp"] as Long)
                            val isRead = notification["read"] as Boolean
                            val dest = notification["DestId"] as String


                            // responder notificaciones
                            if (openDialog.value) {
                                AlertDialog(

                                    onDismissRequest = { openDialog.value = false },
                                    title = { Text("Responder notificación") },
                                    text = {
                                        Column {


                                            TextField(
                                                value = newMessage,
                                                onValueChange = { newMessage = it },
                                                label = { Text("Mensaje") }
                                            )
                                        }
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = { respondNotification(destId = remId , remId = nutId, type = "Respuesta", message = newMessage, notificationId)
                                                openDialog.value = false

                                            }
                                        ) {
                                            Text("Responder")
                                        }
                                    },
                                    dismissButton = {
                                        Button(onClick = { openDialog.value = false }) {
                                            Text("Cancelar")
                                        }
                                    }
                                )
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.elevatedCardElevation(4.dp),
                                colors = if (isRead) {
                                    CardDefaults.cardColors(containerColor = Color.DarkGray)
                                } else {
                                    CardDefaults.cardColors(containerColor = Color(0xFFB8A8D9))
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isRead) Color.Gray else Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "De: $remId | Tipo: $type",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "Hora: $timestamp",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = { markNotificationAsRead(notificationId) },
                                            // modifier = Modifier.align(Alignment.End),
                                            enabled = !isRead
                                        ) {
                                            Text(if (isRead) "Leída" else "Marcar como leída")
                                        }
                                        Button(
                                            //abrir dialogo para responder
                                            onClick = {  openDialog.value = true },

                                            ) {
                                            Text("Responder")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )

} //mandar notificacion de vuelta
fun respondNotification(destId: String, remId: String, type: String, message: String, notificationId: String) {
    val db = FirebaseFirestore.getInstance()
    markNotificationAsRead(notificationId)
    val notification = hashMapOf(
        "DestId" to destId,
        "RemId" to remId,
        "type" to type,
        "message" to message,
        "timestamp" to System.currentTimeMillis(),
        "read" to false
    )

    db.collection("notificaciones")
        .add(notification)
        .addOnSuccessListener {
            Log.d("Firestore", "Notificación creada correctamente.")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al crear la notificación.", e)
        }
}
fun addNotificationForNutritionist(destId: String, remId: String, type: String, message: String) {
    val db = FirebaseFirestore.getInstance()



    val notification = hashMapOf(
        "DestId" to destId,
        "RemId" to remId,
        "type" to type,
        "message" to message,
        "timestamp" to System.currentTimeMillis(),
        "read" to false
    )

    db.collection("notificaciones")
        .add(notification)
        .addOnSuccessListener {
            Log.d("Firestore", "Notificación creada correctamente.")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al crear la notificación.", e)
        }
}

fun listenToNotifications(nutId: String, onNotificationReceived: (List<Map<String, Any>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("notificaciones")
        .whereEqualTo("DestId", nutId)
        .addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.w("Firestore", "Error al escuchar notificaciones", error)
                return@addSnapshotListener
            }

            val notifications = snapshots?.documents?.map { doc ->
                val data = doc.data!!
                data["id"] = doc.id // Agregar ID del documento para identificarlo
                data
            } ?: emptyList()
            onNotificationReceived(notifications)
        }
}
fun markNotificationAsRead(notificationId: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("notificaciones").document(notificationId)
        .update("read", true)
        .addOnSuccessListener {
            Log.d("Firestore", "Notificación marcada como leída.")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error al marcar notificación como leída.", e)
        }
}
//Elimnar notificaciones leidas

fun DeleteNotificationes() {
    val db = FirebaseFirestore.getInstance()
    db.collection("notificaciones")
        .whereEqualTo("read", true)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                db.collection("notificaciones").document(document.id).delete()
            }
        }
        .addOnFailureListener { exception ->
            Log.w("Firestore", "Error getting documents: ", exception)
        }
}

