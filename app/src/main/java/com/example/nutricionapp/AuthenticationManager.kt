package com.example.nutricionapp

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID
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
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                // Usar `suspendCoroutine` para manejar el callback de Firebase y suspender la ejecución hasta obtener el resultado
                suspendCoroutine<Result<AuthResponse>> { continuation ->
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(Result.success(AuthResponse.Success))
                            } else {
                                continuation.resume(
                                    Result.failure(Exception(task.exception?.message ?: "Error al iniciar sesión con Firebase"))
                                )
                            }
                        }
                }
            } else {
                Result.failure(Exception("Error al obtener las credenciales de Google"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}



sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val message: String) : AuthResponse
}