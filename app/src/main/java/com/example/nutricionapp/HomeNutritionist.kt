package com.example.nutricionapp

import android.util.Log
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutricionapp.ui.theme.NutricionAppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Reminder(val patientName: String, val message: String)
data class Patient(val name: String, val age: Int, val diet: String)
data class Notification(val title: String, val message: String)

@Composable
fun HomeNutritionist(navController: NavHostController) {

    // Acceder al ConnectivityObserver
    val context = LocalContext.current
    val connectivityObserver = (context.applicationContext as NutricionApp).connectivityObserver

    // Coleccionar el estado de conectividad
    val isConnected by connectivityObserver.isConnected.collectAsState(initial = true)

    // Crear SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado para rastrear si ya se ha recibido la primera emisión
    var isFirstEmission by remember { mutableStateOf(true) }

    // Crear un CoroutineScope para manejar las Snackbars
    val coroutineScope = rememberCoroutineScope()

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
                Log.d("Snackbar", "Usuario clicó en Reintentar")
                // Aquí puedes llamar a una función para intentar reconectar
            }
        } else {
            snackbarHostState.showSnackbar(
                message = "Conexión restablecida",
                duration = SnackbarDuration.Short
            )
        }
    }

    val reminders = remember {
        listOf(
            Reminder("Juan Pérez", "Actualizar dieta"),
            Reminder("María López", "Cita el 30 de septiembre"),
            Reminder("Carlos García", "Enviar resultados de análisis"),
            Reminder("Laura Martínez", "Cita el 5 de octubre")
        )
    }

    val notifications = remember {
        listOf(
            Notification("Mensaje de Maria", "xxxxxxx."),
            Notification("Dieta Actualizada", "La dieta de María López ha sido actualizada."),
            Notification("Resultados de Análisis", "Los resultados de análisis de Carlos García están disponibles.")
        )
    }

    var selectedItem by remember { mutableStateOf(0) } // Mantener el ítem seleccionado
    var currentScreen by remember { mutableStateOf("home") } // Controlar qué pantalla mostrar

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Recordatorios",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de recordatorios
        LazyColumn {
            items(reminders.size) { index ->
                ReminderItem(reminder = reminders[index])
            }
        }
    }
    // Utilizar Scaffold para integrar SnackbarHost y BottomBar
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF4B3D6E)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        currentScreen = "home"
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "patient") },
                    label = { Text("Pacientes") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        currentScreen = "patients"
                    },
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
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        currentScreen = "notifications"
                    },
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
        // Contenido principal
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFF65558F))
        ) {
            when (currentScreen) {
                "home" -> {
                    RemindersScreen(navController, reminders)
                }
                "patients" -> {
                    PatientListScreen(navController
                        , onBackClick = { currentScreen = "home" })
                }
                "notifications" -> {
                    NotificationScreen(
                        navController = navController,
                        notifications = notifications,
                        onBackClick = { currentScreen = "home" }
                    )
                }
                // Otras pantallas...
            }
        }
    }
}

@Composable
fun RemindersScreen(navController: NavHostController, reminders: List<Reminder>) {
    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Recordatorios",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de recordatorios
        LazyColumn {
            items(reminders.size) { index ->
                ReminderItem(reminder = reminders[index])
            }
        }
    }
}
fun addReminder(patientName: String, message: String) {
    val db = FirebaseFirestore.getInstance()
    val remindersRef = db.collection("reminders")

    val reminder = hashMapOf(
        "patientName" to patientName,
        "message" to message
    )

    remindersRef.add(reminder)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore", "Recordatorio agregado con ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error al agregar recordatorio", e)
        }
}
@Composable
fun PatientListScreen(navController: NavHostController, onBackClick: () -> Unit) {
    val patients = remember { mutableStateListOf<Patient>() } // Lista mutable para almacenar los datos de pacientes
    var isLoading by remember { mutableStateOf(true) } // Controla el estado de carga

    // Llama a Firestore para cargar los datos de pacientes al iniciar la pantalla
    LaunchedEffect(Unit) {
        loadPatientsData(patients)
        isLoading = false // Cambia el estado de carga una vez que se carguen los datos
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp),
    ) {
        Text(
            text = "Lista de Pacientes",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            Text(
                text = "Cargando pacientes...",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        } else if (patients.isEmpty()) {
            Text(
                text = "No se encontraron pacientes.",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn {
                items(patients.size) { index ->
                    PatientItem(patient = patients[index]) {
                        // Navega a la pantalla de detalles del paciente con los parámetros
                        navController.navigate("patient_detail/${patients[index].name}/${patients[index].age}/${patients[index].diet}")
                    }
                }
            }
        }
    }
}
suspend fun loadPatientsData(patients: MutableList<Patient>) {
    val db = FirebaseFirestore.getInstance()
    val patientsRef = db.collection("pacientes")

    try {
        val documents = patientsRef.get().await() // Espera los datos con await()

        for (document in documents) {
            val patient = Patient(
                name = document.getString("fullName") ?: "Sin nombre",
                age = document.getLong("age")?.toInt() ?: 0,
                diet = document.getString("diet") ?: "Sin dieta"
            )
            patients.add(patient) // Agrega cada paciente a la lista
        }
    } catch (e: Exception) {
        Log.e("PatientListScreen", "Error al cargar los datos de pacientes", e)
    }
}


@Composable
fun PatientItem(patient: Patient, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = patient.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
            Text(text = "Edad: ${patient.age}", fontSize = 16.sp, color = Color.LightGray)
            Text(text = "Dieta: ${patient.diet}", fontSize = 16.sp, color = Color.LightGray)
        }
    }
}


