package com.example.nutricionapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale


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
    var dbPatient = Firebase.firestore
    var fullName by remember { mutableStateOf("") }
    //var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val dialogText = remember { mutableStateOf("") }
    //combo box
    var selectedDay by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }

    val days = (1..31).map { it.toString() }
    val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    val years = (1930..2010).map { it.toString() }

// Diálogo
    if (showDialog.value) {
        DialogoRegistroPaciente(
            onDismissRequest = { showDialog.value = false },
            dialogText = dialogText.value
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F)

            ),
        contentAlignment = Alignment.Center
    ){}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).background(Color(0xFF65558F)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Paciente",fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall,color= Color.White)

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp)
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

        Spacer(modifier = Modifier.height(16.dp))



        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Espacio entre cada ComboBox
            verticalAlignment = Alignment.CenterVertically
        ) {
            ComboBox(
                label = "Día",
                items = days,
                selectedItem = selectedDay,
                onItemSelected = { selectedDay = it },
                //modifier = Modifier.weight(1f)
            )


            ComboBox(
                label = "Mes",
                items = months,
                selectedItem = selectedMonth,
                onItemSelected = { selectedMonth = it },
               // modifier = Modifier.padding(4.dp)
            )



            ComboBox(
                label = "Año",
                items = years,
                selectedItem = selectedYear,
                onItemSelected = { selectedYear = it },
                //modifier = Modifier.weight(1f)
            )
        }
        //DatePickerDocked()
        //Text(text = "Fecha seleccionada: $selectedDay de $selectedMonth del $selectedYear",color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp)
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

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
                //.padding(vertical = 8.dp)
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
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {
            /* Aquí hacer registro */

                if(email.isNotEmpty() && password.isNotEmpty() && selectedDay.isNotEmpty() && selectedMonth.isNotEmpty() && selectedYear.isNotEmpty()){


                // Formatear fecha ingresada
                    val calendar = GregorianCalendar(
                        selectedYear.toInt(),
                        months.indexOf(selectedMonth), // El índice correcto del mes (0-11)
                        selectedDay.toInt()
                    )

                    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                    val formattedDate = formatter.format(calendar.time) // Esto devuelve un String compatible con ISO 8601


                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.toString(),
                        password.toString()).addOnCompleteListener(){
                            if(it.isSuccessful){

                                dbPatient.collection("/usuarios/pacientes").document(email.toString()).set(
                                    hashMapOf(
                                        "historial" to listOf<DocumentReference>(),
                                        "medidas" to listOf<DocumentReference>(),
                                        "fullName" to fullName.toString(),
                                        "nutriAsign" to null,
                                        "fechaNacimiento" to formattedDate
                                    )
                                )


                                
                                // Implementar dialogo de exito aqui
                                dialogText.value = "Registro exitoso."
                                showDialog.value = true
                                navController.navigate("Login")

                            }else {
                                // Implementar dialogo de error aqui
                                dialogText.value = "Error de registro."
                                showDialog.value = true
                            }
                    }
                }

            },
            modifier = Modifier.width(220.dp)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )

        ) {
            Text("Registrarse",color = Color(0xFF65558F), fontSize = 16.sp)
        }

        //Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = { /* Aquí hacer registro con Google */ },
            modifier = Modifier
                .width(220.dp)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        ) {
            Text("Registrarse con Google",color = Color(0xFF65558F), fontSize = 16.sp) // Cambia a colorScheme
        }

        //Spacer(modifier = Modifier.height(.dp))


        TextButton(onClick = { navController.navigate("login") }) {
            Text("Ya tienes cuanta? Inicia sesion", color = Color.White)
        }
    }
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