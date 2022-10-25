package com.example.es.utils

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showSnackLong(@StringRes message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .show()
    this.textAlignment = View.TEXT_ALIGNMENT_CENTER
}

fun View.showSnackIndefinite(message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
        .show()
    this.textAlignment = View.TEXT_ALIGNMENT_CENTER
}
