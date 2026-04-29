package com.memoryproject.app.data.auth

import android.content.SharedPreferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class WorkOSAuthService(private val prefs: SharedPreferences) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

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
        private const val KEY_FLOW_ID = "workos_mobile_flow_id"
        private const val KEY_STATE = "workos_mobile_state"
    }

    suspend fun initiateAuth(backendBaseUrl: String): Result<String> = runCatching {
        val response: HttpResponse = client.post("$backendBaseUrl/api/auth/mobile/google/start") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }
        val bodyText = response.bodyAsText()

        if (response.status != HttpStatusCode.OK) {
            throw Exception(readError(bodyText, "Failed to start Google login"))
        }

        val startResponse = json.decodeFromString<MobileAuthStartResponse>(bodyText)
        val state = android.net.Uri.parse(startResponse.authorizationUrl).getQueryParameter("state")
            ?: throw Exception("WorkOS authorization URL did not include state")

        prefs.edit()
            .putString(KEY_FLOW_ID, startResponse.flowId)
            .putString(KEY_STATE, state)
            .apply()

        startResponse.authorizationUrl
    }

    fun getStoredState(): String {
        return prefs.getString(KEY_STATE, null)
            ?: throw IllegalStateException("No WorkOS state stored - call initiateAuth() first")
    }

    fun clearStoredAuth() {
        prefs.edit()
            .remove(KEY_FLOW_ID)
            .remove(KEY_STATE)
            .apply()
    }

    suspend fun exchangeCodeForSession(
        code: String,
        state: String,
        backendBaseUrl: String,
    ): Result<String> = runCatching {
        val flowId = prefs.getString(KEY_FLOW_ID, null)
            ?: throw IllegalStateException("No WorkOS flow stored - call initiateAuth() first")

        val response: HttpResponse = client.post("$backendBaseUrl/api/auth/mobile/google/finish") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(MobileAuthFinishRequest(flowId = flowId, code = code, state = state))
        }
        val bodyText = response.bodyAsText()

        if (response.status != HttpStatusCode.OK) {
            throw Exception(readError(bodyText, "Google login failed with status ${response.status.value}"))
        }

        val sessionCookie = response.headers.getAll(HttpHeaders.SetCookie)
            ?.asSequence()
            ?.flatMap { it.split(";").asSequence() }
            ?.firstOrNull { cookie ->
                cookie.contains("session", ignoreCase = true) ||
                    cookie.contains("token", ignoreCase = true)
            }
            ?: throw Exception("No session cookie in mobile auth response")

        clearStoredAuth()
        sessionCookie
    }

    private fun readError(bodyText: String, fallback: String): String {
        return try {
            json.decodeFromString<ErrorResponse>(bodyText).error
        } catch (_: Exception) {
            bodyText.take(160).ifBlank { fallback }
        }
    }
}

@Serializable
private data class MobileAuthStartResponse(
    val authorizationUrl: String,
    val flowId: String,
)

@Serializable
private data class MobileAuthFinishRequest(
    val flowId: String,
    val code: String,
    val state: String,
)

@Serializable
private data class ErrorResponse(val error: String)
