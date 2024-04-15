package com.example.es.utils

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.example.es.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun showToast(context: Context, message: Int) {
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

fun View.snackIndefinite(message: String) {
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

fun View.visible(show: Boolean) =
    if (show) this.visibility = View.VISIBLE else this.visibility = View.GONE

fun longToStringDateFormat(time: Long) {
//    val dateString = DateFormat.getDateTimeInstance().format(time)
//    val format = SimpleDateFormat("HH:mm:ss, dd.MM.yyyy", Locale.getDefault())
//    return format.format(time).toString()
}

fun dialogLocationShow(
    context: Context,
    title: Int = R.string.location_mode_disabled,
    message: Int = R.string.location_enable_all_sources_message,
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ ->
            val intent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(context, intent, null)
        }
        .create()
        .show()
}

fun dialogShow(
    context: Context,
    title: String = "",
    message: String = "",
    positiveButtonBlock: () -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ ->
            positiveButtonBlock()
        }
        .create()
        .show()
}

fun dialogShow(
    context: Context,
    title: Int,
    message: Int,
    positiveButtonBlock: () -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ ->
            positiveButtonBlock()
        }
        .create()
        .show()
}

fun View.customLongClickListener(
    duration: Long,
    listener: () -> Unit
) {
    setOnTouchListener(object : View.OnTouchListener {

        private val handler = Handler(Looper.getMainLooper())

        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            view?.performClick()
            if (event?.action == MotionEvent.ACTION_DOWN) {
                handler.postDelayed({ listener.invoke() }, duration)
            } else if (event?.action == MotionEvent.ACTION_UP) {
                handler.removeCallbacksAndMessages(null)
            }
            return true
        }
    })
}
