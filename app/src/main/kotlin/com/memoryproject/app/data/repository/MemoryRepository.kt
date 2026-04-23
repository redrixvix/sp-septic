package com.memoryproject.app.data.repository

import com.memoryproject.app.data.api.ApiClient
import com.memoryproject.app.data.api.ApiException
import com.memoryproject.app.data.api.UnauthorizedException
import com.memoryproject.app.data.model.Book
import com.memoryproject.app.data.model.Memory
import com.memoryproject.app.data.model.User

class MemoryRepository(private val api: ApiClient) {

    suspend fun login(email: String, password: String): Result<User> = api.login(email, password)

    suspend fun signup(email: String, name: String, password: String): Result<User> = api.signup(email, name, password)

    suspend fun logout(): Result<Unit> = api.logout()

    suspend fun getCurrentUser(): Result<User> = api.me()

    suspend fun getBooks(): Result<List<Book>> = api.getBooks()

    suspend fun getBook(id: Int): Result<Pair<Book, List<Memory>>> = api.getBook(id)

    suspend fun createBook(title: String, description: String?): Result<Book> = api.createBook(title, description)

    suspend fun createMemory(bookId: Int, promptQuestion: String, answerText: String): Result<Memory> =
        api.createMemory(bookId, promptQuestion, answerText)

    suspend fun updateMemory(id: Int, promptQuestion: String, answerText: String): Result<Memory> =
        api.updateMemory(id, promptQuestion, answerText)

    suspend fun deleteMemory(id: Int): Result<Unit> = api.deleteMemory(id)

    suspend fun deleteBook(id: Int): Result<Unit> = api.deleteBook(id)

    fun isLoggedIn(): Boolean = api.isLoggedIn()
}
