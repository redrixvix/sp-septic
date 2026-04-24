package com.memoryproject.app.ui.onboarding

import androidx.lifecycle.ViewModel
import com.memoryproject.app.data.preferences.PreferencesManager

class OnboardingViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    fun completeOnboarding() {
        preferencesManager.onboardingCompleted = true
    }
}