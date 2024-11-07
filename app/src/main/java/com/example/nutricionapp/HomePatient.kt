package com.example.nutricionapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.example.nutricionapp.ui.theme.NutricionAppTheme



@Composable
fun HomePatient(navController: NavHostController) {
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

    // Datos del paciente (puedes mantener tu implementación existente)
    val patientData = PatientData(
        name = "Carlos Ramírez",
        email = "carlos.ramirez@example.com",
        phone = "555-1234-567",
        address = "Av. Ejemplo 123, Ciudad",
        assignedNutritionist = "Dra. Martínez",
        nextAppointment = "5 de Noviembre, 10:00 AM"
    )

    var selectedItem by remember { mutableStateOf(0) }
    var currentScreen by remember { mutableStateOf("home") }

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
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Dietas") },
                    label = { Text("Dietas") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        currentScreen = "diet"
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
                    PatientHomeScreen(navController, patient = patientData)
                }
                "diet" -> {
                    DietScreen(navController, patient = patientData,onBackClick = { currentScreen = "home" })
                }
                "notifications" -> {
                    NotificationScreenPatient(
                        navController = rememberNavController(),
                        notifications = listOf(
                            Notification("Mensaje de Maria", "xxxxxxx."),
                            Notification("Dieta Actualizada", "La dieta de María López ha sido actualizada."),
                            Notification("Resultados de Análisis", "Los resultados de análisis de Carlos García están disponibles.")
                        ),
                        onBackClick = { currentScreen = "home" }
                    )
                }
                // Otras pantallas...
            }
        }
    }
}


