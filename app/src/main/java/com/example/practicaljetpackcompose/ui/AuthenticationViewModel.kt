package com.example.practicaljetpackcompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {

    val uiState = MutableStateFlow(AuthenticationState())

    fun handleEvent(event: AuthenticationEvent) {
        when (event) {
            AuthenticationEvent.ToggleAuthenticationMode -> toggleAuthentication()
            is AuthenticationEvent.EmailChanged -> updateEmail(event.email)
            is AuthenticationEvent.PasswordChanged -> updatePassword(event.password)
            AuthenticationEvent.Authenticate -> authenticate()
            AuthenticationEvent.ErrorDismissed -> dismissError()
        }
    }

    private fun toggleAuthentication() {
        val authenticationMode = uiState.value.authenticationMode
        val newAuthenticationMode = if (authenticationMode == AuthenticationMode.SIGN_IN) {
            AuthenticationMode.SIGN_UP
        } else {
            AuthenticationMode.SIGN_IN
        }
        uiState.update { it.copy(authenticationMode = newAuthenticationMode) }
    }

    private fun updateEmail(email: String) {
        uiState.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        val requirements = mutableListOf<PasswordRequirement>()
        if (password.length > 7) {
            requirements.add(PasswordRequirement.EIGHT_CHARACTERS)
        }

        if (password.any { it.isUpperCase() }) {
            requirements.add(PasswordRequirement.CAPITAL_LETTER)
        }

        if (password.any { it.isDigit() }) {
            requirements.add(PasswordRequirement.NUMBER)
        }

        uiState.update {
            it.copy(
                password = password,
                passwordRequirements = requirements
            )
        }
    }

    private fun authenticate() {
        uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            delay(2000L)
            withContext(Dispatchers.Main) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = "Something went wrong !"
                )
            }
        }
    }

    private fun dismissError() {
        uiState.update { it.copy(error = null) }
    }
}