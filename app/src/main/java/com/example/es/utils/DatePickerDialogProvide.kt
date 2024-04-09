package com.example.es.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.es.R
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

interface DatePickerDialogProvide {

    fun provide(textView: TextView, block: (Date) -> Unit)

    class Base(val context: Context) : DatePickerDialogProvide {

        private val currentDateTime: Calendar = Calendar.getInstance()

        private val startYear = currentDateTime.get(Calendar.YEAR)
        private val startMonth = currentDateTime.get(Calendar.MONTH)
        private val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        override fun provide(textView: TextView, block: (Date) -> Unit) {
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val pickedDateTime = Calendar.getInstance()
                    pickedDateTime.set(Calendar.YEAR, year)
                    pickedDateTime.set(Calendar.MONTH, month)
                    pickedDateTime.set(Calendar.DAY_OF_MONTH, day)
                    pickedDateTime.set(Calendar.HOUR_OF_DAY, 0)
                    pickedDateTime.set(Calendar.MINUTE, 0)
                    pickedDateTime.set(Calendar.SECOND, 0)
                    pickedDateTime.set(Calendar.MILLISECOND, 0)

                    textView.setTextColor(ContextCompat.getColor(context, R.color.black_textView))
                    textView.text = DateFormat.getDateInstance().format(pickedDateTime.time)

                    block(pickedDateTime.time)
                },
                startYear,
                startMonth,
                startDay
            ).show()
        }
    }
}


