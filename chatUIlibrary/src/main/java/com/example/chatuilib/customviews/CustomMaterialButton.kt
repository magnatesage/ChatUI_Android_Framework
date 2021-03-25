package com.example.chatuilib.customviews

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import com.example.chatuilib.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.AbsoluteCornerSize
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.RelativeCornerSize

/**
 * This is Custom Material Button Class extended from Material Button Class of Material Designing
 * Using @property setCustomFont we can set Custom Fonts to text of Material Buttons
 * This class is mainly used for making custom shapes of buttons.
 */
class CustomMaterialButton : MaterialButton {
    private var cornerFamily: Int = CornerFamily.ROUNDED
    private var allCornerSize: CornerSize = AbsoluteCornerSize(0F)
    private var topLeftCornerSize: CornerSize = allCornerSize
    private var topRightCornerSize: CornerSize = allCornerSize
    private var bottomRightCornerSize: CornerSize = allCornerSize
    private var bottomLeftCornerSize: CornerSize = allCornerSize

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

    private fun customAttr(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomMaterialButton)
        val customFont = a.getString(R.styleable.CustomMaterialButton_customMaterialButtonFont)
        setCustomFont(customFont)

        cornerFamily =
            a.getInteger(R.styleable.CustomMaterialButton_buttonAllCornerFamily, CornerFamily.ROUNDED)

        allCornerSize = getCornerSize(
            a,
            R.styleable.CustomMaterialCardView_allCornerSize,
            AbsoluteCornerSize(0F)
        )

        topLeftCornerSize = getCornerSize(
            a,
            R.styleable.CustomMaterialCardView_topLeftCornerSize,
            allCornerSize
        )
        topRightCornerSize = getCornerSize(
            a,
            R.styleable.CustomMaterialCardView_topRightCornerSize,
            allCornerSize
        )
        bottomLeftCornerSize = getCornerSize(
            a,
            R.styleable.CustomMaterialCardView_bottomLeftCornerSize,
            allCornerSize
        )
        bottomRightCornerSize = getCornerSize(
            a,
            R.styleable.CustomMaterialCardView_bottomRightCornerSize,
            allCornerSize
        )
        
        updateMaterialButton()
        a.recycle()
    }

    private fun getCornerSize(
        a: TypedArray, index: Int, defaultValue: CornerSize
    ): CornerSize {
        val value = a.peekValue(index) ?: return defaultValue
        return if (value.type == TypedValue.TYPE_DIMENSION) {
            AbsoluteCornerSize(
                TypedValue.complexToDimensionPixelSize(
                    value.data,
                    a.resources.displayMetrics
                ).toFloat()
            )
        } else if (value.type == TypedValue.TYPE_FRACTION) {
            RelativeCornerSize(value.getFraction(1.0f, 1.0f))
        } else {
            defaultValue
        }
    }

    fun setCustomFont(fontName: String?) {
        var myTypeface: Typeface? = null
        try {
            if (!fontName.isNullOrBlank())
                myTypeface = Typeface.createFromAsset(context.assets, "fonts/" + fontName!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        typeface = myTypeface
    }

    fun setCornerFamily(cornerFamily: Int) {
        this.cornerFamily = cornerFamily
        updateMaterialButton()
    }

    fun setAllCorners(allCornerSize: Float) {
        this.allCornerSize = AbsoluteCornerSize(allCornerSize)
        this.topLeftCornerSize = AbsoluteCornerSize(allCornerSize)
        this.topRightCornerSize = AbsoluteCornerSize(allCornerSize)
        this.bottomLeftCornerSize = AbsoluteCornerSize(allCornerSize)
        this.bottomRightCornerSize = AbsoluteCornerSize(allCornerSize)
        updateMaterialButton()
    }

    fun setAllCornersInPercent(allCornerSize: Float) {
        this.allCornerSize = RelativeCornerSize(allCornerSize)
        this.topLeftCornerSize = RelativeCornerSize(allCornerSize)
        this.topRightCornerSize = RelativeCornerSize(allCornerSize)
        this.bottomLeftCornerSize = RelativeCornerSize(allCornerSize)
        this.bottomRightCornerSize = RelativeCornerSize(allCornerSize)
        updateMaterialButton()
    }

    fun setTopLeftCorner(topLeftCornerSize: Float) {
        this.topLeftCornerSize = AbsoluteCornerSize(topLeftCornerSize)
        updateMaterialButton()
    }

    fun setTopLeftCornerInPercent(topLeftCornerSize: Float) {
        this.topLeftCornerSize = RelativeCornerSize(topLeftCornerSize)
        updateMaterialButton()
    }

    fun setTopRightCorner(topRightCornerSize: Float) {
        this.topRightCornerSize = AbsoluteCornerSize(topRightCornerSize)
        updateMaterialButton()
    }

    fun setTopRightCornerInPercent(topRightCornerSize: Float) {
        this.topRightCornerSize = RelativeCornerSize(topRightCornerSize)
        updateMaterialButton()
    }

    fun setBottomLeftCorner(bottomLeftCornerSize: Float) {
        this.bottomLeftCornerSize = AbsoluteCornerSize(bottomLeftCornerSize)
        updateMaterialButton()
    }

    fun setBottomLeftCornerInPercent(bottomLeftCornerSize: Float) {
        this.bottomLeftCornerSize = RelativeCornerSize(bottomLeftCornerSize)
        updateMaterialButton()
    }

    fun setBottomRightCorner(bottomRightCornerSize: Float) {
        this.bottomRightCornerSize = AbsoluteCornerSize(bottomRightCornerSize)
        updateMaterialButton()
    }

    fun setBottomRightCornerInPercent(bottomRightCornerSize: Float) {
        this.bottomRightCornerSize = RelativeCornerSize(bottomRightCornerSize)
        updateMaterialButton()
    }

    private fun updateMaterialButton() {
        shapeAppearanceModel = shapeAppearanceModel.toBuilder()
            .setTopLeftCorner(cornerFamily, topLeftCornerSize)
            .setTopRightCorner(cornerFamily, topRightCornerSize)
            .setBottomLeftCorner(cornerFamily, bottomLeftCornerSize)
            .setBottomRightCorner(cornerFamily, bottomRightCornerSize)
            .build()
    }
}
