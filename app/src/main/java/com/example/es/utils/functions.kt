package com.example.es.utils

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun View.snackLong(@StringRes message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .show()
    this.textAlignment = View.TEXT_ALIGNMENT_CENTER
}

fun View.snackIndefinite(@StringRes message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
        .show()
    this.textAlignment = View.TEXT_ALIGNMENT_CENTER
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

fun View.snowSnackIndefiniteTop(@StringRes message: Int) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
    val layoutParams = FrameLayout.LayoutParams(snackBar.view.layoutParams)

    layoutParams.gravity = Gravity.TOP
    layoutParams.marginStart = 40
    layoutParams.marginEnd = 40
    snackBar.view.setPadding(0, 8, 0, 8)
    snackBar.view.layoutParams = layoutParams
    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackBar.show()
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
