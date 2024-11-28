package com.example.nutricionapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.input.pointer.pointerInput
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterNutScreen(navController: NavHostController) {
    // Inicializar Firebase
    val dbPatient = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance("gs://nutri-app-a90ca.appspot.com")
    var selectedDate: Date? by remember { mutableStateOf(null) }   // Estado para la fecha seleccionada

    val calendar = GregorianCalendar()

    // Estados para manejar la carga y el diálogo de éxito
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // datos adicionales para modo paciente
    var Nid by remember { mutableStateOf("") }
    var diet by remember { mutableStateOf("") }
    var nextAppointment by remember { mutableStateOf("") }
    var imc by remember { mutableStateOf(0) }
    var imcI by remember { mutableStateOf(0) }
    var imm by remember { mutableStateOf(0) }
    var immI by remember { mutableStateOf(0) }
    var peso by remember { mutableStateOf(0) }
    var pesoI by remember { mutableStateOf(0) }
    var PesoMeta by remember { mutableStateOf(0) }
    var profileImage by remember { mutableStateOf<Uri?>(null) }

    // Variables de estado para los URIs seleccionados
    var photoIneUri by remember { mutableStateOf<Uri?>(null) }
    var professionalLicenseUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para seleccionar archivos PDF (Cédula profesional)
    val pickPdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            professionalLicenseUri = uri
        }
    }

    // Launcher para seleccionar imágenes o PDFs (Foto INE)
    val pickImageOrPdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            photoIneUri = uri
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF5A4A8A), Color(0xFF65558F)),
                    startY = 0f,
                    endY = 1000f
                )
            ),
        //contentAlignment = Alignment.Center
    ) {
        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .alpha(if (isLoading) 0.5f else 1f) // Cambia la opacidad si está cargando
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro de Nutriólogo",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nombre completo") },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor =  Color.White,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = licenseNumber,
                onValueChange = { licenseNumber = it },
                label = { Text("Número de cédula") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor =  Color.White,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor =  Color.White,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor =  Color.White,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Direccion") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor =  Color.White,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de teléfono
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor =  Color.White,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // date picke
            DatePickerComponent { selectedDate = it }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para "Agregar Foto del INE"
            Button(
                onClick = {
                    // Lanzar selector de archivos para imágenes o PDFs
                    pickImageOrPdfLauncher.launch(arrayOf("application/pdf", "image/*"))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = if (photoIneUri == null) "Agregar Foto del INE" else "Archivo Agregado",
                    color = Color(0xFF4B3D6E)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para "Agregar cédula profesional"
            Button(
                onClick = {
                    // Lanzar selector de archivos para PDFs solamente
                    pickPdfLauncher.launch(arrayOf("application/pdf"))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = if (professionalLicenseUri == null) "Agregar cédula profesional" else "Archivo Agregado",
                    color = Color(0xFF4B3D6E)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Botón de registro
            Button(
                onClick = {

                    isLoading = true
                    errorMessage = null

                    // Validar campos requeridos
                    if (email.isBlank() || password.isBlank() || fullName.isBlank() || licenseNumber.isBlank() || photoIneUri == null || professionalLicenseUri == null) {
                        errorMessage = "Por favor, completa todos los campos y agrega los archivos requeridos."
                        isLoading = false
                        return@Button
                    }

                    // Crear usuario en Firebase Auth
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            val userId = auth.currentUser?.uid ?: ""

                            // Subir archivos a Firebase Storage
                            val ineRef = storage.reference.child("nutriologos/$userId/ine/${UUID.randomUUID()}")
                            val licenseRef = storage.reference.child("nutriologos/$userId/cedula/${UUID.randomUUID()}")

                            val age = calculateAge(selectedDate)

                            val ineUploadTask = ineRef.putFile(photoIneUri!!)
                            val licenseUploadTask = licenseRef.putFile(professionalLicenseUri!!)

                            // Subir la foto del INE
                            ineUploadTask.continueWithTask { ineTask ->
                                if (!ineTask.isSuccessful) {
                                    throw ineTask.exception ?: Exception("Error al subir la foto del INE")
                                }
                                ineRef.downloadUrl
                            }.addOnCompleteListener { ineUrlTask ->
                                if (ineUrlTask.isSuccessful) {
                                    val ineDownloadUrl = ineUrlTask.result.toString()

                                    // Subir la cédula profesional
                                    licenseUploadTask.continueWithTask { licenseTask ->
                                        if (!licenseTask.isSuccessful) {
                                            throw licenseTask.exception ?: Exception("Error al subir la cédula profesional")
                                        }
                                        licenseRef.downloadUrl
                                    }.addOnCompleteListener { licenseUrlTask ->
                                        if (licenseUrlTask.isSuccessful) {
                                            val licenseDownloadUrl = licenseUrlTask.result.toString()

                                            // Guardar datos en Firestore
                                            val nutriologoData = hashMapOf(
                                                "fullName" to fullName,
                                                "licenseNumber" to licenseNumber,
                                                "email" to email,
                                                "ineUrl" to ineDownloadUrl,
                                                "address" to address,
                                                "phone" to phone.toLong(),
                                                "fechaNacimiento" to calendar.time,
                                                "age" to age,
                                                "procesoVerificacion" to "En proceso",
                                                "licenseUrl" to licenseDownloadUrl
                                            )
                                            val modePaciente = hashMapOf(
                                                "fullName" to fullName,
                                                "nutriAsign" to null,
                                                "fechaNacimiento" to calendar.time,
                                                "age" to age,
                                                "phone" to phone.toLong(),
                                                "address" to address,
                                                "email" to email,
                                                "Nid" to Nid,
                                                "diet" to diet,
                                                "nextAppointment" to nextAppointment,
                                                "imc" to imc,
                                                "imcI" to imcI,
                                                "imm" to imm,
                                                "immI" to immI,
                                                "peso" to peso,
                                                "pesoI" to pesoI,
                                                "PesoMeta" to PesoMeta,
                                                "profileImage" to profileImage.toString()
                                            )

                                            firestore.collection("/pacientes").document(email)
                                                .set(modePaciente)
                                                .addOnSuccessListener {
                                                    // Crear dieta principal en Firestore
                                                    val db = FirebaseFirestore.getInstance()
                                                    val diet = hashMapOf("id" to email)
                                                    val dietRef = db.collection("diets").document(email)

                                                    dietRef.set(diet).addOnSuccessListener {
                                                        val desayuno = hashMapOf(
                                                            "comida" to "Sin asignar",
                                                            "descr" to "Sin asignar",
                                                            "hora" to 8
                                                        )
                                                        val comida = hashMapOf(
                                                            "comida" to "Sin asignar",
                                                            "descr" to "Sin asignar",
                                                            "hora" to 14
                                                        )
                                                        val cena = hashMapOf(
                                                            "comida" to "Sin asignar",
                                                            "descr" to "Sin asignar",
                                                            "hora" to 20
                                                        )
                                                        val daysOfWeek = listOf(
                                                            "lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"
                                                        )

                                                        val tasks = mutableListOf<Task<Void>>()
                                                        daysOfWeek.forEach { day ->
                                                            val dayRef = dietRef.collection(day)
                                                            tasks.add(dayRef.document("desayuno").set(desayuno))
                                                            tasks.add(dayRef.document("comida").set(comida))
                                                            tasks.add(dayRef.document("cena").set(cena))
                                                        }

                                                        Tasks.whenAll(tasks).addOnSuccessListener {
                                                            isLoading = false
                                                            showSuccessDialog = true
                                                        }.addOnFailureListener { e ->
                                                            isLoading = false
                                                            errorMessage = "Error al crear dieta: ${e.message}"
                                                        }
                                                    }.addOnFailureListener { e ->
                                                        isLoading = false
                                                        errorMessage = "Error al crear dieta principal: ${e.message}"
                                                    }
                                                }
                                                .addOnFailureListener { e ->
                                                    isLoading = false
                                                    errorMessage = "Error al guardar datos del paciente: ${e.message}"
                                                }

                                            firestore.collection("/nutriologos").document(email)
                                                .set(nutriologoData)
                                                .addOnFailureListener { e ->
                                                    isLoading = false
                                                    errorMessage = "Error al guardar datos del nutriólogo: ${e.message}"
                                                }
                                        } else {
                                            isLoading = false
                                            errorMessage = "Error al obtener URL de la cédula profesional: ${licenseUrlTask.exception?.message}"
                                        }
                                    }
                                } else {
                                    isLoading = false
                                    errorMessage = "Error al obtener URL del INE: ${ineUrlTask.exception?.message}"
                                }
                            }
                        } else {
                            isLoading = false
                            errorMessage = "Error al crear usuario: ${authTask.exception?.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                enabled = !isLoading // Deshabilita el botón mientras está cargando
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color(0xFF4B3D6E))
                } else {
                    Text(
                        text = "Registrarse",
                        color = Color(0xFF4B3D6E)
                    )
                }
            }

            // Mostrar mensaje de error si existe
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Mostrar indicador de carga en el centro de la pantalla
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        // Diálogo de éxito
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { /* No hacer nada para obligar al usuario a interactuar */ },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            // Actualiza el estado para navegar después de cerrar el diálogo
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }
                    ) {
                        Text("Aceptar", color = Color(0xFF4B3D6E))
                    }
                },
                title = { Text("Registro Exitoso") },
                text = { Text("Tu registro ha sido completado con éxito.") },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            )
        }

    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewRegisterNutScreen() {
    RegisterNutScreen(navController = rememberNavController())
}
//@Preview(showBackground = true)
//@Composable
//fun RegisterNutritionistScreenPreview() {
//    RegisterNutritionistScreen { name, licenseNumber, email, password ->
//        // Acciones de registro, como imprimir en consola o navegar a otra pantalla
//        println("Registered: $name, $licenseNumber, $email")
//    }
//}
