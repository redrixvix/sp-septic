package com.memoryproject.app.ui.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memoryproject.app.data.model.Book
import com.memoryproject.app.data.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BooksUiState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null,
    val isCreating: Boolean = false,
    val userName: String = "",
    val userInitial: String = ""
)

class BooksViewModel(
    private val repository: MemoryRepository,
    private val prefsManager: com.memoryproject.app.data.preferences.PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(BooksUiState())
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            repository.getCurrentUser()
                .onSuccess { user ->
                    val initial = user.name.firstOrNull()?.uppercaseChar()?.toString() ?: ""
                    _uiState.value = _uiState.value.copy(
                        userName = user.name,
                        userInitial = initial
                    )
                }
        }
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getBooks()
                .onSuccess { books ->
                    _uiState.value = _uiState.value.copy(isLoading = false, books = books)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load books"
                    )
                }
        }
    }

    fun createBook(title: String, description: String?) {
        if (title.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true)
            repository.createBook(title, description?.takeIf { it.isNotBlank() })
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isCreating = false)
                    loadBooks()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        error = e.message ?: "Failed to create book"
                    )
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}