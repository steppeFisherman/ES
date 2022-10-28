package com.example.es.utils

import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener

typealias Extract = (extractedValue: String) -> Unit

class InputMaskHandle(
    field: EditText,
    listener: ValueListener = ValueListener {},
) : MaskedTextChangedListener
    (
    field = field,
    primaryFormat = "+7 ([000]) [000]-[00]-[00]",
    affineFormats = LIST_AFFINE_FORMATS,
    valueListener = listener
) {

    companion object {
        private val LIST_AFFINE_FORMATS: List<String> = mutableListOf(
            "+1 ([000]) [000]-[00]-[00]",
            "+2 ([000]) [000]-[00]-[00]",
            "+3 ([000]) [000]-[00]-[00]",
            "+4 ([000]) [000]-[00]-[00]",
            "+5 ([000]) [000]-[00]-[00]",
            "+6 ([000]) [000]-[00]-[00]",
            "+8 ([000]) [000]-[00]-[00]",
            "+9 ([000]) [000]-[00]-[00]"
        )
    }
}

class ValueListener(val extractedValue: Extract) : MaskedTextChangedListener.ValueListener {
    override fun onTextChanged(
        maskFilled: Boolean,
        extractedValue: String,
        formattedValue: String
    ) {
        extractedValue(extractedValue)
    }
}









