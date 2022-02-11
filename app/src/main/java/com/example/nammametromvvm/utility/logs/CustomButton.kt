package com.example.nammametromvvm.utility.logs

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.nammametromvvm.R
import com.example.nammametromvvm.utility.logs.LoginScreenEnum.*

class CustomButton (
    private val cvProceed: CardView,
    private val ivLogin: ImageView,
    private val pbLogin: ProgressBar,
    private val activity: Activity
) {
    fun enableLoginButton() {
        loginButtonStatus(
            ButtonStatusEnum.ENABLE.status,
        )
    }

    fun disableLoginButton() {
        loginButtonStatus(
            ButtonStatusEnum.DISABLE.status,
        )
    }

    fun loadingLoginButton() {
        loginButtonStatus(
            ButtonStatusEnum.LOADING.status,
        )

    }

    fun stopLoadingLoginButton() {
        loginButtonStatus(
            ButtonStatusEnum.NOT_LOADING.status,
        )
    }


    private fun loginButtonStatus(
        status: Int
    ) {
        when (status) {
            ButtonStatusEnum.ENABLE.status -> {
                cvProceed.isEnabled = true
                ivLogin.setColorFilter(
                    ContextCompat.getColor(
                        activity,
                        R.color.bmrcl_color
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            ButtonStatusEnum.DISABLE.status -> {
                cvProceed.isEnabled = false
                ivLogin.setColorFilter(
                    ContextCompat.getColor(
                        activity,
                        R.color.lightGray
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            ButtonStatusEnum.LOADING.status -> {
                cvProceed.isEnabled = false
                pbLogin.visibility = View.VISIBLE
                ivLogin.visibility = View.GONE
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }
            else -> {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                cvProceed.isEnabled = true
                pbLogin.visibility = View.GONE
                ivLogin.visibility = View.VISIBLE
            }
        }
    }
}

class LoginScreenEnum {
    enum class ButtonStatusEnum(val status: Int) {
        ENABLE(0),
        DISABLE(1),
        LOADING(2),
        NOT_LOADING(3),
    }
}