package com.example.practicaljetpackcompose.ui

import androidx.annotation.StringRes
import com.example.practicaljetpackcompose.R

enum class PasswordRequirement(
    @StringRes val label: Int
) {
    CAPITAL_LETTER(R.string.password_requirement_capital),
    NUMBER(R.string.password_requirement_digit),
    EIGHT_CHARACTERS(R.string.password_requirement_characters),
}