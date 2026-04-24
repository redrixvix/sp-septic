package com.memoryproject.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memoryproject.app.data.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val userInitial: String = "",
    val booksCount: Int = 0,
    val memoriesCount: Int = 0,
    val storageUsedBytes: Long = 0,
    val message: String? = null
)

class ProfileViewModel(
    private val repository: MemoryRepository
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
                    _uiState.value = _uiState.value.copy(
                        userName = user.name,
                        userEmail = user.email,
                        userInitial = initial
                    )
                }
        }
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

    fun showComingSoon() {
        _uiState.value = _uiState.value.copy(message = "More profile options coming soon")
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}