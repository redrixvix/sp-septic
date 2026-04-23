package com.memoryproject.app.di

import com.memoryproject.app.data.api.ApiClient
import com.memoryproject.app.data.repository.MemoryRepository
import com.memoryproject.app.ui.auth.AuthViewModel
import com.memoryproject.app.ui.books.BooksViewModel
import com.memoryproject.app.ui.books.BookDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { BooksViewModel(get()) }
    viewModel { (bookId: Int) -> BookDetailViewModel(get(), bookId) }
    viewModel { SettingsViewModel(get()) }
}

val dataModule = module {
    single { ApiClient() }
    single { MemoryRepository(get()) }
}
