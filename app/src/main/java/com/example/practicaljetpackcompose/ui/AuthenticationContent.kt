package com.example.practicaljetpackcompose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AuthenticationContent(
    modifier: Modifier = Modifier,
    state: AuthenticationState,
    handleEvent: (event: AuthenticationEvent) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            AuthenticationForm(
                modifier = modifier,
                authenticationMode = state.authenticationMode,
                email = state.email,
                password = state.password,
                completedPasswordRequirements = state.passwordRequirements,
                enableAuthentication = state.isFormValid(),
                onEmailChange = { email ->
                    handleEvent(AuthenticationEvent.EmailChanged(email))
                },
                onPasswordChanged = { password ->
                    handleEvent(AuthenticationEvent.PasswordChanged(password))
                },
                onAuthenticate = {
                    handleEvent(AuthenticationEvent.Authenticate)
                },
                onToggleMode = {
                    handleEvent(AuthenticationEvent.ToggleAuthenticationMode)
                }
            )

            state.error?.let { error ->
                AuthenticationErrorDialog(
                    modifier = modifier,
                    error = error,
                    dismissError = {
                        handleEvent(AuthenticationEvent.ErrorDismissed)
                    }
                )
            }
        }
    }
}