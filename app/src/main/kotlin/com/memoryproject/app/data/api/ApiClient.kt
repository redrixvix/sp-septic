package com.memoryproject.app.data.api

import com.memoryproject.app.data.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = false
            })
        }
        install(Logging) { level = LogLevel.BODY }
        expectSuccess = false
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
    }

    private var sessionCookie: String? = null

    private suspend inline fun <reified T> handleResponse(resp: HttpResponse, block: (String) -> T): Result<T> = runCatching {
        val status = resp.status
        val body: String = resp.bodyAsText()

        // Check if response is HTML (not JSON) - Ktor follows redirects
        // Ktor follows redirects — a failed auth redirect returns HTML instead of JSON
        if (body.startsWith("<!DOCTYPE") || body.startsWith("<html")) {
            throw ApiException("Authentication required. Please sign in again.")
        }

        when {
            status == HttpStatusCode.Unauthorized -> throw UnauthorizedException()
            !status.isSuccess() -> {
                val err = try {
                    Json.decodeFromString<ErrorResponse>(body).error
                } catch (_: Exception) {
                    body.take(100)
                }
                throw ApiException(err)
            }
            else -> block(body)
        }
    }

    private suspend inline fun <reified T> postRequest(path: String, body: Any? = null): Result<T> {
        val fullUrl = ApiClient.BASE_URL + path
        val resp: HttpResponse = client.post(path) {
            url { takeFrom(fullUrl) }
            contentType(ContentType.Application.Json)
            if (body != null) setBody(body)
        }
        sessionCookie = resp.headers[HttpHeaders.SetCookie]?.split(";")?.firstOrNull { it.contains("session") || it.contains("token") }
        return handleResponse(resp) { text ->
            try {
                Json.decodeFromString<T>(text)
            } catch (e: Exception) {
                throw ApiException("Parse error: ${e.message}")
            }
        }
    }

    private suspend inline fun <reified T> getRequest(path: String): Result<T> {
        val fullUrl = ApiClient.BASE_URL + path
        val resp: HttpResponse = client.get(path) {
            url { takeFrom(fullUrl) }
            if (sessionCookie != null) header(HttpHeaders.Cookie, sessionCookie)
        }
        // Capture cookie from login response before handleResponse runs
        sessionCookie = sessionCookie ?: resp.headers[HttpHeaders.SetCookie]?.split(";")?.firstOrNull()
        return handleResponse(resp) { text ->
            try {
                Json.decodeFromString<T>(text)
            } catch (e: Exception) {
                throw ApiException("Parse error: ${e.message}")
            }
        }
    }

    private suspend inline fun <reified T> putRequest(path: String, body: Any? = null): Result<T> {
        val fullUrl = ApiClient.BASE_URL + path
        val resp: HttpResponse = client.put(path) {
            url { takeFrom(fullUrl) }
            contentType(ContentType.Application.Json)
            if (body != null) setBody(body)
        }
        return handleResponse(resp) { text ->
            Json.decodeFromString<T>(text)
        }
    }

    private suspend inline fun deleteRequest(path: String): Result<Unit> = runCatching {
        val fullUrl = ApiClient.BASE_URL + path
        val resp: HttpResponse = client.delete(path) {
            url { takeFrom(fullUrl) }
        }
        when {
            resp.status == HttpStatusCode.Unauthorized -> throw UnauthorizedException()
            !resp.status.isSuccess() -> throw ApiException("Delete failed")
        }
    }

    suspend fun login(email: String, password: String): Result<User> =
        postRequest<LoginResponse>("/api/auth/login", LoginRequest(email, password))
            .map { it.user }

    suspend fun signup(email: String, name: String, password: String): Result<User> =
        postRequest<LoginResponse>("/api/auth/signup", SignupRequest(email, name, password)).map { it.user }

    suspend fun logout(): Result<Unit> = runCatching {
        val resp = client.post("/api/auth/logout") {
            url { takeFrom(ApiClient.BASE_URL) }
        }
        sessionCookie = null
        if (!resp.status.isSuccess()) throw ApiException("Logout failed")
    }

    suspend fun me(): Result<User> = getRequest<UserResponse>("/api/auth/me").map { it.user }

    suspend fun getBooks(): Result<List<Book>> =
        getRequest<BooksResponse>("/api/books")
            .map { it.books }

    suspend fun getBook(id: Int): Result<Pair<Book, List<Memory>>> =
        getRequest<BookResponse>("/api/books/$id").map { it.book to it.memories }

    suspend fun createBook(title: String, description: String?): Result<Book> =
        postRequest<BookResponse>("/api/books", CreateBookRequest(title, description))
            .map { it.book }

    suspend fun createMemory(bookId: Int, promptQuestion: String, answerText: String): Result<Memory> =
        postRequest<MemoryResponse>("/api/books/$bookId/memories", CreateMemoryRequest(promptQuestion, answerText))
            .map { it.memory }

    suspend fun updateMemory(id: Int, promptQuestion: String, answerText: String): Result<Memory> =
        putRequest<MemoryResponse>("/api/memories/$id", UpdateMemoryRequest(promptQuestion, answerText))
            .map { it.memory }

    suspend fun deleteMemory(id: Int): Result<Unit> = deleteRequest("/api/memories/$id")

    suspend fun deleteBook(id: Int): Result<Unit> = deleteRequest("/api/books/$id")

    suspend fun getInviteInfo(token: String): Result<InviteInfo> =
        getRequest<InviteInfoResponse>("/api/invite/$token").map { it.data }

    suspend fun acceptInvite(token: String): Result<InviteAcceptResponse> =
        postRequest<InviteAcceptResponse>("/api/invite/$token/accept")

    suspend fun getBookMembers(bookId: Int): Result<List<BookMember>> =
        getRequest<MemberResponse>("/api/books/$bookId/members").map { it.members }

    suspend fun inviteMember(bookId: Int, email: String): Result<InviteResponse> =
        postRequest<InviteResponse>("/api/books/$bookId/invite", InviteRequest(email))

    suspend fun removeMember(bookId: Int, userId: Int): Result<Unit> =
        deleteRequest("/api/books/$bookId/members/$userId")

    fun isLoggedIn(): Boolean = sessionCookie != null

    /**
     * Sets the session cookie, typically after a successful OAuth flow.
     * The cookie string should be the raw cookie value (e.g., "session=abc123").
     */
    fun setSessionCookie(cookie: String) {
        sessionCookie = cookie
    }

    companion object {
        const val BASE_URL = "https://web-redrixvixs-projects.vercel.app"
    }
}

// Response wrappers
@kotlinx.serialization.Serializable
private data class LoginResponse(val user: User)

class ApiException(message: String) : Exception(message)
class UnauthorizedException : Exception("Unauthorized")
