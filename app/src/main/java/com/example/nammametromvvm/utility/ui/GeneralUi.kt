package com.example.nammametromvvm.utility.ui

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.nammametromvvm.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

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

    private const val qRcodeWidth = 500

    fun textToImageEncode(context: Context, Value: String): Bitmap? {
        val qrColor = ContextCompat.getColor(context, R.color.qrColor)
        val qrBackgroundColor = ContextCompat.getColor(context, R.color.qrBackgroundColor)
        val bitMatrix: BitMatrix = try {
            val hints: Hashtable<EncodeHintType, ErrorCorrectionLevel> = Hashtable()
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
            MultiFormatWriter().encode(
                Value,
                BarcodeFormat.QR_CODE,
                qRcodeWidth, qRcodeWidth, hints
            )
        } catch (Illegalargumentexception: IllegalArgumentException) {
            return null
        }
        val bitMatrixWidth: Int = bitMatrix.getWidth()
        val bitMatrixHeight: Int = bitMatrix.getHeight()
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) qrColor else qrBackgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, qRcodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

}