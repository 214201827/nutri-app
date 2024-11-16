package com.example.nutricionapp.ProfileImagen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ImageSelector(onImageSelected: (Uri?) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri) // Devuelve la URI seleccionada
    }

    Button(
        onClick = { launcher.launch("image/*") },
        modifier = Modifier.padding(top = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E))
    ) {
        Icon(
            imageVector = Icons.Default.Upload,
            contentDescription = "Subir imagen",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Seleccionar Imagen", color = Color.White)
    }
}
