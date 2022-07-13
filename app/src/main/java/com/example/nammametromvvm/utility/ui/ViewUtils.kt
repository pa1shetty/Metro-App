package com.example.nammametromvvm.utility.ui

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

@Suppress("unused")
fun ProgressBar.show() {
    visibility = View.VISIBLE
}

@Suppress("unused")
fun ProgressBar.hide() {
    visibility = View.INVISIBLE
}

@Suppress("unused")
fun Context.logOver(tag: String = "tag", message: String) {
    Log.d(tag, message)
}

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("OK") {
            snackbar.dismiss()
        }.show()
    }
}