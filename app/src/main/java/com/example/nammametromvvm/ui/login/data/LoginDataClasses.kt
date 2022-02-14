package com.example.nammametromvvm.ui.login.data

import com.example.nammametromvvm.utility.StatusEnum

data class LoginResult(
    val success: Boolean = false,
    val error: Int = StatusEnum.ERROR.statusReturn
)

data class LoginFormState(
    val isDataValid: Boolean = false
)