@Composable
fun NotificationScreen(navController: NavHostController, notifications: List<Notification>, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp)
    ) {
        Text(
            text = "Notificaciones",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de notificaciones
        LazyColumn {
            items(notifications.size) { index ->
                NotificationItem(notification = notifications[index])
            }
        }
    }
}

@Composable
fun ReminderItem(reminder: Reminder) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = reminder.patientName,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = reminder.message,
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = notification.title,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}

// pantalla detalles del paciente aqui
///////////////////////////////////////////
//////////////////////////////////////////
/////////////////////////////////////////
@Composable
fun PatientDetailScreen(navController: NavHostController, patient: Patient, onBackClick: () -> Unit) {
    var selectedTab by remember { mutableStateOf("Dieta") } // Controlar la pestaña seleccionada
    var selectedItem by remember { mutableStateOf(1) } // Mantener el ítem seleccionado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
    ) {
        // Información del paciente con foto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Foto del paciente
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Foto del paciente",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 16.dp),
                    tint = Color.White
                )

                // Datos del paciente
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = patient.name, fontSize = 20.sp, color = Color.White)
                    Text(text = "Edad: ${patient.age}", fontSize = 16.sp, color = Color.LightGray)
                    Text(text = "Dieta: ${patient.diet}", fontSize = 16.sp, color = Color.LightGray)
                }
                Button(
                    onClick = {
                        navController.navigate("CreateAppoitment")
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E)),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Crear cita",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        "Cita",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Barra de navegación de pestañas
        NavigationBar(
            containerColor = Color(0xFF4B3D6E),
            modifier = Modifier.fillMaxWidth()
        ) {
            NavigationBarItem(
                icon = { Text("Dieta", color = Color.White) },
                selected = selectedTab == "Dieta",
                onClick = { selectedTab = "Dieta" }
            )
            NavigationBarItem(
                icon = { Text("Progreso", color = Color.White) },
                selected = selectedTab == "Progreso",
                onClick = { selectedTab = "Progreso" }
            )
            NavigationBarItem(
                icon = { Text("Historial", color = Color.White) },
                selected = selectedTab == "Historial",
                onClick = { selectedTab = "Historial" }
            )
        }

        // Mostrar contenido según la pestaña seleccionada
        when (selectedTab) {
            "Dieta" -> DietContent(navController = rememberNavController())
            "Progreso" -> ProgressContent()
            "Historial" -> HistoryContent()
        }


        // Este Spacer empuja la barra de navegación inferior hacia el fondo
        Spacer(modifier = Modifier.weight(1f))

        // Barra de navegación inferior





    }
}
@Composable
fun DietContent(navController: NavHostController) {
    // Aquí va el contenido relacionado con la dieta del paciente
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Dieta del paciente", fontSize = 18.sp, color = Color.White)
        // Añade aquí más detalles sobre la dieta

    }

}

@Composable
fun ProgressContent() {
    // Aquí va el contenido relacionado con el progreso del paciente
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Progreso del paciente", fontSize = 18.sp, color = Color.White)
        // Añade aquí más detalles sobre el progreso
    }
}

@Composable
fun HistoryContent() {
    // Aquí va el contenido relacionado con el historial del paciente
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Historial del paciente", fontSize = 18.sp, color = Color.White)
        // Añade aquí más detalles sobre el historial
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NutricionAppTheme {
        NotificationScreen(
            navController = rememberNavController(),
            notifications = listOf(
                Notification("Recordatorio de Cita", "Juan Pérez tiene una cita el 30 de septiembre."),
                Notification("Dieta Actualizada", "La dieta de María López ha sido actualizada.")
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeNutritionistPreview() {
    NutricionAppTheme {
        HomeNutritionist(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PatientListScreenPreview() {
    NutricionAppTheme {
        PatientListScreen(navController = rememberNavController(), onBackClick = {})
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewPatientDetailScreen() {
    // Crear un paciente de ejemplo
    val samplePatient = Patient(
        name = "Juan Pérez",
        age = 35,
        diet = "Alta en proteínas"
    )

    // Crear un NavController simulado
    val navController = rememberNavController()

    // Llamar a la pantalla con los datos de ejemplo
    PatientDetailScreen(
        navController = navController,
        patient = samplePatient,
        onBackClick = { /* Acción de ejemplo para el botón de atrás */ }
    )
}
