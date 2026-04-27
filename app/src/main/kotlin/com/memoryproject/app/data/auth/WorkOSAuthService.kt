package com.memoryproject.app.data.auth

import android.content.Context
import android.content.SharedPreferences
import com.memoryproject.app.data.model.WorkOSProfileResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * WorkOS Google OAuth service using raw REST API via Ktor.
 *
 * Flow for Android (PKCE, no cookies shared with Custom Tab):
 *  1. [initiateAuth] — generates PKCE verifier/challenge, builds auth URL, stores verifier in prefs
 *  2. [getStoredVerifier] — returns the stored verifier for use in callback
 *  3. [exchangeCodeForSession] — calls backend mobile callback with code+verifier to get session cookie
 *
 * The backend [/api/auth/callback/mobile] endpoint handles the actual WorkOS token exchange
 * and returns the session cookie for the app to store.
 */
class WorkOSAuthService(private val prefs: SharedPreferences) {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) { level = LogLevel.BODY }
        expectSuccess = false
    }

    companion object {
        private const val WORKOS_CLIENT_ID = "client_01KPTJ9V6VTS6BEPNHFAKBJQB1"
        private const val CALLBACK_URL = "memoryproject://oauth/callback"
        private const val WORKOS_API_BASE = "https://auth.workos.com"

        private const val KEY_CODE_VERIFIER = "oauth_code_verifier"
        private const val KEY_STATE = "oauth_state"
    }

    /**
     * Initiates Google OAuth by building the authorization URL with PKCE.
     * The returned URL should be opened in a Chrome Custom Tab.
     *
     * @return The authorization URL to open in a Custom Tab
     */
    fun initiateAuth(): String {
        val codeVerifier = PKCEUtil.generateCodeVerifier()
        val state = PKCEUtil.generateState()
        val codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier)

        // Store for callback exchange
        prefs.edit()
            .putString(KEY_CODE_VERIFIER, codeVerifier)
            .putString(KEY_STATE, state)
            .apply()

        val queryParams = listOf(
            "client_id" to WORKOS_CLIENT_ID,
            "redirect_uri" to CALLBACK_URL,
            "response_type" to "code",
            "provider" to "GoogleOAuth",
            "state" to state,
            "code_challenge" to codeChallenge,
            "code_challenge_method" to "S256"
        ).joinToString("&") { (key, value) ->
            "$key=${java.net.URLEncoder.encode(value, "UTF-8")}"
        }

        return "$WORKOS_API_BASE/oauth2/authorize?$queryParams"
    }

    /**
     * Returns the PKCE code verifier stored by [initiateAuth].
     * @throws IllegalStateException if no auth was initiated
     */
    fun getStoredVerifier(): String {
        return prefs.getString(KEY_CODE_VERIFIER, null)
            ?: throw IllegalStateException("No PKCE verifier — call initiateAuth() first")
    }

    /**
     * Returns the state parameter stored by [initiateAuth].
     * @throws IllegalStateException if no auth was initiated
     */
    fun getStoredState(): String {
        return prefs.getString(KEY_STATE, null)
            ?: throw IllegalStateException("No state stored — call initiateAuth() first")
    }

    /**
     * Clears stored PKCE data. Call after successful auth or on error.
     */
    fun clearStoredAuth() {
        prefs.edit()
            .remove(KEY_CODE_VERIFIER)
            .remove(KEY_STATE)
            .apply()
    }

    /**
     * Exchanges the OAuth authorization code (from the callback URI) for a session cookie
     * by calling the backend mobile callback endpoint.
     *
     * @param code The authorization code from the callback URI
     * @param backendBaseUrl Base URL of the backend API (e.g., https://web-redrixvixs-projects.vercel.app)
     * @return Result containing the raw session cookie string on success
     */
    suspend fun exchangeCodeForSession(code: String, backendBaseUrl: String): Result<String> = runCatching {
        val codeVerifier = getStoredVerifier()
        val state = getStoredState()

        val callbackUrl = listOf(
            "code" to code,
            "state" to state,
            "code_verifier" to codeVerifier
        ).joinToString("&") { (key, value) ->
            "$key=${java.net.URLEncoder.encode(value, "UTF-8")}"
        }

        val mobileCallbackUrl = "$backendBaseUrl/api/auth/callback/mobile?$callbackUrl"

        val response: HttpResponse = client.get(mobileCallbackUrl)

        val bodyText = response.bodyAsText()

        if (response.status != HttpStatusCode.OK) {
            val errorJson = try {
                Json.decodeFromString<Map<String, String>>(bodyText)
            } catch (_: Exception) {
                mapOf("error" to bodyText.take(100))
            }
            throw Exception(errorJson["error"] ?: "OAuth callback failed with status ${response.status.value}")
        }

        // Extract session cookie from Set-Cookie header
        val sessionCookie = response.headers[HttpHeaders.SetCookie]
            ?.split(";")
            ?.firstOrNull { cookie ->
                cookie.contains("session", ignoreCase = true) ||
                cookie.contains("token", ignoreCase = true)
            }
            ?: throw Exception("No session cookie in callback response")

        clearStoredAuth()
        sessionCookie
    }
}
