package com.example.nutricionapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
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
    ) {
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
                .height(64.dp)
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
    var fullName by remember { mutableStateOf("") }
    //var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val dialogText = remember { mutableStateOf("") }

// Diálogo
    if (showDialog.value) {
        DialogoRegistroPaciente(
            onDismissRequest = { showDialog.value = false },
            dialogText = dialogText.value
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Paciente",fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        /*TextField(
            value = birthDate,
            singleLine = true,
            onValueChange = { birthDate = it },
            label = { Text("Fecha de Nacimiento (DD/MM/AAAA)") },
            modifier = Modifier.fillMaxWidth()
        )*/

        DatePickerDocked()

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {
            /* Aquí hacer registro */

                if(email.isNotEmpty() && password.isNotEmpty()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.toString(),
                        password.toString()).addOnCompleteListener(){
                            if(it.isSuccessful){
                                // Implementar dialogo de exito aqui
                                dialogText.value = "Registro exitoso."
                                showDialog.value = true

                            }else {
                                // Implementar dialogo de error aqui
                                dialogText.value = "Error de registro."
                                showDialog.value = true
                            }
                    }
                }

            },
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { /* Aquí hacer registro con Google */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) // Cambia a containerColor
        ) {
            Text("Registrarse con Google", color = MaterialTheme.colorScheme.onPrimary) // Cambia a colorScheme
        }

        Spacer(modifier = Modifier.height(16.dp))


        TextButton(onClick = { navController.navigate("login") }) {
            Text("Ya tienes cuanta? Inicia sesion")
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