package com.example.nutricionapp.ProfileImagen

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

fun uploadImageToFirebase(patientId: String, uri: Uri, onUploadComplete: (Boolean) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference.child("pacientes/$patientId/profile.jpg")

    storageRef.putFile(uri)
        .addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                // Guarda la URL en Firestore
                saveImageUrlToFirestore(patientId, downloadUrl.toString())
                onUploadComplete(true)
            }
        }
        .addOnFailureListener {
            Log.e("Firebase", "Error al subir la imagen: ${it.message}")
            onUploadComplete(false)
        }
}

fun saveImageUrlToFirestore(patientId: String, imageUrl: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("pacientes").document(patientId)
        .update("profileImage", imageUrl)
        .addOnSuccessListener {
            Log.d("Firestore", "Imagen guardada correctamente")
        }
        .addOnFailureListener {
            Log.e("Firestore", "Error al guardar la imagen: ${it.message}")
        }
}
