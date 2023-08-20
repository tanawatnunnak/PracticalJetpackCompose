package com.example.practicaljetpackcompose.ui

sealed class AuthenticationEvent {
    object ToggleAuthenticationMode: AuthenticationEvent()
    class EmailChanged(val email: String): AuthenticationEvent()
    class PasswordChanged(val password: String): AuthenticationEvent()
    object Authenticate: AuthenticationEvent()
    object ErrorDismissed: AuthenticationEvent()
}