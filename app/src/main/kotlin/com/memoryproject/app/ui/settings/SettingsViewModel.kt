package com.memoryproject.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memoryproject.app.data.preferences.PreferencesManager
import com.memoryproject.app.data.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val userEmail: String = "",
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val profileMessage: String? = null
)

class SettingsViewModel(
    private val repository: MemoryRepository,
    private val prefsManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadUser()
        _uiState.value = _uiState.value.copy(
            isDarkMode = prefsManager.darkMode,
            notificationsEnabled = prefsManager.notificationsEnabled
        )
    }

    private fun loadUser() {
        viewModelScope.launch {
            repository.getCurrentUser()
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(userEmail = user.email)
                }
        }
    }

    fun toggleDarkMode() {
        val newValue = !_uiState.value.isDarkMode
        _uiState.value = _uiState.value.copy(isDarkMode = newValue)
        prefsManager.darkMode = newValue
    }

    fun toggleNotifications() {
        val newValue = !_uiState.value.notificationsEnabled
        _uiState.value = _uiState.value.copy(notificationsEnabled = newValue)
        prefsManager.notificationsEnabled = newValue
    }

    fun showProfileComingSoon() {
        _uiState.value = _uiState.value.copy(profileMessage = "Profile editing coming soon")
    }

    fun clearProfileMessage() {
        _uiState.value = _uiState.value.copy(profileMessage = null)
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            prefsManager.clearAll()
        }
    }
}