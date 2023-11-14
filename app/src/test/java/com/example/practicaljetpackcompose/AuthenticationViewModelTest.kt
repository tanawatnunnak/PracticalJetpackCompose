package com.example.practicaljetpackcompose

import com.example.practicaljetpackcompose.ui.AuthenticationEvent
import com.example.practicaljetpackcompose.ui.AuthenticationMode
import com.example.practicaljetpackcompose.ui.AuthenticationViewModel
import com.example.practicaljetpackcompose.ui.PasswordRequirement
import org.junit.Before
import org.junit.Test

class AuthenticationViewModelTest {

    private lateinit var viewModel: AuthenticationViewModel

    @Before
    fun setUp() {
        viewModel = AuthenticationViewModel()
    }

    @Test
    fun toggleAuthenticationMode_currentSignUp_changeToSingInMode() {
        //given
        viewModel.handleEvent(AuthenticationEvent.ToggleAuthenticationMode)

        //when
        viewModel.handleEvent(AuthenticationEvent.ToggleAuthenticationMode)

        //then
        val uiState = viewModel.uiState.value
        assert(uiState.authenticationMode == AuthenticationMode.SIGN_IN)
    }

    @Test
    fun toggleAuthenticationMode_currentSignIn_changeToSingUpMode() {
        viewModel.handleEvent(AuthenticationEvent.ToggleAuthenticationMode)
        val uiState = viewModel.uiState.value
        assert(uiState.authenticationMode == AuthenticationMode.SIGN_UP)
    }

    @Test
    fun updateEmail_correct() {
        val email = "tanawatnunnak@gmail.com"
        viewModel.handleEvent(AuthenticationEvent.EmailChanged(email))
        val uiState = viewModel.uiState.value
        assert(uiState.email == email)
    }

    @Test
    fun updatePassword_eightCharacter_satisfiedEightCharacter() {
        viewModel.handleEvent(AuthenticationEvent.PasswordChanged("12345678"))
        val uiState = viewModel.uiState.value
        assert(uiState.passwordRequirements.contains(PasswordRequirement.EIGHT_CHARACTERS))
    }

    @Test
    fun updatePassword_moreThanEightCharacter_satisfiedEightCharacter() {
        viewModel.handleEvent(AuthenticationEvent.PasswordChanged("123456789"))
        val uiState = viewModel.uiState.value
        assert(uiState.passwordRequirements.contains(PasswordRequirement.EIGHT_CHARACTERS))
    }

    @Test
    fun updatePassword_lessThanEightCharacter_neededEightCharacter() {
        viewModel.handleEvent(AuthenticationEvent.PasswordChanged("1"))
        val uiState = viewModel.uiState.value
        assert(!uiState.passwordRequirements.contains(PasswordRequirement.EIGHT_CHARACTERS))
    }

    @Test
    fun updatePassword_upperCharacter_satisfiedCapitalLetter() {
        viewModel.handleEvent(AuthenticationEvent.PasswordChanged("T@nawat"))
        val uiState = viewModel.uiState.value
        assert(uiState.passwordRequirements.contains(PasswordRequirement.CAPITAL_LETTER))
    }

    @Test
    fun updatePassword_withoutUpperCharacter_neededCapitalLetter() {
        viewModel.handleEvent(AuthenticationEvent.PasswordChanged("t@nawat"))
        val uiState = viewModel.uiState.value
        assert(!uiState.passwordRequirements.contains(PasswordRequirement.CAPITAL_LETTER))
    }

    @Test
    fun updatePassword_digit_satisfiedNumber() {
        viewModel.handleEvent(AuthenticationEvent.PasswordChanged("T@nawat!2"))
        val uiState = viewModel.uiState.value
        assert(uiState.passwordRequirements.contains(PasswordRequirement.NUMBER))
    }

    @Test
    fun updatePassword_withoutDigit_neededNumber() {
        viewModel.handleEvent(AuthenticationEvent.PasswordChanged("T@nawat!"))
        val uiState = viewModel.uiState.value
        assert(!uiState.passwordRequirements.contains(PasswordRequirement.NUMBER))
    }

    @Test
    fun updatePassword_eightAndUpperAndDigitCharacter_satisfiedAllRequirements() {
        viewModel.handleEvent(AuthenticationEvent.PasswordChanged("T@nawat1"))
        val uiState = viewModel.uiState.value
        assert(uiState.passwordRequirements.contains(PasswordRequirement.EIGHT_CHARACTERS))
        assert(uiState.passwordRequirements.contains(PasswordRequirement.CAPITAL_LETTER))
        assert(uiState.passwordRequirements.contains(PasswordRequirement.NUMBER))
    }

    @Test
    fun dismissError_disappearError() {
        viewModel.handleEvent(AuthenticationEvent.ErrorDismissed)
        val uiState = viewModel.uiState.value
        assert(uiState.error == null)
    }

    /*@Test
    fun authenticate_appearLoading() {
        viewModel.handleEvent(AuthenticationEvent.Authenticate)
        val uiState = viewModel.uiState.value
        assert(uiState.isLoading)
    }*/

}