package com.example.es.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun View.snackLong(@StringRes message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .show()
    this.textAlignment = View.TEXT_ALIGNMENT_CENTER
}

fun View.snackIndefinite(@StringRes message: Int = 0) {
    val snack = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
    this.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snack.show()
}

fun View.snackLongTop(@StringRes message: Int) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    val layoutParams = FrameLayout.LayoutParams(snackBar.view.layoutParams)

    layoutParams.gravity = Gravity.TOP
    layoutParams.marginStart = 40
    layoutParams.marginEnd = 40
    snackBar.view.setPadding(0, 8, 0, 8)
    snackBar.view.layoutParams = layoutParams
    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackBar.show()
}

fun View.snackLongTop(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    val layoutParams = FrameLayout.LayoutParams(snackBar.view.layoutParams)

    layoutParams.gravity = Gravity.TOP
    layoutParams.marginStart = 40
    layoutParams.marginEnd = 40
    snackBar.view.setPadding(0, 8, 0, 8)
    snackBar.view.layoutParams = layoutParams
    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackBar.show()
}

fun View.snowSnackIndefiniteTop(snack: Snackbar, @StringRes message: Int) {
    val layoutParams = FrameLayout.LayoutParams(snack.view.layoutParams)

    layoutParams.gravity = Gravity.TOP
    layoutParams.marginStart = 40
    layoutParams.marginEnd = 40
    snack.setText(message)
    snack.view.setPadding(0, 8, 0, 8)
    snack.view.layoutParams = layoutParams
    snack.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snack.show()
}

fun View.dismissSnackIndefiniteTop(@StringRes message: Int) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
    val layoutParams = FrameLayout.LayoutParams(snackBar.view.layoutParams)

    layoutParams.gravity = Gravity.TOP
    layoutParams.marginStart = 40
    layoutParams.marginEnd = 40
    snackBar.view.setPadding(0, 8, 0, 8)
    snackBar.view.layoutParams = layoutParams
    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackBar.dismiss()
}

fun View.visible(show: Boolean) =
    if (show) this.visibility = View.VISIBLE else this.visibility = View.GONE

