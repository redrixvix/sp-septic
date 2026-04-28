package com.memoryproject.app.data.auth.googleid

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialRequest.Builder
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

/**
 * Native Google Sign-In using AndroidX Credential Manager.
 * 
 * Shows the native Google-branded bottom sheet via Sign In With Google (SIWG).
 * No browser, no Custom Tab — just the system credential picker.
 */
class GoogleSignInHelper(
    private val context: Context,
    private val serverClientId: String,
) {
    /**
     * Signs in using Sign In With Google (SIWG) — native Google-branded bottom sheet.
     * No browser, no account picker dialog — just the branded Google experience.
     */
    suspend fun signIn(): Result<GoogleIdTokenCredential> {
        val credentialManager = CredentialManager.create(context)

        // SIWG shows the branded Google bottom sheet — single dialog, no extra picker
        val siwgOption = GetSignInWithGoogleOption.Builder(serverClientId).build()
        val request = Builder().addCredentialOption(siwgOption).build()

        return runCatching {
            credentialManager.getCredential(context, request).credential.let { cred ->
                extractCredential(cred)
            }
        }.recoverCatching { e ->
            throw mapToSignInException(e)
        }
    }

    private fun extractCredential(credential: androidx.credentials.Credential): GoogleIdTokenCredential {
        // Handle GoogleIdTokenCredential directly (from SIWG) or CustomCredential (from other Google flows)
        if (credential is GoogleIdTokenCredential) return credential
        if (credential is CustomCredential) {
            val type = credential.type
            // TYPE_GOOGLE_ID_TOKEN_CREDENTIAL covers both SIWG and GetGoogleIdOption credential types
            if (type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                return GoogleIdTokenCredential.createFrom(credential.data)
            }
        }
        throw GoogleSignInException.InvalidCredentialType(
            "Unexpected credential type: ${credential::class.java.name}"
        )
    }

    private fun mapToSignInException(e: Throwable): GoogleSignInException {
        return when (e) {
            is GetCredentialCancellationException -> GoogleSignInException.UserCancelled
            is NoCredentialException -> GoogleSignInException.NoGoogleAccountsFound
            is GetCredentialException -> GoogleSignInException.GetCredentialError(
                errorType = e.type,
                detail = e.message ?: "Unknown"
            )
            is GoogleSignInException -> e
            else -> GoogleSignInException.Unknown(e.message ?: "Unknown error: ${e::class.java.name}")
        }
    }
}

sealed class GoogleSignInException(
    message: String,
    val userMessage: String? = null
) : Exception(message) {
    data object UserCancelled : GoogleSignInException("User cancelled", "Sign-in was cancelled.")
    data object NoGoogleAccountsFound : GoogleSignInException(
        "No Google accounts on device",
        "No Google account found. Add one in device settings."
    )
    data class GetCredentialError(val errorType: String, val detail: String) : GoogleSignInException(
        "GetCredential error [$errorType]: $detail",
        "Failed to retrieve credentials. Please try again."
    )
    data class InvalidCredentialType(val detail: String) : GoogleSignInException(
        "Invalid credential: $detail",
        "Unexpected sign-in response. Please try again."
    )
    data class Unknown(val detail: String) : GoogleSignInException(
        "Unknown: $detail",
        "An unexpected error occurred. Please try again."
    )
}
