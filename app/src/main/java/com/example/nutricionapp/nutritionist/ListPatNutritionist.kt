import android.util.Log
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
import androidx.navigation.NavHostController
import com.example.nutricionapp.db.FirestoreRepository
import com.example.nutricionapp.db.FirestoreRepository.getemail
import com.example.nutricionapp.db.FirestoreRepository.userId
import com.example.nutricionapp.db.Paciented
import com.google.firebase.auth.FirebaseAuth



@Composable
fun ListPatNutritionist(navController: NavHostController) {
    var patientDataList by remember { mutableStateOf(listOf<Paciented>()) }
    var showDialog by remember { mutableStateOf(false) }
    var pidInput by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var patientToDelete by remember { mutableStateOf<Paciented?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val emailNut = currentUser?.email.toString()

    // Cargar los pacientes asociados al nutriólogo
    LaunchedEffect(emailNut) {
        // Verificar que el emailNut no sea vacío
        if (emailNut.isNotBlank()) {
            FirestoreRepository.getCityDataForNutritionist(emailNut) { data ->
                patientDataList = data
                isLoading = false
            }
        }
    }

    if (isLoading) {
        CircularProgressIndicator()  // Indicador de carga mientras obtenemos los datos
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
                        FirestoreRepository.addPatient(pidInput, emailNut) { success ->
                            if (success) {
                                FirestoreRepository.getCityDataForNutritionist(emailNut) { data ->
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
                    FirestoreRepository.deleteNip(patientToDelete!!.email) { success ->
                        if (success) {
                            FirestoreRepository.getCityDataForNutritionist(emailNut) { data ->
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

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Nuevo Paciente",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Nuevo Paciente", color = Color.White, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de pacientes
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(patientDataList.size) { index ->
                PatientItem(
                    patient = patientDataList[index],
                    onClick = {
                        navController.navigate("dietaNutritionist/${patientDataList[index].email}")
                    },
                    onDelete = {
                        patientToDelete = patientDataList[index]
                        showDeleteDialog = true
                    }
                )
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
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), // Asegura que la fila ocupe todo el ancho de la tarjeta
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Distribuye los elementos
        ) {
            Column {
                Text(
                    text = patient.nombre,
                    fontSize = 18.sp,
                    color = Color.White,
                )
                Text(
                    text = "Email: ${patient.email}",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }

            // Esto empuja el IconButton hacia la derecha
            IconButton(
                onClick = { onDelete() },
                modifier = Modifier.align(Alignment.CenterVertically) // Alineación vertical
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Paciente",
                    tint = Color.Red
                )
            }
        }
    }
}