package com.example.nutricionapp.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.nutricionapp.db.Dieta
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.FirestoreRepository.clearComen
import com.example.nutricionapp.db.FirestoreRepository.updComen
import com.example.nutricionapp.db.PacienteDb
import com.example.nutricionapp.notificaciones.addNotificationForNutritionist
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Comentario(patientId: String, navController: NavHostController, NutId: String) {
    var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    var dieta by remember { mutableStateOf<List<Dieta>?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var editedValues by remember { mutableStateOf<Map<String, Map<String, String>>>(emptyMap()) } // Cambiar a un mapa anidado
    val daysOfWeek = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
    var selectedDayIndex by remember { mutableStateOf(0) } // Índice del día seleccionado

    //crear snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(patientId) {
        // Obtener datos del paciente
        FirestoreRepository.getPatientData(patientId) { data ->
            paciente = data
            data?.correo?.let { email ->
                // Obtener la dieta para el paciente
                FirestoreRepository.getDietData(email) { dietData ->
                    dieta = dietData
                }
            }
        }
    }

    var selectedItem by remember { mutableStateOf(1) }
    var currentScreen by remember { mutableStateOf("dietas") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFF65558F))
        ) {

//        }
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color(0xFF65558F))
//                .padding(10.dp)
//                .systemBarsPadding()
////        )
//        {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Aumentar espacio entre elementos
            ) {
                com.example.nutricionapp.nutritionist.DaySelector3(
                    daysOfWeek = daysOfWeek,
                    selectedDayIndex = selectedDayIndex,
                    onDayChange = { index -> selectedDayIndex = index }
                )

                // Lógica para mostrar la dieta según el día seleccionado
                dieta?.let { dietList ->
                    if (dietList.isNotEmpty()) {
                        val selectedDay = daysOfWeek[selectedDayIndex]
                        val dailyDiet = dietList.filter { it.dia == selectedDay }

                        if (dailyDiet.isNotEmpty()) {
                            dailyDiet.forEach { diet ->
                                Column(modifier = Modifier.fillMaxWidth()
                                    .verticalScroll(rememberScrollState())) {
                                    // Card para Desayuno
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            TextField(
                                                value = editedValues[selectedDay]?.get("desayuno-${diet.email}")
                                                    ?: diet.desayuno.comida,
                                                onValueChange = {}, // Add a no-op lambda or appropriate handler
                                                enabled = false,
                                                label = { Text("Desayuno") },
                                                modifier = Modifier.fillMaxWidth(),

                                            )
                                            TextField(
                                                value = editedValues[selectedDay]?.get("descr-desayuno-${diet.email}")
                                                    ?: diet.desayuno.descr,
                                                onValueChange = {}, // Add a no-op lambda or appropriate handler
                                                enabled = false,
                                                label = { Text("Descripción") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues[selectedDay]?.get("comentario-desayuno-${diet.email}")
                                                    ?: diet.desayuno.comentario,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + (selectedDay to
                                                            (editedValues[selectedDay] ?: emptyMap()) + ("comentario-desayuno-${diet.email}" to newValue))
                                                },
                                                label = { Text("Comentario") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }

                                    // Card para Comida
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            TextField(
                                                value = editedValues[selectedDay]?.get("comida-${diet.email}")
                                                    ?: diet.comida.comida,
                                                onValueChange = {}, // Add a no-op lambda or appropriate handler
                                                enabled = false,
                                                label = { Text("Comida") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues[selectedDay]?.get("descr-comida-${diet.email}")
                                                    ?: diet.comida.descr,
                                                onValueChange = {}, // Add a no-op lambda or appropriate handler
                                                enabled = false,
                                                label = { Text("Descripción") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues[selectedDay]?.get("comentario-comida-${diet.email}")
                                                    ?: diet.comida.comentario,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + (selectedDay to
                                                            (editedValues[selectedDay] ?: emptyMap()) + ("comentario-comida-${diet.email}" to newValue))
                                                },
                                                label = { Text("Comentario") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }

                                    // Card para Cena
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            TextField(
                                                value = editedValues[selectedDay]?.get("cena-${diet.email}")
                                                    ?: diet.cena.comida,
                                                onValueChange = {}, // Add a no-op lambda or appropriate handler
                                                enabled = false,
                                                label = { Text("Cena") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues[selectedDay]?.get("descr-cena-${diet.email}")
                                                    ?: diet.cena.descr,
                                                onValueChange = {}, // Add a no-op lambda or appropriate handler
                                                enabled = false,
                                                label = { Text("Descripción") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            TextField(
                                                value = editedValues[selectedDay]?.get("comentario-cena-${diet.email}")
                                                    ?: diet.cena.comentario,
                                                onValueChange = { newValue ->
                                                    editedValues = editedValues + (selectedDay to
                                                            (editedValues[selectedDay] ?: emptyMap()) + ("comentario-cena-${diet.email}" to newValue))
                                                },
                                                label = { Text("Comentario") },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                    // Botón para actualizar la dieta en todos los días de la semana

                                    /*Button(
                                        onClick = {
                                            // Actualizar dieta para todos los días de la semana
                                            daysOfWeek.forEach { day ->
                                                val dayDiet = dietList.filter { it.dia == day }
                                                dayDiet.forEach { diet ->
                                                    // Datos modificados o valores por defecto si no han sido editados
                                                    val desayunoData: Map<String, Any> = mapOf(
                                                        "comida" to (editedValues[day]?.get("desayuno-${diet.email}") ?: diet.desayuno.comida),
                                                        "descr" to (editedValues[day]?.get("descr-desayuno-${diet.email}") ?: diet.desayuno.descr),
                                                        "hora" to 8, // Ejemplo de hora
                                                        "comentario" to (editedValues[day]?.get("comentario-desayuno-${diet.email}") ?: diet.desayuno.comentario)
                                                    )
                                                    val comidaData: Map<String, Any> = mapOf(
                                                        "comida" to (editedValues[day]?.get("comida-${diet.email}") ?: diet.comida.comida),
                                                        "descr" to (editedValues[day]?.get("descr-comida-${diet.email}") ?: diet.comida.descr),
                                                        "hora" to 13,
                                                        "comentario" to (editedValues[day]?.get("comentario-comida-${diet.email}") ?: diet.comida.comentario)
                                                    )
                                                    val cenaData: Map<String, Any> = mapOf(
                                                        "comida" to (editedValues[day]?.get("cena-${diet.email}") ?: diet.cena.comida),
                                                        "descr" to (editedValues[day]?.get("descr-cena-${diet.email}") ?: diet.cena.descr),
                                                        "hora" to 20,
                                                        "comentario" to (editedValues[day]?.get("comentario-cena-${diet.email}") ?: diet.cena.comentario)
                                                    )

                                                    // Llama a la función `upd` para actualizar Firestore
                                                    updComen(patientId, day, desayunoData, comidaData, cenaData, NutId)
                                                    // Enviar notificación al nutricionista
                                                    addNotificationForNutritionist(
                                                        destId = NutId, // Assuming NutId is the destination ID
                                                        remId = patientId, // Assuming patientId is the remitter ID
                                                        type = "comentario", // Assuming the type is "comment"
                                                        message = "El paciente $patientId ha realizado un comentario sobre la dieta del día $day."
                                                    )
                                                }
                                            }

                                        }*/Button(
                                    onClick = {
                                        //obtener nombre del paciente
                                        val patientName = paciente?.nombre ?: "Paciente"
                                        // Variables para controlar el mensaje de la notificación
                                        var notificationMessage = "El paciente $patientName ha realizado un comentario sobre la dieta de los siguientes días: "
                                        val updatedDetails = mutableListOf<String>()  // Lista para almacenar detalles de los días y comidas

                                        // Actualizar dieta para todos los días de la semana
                                        daysOfWeek.forEach { day ->
                                            val dayDiet = dietList.filter { it.dia == day }
                                            dayDiet.forEach { diet ->
                                                // Obtener los comentarios y comidas de cada día
                                                val desayunoComentario = editedValues[day]?.get("comentario-desayuno-${diet.email}") ?: diet.desayuno.comentario
                                                val comidaComentario = editedValues[day]?.get("comentario-comida-${diet.email}") ?: diet.comida.comentario
                                                val cenaComentario = editedValues[day]?.get("comentario-cena-${diet.email}") ?: diet.cena.comentario

                                                // Solo agregar un día al mensaje si se ha comentado alguna comida
                                                if (desayunoComentario.isNotEmpty() || comidaComentario.isNotEmpty() || cenaComentario.isNotEmpty()) {
                                                    val dayDetails = mutableListOf<String>()
                                                    if (desayunoComentario.isNotEmpty()) dayDetails.add("Desayuno: $desayunoComentario")
                                                    if (comidaComentario.isNotEmpty()) dayDetails.add("Comida: $comidaComentario")
                                                    if (cenaComentario.isNotEmpty()) dayDetails.add("Cena: $cenaComentario")

                                                    // Agregar el detalle de este día a la lista de detalles
                                                    updatedDetails.add("$day: ${dayDetails.joinToString(", ")}")

                                                    // Llama a la función `upd` para actualizar Firestore con los comentarios
                                                    val desayunoData: Map<String, Any> = mapOf(
                                                        "comida" to (editedValues[day]?.get("desayuno-${diet.email}") ?: diet.desayuno.comida),
                                                        "descr" to (editedValues[day]?.get("descr-desayuno-${diet.email}") ?: diet.desayuno.descr),
                                                        "hora" to 8, // Ejemplo de hora
                                                        "comentario" to desayunoComentario
                                                    )
                                                    val comidaData: Map<String, Any> = mapOf(
                                                        "comida" to (editedValues[day]?.get("comida-${diet.email}") ?: diet.comida.comida),
                                                        "descr" to (editedValues[day]?.get("descr-comida-${diet.email}") ?: diet.comida.descr),
                                                        "hora" to 13,
                                                        "comentario" to comidaComentario
                                                    )
                                                    val cenaData: Map<String, Any> = mapOf(
                                                        "comida" to (editedValues[day]?.get("cena-${diet.email}") ?: diet.cena.comida),
                                                        "descr" to (editedValues[day]?.get("descr-cena-${diet.email}") ?: diet.cena.descr),
                                                        "hora" to 20,
                                                        "comentario" to cenaComentario
                                                    )

                                                    // Llama a la función `upd` para actualizar Firestore
                                                    updComen(patientId, day, desayunoData, comidaData, cenaData, NutId)
                                                }
                                            }
                                        }

                                        // Si hay detalles actualizados, crea una notificación con los días y las comidas comentadas
                                        if (updatedDetails.isNotEmpty()) {
                                            notificationMessage += updatedDetails.joinToString("; ")
                                            // Enviar notificación al nutricionista
                                            addNotificationForNutritionist(
                                                destId = NutId, // Asumiendo que NutId es el ID del nutricionista
                                                remId = patientId, // Asumiendo que patientId es el ID del paciente
                                                type = "comentario", // Tipo de notificación
                                                message = notificationMessage
                                            )
                                        }

                                        // Limpiar solo los comentarios (no las comidas ni las descripciones)
                                        dietList.forEach { diet ->
                                            val desayunoData: Map<String, Any> = mapOf(
                                                "comentario" to ""  // Limpiar solo comentario de desayuno
                                            )
                                            val comidaData: Map<String, Any> = mapOf(
                                                "comentario" to ""  // Limpiar solo comentario de comida
                                            )
                                            val cenaData: Map<String, Any> = mapOf(
                                                "comentario" to ""  // Limpiar solo comentario de cena
                                            )

                                            // Llama a la función `upd` para limpiar solo los comentarios en Firestore
                                            clearComen(patientId, diet.dia, desayunoData, comidaData, cenaData, NutId)
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Comentario enviado.")
                                            }
                                        }
                                        navController.popBackStack()
                                    },
                                        modifier = Modifier.fillMaxWidth()

                                    ) {
                                        Text("Comentar")
                                    }
                                }
                            }
                        } else {
                            Text("No hay dieta disponible para el día seleccionado", fontSize = 16.sp, color = Color.LightGray)
                        }
                    }
                }
            }
        }
    }
}
//---------------------------------------------------------------------------------------------------
@Composable
fun DaySelector3(
    daysOfWeek: List<String>,
    selectedDayIndex: Int,
    onDayChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
                // Cambiar al día anterior, asegurándose de que no salga de los límites
                val newIndex = if (selectedDayIndex > 0) selectedDayIndex - 1 else daysOfWeek.lastIndex
                onDayChange(newIndex)
            }
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Día anterior", tint = Color.White)
        }

        // Mostrar el día seleccionado
        Text(
            text = daysOfWeek[selectedDayIndex],
            color = Color.White,
            fontSize = 16.sp
        )

        IconButton(
            onClick = {
                // Cambiar al siguiente día, asegurándose de que no salga de los límites
                val newIndex = if (selectedDayIndex < daysOfWeek.lastIndex) selectedDayIndex + 1 else 0
                onDayChange(newIndex)
            }
        ) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Siguiente día", tint = Color.White)
        }
    }
}