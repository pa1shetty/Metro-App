package com.example.nammametromvvm.utility.ui

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.nammametromvvm.R

object GeneralUi {
    fun setImageDrawable(imageView: ImageView, context: Context, icon: Int) {
        imageView.setImageDrawable(
            AppCompatResources.getDrawable(
                context,
                icon
            )
        )
    }

    private fun setDrawableColor(imageView: ImageView, context: Context, color: Int) {
        imageView.setColorFilter(
            ContextCompat.getColor(
                context,
                color
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    fun setDrawableColorGreen(imageView: ImageView, context: Context) {
        setDrawableColor(imageView, context, R.color.green)
    }

    fun setDrawableColorRed(imageView: ImageView, context: Context) {
        setDrawableColor(imageView, context, R.color.lightRed)
    }

    fun setDrawableColorOrange(imageView: ImageView, context: Context) {
        setDrawableColor(imageView, context, R.color.orange)
    }

    private fun setBackgroundColor(view: View, context: Context, color: Int) {
        view.setBackgroundColor(ContextCompat.getColor(context, color))
    }

    fun setBackgroundColorRed(view: View, context: Context) {
        setBackgroundColor(view, context, R.color.lightRed)
    }

    fun setBackgroundColorGreen(view: View, context: Context) {
        setBackgroundColor(view, context, R.color.green)
    }

    fun fadingAnimation(view: View, animationId: Int, context: Context) {
        view.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                animationId
            )
        )
    }
}