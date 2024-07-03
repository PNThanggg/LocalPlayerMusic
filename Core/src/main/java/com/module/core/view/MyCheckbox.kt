package com.module.core.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.module.core.R
import com.module.core.extension.adjustAlpha

class MyCheckbox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatCheckBox(context, attrs, defStyleAttr) {

    init {
        buttonDrawable = ContextCompat.getDrawable(context, R.drawable.ic_checkbox_not_selected)

        setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setCheckedWithAnimation()
            } else {
                setNotCheckedWithAnimation()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun setColors(textColor: Int, accentColor: Int) {
        setTextColor(textColor)
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ),
            intArrayOf(textColor.adjustAlpha(0.6f), accentColor)
        )
        supportButtonTintList = colorStateList
    }

    fun setNotCheckedWithAnimation() {
        buttonDrawable = ContextCompat.getDrawable(context, R.drawable.ic_checkbox_not_selected)
    }

    fun setCheckedWithAnimation() {
        buttonDrawable = ContextCompat.getDrawable(context, R.drawable.ic_checkbox_selected)
    }
}
