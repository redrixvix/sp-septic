package com.memoryproject.app.di

import com.memoryproject.app.data.api.ApiClient
import com.memoryproject.app.data.preferences.PreferencesManager
import com.memoryproject.app.data.repository.MemoryRepository
import com.memoryproject.app.ui.auth.AuthViewModel
import com.memoryproject.app.ui.books.BooksViewModel
import com.memoryproject.app.ui.books.BookDetailViewModel
import com.memoryproject.app.ui.screens.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PreferencesManager(androidContext()) }
    viewModel { AuthViewModel(get()) }
    viewModel { BooksViewModel(get(), get()) }
    viewModel { (bookId: Int) -> BookDetailViewModel(get(), bookId) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
}

val dataModule = module {
    single { ApiClient() }
    single { MemoryRepository(get()) }
}
