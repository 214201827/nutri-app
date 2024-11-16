package com.example.nutricionapp

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.window.Popup
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""


    Box(
        modifier = Modifier.fillMaxWidth()
    )
    {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Fecha de nacimiento") },
            //colors = TextFieldDefaults(NutricionAppTheme { R.color }),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                //.border(1.dp, Color(0xFF4B3D6E), shape = RoundedCornerShape(16.dp))
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

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}



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
        TextField(
            value = fullName,
            onValueChange = { fullName = it },
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

        Spacer(modifier = Modifier.height(16.dp))

        // ComboBox para fecha de nacimiento
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComboBox(
                label = "Día",
                items = days,
                selectedItem = selectedDay,
                onItemSelected = { selectedDay = it }
            )
            ComboBox(
                label = "Mes",
                items = months,
                selectedItem = selectedMonth,
                onItemSelected = { selectedMonth = it }
            )
            ComboBox(
                label = "Año",
                items = years,
                selectedItem = selectedYear,
                onItemSelected = { selectedYear = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = phone,
            onValueChange = { phone= it },
            label = { Text("Telefono") },
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

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = address,
            onValueChange = { address= it },
            label = { Text("Direccion") },
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

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email= it },
            label = { Text("Correo Electronico") },
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

        Spacer(modifier = Modifier.height(16.dp))

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
                .background(Color(0xFF4B3D6E)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de registro
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && selectedDay.isNotEmpty() && selectedMonth.isNotEmpty() && selectedYear.isNotEmpty()) {
                    // Calcula la edad a partir de la fecha de nacimiento
                    val birthDate = GregorianCalendar(selectedYear.toInt(), months.indexOf(selectedMonth), selectedDay.toInt())
                    val age = calculateAge(birthDate)

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            dbPatient.collection("pacientes").document(email).set(
                                hashMapOf(
                                    "historial" to listOf<DocumentReference>(),
                                    "medidas" to listOf<DocumentReference>(),
                                    "fullName" to fullName,
                                    "nutriAsign" to null,
                                    "fechaNacimiento" to birthDate.time,
                                    "age" to age,
                                    "phone" to phone.toInt(),
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

// Función para calcular la edad
fun calculateAge(birthDate: Calendar): Int {
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return age
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBox(label: String, items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedItem,
            onValueChange = { },
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon( // Custom trailing icon
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle Dropdown",
                    tint = Color.White
                )
            },
            modifier = modifier
                .menuAnchor()
                .background(Color(0xFF4B3D6E))
                .width(110.dp) // Establecer un ancho fijo
                .height(56.dp), // Establecer una altura fija
            maxLines = 1, // Asegúrate de que no expanda a más de una línea
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
        )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
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



@Preview(showBackground = true)
@Composable
fun PreviewRegisterPatientScreen() {
    RegisterPatientScreen(navController = rememberNavController())
}