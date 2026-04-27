package com.memoryproject.app.ui.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memoryproject.app.data.api.ApiClient
import com.memoryproject.app.data.auth.WorkOSAuthService
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
    val googleAuthUrl: String? = null,
    val isGoogleLoading: Boolean = false
)

class AuthViewModel(
    private val repository: MemoryRepository,
    private val apiClient: ApiClient,
    private val workOSAuthService: WorkOSAuthService
) : ViewModel() {

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
                    _uiState.value = AuthUiState(isLoggedIn = true, user = user, isGoogleLoading = false)
                }
                .onFailure {
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
     * Initiates Google OAuth by generating the auth URL and returning it.
     * The caller (AuthScreen) should open this URL in a Chrome Custom Tab.
     */
    fun initiateGoogleLogin() {
        val authUrl = workOSAuthService.initiateAuth()
        _uiState.value = _uiState.value.copy(googleAuthUrl = authUrl, isGoogleLoading = true)
    }

    /**
     * Clears the stored Google auth URL (after Custom Tab is launched).
     */
    fun clearGoogleAuthUrl() {
        _uiState.value = _uiState.value.copy(googleAuthUrl = null)
    }

    /**
     * Sets a pending Google OAuth callback URI to be processed when the auth screen is ready.
     * Called from MainActivity's onNewIntent before Navigation has routed to auth.
     */
    fun setPendingGoogleCallback(uri: String) {
        _pendingGoogleCallback = uri
    }

    private var _pendingGoogleCallback: String? = null
    val pendingGoogleCallback: String? get() = _pendingGoogleCallback

    /**
     * Handles the OAuth callback from the Custom Tab deep link.
     *
     * The backend's mobile callback (/api/auth/callback/mobile) processes the OAuth code,
     * creates a session, and redirects to memoryproject://oauth/callback?session=...
     *
     * @param callbackUri The full callback URI (memoryproject://oauth/callback?session=...&error=...)
     */
    fun handleGoogleCallback(callbackUri: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, googleAuthUrl = null, isGoogleLoading = false)

            val uri = Uri.parse(callbackUri)
            val error = uri.getQueryParameter("error")
            val sessionParam = uri.getQueryParameter("session")

            if (error != null) {
                workOSAuthService.clearStoredAuth()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isGoogleLoading = false,
                    error = "Google login failed: $error"
                )
                return@launch
            }

            if (sessionParam == null) {
                workOSAuthService.clearStoredAuth()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isGoogleLoading = false,
                    error = "Google login failed: no session in callback"
                )
                return@launch
            }

            // Store the session cookie and verify with backend
            apiClient.setSessionCookie(sessionParam)
            repository.verifySession()
                .onSuccess { user ->
                    _uiState.value = AuthUiState(isLoggedIn = true, user = user)
                }
                .onFailure { e ->
                    workOSAuthService.clearStoredAuth()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to verify session"
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
