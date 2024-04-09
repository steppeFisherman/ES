package com.example.es.utils

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.example.es.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

interface SnackBuilder {

    fun snackBarDefault(
        view: View,
        message: Int,
        snackShape: Int = R.drawable.snack_background_shape_default,
    ): Snackbar

    fun snackBarWithPadding(
        view: View,
        message: Int,
        snackShape: Int = R.drawable.snack_background_shape_padding,
    ): Snackbar

    fun buildSnack(
        view: View,
        message: Int,
        duration: Int,
        gravity: Int,
        snackShape: Int,
    ): Snackbar =
        Snackbar.make(view, message, duration).apply {
            val layoutParams = FrameLayout.LayoutParams(this.view.layoutParams)
            layoutParams.gravity = gravity
            this.view.layoutParams = layoutParams
            this.view.setBackgroundResource(snackShape)
        }

    class SnackIndefiniteFadeMode(private val durationIndefinite: Int = Snackbar.LENGTH_INDEFINITE) :
        SnackBuilder {
        override fun snackBarDefault(
            view: View,
            message: Int,
            snackShape: Int,
        ): Snackbar =
            buildSnack(view, message, durationIndefinite, Gravity.CENTER, snackShape).apply {
                this.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
            }

        override fun snackBarWithPadding(
            view: View,
            message: Int,
            snackShape: Int,
        ): Snackbar =
            buildSnack(view, message, durationIndefinite, Gravity.CENTER, snackShape).apply {
                this.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
            }
    }

    class SnackIndefiniteSlideMode(private val durationIndefinite: Int = Snackbar.LENGTH_INDEFINITE) :
        SnackBuilder {
        override fun snackBarDefault(
            view: View,
            message: Int,
            snackShape: Int,
        ): Snackbar =
            buildSnack(view, message, durationIndefinite, Gravity.CENTER, snackShape).apply {
                this.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            }

        override fun snackBarWithPadding(
            view: View,
            message: Int,
            snackShape: Int,
        ): Snackbar =
            buildSnack(view, message, durationIndefinite, Gravity.CENTER, snackShape).apply {
                this.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            }
    }

    class SnackLong(private val durationLong: Int = Snackbar.LENGTH_LONG) : SnackBuilder {
        override fun snackBarDefault(
            view: View,
            message: Int,
            snackShape: Int,
        ): Snackbar =
            buildSnack(view, message, durationLong, Gravity.BOTTOM, snackShape)

        override fun snackBarWithPadding(
            view: View,
            message: Int,
            snackShape: Int,
        ): Snackbar =
            buildSnack(view, message, durationLong, Gravity.BOTTOM, snackShape)
    }

    class SnackShort(private val durationShort: Int = Snackbar.LENGTH_SHORT) : SnackBuilder {
        override fun snackBarDefault(
            view: View,
            message: Int,
            snackShape: Int,
        ): Snackbar =
            buildSnack(view, message, durationShort, Gravity.BOTTOM, snackShape)

        override fun snackBarWithPadding(
            view: View,
            message: Int,
            snackShape: Int,
        ): Snackbar =
            buildSnack(view, message, durationShort, Gravity.BOTTOM, snackShape)
    }
}