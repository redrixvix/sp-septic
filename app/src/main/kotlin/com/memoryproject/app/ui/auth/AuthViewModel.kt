package com.memoryproject.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memoryproject.app.data.api.ApiClient
import com.memoryproject.app.data.auth.WorkOSAuthService
import com.memoryproject.app.data.auth.googleid.GoogleSignInException
import com.memoryproject.app.data.auth.googleid.GoogleSignInHelper
import com.memoryproject.app.data.model.User
import com.memoryproject.app.data.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isSignUp: Boolean = false,
    val forgotPasswordMessage: String? = null,
    val isGoogleLoading: Boolean = false
)

class AuthViewModel(
    private val repository: MemoryRepository,
    private val apiClient: ApiClient,
    private val workOSAuthService: WorkOSAuthService,
    private val googleSignInHelper: GoogleSignInHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Kept for MainActivity compatibility — not used in new native Credential Manager flow
    private val _pendingGoogleCallback = MutableStateFlow<String?>(null)
    val pendingGoogleCallback: StateFlow<String?> = _pendingGoogleCallback.asStateFlow()

    fun setPendingGoogleCallback(uri: String) {
        // No-op — kept for backwards compatibility with MainActivity
    }

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getCurrentUser()
                    .onSuccess { user ->
                        _uiState.value = AuthUiState(isLoggedIn = true, user = user, isGoogleLoading = false)
                    }
                    .onFailure {
                        _uiState.value = AuthUiState(isLoggedIn = false, isGoogleLoading = false)
                    }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(isLoggedIn = false, isGoogleLoading = false)
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
                    _uiState.value = AuthUiState(isLoggedIn = true, user = user, isGoogleLoading = false)
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
                    _uiState.value = AuthUiState(isLoggedIn = true, user = user, isGoogleLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Signup failed"
                    )
                }
        }
    }

    /**
     * Initiates native Google sign-in via AndroidX Credential Manager.
     * Shows the native Google bottom sheet — no browser involved.
     */
    fun initiateGoogleLogin() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isGoogleLoading = true, error = null)

            val result = googleSignInHelper.signIn()

            result
                .onSuccess { credential ->
                    val idToken = credential.idToken
                    if (idToken != null) {
                        exchangeIdTokenForSession(idToken)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isGoogleLoading = false,
                            error = "Google sign-in failed: no ID token received"
                        )
                    }
                }
                .onFailure { e ->
                    val userMessage = if (e is GoogleSignInException) e.userMessage else null
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isGoogleLoading = false,
                        error = userMessage ?: "Google sign-in failed: ${e.message}"
                    )
                }
        }
    }

    private suspend fun exchangeIdTokenForSession(idToken: String) {
        val result = workOSAuthService.exchangeIdTokenForSession(idToken, ApiClient.BASE_URL)

        result
            .onSuccess { sessionCookie ->
                apiClient.setSessionCookie(sessionCookie)
                repository.verifySession()
                    .onSuccess { user ->
                        _uiState.value = AuthUiState(isLoggedIn = true, user = user)
                    }
                    .onFailure { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to verify session"
                        )
                    }
            }
            .onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isGoogleLoading = false,
                    error = "Google login failed: ${e.message}"
                )
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
            _uiState.value = AuthUiState(isLoggedIn = false, isGoogleLoading = false)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun toggleMode() {
        _uiState.value = _uiState.value.copy(isSignUp = !_uiState.value.isSignUp, error = null)
    }
}
