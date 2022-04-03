package com.example.nammametromvvm.utility

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.example.nammametromvvm.R


class GenericMethods {
    fun showKeyPad(activity: Activity, view: View?) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            view,
            0
        )
    }

    fun hideKeypad(activity: Activity) {
        val inputManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    }

    fun showSnackBar(root: View, error: Int, activity: Activity) {
        when (error) {
            StatusEnum.OTP_MISMATCH.statusReturn -> {
                root.snackBar(activity.getString(R.string.otp_mismatch))
            }
            StatusEnum.OTP_TIME_OUT.statusReturn -> {
                root.snackBar(activity.getString(R.string.otp_time_out))
            }
            StatusEnum.NO_INTERNET.statusReturn -> {
                root.snackBar(activity.getString(R.string.no_internet))
            }
            else -> {
                root.snackBar(activity.getString(R.string.something_went_wrong))
            }
        }
    }

    fun getOTPFromMessage(message: String): String {
        val otpLength = 6
        return Regex("(\\d{$otpLength})").find(message)?.value ?: ""
    }

   }