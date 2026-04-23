package com.memoryproject.app.ui.books

import android.util.Log
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
    val showCreateDialog: Boolean = false,
    val isCreating: Boolean = false
)

class BooksViewModel(private val repository: MemoryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BooksUiState())
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getBooks()
                .onSuccess { books ->
                    Log.d("MP", "loadBooks: success ${books.size}")
                    _uiState.value = _uiState.value.copy(isLoading = false, books = books)
                }
                .onFailure { e ->
                    Log.e("MP", "loadBooks: FAILED ${e.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load books"
                    )
                }
        }
    }

    fun showCreateDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = true)
    }

    fun hideCreateDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = false)
    }

    fun createBook(title: String, description: String?) {
        if (title.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true)
            repository.createBook(title, description?.takeIf { it.isNotBlank() })
                .onSuccess {
                    _uiState.value = _uiState.value.copy(showCreateDialog = false, isCreating = false)
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
