package com.example.practicaljetpackcompose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.example.practicaljetpackcompose.ui.AuthenticationScreen
import org.junit.Rule
import org.junit.Test

class AuthenticationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun Sign_In_Title_Displayed_By_Default() {
        composeTestRule.setContent {
            AuthenticationScreen()
        }
        composeTestRule.onNodeWithText(
            InstrumentationRegistry.getInstrumentation()
                .context.getString(
                    R.string.label_sign_in_to_account
                )
        ).assertIsDisplayed()
    }

    @Test
    fun Sign_Up_Title_Displayed_After_Toggled() {
        composeTestRule.setContent {
            AuthenticationScreen()
        }
        composeTestRule
            .onNodeWithText(
            InstrumentationRegistry.getInstrumentation()
                .context.getString(
                    R.string.action_need_account
                )
        ).performClick()

        composeTestRule
            .onNodeWithText(
                InstrumentationRegistry.getInstrumentation()
                    .context.getString(
                        R.string.label_sign_up_for_account
                    )
            ).assertIsDisplayed()
    }
}