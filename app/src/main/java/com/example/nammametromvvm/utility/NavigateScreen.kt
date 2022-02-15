package com.example.nammametromvvm.utility

import android.app.Activity
import android.content.Intent
import com.example.nammametromvvm.ui.homescreen.activity.HomeActivity
import com.example.nammametromvvm.ui.login.ui.activity.LoginActivity
import com.example.nammametromvvm.ui.splashscreen.SplashScreenActivity

class NavigateScreen {
    companion object {
        fun navigateToHomeScreen(activity: Activity) {
            val intent = Intent(activity, HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            activity.startActivity(intent)
        }

        fun navigateToLoginScreen(activity: Activity) {
            val intent = Intent(activity, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            activity.startActivity(intent)

        }

        fun navigateToSplashScreen(activity: Activity) {
            val intent = Intent(activity, SplashScreenActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            activity.startActivity(intent)

        }

    }

}