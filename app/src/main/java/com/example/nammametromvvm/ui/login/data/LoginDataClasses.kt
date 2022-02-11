package com.example.nammametromvvm.ui.login.ui.activity

import com.example.nammametromvvm.utility.StatusEnum

data class LoginResult(
    val success: Boolean = false,
    val error: Int = StatusEnum.ERROR.statusReturn
)

data class LoginFormState(
    val isDataValid: Boolean = false
)