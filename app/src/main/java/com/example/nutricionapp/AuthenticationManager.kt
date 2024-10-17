package com.example.nutricionapp

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthenticationManager(
    private val context: Context
) {

    private val auth = Firebase.auth

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
    }

    fun loginWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    trySendBlocking(AuthResponse.Success).isSuccess
                } else {
                    // Sign in failed
                    trySendBlocking(AuthResponse.Error(message = task.exception?.message ?: ""))
                }
            }

        // Make sure to close the flow otherwise it will crash
        awaitClose()
    }

    suspend fun signInWithGoogle(): Result<AuthResponse> {
        return try {
            Log.d("WARN", "Iniciando sesión con Google")

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .setAutoSelectEnabled(false)
                .setNonce(createNonce())
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential

            // Verificar que el credential sea de tipo CustomCredential y tenga el tipo de credencial esperado
            if (credential is CustomCredential) {
                Log.d("DEBUG", "Credential is CustomCredential")

                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    Log.d("DEBUG", "Credential type is Google ID Token")

                    // Crear el GoogleIdTokenCredential a partir de los datos de la credencial
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                    // Verificar si el ID Token es nulo
                    if (googleIdTokenCredential.idToken != null) {
                        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                        // Usar suspendCoroutine para manejar el callback de Firebase
                        return suspendCoroutine { continuation ->
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("DEBUG", "Autenticación exitosa en Firebase")
                                        continuation.resume(Result.success(AuthResponse.Success))
                                    } else {
                                        val exception = task.exception
                                        Log.e("ERROR", "Error en Firebase: ${exception?.message}")

                                        // Verificar si el error indica que el usuario no existe
                                        if (exception is FirebaseAuthInvalidUserException) {
                                            Log.d("DEBUG", "Usuario no encontrado, creando nuevo usuario")
                                            createUserWithGoogle(firebaseCredential, continuation)
                                        } else {
                                            continuation.resume(Result.failure(Exception(exception?.message ?: "Error al iniciar sesión con Firebase")))
                                        }
                                    }
                                }
                        }
                    } else {
                        Log.e("ERROR", "ID Token is null")
                        return Result.failure(Exception("ID Token is null"))
                    }
                } else {
                    Log.e("ERROR", "Credential type is not Google ID Token")
                    return Result.failure(Exception("Error: Credential type is not Google ID Token"))
                }
            } else {
                Log.e("ERROR", "Credential is not CustomCredential")
                return Result.failure(Exception("Error: Credential is not CustomCredential"))
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Exception during Google sign-in: ${e.message}")
            return Result.failure(e)
        }
    }

    // Función para registrar un usuario si no existe (método de ejemplo)
    private fun createUserWithGoogle(firebaseCredential: AuthCredential, continuation: Continuation<Result<AuthResponse>>) {
        auth.createUserWithEmailAndPassword(firebaseCredential.signInMethod, "default_password") // Aquí el manejo dependerá de cómo quieras registrar al usuario
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("DEBUG", "Nuevo usuario creado exitosamente")
                    continuation.resume(Result.success(AuthResponse.Success))
                } else {
                    val exception = task.exception
                    Log.e("ERROR", "Error al crear el usuario: ${exception?.message}")
                    continuation.resume(Result.failure(Exception("Error al registrar el usuario: ${exception?.message ?: "Error desconocido"}")))
                }
            }
    }




}



sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val message: String) : AuthResponse
}