package com.memoryproject.app.ui.auth

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
    val isGoogleLoading: Boolean = false
)

class AuthViewModel(
    private val repository: MemoryRepository,
    private val apiClient: ApiClient,
    private val workOSAuthService: WorkOSAuthService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _workOSAuthorizationUrl = MutableStateFlow<String?>(null)
    val workOSAuthorizationUrl: StateFlow<String?> = _workOSAuthorizationUrl.asStateFlow()

    fun setPendingGoogleCallback(uri: String) {
        handleWorkOSCallback(uri)
    }

    fun consumeWorkOSAuthorizationUrl() {
        _workOSAuthorizationUrl.value = null
    }

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

    fun initiateGoogleLogin() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isGoogleLoading = true, error = null)
            workOSAuthService.initiateAuth(ApiClient.BASE_URL)
                .onSuccess { authorizationUrl ->
                    _workOSAuthorizationUrl.value = authorizationUrl
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isGoogleLoading = false,
                        error = "Google login failed: ${e.message}"
                    )
                }
        }
    }

    private fun handleWorkOSCallback(uri: String) {
        viewModelScope.launch {
            val parsedUri = android.net.Uri.parse(uri)
            val error = parsedUri.getQueryParameter("error")
            if (error != null) {
                workOSAuthService.clearStoredAuth()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isGoogleLoading = false,
                    error = parsedUri.getQueryParameter("error_description") ?: error
                )
                return@launch
            }

            val code = parsedUri.getQueryParameter("code")
            val returnedState = parsedUri.getQueryParameter("state")
            if (code.isNullOrBlank()) {
                workOSAuthService.clearStoredAuth()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isGoogleLoading = false,
                    error = "Google login failed: missing authorization code"
                )
                return@launch
            }

            val storedState = runCatching { workOSAuthService.getStoredState() }.getOrNull()
            if (returnedState == null || returnedState != storedState) {
                workOSAuthService.clearStoredAuth()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isGoogleLoading = false,
                    error = "Google login failed: invalid OAuth state"
                )
                return@launch
            }

            exchangeCodeForSession(code, returnedState)
        }
    }

    private suspend fun exchangeCodeForSession(code: String, state: String) {
        workOSAuthService.exchangeCodeForSession(code, state, ApiClient.BASE_URL)
            .onSuccess { sessionCookie ->
                apiClient.setSessionCookie(sessionCookie)
                repository.verifySession()
                    .onSuccess { user ->
                        _uiState.value = AuthUiState(isLoggedIn = true, user = user)
                    }
                    .onFailure { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isGoogleLoading = false,
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
            _uiState.value = AuthUiState(isLoggedIn = false)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun toggleMode() {
        _uiState.value = _uiState.value.copy(isSignUp = !_uiState.value.isSignUp, error = null)
    }
}
