package com.example.nutricionapp.paciente

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.sp
import com.example.nutricionapp.db.Dieta2
import androidx.navigation.NavController
import com.example.nutricionapp.calcularEdad
import com.example.nutricionapp.db.FirestoreRepository.addCommentToFirestore
import com.example.nutricionapp.db.FirestoreRepository.getMyDiet
import com.example.nutricionapp.db.FirestoreRepository.getmyData
import com.example.nutricionapp.db.PacienteDb
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun PatientDetailScreenP(navController: NavController){
    var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    var dieta by remember { mutableStateOf<List<Dieta2>?>(null) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val daysOfWeek = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
    var selectedDayIndex by remember { mutableStateOf(0) } // Índice del día seleccionado
    var showDialog by remember { mutableStateOf(false) }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var Nid: Int? = null
    var Pid: Int? = null
    val patientId = paciente?.id
    val patientNid = paciente?.Nid

    LaunchedEffect(Unit) {
        // Convertir userId a Int

            getmyData { data ->
                paciente = data
                getMyDiet { dietData ->
                    dieta = dietData
                }
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B3D6E)) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = { navController.navigate("RecordatorioScreenpac") }, // Navegar a pantalla de inicio
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
                    onClick = { navController.navigate("Pnotifications") }, // Navegar a pantalla de notificaciones
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF65558F))
                .padding(10.dp)
                .systemBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Aumentar espacio entre elementos
            ) {
                // Información del paciente
                paciente?.let {
                    Nid = it.Nid
                    Pid = it.id
                    // Caja con información del paciente
                } ?: run {
                    Text("No se encontraron tus datos", fontSize = 16.sp, color = Color.White)
                }

                // Información del paciente
                paciente?.let {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Foto del paciente",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(end = 16.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Nombre: ${it.nombre}",
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                                val edad = calcularEdad(it.fecha)
                                Text(
                                    text = "Edad: ${edad ?: "Desconocida"}", // Mostrar "Desconocida" si es nula
                                    fontSize = 16.sp,
                                    color = Color.LightGray
                                )
                                Text(
                                    text = "Correo: ${it.correo ?: "No disponible"}",
                                    fontSize = 16.sp,
                                    color = Color.LightGray
                                )
                            }
                            Button(
                                onClick = {
                                    // Muestra el cuadro de diálogo para seleccionar fecha y hora
                                },
                                modifier = Modifier.wrapContentSize(),
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
                                Text("Cita", color = Color.White, fontSize = 12.sp)
                            }

                            // Diálogo para seleccionar la fecha
                        }
                    }
                } ?: run {
                    Text("No se encontraron datos del paciente", fontSize = 16.sp, color = Color.White)
                }




                // TabRow para alternar entre información del paciente y dieta
                CustomTabRow1(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index -> selectedTabIndex = index }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTabIndex) {
                    0 -> {
                        DaySelector1(
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
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            // Card para Desayuno
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 8.dp),
                                                elevation = CardDefaults.elevatedCardElevation(4.dp),
                                                shape = MaterialTheme.shapes.medium
                                            ) {
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    Text(
                                                        text = "Desayuno",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                    Text(
                                                        text = " ${diet.desayuno.comida}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                    Text(
                                                        text = " ${diet.desayuno.descr}",
                                                        style = MaterialTheme.typography.bodySmall,
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
                                                    Text(
                                                        text = "Comida",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                    Text(
                                                        text = " ${diet.comida.comida}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                    Text(
                                                        text = " ${diet.comida.descr}",
                                                        style = MaterialTheme.typography.bodySmall,
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
                                                    Text(
                                                        text = "Cena",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                    Text(
                                                        text = " ${diet.cena.comida}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                    Text(
                                                        text = " ${diet.cena.descr}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                }
                                            }

                                            // Comentarios
                                            Button(
                                                onClick = { showDialog = true },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xFF4B3D6E)
                                                )
                                            ) {
                                                Text("Comentarios", color = Color.White)
                                            }

                                            // Dialogo para agregar comentarios
                                            if (showDialog) {
                                                AlertDialog(
                                                    onDismissRequest = { showDialog = false },
                                                    title = { Text(text = "Nuevo Comentario") },
                                                    text = {
                                                        Column {
                                                            // TextField para Asunto
                                                            OutlinedTextField(
                                                                value = subject,
                                                                onValueChange = { subject = it },
                                                                label = { Text("Asunto") },
                                                                modifier = Modifier.fillMaxWidth()
                                                            )
                                                            Spacer(modifier = Modifier.height(8.dp))
                                                            // TextField para Descripción
                                                            OutlinedTextField(
                                                                value = description,
                                                                onValueChange = {
                                                                    description = it
                                                                },
                                                                label = { Text("Descripción") },
                                                                modifier = Modifier.fillMaxWidth()
                                                            )
                                                        }
                                                    },
                                                    confirmButton = {
                                                        Button(
                                                            onClick = {
                                                                // Solo llama a la función si patientId y patientNid no son nulos
                                                                patientId?.let { Rid ->
                                                                    patientNid?.let { Did ->
                                                                        addCommentToFirestore(
                                                                            subject,
                                                                            description,
                                                                            Rid,
                                                                            Did
                                                                        )
                                                                        showDialog = false
                                                                        subject = ""
                                                                        description = ""
                                                                    }
                                                                } ?: run {
                                                                    // Manejo de error en caso de que alguno sea nulo
                                                                    Log.e(
                                                                        "ConfirmButton",
                                                                        "Error: patientId o patientNid son nulos"
                                                                    )
                                                                }
                                                            }
                                                        ) {
                                                            Text("Enviar")
                                                        }
                                                    },
                                                    dismissButton = {
                                                        Button(onClick = { showDialog = false }) {
                                                            Text("Cancelar")
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        "No hay dieta disponible para el día seleccionado",
                                        fontSize = 16.sp,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        } ?: run {
                            Text(
                                "Aún no tienes una dieta asignada",
                                fontSize = 16.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                    //---------------------------------------------------------------------------------------------------
                    1 -> {
                        paciente?.let { paciente ->
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre ítems
                            ) {
                                // Ítem para el Peso
                                item {
                                    ListItemContent(
                                        title = "Peso Inicial",
                                        value = paciente.pesoI.toString()
                                    )
                                    ListItemContent(
                                        title = "Peso Actual",
                                        value = paciente.peso.toString()
                                    )
                                }

                                // Ítem para el IMC
                                item {
                                    ListItemContent(
                                        title = "IMC Inicial",
                                        value = paciente.imcI.toString()
                                    )
                                    ListItemContent(
                                        title = "IMC Actual",
                                        value = paciente.imc.toString()
                                    )
                                }

                                // Ítem para el IMM
                                item {
                                    ListItemContent(
                                        title = "IMM Inicial",
                                        value = paciente.immI.toString()
                                    )
                                    ListItemContent(
                                        title = "IMM Actual",
                                        value = paciente.imm.toString()
                                    )
                                }
                            }
                        }
                    }
                }
                    //---------------------------------------------------------------------------------------------------
                   // 2 -> {
                        // Lógica para mostrar el historial del paciente
                        //val patientId = "12345" // Obtén el patientId que necesites
                        //HistorialScreen1()

                    //}
                }
            }
        }
    }
//---------------------------------------------------------------------------------------------------
@Composable
fun DietCard1(
    diet: Dieta2,
    mealType: String,
) {
    val mealContent = when (mealType) {
        "Desayuno" -> diet.desayuno
        "Comida" -> diet.comida
        "Cena" -> diet.cena
        else -> "No disponible"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4B3D6E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Título de la comida
            Text(
                text = mealType,
                fontSize = 20.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

//---------------------------------------------------------------------------------------------------

// Navegador entre info del paciente y dieta
@Composable
fun CustomTab1(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    selectedContentColor: Color = Color.White,
    unselectedContentColor: Color = selectedContentColor,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        text = {
            text?.invoke()
        },
        icon = {
            icon?.invoke()
        },
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor
    )
}

@Composable
fun CustomTabRow1(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp)),
        selectedTabIndex = selectedTabIndex,
        containerColor = Color(0xFF4B3D6E),
        contentColor = Color.White
    ) {
        CustomTab1(
            selected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) },
            text = {
                Text(
                    "Dieta",
                    color = Color.White,
                    fontSize = 20.sp, // Ajusta el tamaño de la fuente
                )
            }
        )
        CustomTab1(
            selected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) },
            text = {
                Text(
                    "Progreso",
                    color = Color.White,
                    fontSize = 20.sp, // Ajusta el tamaño de la fuente
                )
            }
        )
        CustomTab1(
            selected = selectedTabIndex == 2,
            onClick = { onTabSelected(2) },
            text = {
                Text(
                    "Historial",
                    color = Color.White,
                    fontSize = 20.sp, // Ajusta el tamaño de la fuente
                )
            }
        )
    }
}
//---------------------------------------------------------------------------------------------------
@Composable
fun DaySelector1(
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

@Composable
fun HistorialScreen1() {

}
// Función para agregar el comentario en Firestore

//---------------------------------------------------------------------------------------------------
@Composable
fun ListItemContent(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4B3D6E), shape = RoundedCornerShape(8.dp)) // Color de fondo y bordes redondeados
            .clip(RoundedCornerShape(8.dp)) // Aplica el redondeo de esquinas
            .padding(16.dp)
    ) {
        Text(text = "$title: $value", fontSize = 16.sp, color = Color.White)
    }
}