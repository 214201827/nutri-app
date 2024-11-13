package com.example.nutricionapp.db

import android.graphics.pdf.PdfDocument
import android.util.Log
import com.example.nutricionapp.db.Dieta
import com.example.nutricionapp.db.Record
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import android.content.Context
import android.graphics.Paint
import android.widget.Toast
import com.example.nutricionapp.db.Comida
import com.google.firebase.auth.FirebaseAuth
import java.sql.Date

object FirestoreRepository {
    data class AuthResult(val isSuccessful: Boolean)
    var userId: Int = 0
    // Crear una instancia de Firestore
    fun upd2(
        patientId: Int,
        pesoInicial: Double?,
        pesoActual: Double?,
        pesoMeta: Double?,
        imcInicial: Double?,
        imcActual: Double?,
        immInicial: Double?,
        immActual: Double?
    ) {
        // Referencia a la base de datos de Firestore
        val db = FirebaseFirestore.getInstance()

        // Crear un mapa de datos a actualizar
        val updates = hashMapOf<String, Any?>(
            "pesoI" to pesoInicial,
            "peso" to pesoActual,
            "pesoMeta" to pesoMeta,
            "imcI" to imcInicial,
            "imc" to imcActual,
            "immI" to immInicial,
            "imm" to immActual
        )

        // Referenciar el documento con el ID del paciente (pacientId)
        db.collection("Pacient").document(patientId.toString())
            .update(updates)
            .addOnSuccessListener {
                Log.d("upd2", "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w("upd2", "Error updating document", e)
            }
    }

    fun upd(pacientId: Int, diaSeleccionado: String, desayunoData: Map<String, Any>, comidaData: Map<String, Any>, cenaData: Map<String, Any>) {
        val db = FirebaseFirestore.getInstance()

        // Validar que el día seleccionado sea válido
        val diasSemana = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
        if (diaSeleccionado !in diasSemana) {
            Log.w("FireStore", "Día seleccionado no válido")
            return
        }

        // Crear el HashMap para cada comida, con valores por defecto si no se pasan
        val desayuno = hashMapOf<String, Any>(
            "comida" to (desayunoData["comida"] ?: ""),
            "descr" to (desayunoData["descr"] ?: ""),
            "hora" to (desayunoData["hora"] ?: 8) // Hora de ejemplo
        )

        val comida = hashMapOf<String, Any>(
            "comida" to (comidaData["comida"] ?: ""),
            "descr" to (comidaData["descr"] ?: ""),
            "hora" to (comidaData["hora"] ?: 13) // Hora de ejemplo
        )

        val cena = hashMapOf<String, Any>(
            "comida" to (cenaData["comida"] ?: ""),
            "descr" to (cenaData["descr"] ?: ""),
            "hora" to (cenaData["hora"] ?: 20) // Hora de ejemplo
        )

        // Actualizar solo el día seleccionado
        db.collection("diets")
            .document(pacientId.toString()) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("desayuno") // Documento para desayuno
            .set(desayuno) // Se configura el desayuno
            .addOnSuccessListener {
                Log.d("FireStore", "Comida desayuno para $diaSeleccionado en paciente $pacientId añadida con éxito")
            }
            .addOnFailureListener { e ->
                Log.w("FireStore", "Error escribiendo desayuno para $diaSeleccionado en paciente $pacientId", e)
            }

        db.collection("diets")
            .document(pacientId.toString()) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("comida") // Documento para comida
            .set(comida) // Se configura la comida
            .addOnSuccessListener {
                Log.d("FireStore", "Comida comida para $diaSeleccionado en paciente $pacientId añadida con éxito")
            }
            .addOnFailureListener { e ->
                Log.w("FireStore", "Error escribiendo comida para $diaSeleccionado en paciente $pacientId", e)
            }

        db.collection("diets")
            .document(pacientId.toString()) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("cena") // Documento para cena
            .set(cena) // Se configura la cena
            .addOnSuccessListener {
                Log.d("FireStore", "Comida cena para $diaSeleccionado en paciente $pacientId añadida con éxito")
            }
            .addOnFailureListener { e ->
                Log.w("FireStore", "Error escribiendo cena para $diaSeleccionado en paciente $pacientId", e)
            }
    }


    // Función para obtener la lista de pacientes con Nid igual a 12345
    fun getCityData(onDataReceived: (List<Paciented>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Pacient")
            .whereEqualTo("Nid", userId) // Filtra los pacientes por Nid
            .get()
            .addOnSuccessListener { result ->
                val pacientes = result.mapNotNull { document ->
                    val nombre = document.getString("nombre") ?: return@mapNotNull null
                    val id = document.getLong("id")?.toInt() // Pid es nullable

                    // Crea una instancia de PacienteDb
                    Paciented(nombre = nombre, id = id)
                }
                onDataReceived(pacientes)
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreRepository", "Error getting documents: ", exception)
                onDataReceived(emptyList())
            }
    }
    //---------------------------------------------------------------------------------------------------------
    // Función para obtener la lista de recordatorios del nutriologo
    fun getRecords(onDataReceived: (List<Record>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("notificaciones")
            .whereEqualTo("DestId", userId) // Filtra los pacientes por Nid
            .get()
            .addOnSuccessListener { result ->
                val records = result.documents.mapNotNull { document ->
                    val nombre = document.getString("titulo")
                    val pid = document.getString("descr")

                    // Si alguno es null, salta este documento
                    if (nombre == null || pid == null) {
                        Log.d("DEBUG_TAG", "Document skipped due to null values. nombre: $nombre, pid: $pid")
                        return@mapNotNull null
                    }

                    Log.d("DEBUG_TAG", "Valor de pid recibido: $pid")

                    // Crea una instancia de Record
                    Record(titulo = nombre, descr = pid) // Asegúrate de que esta línea sea correcta
                }

                // Envía la lista de registros aquí
                onDataReceived(records)
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreRepository", "Error getting documents: ", exception)
                onDataReceived(emptyList()) // En caso de error, se pasa una lista vacía
            }
    }
    //---------------------------------------------------------------------------------------------------

    // Función para obtener los datos de un paciente específico
    fun getPatientData(patientId: String, onDataReceived: (PacienteDb?) -> Unit) {
        Log.d("getPatientData", "patientId recibido: $patientId")  // Imprime el patientId

        // Intenta convertir el patientId a Int
        val patientIdInt = patientId.toIntOrNull()

        if (patientIdInt != null) {
            val db = FirebaseFirestore.getInstance()
            val citiesRef = db.collection("Pacient").document(patientIdInt.toString())

            // Realiza la consulta obteniendo el documento
            citiesRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    // Comprueba si se encontró el documento
                    if (documentSnapshot.exists()) {
                        // Extrae los datos del documento
                        val nombre = documentSnapshot.getString("nombre") ?: ""
                        val pid = documentSnapshot.getLong("id")?.toInt()
                        val correo = documentSnapshot.getString("email") ?: ""
                        val dietaId = documentSnapshot.getLong("dietaId")?.toInt() ?: 0
                        val fecha = documentSnapshot.getTimestamp("fecha") // Obtén el timestamp
                        val foto = documentSnapshot.getString("foto")
                        val imc = documentSnapshot.getDouble("imc")
                        val imcI = documentSnapshot.getDouble("imcI")
                        val imm = documentSnapshot.getDouble("imm")
                        val immI = documentSnapshot.getDouble("immI")
                        val Nid = documentSnapshot.getLong("Nid")?.toInt() ?: 0
                        val peso = documentSnapshot.getDouble("peso")
                        val pesoI = documentSnapshot.getDouble("pesoI")
                        val pm = documentSnapshot.getDouble("PesoMeta")

                        // Crea una instancia de PacienteDb
                        val pacienteDb = PacienteDb(
                            nombre = nombre,
                            id = pid,
                            correo = correo,
                            dietaId = dietaId,
                            fecha = fecha,
                            foto = foto,
                            imc = imc,
                            imcI = imcI,
                            imm = imm,
                            immI = immI,
                            Nid = Nid,
                            peso = peso,
                            pesoI = pesoI,
                            PesoMeta = pm
                        )
                        // Llama a la función de callback con los datos recibidos
                        onDataReceived(pacienteDb)
                    } else {
                        // Si no se encontró el documento, pasa null
                        onDataReceived(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("PatientDetailScreen", "Error getting patient data: ", exception)
                    onDataReceived(null)
                }
        } else {
            Log.d("getPatientData", "Error: patientId no es un número válido.")
            onDataReceived(null) // Pasa null si la conversión falla
        }
    }
    //---------------------------------------------------------------------------------------------------

    //Obtener dieta
    fun getDietData(patientId: String, onDataReceived: (List<Dieta>?) -> Unit) {
        Log.d("getDietData", "patientId recibido: $patientId")

        val patientIdInt = patientId.toIntOrNull()

        if (patientIdInt != null) {
            val db = FirebaseFirestore.getInstance()
            val dietRef = db.collection("diets")

            dietRef.whereEqualTo("id", patientIdInt)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val dietList = mutableListOf<Dieta>()
                        val documentCount = querySnapshot.size()

                        querySnapshot.documents.forEachIndexed { index, document ->
                            val dietId = document.id
                            val daysRef = dietRef.document(dietId)

                            val daysOfWeek = listOf(
                                "lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"
                            )

                            val comidasPorDia = mutableMapOf<String, Triple<Comida, Comida, Comida>>()

                            daysOfWeek.forEach { day ->
                                daysRef.collection(day)
                                    .get()
                                    .addOnSuccessListener { daySnapshot ->
                                        var desayuno = Comida("", "", 0)
                                        var comida = Comida("", "", 0)
                                        var cena = Comida("", "", 0)

                                        daySnapshot.documents.forEach { dayDocument ->
                                            when (dayDocument.id) {
                                                "desayuno" -> {
                                                    desayuno = Comida(
                                                        dayDocument.getString("comida") ?: "",
                                                        dayDocument.getString("descr") ?: "",
                                                        dayDocument.getLong("hora")?.toInt() ?: 0
                                                    )
                                                }

                                                "comida" -> {
                                                    comida = Comida(
                                                        dayDocument.getString("comida") ?: "",
                                                        dayDocument.getString("descr") ?: "",
                                                        dayDocument.getLong("hora")?.toInt() ?: 0
                                                    )
                                                }

                                                "cena" -> {
                                                    cena = Comida(
                                                        dayDocument.getString("comida") ?: "",
                                                        dayDocument.getString("descr") ?: "",
                                                        dayDocument.getLong("hora")?.toInt() ?: 0
                                                    )
                                                }
                                            }
                                        }

                                        comidasPorDia[day] = Triple(desayuno, comida, cena)

                                        if (comidasPorDia.size == daysOfWeek.size) {
                                            comidasPorDia.forEach { (dia, comidas) ->
                                                dietList.add(
                                                    Dieta(
                                                        id = patientIdInt,
                                                        desayuno = comidas.first,
                                                        comida = comidas.second,
                                                        cena = comidas.third,
                                                        dia = dia
                                                    )
                                                )
                                            }

                                            if (index == documentCount - 1) {
                                                onDataReceived(dietList)
                                            }
                                        }
                                    }
                            }
                        }
                    } else {
                        onDataReceived(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DietaDetailScreen", "Error getting diet data: ", exception)
                    onDataReceived(null)
                }
        } else {
            Log.d("getDietData", "Error: patientId no es un número válido.")
            onDataReceived(null)
        }
    }

    //-------------------------------------------------------------------------------------------------------



    // Nueva función para eliminar el paciente por Nid
    fun deleteNidFromPatient(pid: String, onComplete: (Boolean) -> Unit) {
        val patientRef = FirebaseFirestore.getInstance().collection("Pacient").document(pid)

        // Usa update para eliminar solo el campo 'Nid'
        patientRef.update("Nid", FieldValue.delete())
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }


    fun deleteNip(patientId: String, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        // Referencia al documento del paciente en Firestore
        val patientRef = db.collection("Pacient").document(patientId)

        // Actualizar el campo Nid a null
        patientRef.update("Nid", null)
            .addOnSuccessListener {
                // El cambio se realizó exitosamente
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                // Ocurrió un error al intentar actualizar
                Log.w("Firestore", "Error updating document", exception)
                onComplete(false)
            }
    }

    // En FirestoreRepository, agrega la función para cambiar el Nid
    fun changeNid(patientId: String, newNid: Int, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val patientRef = db.collection("Pacient").document(patientId)

        patientRef.update("Nid", newNid)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error updating Nid", exception)
                onComplete(false)
            }
    }

    fun deleteDietFromFirestore(dietId: String) {

    }

    //Pacientes---------------------------------------------------------------------------------------

    fun getmyData(onDataReceived: (PacienteDb?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        Log.d("FirestoreRepository", "Guardando userId: $userId")
        db.collection("Pacient")
            .whereEqualTo("id", userId) // Filtra las notificaciones por Nid
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Comprueba si se encontró al menos un documento
                if (!querySnapshot.isEmpty) {
                    val document =
                        querySnapshot.documents.first() // Obtiene el primer documento
                    val nombre = document.getString("nombre") ?: ""
                    val pid = document.getLong("id")?.toInt()
                    val correo = document.getString("email") ?: ""
                    val dietaId = document.getLong("dietaId")?.toInt() ?: 0
                    val fecha = document.getTimestamp("fecha") // Obtén el timestamp
                    val foto = document.getString("foto")
                    val imc = document.getDouble("imc")
                    val imcI = document.getDouble("imcI")
                    val imm = document.getDouble("imm")
                    val immI = document.getDouble("immI")
                    val Nid = document.getLong("Nid")?.toInt() ?: 0
                    val peso = document.getDouble("peso")
                    val pesoI = document.getDouble("pesoI")

                    // Crea una instancia de PacienteDb
                    val pacienteDb = PacienteDb(
                        nombre = nombre,
                        id = pid,
                        correo = correo,
                        dietaId = dietaId,
                        fecha = fecha,
                        foto = foto,
                        imc = imc,
                        imcI = imcI,
                        imm = imm,
                        immI = immI,
                        Nid = Nid,
                        peso = peso,
                        pesoI = pesoI
                    )
                    onDataReceived(pacienteDb)
                } else {
                    // Si no se encontró ningún documento, pasa null
                    onDataReceived(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("PatientDetailScreen", "Error getting patient data: ", exception)
                onDataReceived(null)
            }
    }

    fun getPatientHistory(
        patientId: String,
        year: String,
        onMonthsReceived: (List<String>) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val historyRef = db.collection("dhist").document(patientId).collection(year)

        historyRef.get().addOnSuccessListener { querySnapshot ->
            val monthsList = querySnapshot.documents.map { it.id } // Lista de meses
            onMonthsReceived(monthsList)
        }.addOnFailureListener { exception ->
            Log.d("PatientHistory", "Error getting patient history: ", exception)
            onMonthsReceived(emptyList())
        }
    }

    fun getMyDiet(onDataReceived: (List<Dieta2>?) -> Unit) {

            val db = FirebaseFirestore.getInstance()
            val dietRef = db.collection("diets")

            dietRef.whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val dietList = mutableListOf<Dieta2>()
                        val documentCount = querySnapshot.size()

                        querySnapshot.documents.forEachIndexed { index, document ->
                            val dietId = document.id
                            val daysRef = dietRef.document(dietId)

                            val daysOfWeek = listOf(
                                "lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"
                            )

                            val comidasPorDia = mutableMapOf<String, Triple<Comida, Comida, Comida>>()

                            daysOfWeek.forEach { day ->
                                daysRef.collection(day)
                                    .get()
                                    .addOnSuccessListener { daySnapshot ->
                                        var desayuno = Comida("", "", 0)
                                        var comida = Comida("", "", 0)
                                        var cena = Comida("", "", 0)

                                        daySnapshot.documents.forEach { dayDocument ->
                                            when (dayDocument.id) {
                                                "desayuno" -> {
                                                    desayuno = Comida(
                                                        dayDocument.getString("comida") ?: "",
                                                        dayDocument.getString("descr") ?: "",
                                                        dayDocument.getLong("hora")?.toInt() ?: 0
                                                    )
                                                }

                                                "comida" -> {
                                                    comida = Comida(
                                                        dayDocument.getString("comida") ?: "",
                                                        dayDocument.getString("descr") ?: "",
                                                        dayDocument.getLong("hora")?.toInt() ?: 0
                                                    )
                                                }

                                                "cena" -> {
                                                    cena = Comida(
                                                        dayDocument.getString("comida") ?: "",
                                                        dayDocument.getString("descr") ?: "",
                                                        dayDocument.getLong("hora")?.toInt() ?: 0
                                                    )
                                                }
                                            }
                                        }

                                        comidasPorDia[day] = Triple(desayuno, comida, cena)

                                        if (comidasPorDia.size == daysOfWeek.size) {
                                            comidasPorDia.forEach { (dia, comidas) ->
                                                dietList.add(
                                                    Dieta2(
                                                        desayuno = comidas.first,
                                                        comida = comidas.second,
                                                        cena = comidas.third,
                                                        dia = dia
                                                    )
                                                )
                                            }

                                            if (index == documentCount - 1) {
                                                onDataReceived(dietList)
                                            }
                                        }
                                    }
                            }
                        }
                    } else {
                        onDataReceived(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DietaDetailScreen", "Error getting diet data: ", exception)
                    onDataReceived(null)
                }

    }

    fun addCommentToFirestore(subject: String, description: String, Rid: Int, Did: Int) {
        val db = FirebaseFirestore.getInstance()
        val comment = hashMapOf(
            "titulo" to subject,
            "descr" to description,
            "DestId" to Did,
            "RemId" to Rid
        )

        db.collection("notificaciones")
            .add(comment)
            .addOnSuccessListener { documentReference ->
                val generatedId = documentReference.id
                documentReference.update("id", generatedId)
                Log.d("Comment", "Comentario agregado exitosamente")
            }
            .addOnFailureListener { e ->
                Log.w("Comment", "Error al agregar el comentario", e)
            }
    }

    //Bases-------------------------------------------------------------------------------------------
    fun downloadPdf(
        context: Context, // Añadido para poder usar getExternalFilesDir
        patientId: String,
        year: String,
        month: String,
        onPdfGenerated: (File) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val monthRef = db.collection("dhist").document(patientId).collection(year).document(month)

        monthRef.collection("Lunes").get().addOnSuccessListener { daySnapshot ->
            // Aquí obtendrás los datos de lunes, martes, etc.
            val desayuno = daySnapshot.documents.find { it.id == "desayuno" }?.getString("comida")
            val comida = daySnapshot.documents.find { it.id == "comida" }?.getString("comida")
            val cena = daySnapshot.documents.find { it.id == "cena" }?.getString("comida")

            // Lógica para generar el PDF
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Tamaño A4
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()

            // Dibujar datos en el PDF
            paint.textSize = 16f
            canvas.drawText("Historial del mes: $month", 80f, 100f, paint)
            canvas.drawText("Desayuno: $desayuno", 80f, 150f, paint)
            canvas.drawText("Comida: $comida", 80f, 200f, paint)
            canvas.drawText("Cena: $cena", 80f, 250f, paint)

            pdfDocument.finishPage(page)

            // Guardar PDF en un archivo
            val pdfFile = File(context.getExternalFilesDir(null), "$month-history.pdf")
            pdfDocument.writeTo(FileOutputStream(pdfFile))
            pdfDocument.close()

            // Devolver el archivo generado
            onPdfGenerated(pdfFile)
        }
    }

    fun authenticateUser(
        email: String,
        password: String,
        context: Context,
        onResult: (AuthResult) -> Unit   // Callback para el resultado
    ) {
        val db = FirebaseFirestore.getInstance()

        // Buscar en la colección "nutriologos"
        db.collection("nutriologos")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { docs ->
                if (!docs.isEmpty) {
                    // Obtener el campo "id" como Int dentro del documento
                    val userId = docs.documents[0].getLong("id")?.toInt() ?: 0  // Obtener "id" como Long y luego convertirlo a Int

                    // Guardamos el "userId" en la variable global
                    this.userId = userId  // Almacenamos el userId en la variable global

                    // Imprimir el userId para verificar
                    Log.d("FirestoreRepository", "userId desde nutriologos: $userId")

                    onResult(AuthResult(isSuccessful = true))  // Retorna resultado exitoso
                } else {
                    // Si no encuentra en "nutriologos", intenta buscar en "Pacient"
                    authenticateUser2(email, password, context, onResult)
                }
            }
            .addOnFailureListener {
                onResult(AuthResult(isSuccessful = false))  // Retorna resultado fallido
            }
    }

    // Segundo intento: buscar en la colección "Pacient" si no se encontró en "nutriologos"
    fun authenticateUser2(
        email: String,
        password: String,
        context: Context,
        onResult: (AuthResult) -> Unit   // Callback para el resultado
    ) {
        val db = FirebaseFirestore.getInstance()

        // Buscar en la colección "Pacient"
        db.collection("Pacient")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { docs ->
                if (!docs.isEmpty) {
                    val userId = docs.documents[0].getLong("id")?.toInt() ?: 0 // Convertir el ID a Int, si es posible
                    this.userId = userId  // Guardamos el userId en la variable global
                    Log.d("FirestoreRepository", "userId desde nutriologos: $userId")
                    onResult(AuthResult(isSuccessful = true))  // Retorna resultado exitoso
                } else {
                    onResult(AuthResult(isSuccessful = false))  // Retorna resultado fallido si no se encuentra
                }
            }
            .addOnFailureListener {
                onResult(AuthResult(isSuccessful = false))  // Retorna resultado fallido
            }
    }
}