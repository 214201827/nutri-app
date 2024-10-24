package com.example.nutricionapp

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

@Composable
fun RegisterNutScreen(navController: NavHostController) {
    // Inicializar Firebase
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance("gs://nutri-app-a90ca.appspot.com")


    // Estados para manejar la carga
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // State variables to hold the selected URIs
    var photoIneUri by remember { mutableStateOf<Uri?>(null) }
    var professionalLicenseUri by remember { mutableStateOf<Uri?>(null) }

    var showSuccessDialog by remember { mutableStateOf(false) }




    // Launcher for selecting PDF files (Cédula profesional)
    val pickPdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            professionalLicenseUri = uri
        }
    }

    // Launcher for selecting images or PDFs (Foto INE)
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
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de Nutriologo",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4B3D6E)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = licenseNumber,
            onValueChange = { licenseNumber = it },
            label = { Text("Número de cédula") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color(0xFF4B3D6E), shape = RoundedCornerShape(16.dp))
                .background(Color(0xFF4B3D6E)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color(0xFF4B3D6E), shape = RoundedCornerShape(16.dp))
                .background(Color(0xFF4B3D6E)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color(0xFF4B3D6E), shape = RoundedCornerShape(16.dp))
                .background(Color(0xFF4B3D6E)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button for "Agregar Foto del INE"
        Button(
            onClick = {
                // Launch file picker for images or PDFs
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

        // Button for "Agregar cédula profesional"
        Button(
            onClick = {
                // Launch file picker for PDFs only
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
            onClick = { /*onRegister(name, licenseNumber, email, password)*/

                isLoading = true
                errorMessage = null

                // Validar campos requeridos
                if (email.isBlank() || password.isBlank() || name.isBlank() || licenseNumber.isBlank() || photoIneUri == null || professionalLicenseUri == null) {
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

                        val ineUploadTask = ineRef.putFile(photoIneUri!!)
                        val licenseUploadTask = licenseRef.putFile(professionalLicenseUri!!)

                        // Esperar a que ambos archivos se suban
                        ineUploadTask.continueWithTask { ineTask ->
                            if (!ineTask.isSuccessful) {
                                throw ineTask.exception ?: Exception("Error al subir la foto del INE")
                            }
                            ineRef.downloadUrl
                        }.addOnCompleteListener { ineUrlTask ->
                            if (ineUrlTask.isSuccessful) {
                                val ineDownloadUrl = ineUrlTask.result.toString()

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
                                            "fullName" to name,
                                            "licenseNumber" to licenseNumber,
                                            "email" to email,
                                            "ineUrl" to ineDownloadUrl,
                                            "licenseUrl" to licenseDownloadUrl,
                                            // Agrega otros campos si es necesario
                                        )

                                        firestore.collection("/usuarios/nutriologos").document(email)
                                            .set(nutriologoData)
                                            .addOnSuccessListener {
                                                isLoading = false
                                                // Navegar a otra pantalla o mostrar un mensaje de éxito
                                            }
                                            .addOnFailureListener { e ->
                                                isLoading = false
                                                errorMessage = "Error al guardar datos: ${e.message}"
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
            )
        ) {
            Text(
                text = "Registrarse",
                color = Color(0xFF4B3D6E)
            )
        }
    }
}

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