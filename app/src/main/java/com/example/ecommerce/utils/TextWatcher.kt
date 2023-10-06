package com.example.ecommerce.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.ParseException

class NumberTextWatcher(private val editText: EditText) : TextWatcher {
    private val decimalFormat = DecimalFormat("#.###")
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        editText.removeTextChangedListener(this)

        try {
            val originalString = editable.toString()
            val longVal = decimalFormat.parse(originalString)?.toLong() ?: 0
            val formattedString = decimalFormat.format(longVal)
            editText.setText(formattedString)
            editText.setSelection(formattedString.length)
        } catch (e: ParseException) {
            // Error handling jika terjadi kesalahan parsing
        }
        editText.addTextChangedListener(this)
    }


}