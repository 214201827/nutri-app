package com.example.nutricionapp.patient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.nutricionapp.HistorialScreen
import com.example.nutricionapp.db.Dieta
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.PacienteDb
import com.example.nutricionapp.nutritionist.CustomTabRow
import com.example.nutricionapp.nutritionist.DaySelector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DietaPatient(patientId: String,navController: NavHostController, NutId: String) {
    var paciente by remember { mutableStateOf<PacienteDb?>(null) }
    var dieta by remember { mutableStateOf<List<Dieta>?>(null) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var isEditing by remember { mutableStateOf(false) }
    var editedValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val daysOfWeek = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
    var selectedDayIndex by remember { mutableStateOf(0) } // Índice del día seleccionado
    var showDialog by remember { mutableStateOf(false) }
    var pesoInicial by remember { mutableStateOf(paciente?.pesoI?.toString() ?: "") }
    var pesoActual by remember { mutableStateOf(paciente?.peso?.toString() ?: "") }
    var pesoMeta by remember { mutableStateOf(paciente?.PesoMeta?.toString() ?: "") }
    var imcInicial by remember { mutableStateOf(paciente?.imcI?.toString() ?: "") }
    var imcActual by remember { mutableStateOf(paciente?.imc?.toString() ?: "") }
    var immInicial by remember { mutableStateOf(paciente?.immI?.toString() ?: "") }
    var immActual by remember { mutableStateOf(paciente?.imm?.toString() ?: "") }
    var showLoading by remember { mutableStateOf(false) }
    val appContext = LocalContext.current


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

    Scaffold(
        modifier = Modifier.fillMaxSize(),

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
                    // Caja con información del paciente
                } ?: run {
                    Text("No se encontraron datos del paciente", fontSize = 16.sp, color = Color.White)
                }

                // TabRow para alternar entre información del paciente y dieta
                CustomTabRow(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index -> selectedTabIndex = index }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTabIndex) {
                    0 -> {
                        DaySelector(
                            daysOfWeek = daysOfWeek,
                            selectedDayIndex = selectedDayIndex,
                            onDayChange = { index -> selectedDayIndex = index }
                        )
                        dieta?.let { dietList ->
                        // Lógica para mostrar la dieta según el día seleccionado
                        if (dietList.isNotEmpty()) {
                            val selectedDay = daysOfWeek[selectedDayIndex]
                            val dailyDiet = dietList.filter { it.dia == selectedDay }

                            if (dailyDiet.isNotEmpty()) {
                                dailyDiet.forEach { diet ->
                                    Column(modifier = Modifier.fillMaxWidth()
                                        .verticalScroll(rememberScrollState())) {

                                        // Variable para mostrar el cuadro de diálogo
                                        var showCommentDialog by remember { mutableStateOf(false) }
                                        var currentComment by remember { mutableStateOf("") }
                                        var selectedMealType by remember { mutableStateOf("") } // Tipo de comida seleccionado

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
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "Desayuno",
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    IconButton(
                                                        onClick = {
                                                            selectedMealType = "Desayuno"
                                                            currentComment = diet.desayuno.comentario ?: ""
                                                            //pasar a la pantalla de comentario con patientId y NutId

                                                            navController.navigate("comentario/$patientId/$NutId")

                                                            // showCommentDialog = true
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Message,
                                                            contentDescription = "Comentar",
                                                            tint = Color.Gray
                                                        )
                                                    }
                                                }
                                                Text(
                                                    text = diet.desayuno.comida,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = diet.desayuno.descr,
                                                    style = MaterialTheme.typography.bodySmall
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
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "Comida",
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    IconButton(
                                                        onClick = {
                                                            selectedMealType = "Comida"
                                                            currentComment = diet.comida.comentario ?: ""
                                                            navController.navigate("comentario/$patientId/$NutId")
                                                           // showCommentDialog = true
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Message,
                                                            contentDescription = "Comentar",
                                                            tint = Color.Gray
                                                        )
                                                    }
                                                }
                                                Text(
                                                    text = diet.comida.comida,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = diet.comida.descr,
                                                    style = MaterialTheme.typography.bodySmall
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
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "Cena",
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    IconButton(
                                                        onClick = {
                                                            selectedMealType = "Cena"
                                                            currentComment = diet.cena.comentario ?: ""
                                                            navController.navigate("comentario/$patientId/$NutId")
                                                           // showCommentDialog = true
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Message,
                                                            contentDescription = "Comentar",
                                                            tint = Color.Gray
                                                        )
                                                    }
                                                }
                                                Text(
                                                    text = diet.cena.comida,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = diet.cena.descr,
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }

                                        // Diálogo para agregar comentario
                                        if (showCommentDialog) {
                                            AlertDialog(
                                                onDismissRequest = { showCommentDialog = false },
                                                title = { Text("Agregar comentario a $selectedMealType") },
                                                text = {
                                                    TextField(
                                                        value = currentComment,
                                                        onValueChange = { currentComment = it },
                                                        label = { Text("Comentario") },
                                                        placeholder = { Text("Escribe tu comentario aquí") }
                                                    )
                                                },
                                                confirmButton = {
                                                    Button(
                                                        onClick = {
                                                            navController.navigate("comentario/$patientId")
                                                        }
                                                    ) {
                                                        Text("Guardar")
                                                    }//agregar boton de cancelar
                                                },
                                                dismissButton = {
                                                    Button(
                                                        onClick = { showCommentDialog = false }
                                                    ) {
                                                        Text("Cancelar")
                                                    }
                                                }



                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    }

                    //---------------------------------------------------------------------------------------------------
                    1 -> {
                        paciente?.let { paciente ->
                            Column {
                                // Mostrar la información del paciente en Cards
                                // Card para el Peso
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                                    shape = MaterialTheme.shapes.medium
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
                                        .padding(vertical = 8.dp),
                                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                                    shape = MaterialTheme.shapes.medium
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
                                        .padding(vertical = 8.dp),
                                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                                    shape = MaterialTheme.shapes.medium
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

                        //Spacer(modifier = Modifier.height(16.dp))

                        /*Button(
                            onClick = {
                                pesoInicial = paciente?.pesoI?.toString() ?: ""  // Si paciente es nulo o pesoI es nulo, asigna ""
                                pesoActual = paciente?.peso?.toString() ?: ""
                                pesoMeta = paciente?.PesoMeta?.toString() ?: ""
                                imcInicial = paciente?.imcI?.toString() ?: ""
                                imcActual = paciente?.imc?.toString() ?: ""
                                immInicial = paciente?.immI?.toString() ?: ""
                                immActual = paciente?.imm?.toString() ?: ""
                                showDialog = true },
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
                                            onValueChange = { handleValueChange(it, { pesoInicial = it }) },
                                            label = { Text("Peso Inicial") },
                                            isError = errorMessage != null
                                        )
                                        OutlinedTextField(
                                            value = pesoActual,
                                            onValueChange = { handleValueChange(it, { pesoActual = it }) },
                                            label = { Text("Peso Actual") },
                                            isError = errorMessage != null
                                        )
                                        OutlinedTextField(
                                            value = pesoMeta,
                                            onValueChange = { handleValueChange(it, { pesoMeta = it }) },
                                            label = { Text("Peso Meta") },
                                            isError = errorMessage != null
                                        )
                                        OutlinedTextField(
                                            value = imcInicial,
                                            onValueChange = { handleValueChange(it, { imcInicial = it }) },
                                            label = { Text("IMC Inicial") },
                                            isError = errorMessage != null
                                        )
                                        OutlinedTextField(
                                            value = imcActual,
                                            onValueChange = { handleValueChange(it, { imcActual = it }) },
                                            label = { Text("IMC Actual") },
                                            isError = errorMessage != null
                                        )
                                        OutlinedTextField(
                                            value = immInicial,
                                            onValueChange = { handleValueChange(it, { immInicial = it }) },
                                            label = { Text("IMM Inicial") },
                                            isError = errorMessage != null
                                        )
                                        OutlinedTextField(
                                            value = immActual,
                                            onValueChange = { handleValueChange(it, { immActual = it }) },
                                            label = { Text("IMM Actual") },
                                            isError = errorMessage != null
                                        )

                                        // Mostrar el mensaje de error debajo del campo si es necesario
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
                                            showDialog = false
                                        }
                                    ) {
                                        Text("Guardar")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = { showDialog = false }
                                    ) {
                                        Text("Cancelar")
                                    }
                                }
                            )
                        }*/
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
}
