package com.example.nutricionapp.nutritionist

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import com.example.nutricionapp.calcularEdad
import com.example.nutricionapp.db.Dieta
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.FirestoreRepository.upd2
import com.example.nutricionapp.db.PacienteDb
import java.util.Date
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.nutricionapp.HistorialScreen


@Composable
fun DietaNutritionist(patientId: String, navController: NavHostController){
    var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    var dieta by remember { mutableStateOf<List<Dieta>?>(null) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var isEditing by remember { mutableStateOf(false) }
    var editedValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val daysOfWeek = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
    var selectedDayIndex by remember { mutableStateOf(0) } // Índice del día seleccionado
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var pesoInicial by remember { mutableStateOf(paciente?.pesoI?.toString() ?: "") }
    var pesoActual by remember { mutableStateOf(paciente?.peso?.toString() ?: "") }
    var pesoMeta by remember { mutableStateOf(paciente?.PesoMeta?.toString() ?: "") }
    var imcInicial by remember { mutableStateOf(paciente?.imcI?.toString() ?: "") }
    var imcActual by remember { mutableStateOf(paciente?.imc?.toString() ?: "") }
    var immInicial by remember { mutableStateOf(paciente?.immI?.toString() ?: "") }
    var immActual by remember { mutableStateOf(paciente?.imm?.toString() ?: "") }
    val imageUrl = paciente?.profileImage  // La URL de la imagen en Firebase



    // Llama a la función para obtener los datos del paciente
    LaunchedEffect(patientId) {
        FirestoreRepository.getPatientData(patientId) { data ->
            paciente = data
            // Usa el correo del paciente (no el ID) para obtener los datos de la dieta
            data?.correo?.let { correo ->
                FirestoreRepository.getDietData(correo) { dietData ->
                    dieta = dietData
                }
            }
        }
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Expresión regular para verificar solo números y un punto decimal
    val validInputPattern = "^[0-9]*\\.?[0-9]+\$".toRegex()

    fun isValidInput(input: String): Boolean {
        return input.isEmpty() || validInputPattern.matches(input)
    }

    fun handleValueChange(value: String, updateState: (String) -> Unit) {
        if (isValidInput(value)) {
            updateState(value)
            errorMessage = null
        } else {
            errorMessage = "Solo se permiten números y un punto decimal."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                // Caja con información del paciente
            } ?: run {
                Text("No se encontraron datos del paciente", fontSize = 16.sp, color = Color.White)
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
                        //cargar imagen de la base de datos aqui
                        if (imageUrl == "null"){
                            //poner icono de user
                            Icon(
                                //pintar blanco

                                imageVector = Icons.Filled.Person,
                                contentDescription = "Perfil de paciente",
                                tint = Color.White,
                                modifier = Modifier.size(80.dp)
                            )
                        }else {
                            val painter = rememberImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl) // Firestore URL or Firebase Storage URL
                                    .crossfade(true) // Enable crossfade transition
                                    .build()
                            )
                            if (painter.state is AsyncImagePainter.State.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp), // Adjust size as necessary
                                    color = Color.Gray
                                )
                            }
                            Image(
                                painter = painter,
                                contentDescription = "Perfil de paciente",
                                modifier = Modifier.size(80.dp)
                            )
                        }


                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${it.nombre}",
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
                        

                        // Diálogo para seleccionar la fecha
//                        if (showDatePicker) {
//                            com.example.nutricionapp.nutriologo.DatePickerDialog(
//                                onDismissRequest = { showDatePicker = false },
//                                onDateSelected = { date ->
//                                    selectedDate = date
//                                    showDatePicker = false
//                                    // Aquí puedes agregar la lógica para manejar la hora, si es necesario
//                                }
//                            )
//                        }
                    }
                }
            } ?: run {
                Text("No se encontraron datos del paciente", fontSize = 16.sp, color = Color.White)
            }




            // TabRow para alternar entre información del paciente y dieta
            CustomTabRow(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index -> selectedTabIndex = index }
            )
            when (selectedTabIndex) {
                0 -> {
                    DaySelector(
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
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        // Card para Desayuno
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFB8A8D9)),
                                            elevation = CardDefaults.elevatedCardElevation(4.dp),
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                    text = "Desayuno",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = Color(0xFF4B3D6E)
                                                )
                                                Text(
                                                    text = " ${diet.desayuno.comida}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = Color(0xFF4B3D6E)
                                                )
                                                Text(
                                                    text = " ${diet.desayuno.descr}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = Color(0xFF616161)
                                                )
                                            }
                                        }

                                        // Card para Comida
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFB8A8D9)),
                                            elevation = CardDefaults.elevatedCardElevation(4.dp),
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                    text = "Comida",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = Color(0xFF4B3D6E)
                                                )
                                                Text(
                                                    text = " ${diet.comida.comida}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = Color(0xFF4B3D6E)
                                                )
                                                Text(
                                                    text = " ${diet.comida.descr}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = Color(0xFF616161)
                                                )
                                            }
                                        }

                                        // Card para Cena
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFB8A8D9)),
                                            elevation = CardDefaults.elevatedCardElevation(4.dp),
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                    text = "Cena",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = Color(0xFF4B3D6E)
                                                )
                                                Text(
                                                    text = " ${diet.cena.comida}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = Color(0xFF4B3D6E)
                                                )
                                                Text(
                                                    text = " ${diet.cena.descr}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = Color(0xFF616161)
                                                )
                                            }
                                        }
                                        //botton flotante para editar dieta

                                        FloatingActionButton(
                                            onClick = { navController.navigate("updDiet/$patientId") }
                                            ,modifier = Modifier.align(Alignment.CenterHorizontally),
                                            containerColor = Color(0xFF4B3D6E)

                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Editar dieta",
                                                tint = Color.White
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }

                                }
                            }
                        }
                    }
                }

                //---------------------------------------------------------------------------------------------------
                1 -> {
                    var pacienteState by remember { mutableStateOf(paciente) }

                    pacienteState?.let { paciente ->
                        Column {
                            // Mostrar la información del paciente en Cards
                            // Card para el Peso
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE0E0E0)),
                                elevation = CardDefaults.elevatedCardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Peso Inicial: ${paciente.pesoI}",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Peso Actual: ${paciente.peso}",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Peso Meta: ${paciente.PesoMeta}",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                    val pesoActual = paciente.peso ?: 0.0
                                    val pesoMeta = paciente.PesoMeta ?: 0.0

                                    val diferenciaPeso = pesoActual - pesoMeta
                                    val mensajePeso = if (diferenciaPeso > 0) {
                                        "Estás a ${"%.2f".format(diferenciaPeso)} kilos de tu peso ideal."
                                    } else if (diferenciaPeso == 0.0) {
                                        "¡Has alcanzado tu peso ideal!"
                                    } else {
                                        "Estás a ${"%.2f".format(-diferenciaPeso)} kilos por debajo de tu peso ideal."
                                    }
                                    Text(
                                        text = mensajePeso,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Card para el IMC
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE0E0E0)),
                                elevation = CardDefaults.elevatedCardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "IMC Inicial: ${paciente.imcI}",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "IMC Actual: ${paciente.imc}",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Card para el IMM
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE0E0E0)),
                                elevation = CardDefaults.elevatedCardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "IMM Inicial: ${paciente.immI}",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "IMM Actual: ${paciente.imm}",
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            pesoInicial = pacienteState?.pesoI?.toString()
                                ?: "" // Si paciente es nulo o pesoI es nulo, asigna ""
                            pesoActual = pacienteState?.peso?.toString() ?: ""
                            pesoMeta = pacienteState?.PesoMeta?.toString() ?: ""
                            imcInicial = pacienteState?.imcI?.toString() ?: ""
                            imcActual = pacienteState?.imc?.toString() ?: ""
                            immInicial = pacienteState?.immI?.toString() ?: ""
                            immActual = pacienteState?.imm?.toString() ?: ""
                            showDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3D6E))
                    ) {
                        Text("Editar Dieta", color = Color.White)
                    }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Actualizar datos") },
                            text = {
                                Column {
                                    OutlinedTextField(
                                        value = pesoInicial,
                                        onValueChange = {
                                            handleValueChange(
                                                it,
                                                { pesoInicial = it })
                                        },
                                        label = { Text("Peso Inicial") },
                                        isError = errorMessage != null
                                    )
                                    OutlinedTextField(
                                        value = pesoActual,
                                        onValueChange = {
                                            handleValueChange(
                                                it,
                                                { pesoActual = it })
                                        },
                                        label = { Text("Peso Actual") },
                                        isError = errorMessage != null
                                    )
                                    OutlinedTextField(
                                        value = pesoMeta,
                                        onValueChange = {
                                            handleValueChange(
                                                it,
                                                { pesoMeta = it })
                                        },
                                        label = { Text("Peso Meta") },
                                        isError = errorMessage != null
                                    )
                                    OutlinedTextField(
                                        value = imcInicial,
                                        onValueChange = {
                                            handleValueChange(
                                                it,
                                                { imcInicial = it })
                                        },
                                        label = { Text("IMC Inicial") },
                                        isError = errorMessage != null
                                    )
                                    OutlinedTextField(
                                        value = imcActual,
                                        onValueChange = {
                                            handleValueChange(
                                                it,
                                                { imcActual = it })
                                        },
                                        label = { Text("IMC Actual") },
                                        isError = errorMessage != null
                                    )
                                    OutlinedTextField(
                                        value = immInicial,
                                        onValueChange = {
                                            handleValueChange(
                                                it,
                                                { immInicial = it })
                                        },
                                        label = { Text("IMM Inicial") },
                                        isError = errorMessage != null
                                    )
                                    OutlinedTextField(
                                        value = immActual,
                                        onValueChange = {
                                            handleValueChange(
                                                it,
                                                { immActual = it })
                                        },
                                        label = { Text("IMM Actual") },
                                        isError = errorMessage != null
                                    )

                                    errorMessage?.let {
                                        Text(
                                            text = it,
                                            color = Color.Red,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        upd2(
                                            patientId,
                                            pesoInicial.toDoubleOrNull(),
                                            pesoActual.toDoubleOrNull(),
                                            pesoMeta.toDoubleOrNull(),
                                            imcInicial.toDoubleOrNull(),
                                            imcActual.toDoubleOrNull(),
                                            immInicial.toDoubleOrNull(),
                                            immActual.toDoubleOrNull()
                                        )
                                        // Actualizar pacienteState con los nuevos datos
                                        pacienteState = pacienteState?.copy(
                                            pesoI = pesoInicial.toDoubleOrNull(),
                                            peso = pesoActual.toDoubleOrNull(),
                                            PesoMeta = pesoMeta.toDoubleOrNull(),
                                            imcI = imcInicial.toDoubleOrNull(),
                                            imc = imcActual.toDoubleOrNull(),
                                            immI = immInicial.toDoubleOrNull(),
                                            imm = immActual.toDoubleOrNull()
                                        )
                                        showDialog = false
                                    }
                                ) {
                                    Text("Guardar")
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

                //---------------------------------------------------------------------------------------------------
                2 -> {
                    // Lógica para mostrar el historial del paciente
                    HistorialScreen(patientId)

                }
            }
        }
    }
}
@Composable
fun DietCard(
    diet: Dieta,
    mealType: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {


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
            Text(
                text = mealType,
                fontSize = 18.sp,
                color = Color.White
            )

        }
    }
}

//---------------------------------------------------------------------------------------------------

// Navegador entre info del paciente y dieta
@Composable
fun CustomTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: (@Composable () -> Unit)? = null,
    //icon: (@Composable () -> Unit)? = null,
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
//        j,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor
    )
}

@Composable
fun CustomTabRow(
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
        CustomTab(
            selected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) },
            text = {
                Text(
                    "Dieta",
                    color = Color.White,

                )
            }
        )
        CustomTab(
            selected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) },
            text = {
                Text(
                    "Progreso",
                    color = Color.White,
                    //modifier = Modifier.wrapContentWidth()
                )
            }
        )
        CustomTab(
            selected = selectedTabIndex == 2,
            onClick = { onTabSelected(2) },

            text = {
                Text(
                    "Historial",
                    color = Color.White,
                    //modifier = Modifier.wrapContentWidth()
                )
            }
        )
    }
}
//---------------------------------------------------------------------------------------------------
@Composable
fun DaySelector(
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
