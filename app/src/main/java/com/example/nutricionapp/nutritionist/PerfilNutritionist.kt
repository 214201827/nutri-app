package com.example.nutricionapp.nutritionist

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.nutricionapp.NutricionApp
import com.example.nutricionapp.ProfileImagen.ImageSelector
import com.example.nutricionapp.ProfileImagen.uploadImageToFirebase
import com.example.nutricionapp.R
import com.example.nutricionapp.calcularEdad
import com.example.nutricionapp.db.Dieta
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.PacienteDb
import com.example.nutricionapp.ui.theme.signOut
import java.util.Date

@Composable
fun PerfilNutritionist(patientId: String,navController: NavHostController) {
    var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    var dieta by remember { mutableStateOf<List<Dieta>?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var Nid: String? = null
    var Pid: String? = null
    val patientNid = paciente?.Nid
    var showDialog by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }


    // Acceder al ConnectivityObserver
    val context = LocalContext.current
    val connectivityObserver = (context.applicationContext as NutricionApp).connectivityObserver

    // Coleccionar el estado de conectividad
    val isConnected by connectivityObserver.isConnected.collectAsState(initial = true)

    // Crear SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado para rastrear si ya se ha recibido la primera emisión
    var isFirstEmission by remember { mutableStateOf(true) }

    // Mostrar Snackbar cuando cambie la conectividad, ignorando la primera emisión


    LaunchedEffect(isConnected) {
        if (isFirstEmission) {
            isFirstEmission = false
            return@LaunchedEffect // Ignorar la primera emisión
        }

        if (!isConnected) {
            val result = snackbarHostState.showSnackbar(
                message = "Desconectado de la red",
                actionLabel = "Reintentar",
                duration = SnackbarDuration.Indefinite
            )
            if (result == SnackbarResult.ActionPerformed) {
                // Lógica para reintentar la conexión
                // Por ejemplo, podrías intentar reconectar o refrescar datos
            }
        } else {
            snackbarHostState.showSnackbar(
                message = "Conexión restablecida",
                duration = SnackbarDuration.Short
            )

        }
    }
    // Llama a la función getmyData para obtener los datos de Firestore
    LaunchedEffect(patientId) {
        FirestoreRepository.getPatientData(patientId) { data ->
            paciente = data
            // Usa el correo del paciente (no el ID) para obtener los datos de la dieta
            data?.correo?.let { correo ->
                FirestoreRepository.getDietData(correo) { dietData ->
                    dieta = dietData
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ){innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF65558F))
                .padding(10.dp)
                .systemBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Aumentar espacio entre elementos
            ) {

                // mostrar datos del paciente
                Text(
                    text = "Bienvenido ${paciente?.nombre}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
                paciente?.let {
                    // Foto del paciente

                    Image(
                        painter = when {
                            selectedImageUri != null -> {
                                // Si el usuario ha seleccionado una imagen
                                rememberAsyncImagePainter(selectedImageUri)
                            }
                            paciente?.profileImage != null -> {
                                // Si hay una imagen guardada en Firestore
                                rememberAsyncImagePainter(paciente?.profileImage)
                            }
                            else -> {
                                // Imagen predeterminada
                                painterResource(id = R.drawable.ic_launcher_foreground)
                            }
                        },
                        contentDescription = "Foto del paciente",
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { showDialog = true }
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.3f))
                            .padding(4.dp),
                        contentScale = ContentScale.Crop
                    )
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Seleccionar acción") },
                            text = {
                                Column {
                                    // Botón para seleccionar imagen
                                    ImageSelector { uri ->
                                        selectedImageUri = uri
                                    }
                                    // Botón para subir imagen
                                    if (selectedImageUri != null) {

                                        isUploading = true
                                        uploadImageToFirebase(patientId, selectedImageUri!!) { success ->
                                            isUploading = false
                                            if (success) {
                                                Log.d("Firebase", "Imagen subida exitosamente")
                                                showDialog = false
                                            }
                                        }
                                    }
                                }

                            },
                            confirmButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
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
                            Row {
                                Text(
                                    text = "Datos del nutriologo",
                                    fontSize = 20.sp,
                                    color = Color(0xFF4B3D6E),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                // boton para cerrar sesion
                                Button(
                                    onClick = {
                                        // Cerrar sesión de Firebase y limpiar preferencias
                                        signOut(navController)
                                    },
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .wrapContentSize(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E)),
                                    contentPadding = PaddingValues(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Output,
                                        contentDescription = "Cerrar sesión",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp) // Tamaño del ícono
                                    )
                                }

                            }
                            // Muestra información con valores por defecto si es nula
                            PatientInfoRow(label = "Nombre", info = paciente?.nombre ?: "No disponible")
                            val edad = calcularEdad(paciente?.fecha)
                            PatientInfoRow(label = "Edad", info = edad.toString() ?: "No disponible")
                            PatientInfoRow(label = "Correo", info = patientId?: "No disponible")
                            PatientInfoRow(label = "Teléfono", info = paciente?.cel.toString()?: "No disponible")
                            PatientInfoRow(label = "Dirección", info = paciente?.dir ?: "No disponible")
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun PatientInfoRow(label: String, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = "$label:",
            color = Color(0xFF4B3D6E),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp)
        )
        Text(text = info, color = Color(0xFF616161))
    }
}