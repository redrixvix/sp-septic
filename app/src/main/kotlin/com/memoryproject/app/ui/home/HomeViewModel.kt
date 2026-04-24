package com.memoryproject.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memoryproject.app.data.model.Book
import com.memoryproject.app.data.model.Memory
import com.memoryproject.app.data.repository.MemoryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val userInitial: String = "",
    val booksCount: Int = 0,
    val memoriesCount: Int = 0,
    val recentMemories: List<Memory> = emptyList(),
    val books: List<Book> = emptyList(),
    val error: String? = null
)

class HomeViewModel(
    private val repository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun refresh() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Load user info + books in parallel
            val userDeferred = async { repository.getCurrentUser() }
            val booksDeferred = async { repository.getBooks() }

            userDeferred.await()
                .onSuccess { user ->
                    val initial = user.name.firstOrNull()?.uppercaseChar()?.toString() ?: ""
                    _uiState.value = _uiState.value.copy(
                        userName = user.name,
                        userInitial = initial
                    )
                }

            booksDeferred.await()
                .onSuccess { books ->
                    val booksCount = books.size
                    val memoriesCount = books.sumOf { it.memories_count ?: 0 }

                    _uiState.value = _uiState.value.copy(
                        booksCount = booksCount,
                        memoriesCount = memoriesCount,
                        books = books
                    )

                    // Load memories from the 3 most recently updated books, in parallel
                    loadRecentMemoriesInParallel(books)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load data"
                    )
                }
        }
    }

    private suspend fun loadRecentMemoriesInParallel(books: List<Book>) {
        if (books.isEmpty()) {
            _uiState.value = _uiState.value.copy(isLoading = false)
            return
        }

        // Sort by updated_at or created_at descending, take only 3 most recent
        val recentBooks = books
            .sortedByDescending { it.updated_at ?: it.created_at }
            .take(3)

        // Fetch all in parallel with a timeout
        val memoriesDeferreds = recentBooks.map { book ->
            async {
                repository.getBook(book.id)
                    .getOrNull()
                    ?.second
                    ?: emptyList()
            }
        }

        try {
            withTimeoutOrNull(8000L) {
                val results = memoriesDeferreds.awaitAll()
                val allMemories = results.flatten()
                    .sortedByDescending { it.created_at }
                    .take(10)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    recentMemories = allMemories
                )
            } ?: run {
                // Timeout — show what we have
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}