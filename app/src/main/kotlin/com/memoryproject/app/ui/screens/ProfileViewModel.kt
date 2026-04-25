package com.memoryproject.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memoryproject.app.data.preferences.PreferencesManager
import com.memoryproject.app.data.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val userInitial: String = "",
    val userCreatedAt: String = "",
    val booksCount: Int = 0,
    val memoriesCount: Int = 0,
    val storageUsedBytes: Long = 0,
    val message: String? = null
)

class ProfileViewModel(
    private val repository: MemoryRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUser()
        loadLibraryStats()
    }

    private fun loadUser() {
        viewModelScope.launch {
            repository.getCurrentUser()
                .onSuccess { user ->
                    val initial = user.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                    // Use locally saved display name if available, otherwise server name
                    val displayName = preferencesManager.displayName.takeIf { it.isNotBlank() } ?: user.name
                    val createdAt = formatMemberSince(user.created_at)
                    _uiState.value = _uiState.value.copy(
                        userName = displayName,
                        userEmail = user.email,
                        userInitial = initial,
                        userCreatedAt = createdAt
                    )
                }
        }
    }

    private fun formatMemberSince(isoDate: String): String {
        return try {
            val parts = isoDate.substringBefore("T").split("-")
            if (parts.size < 3) return ""
            val months = listOf(
                "", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )
            val month = parts[1].toInt()
            val year = parts[0].toInt()
            val day = parts[2].toInt()
            if (month < 1 || month > 12) return ""
            "Member since ${months[month]} ${ordinalOf(day)}, $year"
        } catch (e: Exception) {
            ""
        }
    }


    private fun ordinalOf(n: Int): String {
        val d = n % 10
        val suffix = when {
            d == 1 && n != 11 -> "st"
            d == 2 && n != 12 -> "nd"
            d == 3 && n != 13 -> "rd"
            else -> "th"
        }
        return "$n$suffix"
    }

    private fun loadLibraryStats() {
        viewModelScope.launch {
            repository.getBooks()
                .onSuccess { books ->
                    val totalMemories = books.sumOf { it.memories_count ?: 0 }
                    val totalStorage = books.sumOf { it.storage_used_bytes.toLongOrNull() ?: 0L }
                    _uiState.value = _uiState.value.copy(
                        booksCount = books.size,
                        memoriesCount = totalMemories,
                        storageUsedBytes = totalStorage
                    )
                }
        }
    }

    fun updateDisplayName(newName: String) {
        if (newName.isBlank()) return
        preferencesManager.displayName = newName
        val initial = newName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
        _uiState.value = _uiState.value.copy(
            userName = newName,
            userInitial = initial,
            message = "Name updated"
        )
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}