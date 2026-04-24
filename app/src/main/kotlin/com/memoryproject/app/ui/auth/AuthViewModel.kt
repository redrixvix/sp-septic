package com.memoryproject.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memoryproject.app.data.model.User
import com.memoryproject.app.data.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthMode {
    EMAIL_PASSWORD,
    MAGIC_LINK_CODE,
    GOOGLE
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isSignUp: Boolean = false,
    val forgotPasswordMessage: String? = null,
    val authMode: AuthMode = AuthMode.EMAIL_PASSWORD,
    val magicEmail: String = "",
    val magicCodeSent: Boolean = false,
    val googleAuthLoading: Boolean = false,
    val authMessage: String? = null,
    val googleAuthUrl: String? = null
)

class AuthViewModel(private val repository: MemoryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getCurrentUser()
                .onSuccess { user ->
                    _uiState.value = AuthUiState(isLoggedIn = true, user = user)
                }
                .onFailure {
                    _uiState.value = AuthUiState(isLoggedIn = false)
                }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please fill in all fields")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.login(email, password)
                .onSuccess { user ->
                    _uiState.value = AuthUiState(isLoggedIn = true, user = user)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Login failed"
                    )
                }
        }
    }

    fun signup(email: String, name: String, password: String) {
        if (email.isBlank() || name.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please fill in all fields")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.signup(email, name, password)
                .onSuccess { user ->
                    _uiState.value = AuthUiState(isLoggedIn = true, user = user)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Signup failed"
                    )
                }
        }
    }

    fun showForgotPassword() {
        _uiState.value = _uiState.value.copy(
            forgotPasswordMessage = "Password reset coming soon — contact support@memoryproject.app"
        )
    }

    fun clearForgotPasswordMessage() {
        _uiState.value = _uiState.value.copy(forgotPasswordMessage = null)
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState(isLoggedIn = false)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun toggleMode() {
        _uiState.value = _uiState.value.copy(isSignUp = !_uiState.value.isSignUp, error = null)
    }

    fun sendMagicLink(email: String) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter your email address")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, magicEmail = email)
            repository.sendMagicLink(email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        magicCodeSent = true,
                        authMode = AuthMode.MAGIC_LINK_CODE,
                        authMessage = "Check your email for a 6-digit code"
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to send magic link"
                    )
                }
        }
    }

    fun verifyMagicCode(code: String) {
        val email = _uiState.value.magicEmail
        if (code.isBlank() || email.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter the 6-digit code")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.verifyMagicCode(email, code)
                .onSuccess { user ->
                    _uiState.value = AuthUiState(isLoggedIn = true, user = user)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Invalid or expired code"
                    )
                }
        }
    }

    fun onGoogleAuthComplete(session: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.mobileLogin(session)
                .onSuccess { user ->
                    _uiState.value = AuthUiState(isLoggedIn = true, user = user)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Google sign-in failed"
                    )
                }
        }
    }

    fun startGoogleAuth() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, googleAuthLoading = true)
            repository.getMobileAuthUrl()
                .onSuccess { url ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        googleAuthLoading = false,
                        googleAuthUrl = url,
                        authMode = AuthMode.GOOGLE
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        googleAuthLoading = false,
                        error = e.message ?: "Failed to start Google sign-in"
                    )
                }
        }
    }

    fun clearGoogleAuthUrl() {
        _uiState.value = _uiState.value.copy(googleAuthUrl = null)
    }

    fun resetToEmailPassword() {
        _uiState.value = _uiState.value.copy(
            authMode = AuthMode.EMAIL_PASSWORD,
            magicCodeSent = false,
            magicEmail = "",
            authMessage = null,
            error = null,
            googleAuthUrl = null
        )
    }
}