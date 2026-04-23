package com.memoryproject.app.ui.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memoryproject.app.data.model.Book
import com.memoryproject.app.data.model.Memory
import com.memoryproject.app.data.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookDetailUiState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val memories: List<Memory> = emptyList(),
    val error: String? = null,
    val showAddMemory: Boolean = false,
    val editMemory: Memory? = null,
    val deleteConfirmMemory: Memory? = null,
    val isSaving: Boolean = false
)

class BookDetailViewModel(
    private val repository: MemoryRepository,
    private val bookId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    init {
        loadBook()
    }

    fun loadBook() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getBook(bookId)
                .onSuccess { (book, memories) ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        book = book,
                        memories = memories
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load book"
                    )
                }
        }
    }

    fun showAddMemory() {
        _uiState.value = _uiState.value.copy(showAddMemory = true)
    }

    fun hideAddMemory() {
        _uiState.value = _uiState.value.copy(showAddMemory = false)
    }

    fun addMemory(promptQuestion: String, answerText: String) {
        if (promptQuestion.isBlank() || answerText.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            repository.createMemory(bookId, promptQuestion, answerText)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(showAddMemory = false, isSaving = false)
                    loadBook()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "Failed to add memory"
                    )
                }
        }
    }

    fun showEditMemory(memory: Memory) {
        _uiState.value = _uiState.value.copy(editMemory = memory)
    }

    fun hideEditMemory() {
        _uiState.value = _uiState.value.copy(editMemory = null)
    }

    fun editMemory(id: Int, promptQuestion: String, answerText: String) {
        if (promptQuestion.isBlank() || answerText.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            repository.updateMemory(id, promptQuestion, answerText)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(editMemory = null, isSaving = false)
                    loadBook()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "Failed to update memory"
                    )
                }
        }
    }

    fun showDeleteConfirm(memory: Memory) {
        _uiState.value = _uiState.value.copy(deleteConfirmMemory = memory)
    }

    fun hideDeleteConfirm() {
        _uiState.value = _uiState.value.copy(deleteConfirmMemory = null)
    }

    fun deleteMemory(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(deleteConfirmMemory = null)
            repository.deleteMemory(id)
                .onSuccess {
                    loadBook()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "Failed to delete memory"
                    )
                }
        }
    }
}
