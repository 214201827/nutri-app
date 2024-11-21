package com.example.nutricionapp.patient
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutricionapp.calcularEdad
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nutricionapp.R
import com.example.nutricionapp.db.Dieta
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.PacienteDb
import androidx.compose.ui.platform.LocalContext
import com.example.nutricionapp.NutricionApp
import com.example.nutricionapp.ProfileImagen.ImageSelector
import com.example.nutricionapp.ProfileImagen.uploadImageToFirebase
import java.util.Date
import coil.compose.rememberAsyncImagePainter
import com.example.nutricionapp.ui.theme.signOut


@Composable
fun InicioPatient(patientId: String,navController: NavHostController) {
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
    paciente?.let {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                // Diálogo para seleccionar la fecha
                if (showDatePicker) {
                    com.example.nutricionapp.nutritionist.DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        onDateSelected = { date ->
                            selectedDate = date
                            showDatePicker = false
                            // Aquí puedes agregar la lógica para manejar la hora, si es necesario
                        }
                    )
                }
            }
        }
    } ?: run {
        Text("No se encontraron datos del paciente", fontSize = 16.sp, color = Color.White)
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
                    //.padding(16.dp),
                ,horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Aumentar espacio entre elementos
            ) {

                // mostrar datos del paciente
                Text(
                    text = "Bienvenido ${paciente?.nombre}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
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
                                    text = "Datos del paciente",
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
                                    //icono
                                    Icon(
                                        imageVector = Icons.Default.Output,
                                        contentDescription = "Cerrar sesión",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
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

                            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

                            PatientInfoRow(label = "Nutriólogo asignado", info = paciente?.Nid ?: "No disponible")

                            val nextAppointment = paciente?.nextAppointment ?:"No disponible"
                            Text(
                                text = if (nextAppointment != null) {
                                    "Próxima cita: $nextAppointment"
                                } else {
                                    "No tiene citas programadas"
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                Button(
                    onClick = {
                        // Muestra el cuadro de diálogo para seleccionar fecha y hora
                        showDatePicker = true
                    },
                    modifier = Modifier.wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E)),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Solicitar cita",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Solicitar dieta", color = Color.White)

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

@Preview(showBackground = true)
@Composable
fun PreviewInicioPatient() {
    val navController = rememberNavController()
    InicioPatient(navController = navController as NavHostController, patientId = "abad@gmail.com")
}

