package com.example.nutricionapp

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutricionapp.db.FirestoreRepository.downloadFile
import com.example.nutricionapp.db.FirestoreRepository.getHistorialFiles
import com.example.nutricionapp.db.Hist
import com.example.nutricionapp.db.Record
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

fun calcularEdad(fechaNacimiento: Timestamp?): Int? {
    if (fechaNacimiento == null) return null

    val fechaActual = Calendar.getInstance().time
    val nacimiento = fechaNacimiento.toDate()

    val calendarioNacimiento = Calendar.getInstance().apply { time = nacimiento }
    val calendarioActual = Calendar.getInstance().apply { time = fechaActual }

    var edad = calendarioActual.get(Calendar.YEAR) - calendarioNacimiento.get(Calendar.YEAR)
    if (calendarioActual.get(Calendar.DAY_OF_YEAR) < calendarioNacimiento.get(Calendar.DAY_OF_YEAR)) {
        edad--
    }

    return edad
}

@Composable
fun ReminderItem(record: Record) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = record.titulo,
                    fontSize = 18.sp,
                    color = Color.White
                )
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar notif",
                        tint = Color.Red
                    )
                }

            }

            Text(
                text = record.descr,
                fontSize = 14.sp,
                color = Color.LightGray
            )

        }
    }
}
@Composable
fun HistorialScreen(patientId: String) {
    val context = LocalContext.current
    var files by remember { mutableStateOf<List<Hist>>(emptyList()) }

    // Obtener los archivos de Firebase Storage
    LaunchedEffect(patientId) {
        getHistorialFiles(patientId) { fileNames ->
            files = fileNames // Aquí 'files' es una lista de objetos Hist
        }
    }

    // Mostrar los archivos en una lista de Cards sin usar 'items'
    LazyColumn {
        // Iterar sobre la lista de 'files'
        files.forEach { hist ->
            item {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mostrar el nombre del archivo
                        Text(
                            text = hist.name, // Acceso correcto a la propiedad 'name' de Hist
                            modifier = Modifier.weight(1f)
                        )

                        // Botón con ícono de descarga
                        IconButton(onClick = {
                            downloadFile(patientId, hist.name) { success ->
                                if (success) {
                                    Toast.makeText(context, "Archivo descargado", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error al descargar el archivo", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Descargar archivo"
                            )
                        }

                    }
                }
            }
        }
    }
}