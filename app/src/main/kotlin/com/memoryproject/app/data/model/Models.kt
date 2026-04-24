package com.memoryproject.app.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonProperty

@Serializable
data class User(
    val id: Int,
    val email: String,
    val name: String,
    val created_at: String = ""
)

@Serializable
data class Book(
    val id: Int,
    val title: String,
    val description: String? = null,
    val storage_tier: String = "free",
    val storage_used_bytes: String = "0",
    val created_at: String,
    val owner_name: String = "",
    val role: String = "owner",
    val owner_id: Int = 0,
    val memories_count: Int? = null,
    val updated_at: String? = null
)

@Serializable
data class Memory(
    val id: Int,
    val prompt_question: String,
    val answer_text: String,
    val photo_urls: List<String> = emptyList(),
    val audio_url: String? = null,
    val created_at: String,
    val book_id: Int = 0
)

@Serializable
data class BooksResponse(val books: List<Book>)
@Serializable
data class BookResponse(val book: Book, val memories: List<Memory> = emptyList())
@Serializable
data class MemoryResponse(val memory: Memory)
@Serializable
data class UserResponse(val user: User)
@Serializable
data class SuccessResponse(val success: Boolean)
@Serializable
data class ErrorResponse(val error: String)

@Serializable
data class LoginRequest(val email: String, val password: String)
@Serializable
data class SignupRequest(val email: String, val name: String, val password: String)
@Serializable
data class CreateBookRequest(val title: String, val description: String? = null)
@Serializable
data class CreateMemoryRequest(val prompt_question: String, val answer_text: String)
@Serializable
data class UpdateMemoryRequest(val prompt_question: String, val answer_text: String)

@Serializable
data class InviteInfoResponse(val data: InviteInfo)
@Serializable
data class InviteInfo(val book_id: Int, val book_title: String, val role: String, val invite_email: String)

@Serializable
data class InviteAcceptResponse(val success: Boolean, val book_id: Int = 0)
@Serializable
data class InviteResponse(@JsonProperty("invite_url") val invite_link: String)
@Serializable
data class InviteRequest(val email: String)
@Serializable
data class MemberResponse(val members: List<BookMember>)
@Serializable
data class BookMember(val user_id: Int, val role: String, val name: String = "", val email: String = "", val joined_at: String? = null, val invite_email: String? = null)
