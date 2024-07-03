package com.module.core.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.module.core.R
import com.module.core.extension.adjustAlpha
import com.module.core.extension.applyColorFilter
import com.module.core.utils.CoreConstants.MEDIUM_ALPHA

class MyEditText : AppCompatEditText {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        val fontRes: Int = R.font.poppins_medium_500
        typeface = ResourcesCompat.getFont(context, fontRes)
    }

    fun setColors(textColor: Int, accentColor: Int) {
        background?.mutate()?.applyColorFilter(accentColor)

        setTextColor(textColor)
        setHintTextColor(textColor.adjustAlpha(MEDIUM_ALPHA))
        setLinkTextColor(accentColor)
    }
}
