package com.example.nutricionapp.nutriologo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.Paciented

@Composable
fun PatientListScreen(navController: NavController) {
    var patientDataList by remember { mutableStateOf(listOf<Paciented>()) }
    var showDialog by remember { mutableStateOf(false) }
    var pidInput by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) } // Para el diálogo de confirmación de eliminación
    var patientToDelete by remember { mutableStateOf<Paciented?>(null) } // Paciente seleccionado para eliminar

    // Llama a la función para obtener los datos usando FirestoreRepository
    LaunchedEffect(Unit) {
        FirestoreRepository.getCityData { data ->
            patientDataList = data
        }
    }


    // Diálogo para ingresar el Pid
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cambiar Nid") },
            text = {
                Column {
                    Text("Ingrese el Pid del paciente:")
                    TextField(
                        value = pidInput,
                        onValueChange = { pidInput = it },
                        label = { Text("Pid") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (pidInput.isNotBlank()) {
                        // Cambiar el Nid del paciente a 12345
                        FirestoreRepository.changeNid(pidInput, 12345) { success ->
                            if (success) {
                                // Recarga la lista de pacientes
                                FirestoreRepository.getCityData { data ->
                                    patientDataList = data
                                }
                            }
                            showDialog = false
                            pidInput = "" // Resetear el campo
                        }
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de confirmación para eliminar paciente
    if (showDeleteDialog && patientToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Paciente") },
            text = { Text("¿Está seguro de que desea eliminar a ${patientToDelete!!.nombre}?") },
            confirmButton = {
                Button(onClick = {
                    // Lógica para eliminar el paciente
                    FirestoreRepository.deleteNip(patientToDelete!!.id.toString()) { success ->
                        if (success) {
                            // Recarga la lista de pacientes
                            FirestoreRepository.getCityData { data ->
                                patientDataList = data
                            }
                        }
                    }
                    showDeleteDialog = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B3D6E)) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { navController.navigate("reminders") }, // Navegar a pantalla de inicio
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Pacientes") },
                    label = { Text("Pacientes") },
                    selected = true,
                    onClick = { /* Ya estamos en la pantalla de pacientes */ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones") },
                    label = { Text("Notificaciones") },
                    selected = false,
                    onClick = { navController.navigate("notifications") }, // Navegar a pantalla de notificaciones
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF65558F))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Lista de Pacientes",
                    fontSize = 24.sp,
                    color = Color.White,
                )
                // Botón para crear un nuevo paciente
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showDialog = true } // Abre el diálogo
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Nuevo Paciente",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Nuevo Paciente",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(patientDataList.size) { index ->
                    PatientItem(
                        patient = patientDataList[index],
                        onClick = {
                            navController.navigate("patientDetail/${patientDataList[index].id}")
                        },
                        onDelete = {
                            // Establece el paciente a eliminar y muestra el diálogo de confirmación
                            patientToDelete = patientDataList[index]
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }
}

// Composable para mostrar un paciente en una Card
@Composable
fun PatientItem(patient: Paciented, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E)),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = patient.nombre,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = "ID: ${patient.id}",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }
            IconButton(onClick = { onDelete() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Paciente",
                    tint = Color.Red
                )
            }
        }
    }
}


