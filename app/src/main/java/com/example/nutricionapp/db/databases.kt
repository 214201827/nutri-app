package com.example.nutricionapp.db

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.security.Timestamp
import java.util.*
import com.google.firebase.storage.FirebaseStorage
import android.content.Context
import android.net.Uri
import android.os.Environment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat


object FirestoreRepository {

    var userId: String = ""
    private val REQUEST_CODE_CREATE_DOCUMENT = 1
    fun getemail(email: String) {
        this.userId = email
        Log.d("UserId", "UserId: $userId")
    }

    // Crear una instancia de Firestore
    fun upd2(
        patientId: String,
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
        db.collection("pacientes").document(patientId)
            .update(updates)
            .addOnSuccessListener {
                Log.d("upd2", "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w("upd2", "Error updating document", e)
            }
    }

    fun upd(
        patientId: String,
        diaSeleccionado: String,
        desayunoData: Map<String, Any>,
        comidaData: Map<String, Any>,
        cenaData: Map<String, Any>
    ) {
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

        // Realizamos la actualización de todas las comidas de una vez
        val batch = db.batch()

        // Añadir desayuno al batch
        val desayunoRef = db.collection("diets")
            .document(patientId) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("desayuno") // Documento para desayuno
        batch.set(desayunoRef, desayuno)

        // Añadir comida al batch
        val comidaRef = db.collection("diets")
            .document(patientId) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("comida") // Documento para comida
        batch.set(comidaRef, comida)

        // Añadir cena al batch
        val cenaRef = db.collection("diets")
            .document(patientId) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("cena") // Documento para cena
        batch.set(cenaRef, cena)

        // Ejecutar el batch de actualización
        batch.commit()
            .addOnSuccessListener {
                Log.d("FireStore", "Comidas (desayuno, comida, cena) para $diaSeleccionado en paciente $patientId añadidas con éxito")
            }
            .addOnFailureListener { e ->
                Log.w("FireStore", "Error actualizando comidas para $diaSeleccionado en paciente $patientId", e)
            }
    }

    fun updComen(
        patientId: String,
        diaSeleccionado: String,
        desayunoData: Map<String, Any>,
        comidaData: Map<String, Any>,
        cenaData: Map<String, Any>,
        nutId: String // id del nutritionist que recibira la notificacion
    ) {
        val db = FirebaseFirestore.getInstance()
            //obtener  nombre del paciente


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
            "hora" to (desayunoData["hora"] ?: 8), // Hora de ejemplo
            "comentario" to (desayunoData["comentario"] ?: "")
        )

        val comida = hashMapOf<String, Any>(
            "comida" to (comidaData["comida"] ?: ""),
            "descr" to (comidaData["descr"] ?: ""),
            "hora" to (comidaData["hora"] ?: 8),
            "comentario" to (comidaData["comentario"] ?: "")
        )

        val cena = hashMapOf<String, Any>(
            "comida" to (cenaData["comida"] ?: ""),
            "descr" to (cenaData["descr"] ?: ""),
            "hora" to (cenaData["hora"] ?: 8),
            "comentario" to (cenaData["comentario"] ?: "")
        )

        // Realizamos la actualización de todas las comidas de una vez
        val batch = db.batch()

        // Añadir desayuno al batch
        val desayunoRef = db.collection("diets")
            .document(patientId) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("desayuno") // Documento para desayuno
        batch.set(desayunoRef, desayuno)

        // Añadir comida al batch
        val comidaRef = db.collection("diets")
            .document(patientId) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("comida") // Documento para comida
        batch.set(comidaRef, comida)

        // Añadir cena al batch
        val cenaRef = db.collection("diets")
            .document(patientId) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("cena") // Documento para cena
        batch.set(cenaRef, cena)

        // Ejecutar el batch de actualización
        batch.commit()
            .addOnSuccessListener {
                //obtener nombre del paciente
                val nombre = db.collection("pacientes").document(patientId).collection("fullName").toString()
                Log.d("FireStore", "Comidas (desayuno, comida, cena) para $diaSeleccionado en paciente $patientId añadidas con éxito")
                val notification = hashMapOf(
                    "recipient" to nutId,
                    "message" to "El paciente $nombre comentó en su dieta del día $diaSeleccionado.",
                    "timestamp" to System.currentTimeMillis(),
                    "read" to false
                )

                db.collection("notifications").add(notification)
                    .addOnSuccessListener {
                        Log.d("FireStore", "Notificación enviada al nutricionista con ID $nutId.")
                    }
                    .addOnFailureListener { e ->
                        Log.w("FireStore", "Error al enviar notificación.", e)
                    }
            }

            .addOnFailureListener { e ->
                Log.w("FireStore", "Error actualizando comidas para $diaSeleccionado en paciente $patientId", e)
            }
    }
    fun clearComen(
        patientId: String,
        diaSeleccionado: String,
        desayunoData: Map<String, Any>,
        comidaData: Map<String, Any>,
        cenaData: Map<String, Any>,
        nutId: String // id del nutritionist que recibira la notificacion
    ) {
        val db = FirebaseFirestore.getInstance()

        // Validar que el día seleccionado sea válido
        val diasSemana = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
        if (diaSeleccionado !in diasSemana) {
            Log.w("FireStore", "Día seleccionado no válido")
            return
        }

        // Crear el HashMap para cada comida, con valores por defecto si no se pasan
        val desayuno = hashMapOf<String, Any>(
            "comentario" to (desayunoData["comentario"] ?: "")
        )

        val comida = hashMapOf<String, Any>(
            "comentario" to (comidaData["comentario"] ?: "")
        )

        val cena = hashMapOf<String, Any>(
            "comentario" to (cenaData["comentario"] ?: "")
        )

        // Realizamos la actualización de las comidas solo para los comentarios
        val batch = db.batch()

        // Añadir desayuno al batch (solo actualizando el comentario)
        val desayunoRef = db.collection("diets")
            .document(patientId) // Documento con el patientId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("desayuno") // Documento para desayuno
        batch.update(desayunoRef, desayuno)

        // Añadir comida al batch (solo actualizando el comentario)
        val comidaRef = db.collection("diets")
            .document(patientId) // Documento con el patientId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("comida") // Documento para comida
        batch.update(comidaRef, comida)

        // Añadir cena al batch (solo actualizando el comentario)
        val cenaRef = db.collection("diets")
            .document(patientId) // Documento con el patientId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document("cena") // Documento para cena
        batch.update(cenaRef, cena)

        // Ejecutar el batch de actualización
        batch.commit()
            .addOnSuccessListener {
                Log.d("FireStore", "Comidas (desayuno, comida, cena) para $diaSeleccionado en paciente $patientId actualizadas con éxito")
                val notification = hashMapOf(
                    "recipient" to nutId,
                    "message" to "El paciente $patientId comentó en su dieta del día $diaSeleccionado.",
                    "timestamp" to System.currentTimeMillis(),
                    "read" to false
                )

                db.collection("notifications").add(notification)
                    .addOnSuccessListener {
                        Log.d("FireStore", "Notificación enviada al nutricionista con ID $nutId.")
                    }
                    .addOnFailureListener { e ->
                        Log.w("FireStore", "Error al enviar notificación.", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("FireStore", "Error actualizando comidas para $diaSeleccionado en paciente $patientId", e)
            }
    }

    /*
    fun updcomen(
        patientId: String,
        diaSeleccionado: String,
        comidaTipo: String, // "desayuno", "comida" o "cena"
        comentario: String
    ) {
        val db = FirebaseFirestore.getInstance()

        // Validar que el día seleccionado sea válido
        val diasSemana = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
        if (diaSeleccionado !in diasSemana) {
            Log.w("FireStore", "Día seleccionado no válido")
            return
        }

        // Validar que el tipo de comida sea válido
        val tiposComida = listOf("desayuno", "comida", "cena")
        if (comidaTipo !in tiposComida) {
            Log.w("FireStore", "Tipo de comida no válido")
            return
        }

        // Referencia al documento específico de la comida
        val comidaRef = db.collection("diets")
            .document(patientId) // Documento con el pacienteId
            .collection(diaSeleccionado) // Subcolección con el día seleccionado
            .document(comidaTipo) // Documento para el tipo de comida (desayuno/comida/cena)

        // Actualizar solo el campo "comentario"
        comidaRef.update("comentario", comentario)
            .addOnSuccessListener {
                Log.d("FireStore", "Comentario actualizado correctamente para $comidaTipo del día $diaSeleccionado en paciente $patientId")
            }
            .addOnFailureListener { e ->
                Log.w("FireStore", "Error actualizando comentario para $comidaTipo del día $diaSeleccionado en paciente $patientId", e)
            }
    }
*/



        // Esta función obtiene los pacientes asociados al nutriólogo por su correo
        fun getCityDataForNutritionist(emailNut: String, callback: (List<Paciented>) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection("pacientes")
                .whereEqualTo("Nid", emailNut) // Filtra por el correo del nutriólogo
                .get()
                .addOnSuccessListener { result ->
                    val patients = result.mapNotNull { document ->
                        val nombre = document.getString("fullName")
                        val id = document.getString("email")
                        val cita = document.getString("nextAppointment")

                        if (nombre != null && id != null) {
                            Paciented(nombre = nombre, email = id, nextAppointment = cita)
                        } else {
                            null
                        }
                        //document.toObject(Paciented::class.java)
                    }
                    callback(patients)
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error getting patients: ", exception)
                    callback(emptyList()) // Si hay un error, se devuelve una lista vacía
                }
        }



    //---------------------------------------------------------------------------------------------------------
    // Función para obtener la lista de recordatorios del nutriologo
    fun getRecords(onDataReceived: (List<Record>) -> Unit) {
        Log.d("UserId", "UserId: $userId")
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
                        Log.d(
                            "DEBUG_TAG",
                            "Document skipped due to null values. nombre: $nombre, pid: $pid"
                        )
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

            val db = FirebaseFirestore.getInstance()
            val citiesRef = db.collection("pacientes").document(patientId)

            // Realiza la consulta obteniendo el documento
            citiesRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    // Comprueba si se encontró el documento
                    if (documentSnapshot.exists()) {
                        // Extrae los datos del documento
                        val nombre = documentSnapshot.getString("fullName") ?: ""
                        val correo = documentSnapshot.getString("email") ?: ""
                        val fecha = documentSnapshot.getTimestamp("fechaNacimiento") // Obtén el timestamp
                        val imc = documentSnapshot.getDouble("imc")
                        val imcI = documentSnapshot.getDouble("imcI")
                        val imm = documentSnapshot.getDouble("imm")
                        val immI = documentSnapshot.getDouble("immI")
                        val Nid = documentSnapshot.getString("Nid") ?: ""
                        val peso = documentSnapshot.getDouble("peso")
                        val pesoI = documentSnapshot.getDouble("pesoI")
                        val pm = documentSnapshot.getDouble("pesoMeta")
                        val dir = documentSnapshot.getString("address") ?: ""
                        val cel = documentSnapshot.getLong("phone")?: 0
                        val diet = documentSnapshot.getString("diet") ?: ""
                        val nextAppointment = documentSnapshot.getString("nextAppointment") ?: ""
                        val profileImage = documentSnapshot.getString("profileImage") ?: ""

                        // Crea una instancia de PacienteDb
                        val pacienteDb = PacienteDb(
                            nombre = nombre,
                            correo = correo,
                            fecha = fecha,
                            imc = imc,
                            imcI = imcI,
                            imm = imm,
                            immI = immI,
                            Nid = Nid,
                            peso = peso,
                            pesoI = pesoI,
                            PesoMeta = pm,
                            dir = dir,
                            cel = cel,
                            diet = diet,
                            nextAppointment = nextAppointment,
                            profileImage = profileImage
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
    }
    //---------------------------------------------------------------------------------------------------

    //Obtener dieta
    fun getDietData(email: String, onDataReceived: (List<Dieta>?) -> Unit) {
        Log.d("getDietData", "patientId recibido: $email")


        val db = FirebaseFirestore.getInstance()
        val dietRef = db.collection("diets")

        dietRef.whereEqualTo("id", email)
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
                                    var desayuno = Comida("", "", 0,"")
                                    var comida = Comida("", "", 0, "")
                                    var cena = Comida("", "", 0, "")

                                    daySnapshot.documents.forEach { dayDocument ->
                                        when (dayDocument.id) {
                                            "desayuno" -> {
                                                desayuno = Comida(
                                                    dayDocument.getString("comida") ?: "",
                                                    dayDocument.getString("descr") ?: "",
                                                    dayDocument.getLong("hora")?.toInt() ?: 0 ,
                                                    dayDocument.getString("comentario") ?: ""
                                                )
                                            }

                                            "comida" -> {
                                                comida = Comida(
                                                    dayDocument.getString("comida") ?: "",
                                                    dayDocument.getString("descr") ?: "",
                                                    dayDocument.getLong("hora")?.toInt() ?: 0
                                                    ,dayDocument.getString("comentario") ?: ""
                                                )
                                            }

                                            "cena" -> {
                                                cena = Comida(
                                                    dayDocument.getString("comida") ?: "",
                                                    dayDocument.getString("descr") ?: "",
                                                    dayDocument.getLong("hora")?.toInt() ?: 0
                                                    ,dayDocument.getString("comentario") ?: ""
                                                )
                                            }
                                        }
                                    }

                                    comidasPorDia[day] = Triple(desayuno, comida, cena)

                                    if (comidasPorDia.size == daysOfWeek.size) {
                                        comidasPorDia.forEach { (dia, comidas) ->
                                            dietList.add(
                                                Dieta(
                                                    email = email,
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
    //crear nueva dieta para usuario nuevo




    //-------------------------------------------------------------------------------------------------------


    fun deleteNip(patientId: String, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        // Referencia al documento del paciente en Firestore
        val patientRef = db.collection("pacientes").document(patientId)

        // Actualizar el campo Nid a null
        patientRef.update("Nid", "")
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
    fun changeNid(patientId: String, email: String, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val patientRef = db.collection("pacientes").document(patientId)
        Log.d("UserId", "UserId: $patientId")
        Log.d("UserId", "UserId: $userId")

        patientRef.update("email", email)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error updating Nid", exception)
                onComplete(false)
            }
    }

    //get lista de pacientes con Nid null
    fun getPatients(onDataReceived: (List<Paciented>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("pacientes")
            .whereEqualTo("Nid", "") // Filtra los pacientes por Nid
            .get()
            .addOnSuccessListener { result ->
                val pacientes = result.mapNotNull { document ->
                    val nombre = document.getString("fullName") ?: return@mapNotNull null
                    val id = document.getString("email") ?: return@mapNotNull null// Pid es nullable

                    // Crea una instancia de PacienteDb
                    Paciented(nombre = nombre, email = id)
                }
                onDataReceived(pacientes)
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreRepository", "Error getting documents: ", exception)
                onDataReceived(emptyList())
            }
    }

    //Pacientes---------------------------------------------------------------------------------------

    fun getmyData(onDataReceived: (PacienteDb?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        Log.d("FirestoreRepository", "Guafsgsgdfgsdfgsdfgrdando usersgfsdgsdfgId: $userId")
        db.collection("pacientes")
            .whereEqualTo("email", userId) // Filtra las notificaciones por Nid
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Comprueba si se encontró al menos un documento
                if (!querySnapshot.isEmpty) {
                    val document =
                        querySnapshot.documents.first() // Obtiene el primer documento
                    val nombre = document.getString("fullName") ?: ""
                    val correo = document.getString("email") ?: ""
                    val fecha = document.getTimestamp("fechaNacimiento") // Obtén el timestamp
                    val imc = document.getDouble("imc")
                    val imcI = document.getDouble("imcI")
                    val imm = document.getDouble("imm")
                    val immI = document.getDouble("immI")
                    val Nid = document.getString("Nid") ?: ""
                    val peso = document.getDouble("peso")
                    val pesoI = document.getDouble("pesoI")
                    val pesometa = document.getDouble("pesoMeta")
                    val dir = document.getString("address") ?: ""
                    val cel = document.getLong("phone") ?: 0

                    // Crea una instancia de PacienteDb
                    val pacienteDb = PacienteDb(
                        nombre = nombre,
                        correo = correo,
                        fecha = fecha,
                        imc = imc,
                        imcI = imcI,
                        imm = imm,
                        immI = immI,
                        Nid = Nid,
                        peso = peso,
                        pesoI = pesoI,
                        PesoMeta = pesometa,
                        dir = dir,
                        cel = cel

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

    fun getMyDiet(onDataReceived: (List<Dieta2>?) -> Unit) {

        val db = FirebaseFirestore.getInstance()
        val dietRef = db.collection("diets")

        dietRef.whereEqualTo("email", userId)
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
                                    var desayuno = Comida("", "", 0, "")
                                    var comida = Comida("", "", 0,  "")
                                    var cena = Comida("", "", 0, "")

                                    daySnapshot.documents.forEach { dayDocument ->
                                        when (dayDocument.id) {
                                            "desayuno" -> {
                                                desayuno = Comida(
                                                    dayDocument.getString("comida") ?: "",
                                                    dayDocument.getString("descr") ?: "",
                                                    dayDocument.getLong("hora")?.toInt() ?: 0
                                                    ,dayDocument.getString("comentario") ?: ""

                                                )
                                            }

                                            "comida" -> {
                                                comida = Comida(
                                                    dayDocument.getString("comida") ?: "",
                                                    dayDocument.getString("descr") ?: "",
                                                    dayDocument.getLong("hora")?.toInt() ?: 0
                                                    ,dayDocument.getString("comentario") ?: ""
                                                )
                                            }

                                            "cena" -> {
                                                cena = Comida(
                                                    dayDocument.getString("comida") ?: "",
                                                    dayDocument.getString("descr") ?: "",
                                                    dayDocument.getLong("hora")?.toInt() ?: 0
                                                    ,dayDocument.getString("comentario") ?: ""
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

    fun addCommentToFirestore(subject: String, description: String, Rid: String, Did: String) {
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

    fun updateDietData(newDiet: Dieta, any: Any) {

    }

//Bases-------------------------------------------------------------------------------------------
fun uploadHist(
    context: Context,
    patientId: String,
    onUploadComplete: (Boolean) -> Unit
) {
    // Crear el archivo Excel
    val fileUri = createExcelFile(context, patientId)
    if (fileUri == null) {
        onUploadComplete(false)
        return
    }

    // Obtener la fecha actual para el nombre del archivo
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val date = dateFormat.format(Date())
    val storageRef = FirebaseStorage.getInstance().reference
        .child("pacientes/$patientId/historial/$date.xlsx")

    // Subir el archivo
    storageRef.putFile(fileUri)
        .addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                Log.d("Firebase", "Archivo subido: $downloadUrl")
                onUploadComplete(true)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Firebase", "Error al subir el archivo: ${exception.message}")
            onUploadComplete(false)
        }
}
    fun createExcelFile(context: Context, patientId: String): Uri? {
        try {
            // Crear el libro de Excel
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Historial")

            // Crear el nombre del archivo con fecha actual
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = dateFormat.format(Date())
            val fileName = "$date.xlsx"

            // Guardar el archivo en el almacenamiento local temporal
            val file = File(context.cacheDir, fileName)
            val fileOut = FileOutputStream(file)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()
            val firestore = FirebaseFirestore.getInstance()

            // Crear el mapa de datos que se enviará a Firestore
            val data = hashMapOf(
                "idhist" to fileName  // Aquí estamos asignando el fileName al campo idhist
            )

            // Actualizar el documento de paciente en Firestore
            firestore.collection("pacientes")
                .document(patientId)  // Usa el patientId del paciente
                .update(data as Map<String, Any>)  // Actualizamos solo el campo idhist
                .addOnSuccessListener {
                    // Si la operación es exitosa
                    Log.d("Firestore", "fileName guardado correctamente en idhist")
                }
                .addOnFailureListener { e ->
                    // Si hay un error
                    Log.e("Firestore", "Error al guardar el fileName en idhist", e)
                }

            // Retornar la URI del archivo creado
            return Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    fun createAndUploadExcelFile(
        context: Context,
        patientId: String,
        dietList: List<Dieta>,
        editedValues: Map<String, Map<String, Any>>,
        onUploadComplete: (Boolean) -> Unit
    ) {
        try {
            // Crear el libro de Excel
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Historial")

            // Crear el encabezado
            val headerRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("Comidas/Días") // Primera celda para los tipos de comida

            val daysOfWeek = listOf("lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo")
            daysOfWeek.forEachIndexed { index, day ->
                headerRow.createCell(index + 1).setCellValue(day) // Crear columnas para los días de la semana
            }

            // Agregar datos de desayuno, comida y cena
            val breakfastRow = sheet.createRow(1)
            breakfastRow.createCell(0).setCellValue("Desayuno")

            val lunchRow = sheet.createRow(2)
            lunchRow.createCell(0).setCellValue("Comida")

            val dinnerRow = sheet.createRow(3)
            dinnerRow.createCell(0).setCellValue("Cena")

            daysOfWeek.forEachIndexed { index, day ->
                val dayDiet = dietList.filter { it.dia == day }

                dayDiet.forEach { diet ->
                    // Recuperar valores editados o usar los valores predeterminados
                    val desayuno = editedValues[day]?.get("desayuno-${diet.email}") ?: diet.desayuno.comida
                    val comida = editedValues[day]?.get("comida-${diet.email}") ?: diet.comida.comida
                    val cena = editedValues[day]?.get("cena-${diet.email}") ?: diet.cena.comida

                    breakfastRow.createCell(index + 1).setCellValue(desayuno.toString())
                    lunchRow.createCell(index + 1).setCellValue(comida.toString())
                    dinnerRow.createCell(index + 1).setCellValue(cena.toString())
                }
            }

            // Crear el nombre del archivo con fecha actual
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = dateFormat.format(Date())
            val fileName = "$date.xlsx"

            // Guardar el archivo en el almacenamiento local temporal
            val file = File(context.cacheDir, fileName)
            val fileOut = FileOutputStream(file)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()

            // Subir el archivo a Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference
                .child("pacientes/$patientId/historial/$fileName")
            val fileUri = Uri.fromFile(file)

            storageRef.putFile(fileUri)
                .addOnSuccessListener {
                    // Subida completada
                    file.delete() // Eliminar el archivo local
                    onUploadComplete(true)
                }
                .addOnFailureListener { exception ->
                    // Manejar el error
                    exception.printStackTrace()
                    file.delete() // Eliminar el archivo local incluso si falla
                    onUploadComplete(false)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            onUploadComplete(false)
        }
    }


    fun getHistorialFiles(patientId: String, onFilesLoaded: (List<Hist>) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("pacientes/$patientId/historial")

        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                // Convertir los nombres en objetos Hist
                val histFiles = listResult.items.map { Hist(it.name) }
                onFilesLoaded(histFiles)
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error al obtener los archivos: ${exception.message}")
                onFilesLoaded(emptyList()) // Retorna una lista vacía en caso de error
            }
    }

    fun downloadFile(patientId: String, fileName: String, callback: (Boolean) -> Unit) {
        // Crear la referencia al archivo en Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference
            .child("pacientes/$patientId/historial/$fileName")

        // Crear un archivo en el directorio de descargas
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDirectory, fileName)

        // Descargar el archivo desde Firebase Storage
        storageRef.getFile(file)
            .addOnSuccessListener {
                // Si la descarga fue exitosa, ejecutamos el callback con 'true'
                callback(true)
            }
            .addOnFailureListener { exception ->
                // Si hubo un error, ejecutamos el callback con 'false'
                callback(false)
                exception.printStackTrace()
            }
    }

    fun obtenerHorasDisponibles(diaTimestamp: Timestamp) {
        // Referencia a la colección "citas"
        val db = FirebaseFirestore.getInstance()
        val citasRef = db.collection("citas")

        // Realizamos la consulta con un `where` que filtra por el día (Timestamp)
        citasRef
            .whereEqualTo("cita", diaTimestamp)  // Filtra por el valor del Timestamp (día específico)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val horasDisponibles = mutableListOf<Horas>()

                    querySnapshot.forEach { document ->
                        // Filtramos solo las horas que están disponibles (ocupado == false)
                        val horas = document.data?.filter { it.value is Map<*, *> }  // Filtra solo los campos que sean mapas (horas)
                        horas?.forEach { (hora, valor) ->
                            val ocupado = (valor as Map<*, *>)["ocupado"] as? Boolean
                            if (ocupado == false) {
                                // Si la hora está disponible, agregamos una instancia de Horas
                                horasDisponibles.add(Horas(hora.toString().toInt()))
                            }
                        }
                    }

                    // Mostrar las horas disponibles
                    if (horasDisponibles.isEmpty()) {
                        println("No hay horas disponibles para el día especificado.")
                    } else {
                        println("Horas disponibles para el día especificado: $horasDisponibles")
                    }
                } else {
                    println("No hay citas para el día especificado.")
                }
            }
            .addOnFailureListener { exception ->
                println("Error al obtener las horas: $exception")
            }
    }
    fun createCitaAndUpdateAppointment(
        pid: String,
        day: String,
        hour: Int,
        onComplete: (Boolean) -> Unit
    ) {
        val citaData = mapOf(
            "pid" to pid,  // Email o identificador del paciente
            "nid" to userId,  // Email o ID del nutriólogo
            "dia" to day,  // Día de la cita
            "hora" to hour // Hora de la cita
        )

        val db = FirebaseFirestore.getInstance()

        // Crear una nueva cita en la colección `citas`, utilizando el `pid` como ID del documento
        db.collection("citas")
            .document(pid) // Usar el `pid` como ID del documento
            .set(citaData)
            .addOnSuccessListener {
                // Buscar al paciente en la colección `pacientes` con `email` igual a `pid`
                db.collection("pacientes")
                    .whereEqualTo("email", pid)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            // Si se encuentra el paciente, actualizar los campos `nextAppointment` y `hour`
                            for (document in querySnapshot.documents) {
                                document.reference.update(
                                    mapOf(
                                        "nextAppointment" to day, // Actualizar con solo la fecha
                                        "hour" to hour // Guardar solo la hora
                                    )
                                ).addOnSuccessListener {
                                    onComplete(true)
                                }.addOnFailureListener {
                                    onComplete(false)
                                }
                            }
                        } else {
                            // Si no se encuentra el paciente, fallar la operación
                            onComplete(false)
                        }
                    }
                    .addOnFailureListener {
                        onComplete(false)
                    }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    // Función para obtener las horas ocupadas para una fecha y nid
    fun getOccupiedHours(date: String, onComplete: (List<Int>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        Log.d("FechaConsulta", "Consultando citas para la fecha: $date")
        Log.d("FechaConsulta", "Consultando citas para la fecha: $userId")
        // Realizar la consulta usando el formato de fecha como string
        db.collection("citas")
            .whereEqualTo("dia", date)  // Comparar la fecha como string
            .whereEqualTo("nid", userId)     // Comparar el nid
            .get()
            .addOnSuccessListener { querySnapshot ->
                val occupied = mutableListOf<Int>()
                // Recorrer los documentos para obtener las horas ocupadas
                for (document in querySnapshot.documents) {
                    val hour = document.getLong("hora")?.toInt()
                    if (hour != null) {
                        occupied.add(hour) // Agregar hora ocupada a la lista
                    }
                }
                onComplete(occupied) // Retornar las horas ocupadas
            }
            .addOnFailureListener {
                onComplete(emptyList()) // Si ocurre un error, retornar lista vacía
            }
    }

    fun addPatient(patientId: String, email: String, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val patientRef = db.collection("pacientes").document(patientId)
        Log.d("UserId", "UserId: $patientId")
        Log.d("UserId", "UserId: $userId")

        patientRef.update("Nid", email)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error updating Nid", exception)
                onComplete(false)
            }
    }

}
