package com.example.nutricionapp

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.util.Date
import java.util.GregorianCalendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterPatientScreen(navController: NavHostController) {
    val dbPatient = Firebase.firestore
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
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

    val showDialog = remember { mutableStateOf(false) }
    val dialogText = remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    var selectedDate: Date? by remember { mutableStateOf(null) }   // Estado para la fecha seleccionada
    val calendar = GregorianCalendar()

    // Combo box para fecha de nacimiento
    var selectedDay by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }

    val days = (1..31).map { it.toString() }
    val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    val years = (1930..2010).map { it.toString() }

    // Diálogo de mensaje
    if (showDialog.value) {
        DialogoRegistroPaciente(
            onDismissRequest = { showDialog.value = false },
            dialogText = dialogText.value
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Paciente", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall, color= Color.White)

        Spacer(modifier = Modifier.height(24.dp))

        // Campos de entrada
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

        Spacer(modifier = Modifier.height(16.dp))

        //fechapicker

        DatePickerComponent { selectedDate = it }

        var fechaNacimiento = selectedDate

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone= it },
            label = { Text("Telefono") },
            modifier = Modifier
                .fillMaxWidth() ,
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

        OutlinedTextField(
            value = address,
            onValueChange = { address= it },
            label = { Text("Direccion") },
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email= it },
            label = { Text("Correo Electronico") },
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

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de registro
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()&& fullName.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()) {
                    // Calcula la edad a partir de la fecha de nacimiento
                    //val birthDate = GregorianCalendar(selectedYear.toInt(), months.indexOf(selectedMonth), selectedDay.toInt())
                    val age = calculateAge(selectedDate)

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            dbPatient.collection("pacientes").document(email).set(
                                hashMapOf(
//                                    "historial" to listOf<DocumentReference>(),
//                                    "medidas" to listOf<DocumentReference>(),
                                    "fullName" to fullName,
                                    "nutriAsign" to null,
                                    "fechaNacimiento" to fechaNacimiento,
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

                            ).addOnCompleteListener { result ->
                                if (result.isSuccessful) {
                                    dialogText.value = "Registro exitoso."
                                    showDialog.value = true
                                    navController.navigate("Login")
                                } else {
                                    dialogText.value = "Error de registro en Firestore."
                                    showDialog.value = true
                                }
                            }
                        } else {
                            dialogText.value = "Error de autenticación."
                            showDialog.value = true
                        }
                    }
                }
            },
            modifier = Modifier.width(220.dp).padding(vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Registrarse", color = Color(0xFF65558F), fontSize = 16.sp)
        }

        TextButton(onClick = { navController.navigate("login") }) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = Color.White)
        }
    }
}

@Composable
fun DialogoRegistroPaciente(onDismissRequest: () -> Unit, dialogText: String) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = dialogText,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewRegisterPatientScreen() {
    RegisterPatientScreen(navController = rememberNavController())
}