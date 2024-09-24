package com.example.nutriapp

val db = FirebaseFirestore.getInstance()

fun agregarDatosPacienteYNutriologo() {
    // Paso 1: Crear el documento del nutriólogo
    val nutriologoRef = db.collection("usuarios").document("nutriologos").collection("id").document()

    val nuevoNutriologo = Nutriologo(
        nombres = "Ana",
        apellidos = "Martínez",
        email = "ana.martinez@example.com",
        direccion = "Calle Falsa 123",
        tel = "123456789",
        passwordHash = "hashed_password",
        verificado = true,
        sessionToken = "sdfsdf56786dsfsad67",
        numCedula = "1234567"
    )

    // Añadimos el nutriólogo a Firestore
    nutriologoRef.set(nuevoNutriologo).addOnSuccessListener {
        Log.d("Firestore", "Nutriólogo agregado exitosamente")

        // Paso 2: Crear el documento del paciente y asignar la referencia del nutriólogo
        val pacienteRef = db.collection("usuarios").document("pacientes").collection("id").document()

        val nuevoPaciente = Paciente(
            nombres = "Juan",
            apellidos = "Pérez",
            email = "juan.perez@example.com",
            fechaNacimiento = "1990-01-01",
            nutriologoAsign = nutriologoRef.path, // Referencia al documento del nutriólogo
            passwordHash = "hashed_password",
            sessionToken = "sdf786asdf564sdf",
            tel = "987654321"
        )

        // Añadimos el paciente a Firestore
        pacienteRef.set(nuevoPaciente).addOnSuccessListener {
            Log.d("Firestore", "Paciente agregado exitosamente")

            // Paso 3: Actualizar el nutriólogo con la referencia del paciente
            nutriologoRef.update("pacientesAssign", FieldValue.arrayUnion(pacienteRef.path))
                .addOnSuccessListener {
                    Log.d("Firestore", "Nutriólogo actualizado con el paciente asignado")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error al actualizar nutriólogo", e)
                }
        }.addOnFailureListener { e ->
            Log.w("Firestore", "Error al agregar paciente", e)
        }
    }.addOnFailureListener { e ->
        Log.w("Firestore", "Error al agregar nutriólogo", e)
    }
}
