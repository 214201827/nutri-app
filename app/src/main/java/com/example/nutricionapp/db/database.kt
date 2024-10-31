package com.example.myapplication000

import android.graphics.pdf.PdfDocument
import android.util.Log
import com.example.myapplication000.db.Dieta
import com.example.myapplication000.db.notify
import com.example.myapplication000.db.Record
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import android.content.Context
import android.graphics.Paint

object FirestoreRepository {

    // Función para obtener la lista de pacientes con Nid igual a 12345
    fun getCityData(onDataReceived: (List<Paciented>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Pacient")
            .whereEqualTo("Nid", 1000) // Filtra los pacientes por Nid
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
        db.collection("Recor")
            .whereEqualTo("Nid", 1000) // Filtra los pacientes por Nid
            .get()
            .addOnSuccessListener { result ->
                val records = result.mapNotNull { document ->
                    val nombre = document.getString("titulo") ?: return@mapNotNull null
                    val pid = document.getString("desc") ?: return@mapNotNull null

                    // Crea una instancia de Record
                    Record(titulo = nombre, descripcion = pid) // Asegúrate de que esta línea sea correcta
                }
                onDataReceived(records) // Envía la lista de registros aquí
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreRepository", "Error getting documents: ", exception)
                onDataReceived(emptyList())
            }
    }
    //---------------------------------------------------------------------------------------------------
    // Función para obtener la lista de recordatorios del paciente
    fun getRecordspac(onDataReceived: (List<Record>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Recor")
            .whereEqualTo("Pid", 101) // Filtra los pacientes por Pid
            .get()
            .addOnSuccessListener { result ->
                val records = result.mapNotNull { document ->
                    val nombre = document.getString("titulo") ?: return@mapNotNull null
                    val pid = document.getString("desc") ?: return@mapNotNull null

                    // Crea una instancia de Record
                    Record(titulo = nombre, descripcion = pid) // Asegúrate de que esta línea sea correcta
                }
                onDataReceived(records) // Envía la lista de registros aquí
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreRepository", "Error getting documents: ", exception)
                onDataReceived(emptyList())
            }
    }

    // Función para obtener la lista de notificaciones del nutriologo
    fun getnotf(onDataReceived: (List<notify>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("notif")
            .whereEqualTo("Nid", 1000) // Filtra las notificaciones por Nid
            .get()
            .addOnSuccessListener { result ->
                val notifyList = result.mapNotNull { document ->
                    val titulo = document.getString("titulo") ?: return@mapNotNull null
                    val descripcion = document.getString("descr") ?: return@mapNotNull null

                    // Crea una instancia de notify
                    notify(titulo = titulo, descripcion = descripcion)
                }
                onDataReceived(notifyList)
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreRepository", "Error getting documents: ", exception)
                onDataReceived(emptyList())
            }
    }
    //----------------------------------------------------------------------------------------------------
    // Función para obtener la lista de notificaciones del paciente
    fun getnotfpac(onDataReceived: (List<notify>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("notif")
            .whereEqualTo("Pid", 12345) // Filtra las notificaciones por Nid
            .get()
            .addOnSuccessListener { result ->
                val notifyList = result.mapNotNull { document ->
                    val titulo = document.getString("titulo") ?: return@mapNotNull null
                    val descripcion = document.getString("descr") ?: return@mapNotNull null

                    // Crea una instancia de notify
                    notify(titulo = titulo, descripcion = descripcion)
                }
                onDataReceived(notifyList)
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreRepository", "Error getting documents: ", exception)
                onDataReceived(emptyList())
            }
    }

    // Función para obtener los datos de un paciente específico
    fun getPatientData(patientId: String, onDataReceived: (PacienteDb?) -> Unit) {
        Log.d("getPatientData", "patientId recibido: $patientId")  // Imprime el patientId

        // Intenta convertir el patientId a Int
        val patientIdInt = patientId.toIntOrNull()

        if (patientIdInt != null) {
            val db = FirebaseFirestore.getInstance()
            val citiesRef = db.collection("Pacient")

            // Realiza la consulta buscando el Pid
            citiesRef.whereEqualTo("id", patientIdInt)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    // Comprueba si se encontró al menos un documento
                    if (!querySnapshot.isEmpty) {
                        val document =
                            querySnapshot.documents.first() // Obtiene el primer documento
                        val nombre = document.getString("nombre") ?: ""
                        val pid = document.getLong("id")?.toInt()
                        val correo = document.getString("correo") ?: ""
                        val dietaId = document.getLong("dietaId")?.toInt() ?: 0
                        val fecha = document.getTimestamp("fecha") // Obtén el timestamp
                        val foto = document.getString("foto")
                        val imc = document.getDouble("imc")
                        val imcI = document.getDouble("imcI")
                        val imm = document.getDouble("imm")
                        val immI = document.getDouble("immI")
                        val Nid = document.getLong("nutriologoid")?.toInt() ?: 0
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
        } else {
            Log.d("getPatientData", "Error: patientId no es un número válido.")
            onDataReceived(null) // Pasa null si la conversión falla
        }
    }
    //---------------------------------------------------------------------------------------------------
    // Función para obtener los datos de un paciente específico
    fun getmyData(onDataReceived: (PacienteDb?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Pacient")
            .whereEqualTo("id", 101) // Filtra las notificaciones por Nid
            .get()
                .addOnSuccessListener { querySnapshot ->
                    // Comprueba si se encontró al menos un documento
                    if (!querySnapshot.isEmpty) {
                        val document =
                            querySnapshot.documents.first() // Obtiene el primer documento
                        val nombre = document.getString("nombre") ?: ""
                        val pid = document.getLong("id")?.toInt()
                        val correo = document.getString("correo") ?: ""
                        val dietaId = document.getLong("dietaId")?.toInt() ?: 0
                        val fecha = document.getTimestamp("fecha") // Obtén el timestamp
                        val foto = document.getString("foto")
                        val imc = document.getDouble("imc")
                        val imcI = document.getDouble("imcI")
                        val imm = document.getDouble("imm")
                        val immI = document.getDouble("immI")
                        val Nid = document.getLong("nutriologoid")?.toInt() ?: 0
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
    //---------------------------------------------------------------------------------------------------


    fun getDietData(patientId: String, onDataReceived: (List<Dieta>?) -> Unit) {
        Log.d("getDietData", "patientId recibido: $patientId") // Imprime el patientId

        // Intenta convertir el patientId a Int
        val patientIdInt = patientId.toIntOrNull()

        if (patientIdInt != null) {
            val db = FirebaseFirestore.getInstance()
            val dietRef = db.collection("diets")

            // Realiza la consulta buscando el Did
            dietRef.whereEqualTo("id", patientIdInt)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    // Comprueba si se encontró al menos un documento
                    if (!querySnapshot.isEmpty) {
                        val dietList = mutableListOf<Dieta>()
                        val documentCount = querySnapshot.size() // Para contar los documentos

                        // Iterar a través de los documentos de dieta
                        querySnapshot.documents.forEachIndexed { index, document ->
                            val dietId = document.id // Obtiene el ID del documento (subcolección)
                            val daysRef =
                                dietRef.document(dietId) // Referencia a la subcolección del dietId

                            // Días de la semana a consultar
                            val daysOfWeek = listOf(
                                "Lunes",
                                "Martes",
                                "Miércoles",
                                "Jueves",
                                "Viernes",
                                "Sábado",
                                "Domingo"
                            )

                            // Inicializa las comidas para el día
                            val comidasPorDia =
                                mutableMapOf<String, Triple<String, String, String>>() // día -> (desayuno, comida, cena)

                            // Iterar a través de los días de la semana
                            daysOfWeek.forEach { day ->
                                daysRef.collection(day) // Accede a la subcolección de cada día
                                    .get()
                                    .addOnSuccessListener { daySnapshot ->
                                        // Inicializa las comidas
                                        var desayuno = ""
                                        var comida = ""
                                        var cena = ""

                                        // Obtiene los documentos de desayuno, comida y cena
                                        daySnapshot.documents.forEach { dayDocument ->
                                            when (dayDocument.id) {
                                                "desayuno" -> desayuno =
                                                    dayDocument.getString("comida") ?: ""

                                                "comida" -> comida =
                                                    dayDocument.getString("comida") ?: ""

                                                "cena" -> cena =
                                                    dayDocument.getString("comida") ?: ""
                                            }
                                        }

                                        // Guarda las comidas en el mapa por día
                                        comidasPorDia[day] = Triple(desayuno, comida, cena)

                                        // Si es el último día procesado, crea la instancia de Dieta
                                        if (comidasPorDia.size == daysOfWeek.size) {
                                            // Crear instancias de Dieta para cada día
                                            comidasPorDia.forEach { (dia, comidas) ->
                                                dietList.add(
                                                    Dieta(
                                                        Did = patientIdInt,
                                                        desayuno = comidas.first,
                                                        comida = comidas.second,
                                                        cena = comidas.third,
                                                        dia = dia
                                                    )
                                                )
                                            }

                                            // Si es el último documento, devuelve la lista
                                            if (index == documentCount - 1) {
                                                onDataReceived(dietList)
                                            }
                                        }
                                    }
                            }
                        }
                    } else {
                        // Si no se encontró ningún documento, pasa null
                        onDataReceived(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DietaDetailScreen", "Error getting diet data: ", exception)
                    onDataReceived(null)
                }
        } else {
            Log.d("getDietData", "Error: patientId no es un número válido.")
            onDataReceived(null) // Pasa null si la conversión falla
        }
    }
    //---------------------------------------------------------------------------------------------------
    //Obtener dieta
    fun getmyDiet(onDataReceived: (List<Dieta>?) -> Unit) {

            val patientIdInt = 101
            val db = FirebaseFirestore.getInstance()
            val dietRef = db.collection("diets")

            // Realiza la consulta buscando el Did
            dietRef.whereEqualTo("id", patientIdInt)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    // Comprueba si se encontró al menos un documento
                    if (!querySnapshot.isEmpty) {
                        val dietList = mutableListOf<Dieta>()
                        val documentCount = querySnapshot.size() // Para contar los documentos

                        // Iterar a través de los documentos de dieta
                        querySnapshot.documents.forEachIndexed { index, document ->
                            val dietId = document.id // Obtiene el ID del documento (subcolección)
                            val daysRef =
                                dietRef.document(dietId) // Referencia a la subcolección del dietId

                            // Días de la semana a consultar
                            val daysOfWeek = listOf(
                                "Lunes",
                                "Martes",
                                "Miércoles",
                                "Jueves",
                                "Viernes",
                                "Sábado",
                                "Domingo"
                            )

                            // Inicializa las comidas para el día
                            val comidasPorDia =
                                mutableMapOf<String, Triple<String, String, String>>() // día -> (desayuno, comida, cena)

                            // Iterar a través de los días de la semana
                            daysOfWeek.forEach { day ->
                                daysRef.collection(day) // Accede a la subcolección de cada día
                                    .get()
                                    .addOnSuccessListener { daySnapshot ->
                                        // Inicializa las comidas
                                        var desayuno = ""
                                        var comida = ""
                                        var cena = ""

                                        // Obtiene los documentos de desayuno, comida y cena
                                        daySnapshot.documents.forEach { dayDocument ->
                                            when (dayDocument.id) {
                                                "desayuno" -> desayuno =
                                                    dayDocument.getString("comida") ?: ""

                                                "comida" -> comida =
                                                    dayDocument.getString("comida") ?: ""

                                                "cena" -> cena =
                                                    dayDocument.getString("comida") ?: ""
                                            }
                                        }

                                        // Guarda las comidas en el mapa por día
                                        comidasPorDia[day] = Triple(desayuno, comida, cena)

                                        // Si es el último día procesado, crea la instancia de Dieta
                                        if (comidasPorDia.size == daysOfWeek.size) {
                                            // Crear instancias de Dieta para cada día
                                            comidasPorDia.forEach { (dia, comidas) ->
                                                dietList.add(
                                                    Dieta(
                                                        Did = patientIdInt,
                                                        desayuno = comidas.first,
                                                        comida = comidas.second,
                                                        cena = comidas.third,
                                                        dia = dia
                                                    )
                                                )
                                            }

                                            // Si es el último documento, devuelve la lista
                                            if (index == documentCount - 1) {
                                                onDataReceived(dietList)
                                            }
                                        }
                                    }
                            }
                        }
                    } else {
                        // Si no se encontró ningún documento, pasa null
                        onDataReceived(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DietaDetailScreen", "Error getting diet data: ", exception)
                    onDataReceived(null)
                }
    }

    //-------------------------------------------------------------------------------------------------------

    fun getDhist(patientId: String, onDataReceived: (List<Dieta>?) -> Unit) {
        Log.d("getDietData", "patientId recibido: $patientId") // Imprime el patientId

        // Intenta convertir el patientId a Int
        val patientIdInt = patientId.toIntOrNull()

        if (patientIdInt != null) {
            val db = FirebaseFirestore.getInstance()
            val dietRef = db.collection("Dieta")

            // Realiza la consulta buscando el Did
            dietRef.whereEqualTo("Did", patientIdInt)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    // Comprueba si se encontró al menos un documento
                    if (!querySnapshot.isEmpty) {
                        val dietList = mutableListOf<Dieta>()
                        val documentCount = querySnapshot.size() // Para contar los documentos

                        // Iterar a través de los documentos de dieta
                        querySnapshot.documents.forEachIndexed { index, document ->
                            val dietId = document.id // Obtiene el ID del documento (subcolección)
                            val daysRef =
                                dietRef.document(dietId) // Referencia a la subcolección del dietId

                            // Días de la semana a consultar
                            val daysOfWeek = listOf(
                                "Lunes",
                                "Martes",
                                "Miércoles",
                                "Jueves",
                                "Viernes",
                                "Sábado",
                                "Domingo"
                            )

                            // Inicializa las comidas para el día
                            val comidasPorDia =
                                mutableMapOf<String, Triple<String, String, String>>() // día -> (desayuno, comida, cena)

                            // Iterar a través de los días de la semana
                            daysOfWeek.forEach { day ->
                                daysRef.collection(day) // Accede a la subcolección de cada día
                                    .get()
                                    .addOnSuccessListener { daySnapshot ->
                                        // Inicializa las comidas
                                        var desayuno = ""
                                        var comida = ""
                                        var cena = ""

                                        // Obtiene los documentos de desayuno, comida y cena
                                        daySnapshot.documents.forEach { dayDocument ->
                                            when (dayDocument.id) {
                                                "desayuno" -> desayuno =
                                                    dayDocument.getString("comida") ?: ""

                                                "comida" -> comida =
                                                    dayDocument.getString("comida") ?: ""

                                                "cena" -> cena =
                                                    dayDocument.getString("comida") ?: ""
                                            }
                                        }

                                        // Guarda las comidas en el mapa por día
                                        comidasPorDia[day] = Triple(desayuno, comida, cena)

                                        // Si es el último día procesado, crea la instancia de Dieta
                                        if (comidasPorDia.size == daysOfWeek.size) {
                                            // Crear instancias de Dieta para cada día
                                            comidasPorDia.forEach { (dia, comidas) ->
                                                dietList.add(
                                                    Dieta(
                                                        Did = patientIdInt,
                                                        desayuno = comidas.first,
                                                        comida = comidas.second,
                                                        cena = comidas.third,
                                                        dia = dia
                                                    )
                                                )
                                            }

                                            // Si es el último documento, devuelve la lista
                                            if (index == documentCount - 1) {
                                                onDataReceived(dietList)
                                            }
                                        }
                                    }
                            }
                        }
                    } else {
                        // Si no se encontró ningún documento, pasa null
                        onDataReceived(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DietaDetailScreen", "Error getting diet data: ", exception)
                    onDataReceived(null)
                }
        } else {
            Log.d("getDietData", "Error: patientId no es un número válido.")
            onDataReceived(null) // Pasa null si la conversión falla
        }
    }

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

    // Función para insertar pacientes
    fun insertPatients(onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Datos de pacientes con dietaId y correo
        val patientsData = listOf(
            Pair(54321, "user1@example.com"),
            Pair(23456, "user2@example.com"),
            Pair(34567, "user3@example.com"),
            Pair(45678, "user4@example.com"),
            Pair(56789, "user5@example.com")
        )

        // Comidas para cada día de la semana
        val meals = mapOf(
            "Lunes" to listOf(
                hashMapOf(
                    "miniId" to "miniIdDesayuno",
                    "comida" to "Tostada con aguacate",
                    "hora" to 8
                ),
                hashMapOf("miniId" to "miniIdComida", "comida" to "Ensalada César", "hora" to 13),
                hashMapOf("miniId" to "miniIdCena", "comida" to "Pescado al horno", "hora" to 20)
            ),
            "Martes" to listOf(
                hashMapOf(
                    "miniId" to "miniIdDesayuno",
                    "comida" to "Yogur con frutas",
                    "hora" to 8
                ),
                hashMapOf(
                    "miniId" to "miniIdComida",
                    "comida" to "Pollo a la parrilla con verduras",
                    "hora" to 13
                ),
                hashMapOf("miniId" to "miniIdCena", "comida" to "Sopa de lentejas", "hora" to 20)
            ),
            "Miércoles" to listOf(
                hashMapOf(
                    "miniId" to "miniIdDesayuno",
                    "comida" to "Batido de plátano y espinacas",
                    "hora" to 8
                ),
                hashMapOf(
                    "miniId" to "miniIdComida",
                    "comida" to "Pasta integral con tomate",
                    "hora" to 13
                ),
                hashMapOf(
                    "miniId" to "miniIdCena",
                    "comida" to "Tortilla de espinacas",
                    "hora" to 20
                )
            ),
            "Jueves" to listOf(
                hashMapOf("miniId" to "miniIdDesayuno", "comida" to "Avena con miel", "hora" to 8),
                hashMapOf("miniId" to "miniIdComida", "comida" to "Bowl de quinoa", "hora" to 13),
                hashMapOf("miniId" to "miniIdCena", "comida" to "Pollo al curry", "hora" to 20)
            ),
            "Viernes" to listOf(
                hashMapOf(
                    "miniId" to "miniIdDesayuno",
                    "comida" to "Smoothie de frutos rojos",
                    "hora" to 8
                ),
                hashMapOf(
                    "miniId" to "miniIdComida",
                    "comida" to "Salmón con espárragos",
                    "hora" to 13
                ),
                hashMapOf("miniId" to "miniIdCena", "comida" to "Pizza de verduras", "hora" to 20)
            ),
            "Sábado" to listOf(
                hashMapOf(
                    "miniId" to "miniIdDesayuno",
                    "comida" to "Huevos revueltos",
                    "hora" to 9
                ),
                hashMapOf(
                    "miniId" to "miniIdComida",
                    "comida" to "Hamburguesa de pavo",
                    "hora" to 13
                ),
                hashMapOf("miniId" to "miniIdCena", "comida" to "Tacos de pollo", "hora" to 19)
            ),
            "Domingo" to listOf(
                hashMapOf(
                    "miniId" to "miniIdDesayuno",
                    "comida" to "Pancakes de avena",
                    "hora" to 9
                ),
                hashMapOf("miniId" to "miniIdComida", "comida" to "Asado de ternera", "hora" to 14),
                hashMapOf(
                    "miniId" to "miniIdCena",
                    "comida" to "Ensalada de garbanzos",
                    "hora" to 20
                )
            )
        )

        val batch = db.batch()

        // Recorrer cada paciente
        for ((dietaId, correo) in patientsData) {
            val dietaRef = db.collection("Dieta").document(dietaId.toString())

            // Crear el HashMap con los datos de la dieta
            val data1 = hashMapOf(
                "Did" to dietaId,
                "correo" to correo // Puedes agregar otros campos que desees almacenar
            )

            // Establecer los datos de la dieta en el documento
            batch.set(dietaRef, data1)

            // Crea una colección para cada día de la semana
            for (day in meals.keys) {
                val dayRef = dietaRef.collection(day)

                // Agrega documentos de desayuno, comida y cena para cada día
                meals[day]?.forEach { meal ->
                    val mealRef = dayRef.document(meal["miniId"].toString())
                    batch.set(mealRef, meal)
                }
            }
        }

        // Realizar el commit del batch
        batch.commit()
            .addOnSuccessListener {
                Log.d("FirestoreRepository", "Dietas insertadas correctamente.")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreRepository", "Error al insertar dietas", e)
                onComplete(false)
            }
    }

    fun deleteNip(patientId: String, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        // Referencia al documento del paciente en Firestore
        val patientRef = db.collection("Pacientes").document(patientId)

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
        val patientRef = db.collection("Pacientes").document(patientId)

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
}