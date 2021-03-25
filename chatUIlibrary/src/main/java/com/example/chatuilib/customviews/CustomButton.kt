package com.example.chatuilib.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.example.chatuilib.R

/**
 * This is Custom Button Class extended from AppCompatButton Class of Android
 * Using @property setCustomFont we can set Custom Fonts to text of Buttons
 */
class CustomButton : AppCompatButton {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        customAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        customAttr(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun customAttr(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton)
        val customFont = a.getString(R.styleable.CustomButton_customButtonFont)
        setCustomFont(customFont)
        a.recycle()
    }

    private fun setCustomFont(fontName: String?) {
        var myTypeface: Typeface? = null
        try {
            if (!fontName.isNullOrBlank())
                myTypeface = Typeface.createFromAsset(context.assets, "fonts/$fontName")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        typeface = myTypeface
    }
}