@Composable
fun PatientHomeScreen(navController: NavHostController, patient: PatientData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Bienvenido, ${patient.name}",
            fontSize = 28.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Foto del paciente
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Cambia este recurso según la imagen del paciente
            contentDescription = "Foto del paciente",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f))
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )

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
                Text(
                    text = "Datos del paciente",
                    fontSize = 20.sp,
                    color = Color(0xFF4B3D6E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                PatientInfoRow(label = "Nombre", info = patient.name)
                PatientInfoRow(label = "Correo", info = patient.email)
                PatientInfoRow(label = "Teléfono", info = patient.phone)
                PatientInfoRow(label = "Dirección", info = patient.address)

                Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

                PatientInfoRow(label = "Nutriólogo asignado", info = patient.assignedNutritionist)

                if (patient.nextAppointment != null) {
                    Text(
                        text = "Próxima cita: ${patient.nextAppointment}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).
                        align(Alignment.CenterHorizontally)
                    )
                } else {
                    Text(
                        text = "No tiene citas programadas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp).
                        align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

}
@Composable
fun NotificationScreenPatient(navController: NavHostController,notifications: List<Notification>, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
            .padding(16.dp),
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
fun DietScreen(navController: NavHostController, patient: PatientData, onBackClick: () -> Unit) {
    var selectedTab by remember { mutableStateOf("Dieta") }
    val meals = remember { mutableStateListOf("Desayuno", "Comida", "Cena") } // Lista de comidas, modificable

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF65558F))
    ) {
        Text(
            text = "Dietas",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp).padding(16.dp)
        )
        // Información del paciente con foto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // (Info del paciente aquí, como ya está en tu código original)
        }

        // Barra de navegación de pestañas
//        NavigationBar(
//            containerColor = Color(0xFF4B3D6E),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            NavigationBarItem(
//                icon = { Text("Dieta", color = Color.White) },
//                selected = selectedTab == "Dieta",
//                onClick = { selectedTab = "Dieta" }
//            )
//            // Otras opciones de pestañas
//        }

        // Mostrar contenido según la pestaña seleccionada
        if (selectedTab == "Dieta") {
            DietContent(
                meals = meals,
                onAddSnack = { /* Código para agregar meriendas */ },
                onReorderMeals = { /* Código para cambiar el orden de comidas */ },
                onCommentClick= {/* Código para comentar comidas */  }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun DietContent(meals: List<String>, onAddSnack: () -> Unit, onReorderMeals: () -> Unit, onCommentClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        meals.forEach { meal ->
            MealCard(mealName = meal, onCommentClick = onCommentClick)

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(onClick = onReorderMeals) {
            Text("Reordenar Comidas")
        }
    }
}

@Composable
fun MealCard(mealName: String, onCommentClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB8A8D9)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = mealName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
            Text(
                text = "2 Huevos con jamon ",
                fontSize = 14.sp,
                color = Color.LightGray
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {

                IconButton(onClick = onCommentClick) {
                    Icon(
                        imageVector = Icons.Default.Email, // Icono de comentario
                        contentDescription = "Agregar comentario",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


//@Composable
//fun DietScreen(navController: NavHostController,patient: PatientData, onBackClick: () -> Unit) {
//    var selectedTab by remember { mutableStateOf("Dieta") }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF65558F))
//    ) {
//        // Información del paciente con foto
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                // Foto del paciente
//                Icon(
//                    imageVector = Icons.Filled.Person,
//                    contentDescription = "Foto del paciente",
//                    modifier = Modifier
//                        .size(64.dp)
//                        .padding(end = 16.dp),
//                    tint = Color.White
//                )
//
//                // Datos del paciente
//                Column(
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(text = patient.name, fontSize = 20.sp, color = Color.White)
//                    //Text(text = "Edad: ${patient.age}", fontSize = 16.sp, color = Color.LightGray)
//                    //Text(text = "Dieta: ${patient.diet}", fontSize = 16.sp, color = Color.LightGray)
//                }
//
//            }
//        }
//
//        // Barra de navegación de pestañas
//        NavigationBar(
//            containerColor = Color(0xFF4B3D6E),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            NavigationBarItem(
//                icon = { Text("Dieta", color = Color.White) },
//                selected = selectedTab == "Dieta",
//                onClick = { selectedTab = "Dieta" }
//            )
//            NavigationBarItem(
//                icon = { Text("Progreso", color = Color.White) },
//                selected = selectedTab == "Progreso",
//                onClick = { selectedTab = "Progreso" }
//            )
//            NavigationBarItem(
//                icon = { Text("Historial", color = Color.White) },
//                selected = selectedTab == "Historial",
//                onClick = { selectedTab = "Historial" }
//            )
//        }
//
//        // Mostrar contenido según la pestaña seleccionada
//        when (selectedTab) {
//            "Dieta" -> DietContent(navController = rememberNavController())
//            "Progreso" -> ProgressContent()
//            "Historial" -> HistoryContent()
//        }
//
//
//        // Este Spacer empuja la barra de navegación inferior hacia el fondo
//        Spacer(modifier = Modifier.weight(1f))
//
//        // Barra de navegación inferior
//
//
//
//
//
//    }
//}
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
fun NotificationScreenPreviewPatient() {
    NutricionAppTheme {
        NotificationScreenPatient(
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
fun PatientHomeScreenPatientPreview() {
    val patientData = PatientData(
        name = "Carlos Ramírez",
        email = "carlos.ramirez@example.com",
        phone = "555-1234-567",
        address = "Av. Ejemplo 123, Ciudad",
        assignedNutritionist = "Dra. Martínez",
        nextAppointment = "5 de Noviembre, 10:00 AM"
    )
    NutricionAppTheme {
        PatientHomeScreen(navController = rememberNavController(), patient = patientData)
    }
}
@Preview(showBackground = true)
@Composable
fun DietScreenPreview() {
    val patientData = PatientData(
        name = "Carlos Ramírez",
        email = "carlos.ramirez@example.com",
        phone = "555-1234-567",
        address = "Av. Ejemplo 123, Ciudad",
        assignedNutritionist = "Dra. Martínez",
        nextAppointment = "5 de Noviembre, 10:00 AM"
    )

    NutricionAppTheme {
        DietScreen(
            navController = rememberNavController(),
            patient = patientData,
            onBackClick = {}
        )
    }
}

