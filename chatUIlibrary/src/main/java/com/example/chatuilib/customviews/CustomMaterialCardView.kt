package com.example.chatuilib.customviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import com.example.chatuilib.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.AbsoluteCornerSize
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.RelativeCornerSize

class CustomMaterialCardView : MaterialCardView {
    private var cornerFamily: Int = CornerFamily.ROUNDED
    private var allCornerSize: CornerSize = AbsoluteCornerSize(0F)
    private var topLeftCornerSize: CornerSize = allCornerSize
    private var topRightCornerSize: CornerSize = allCornerSize
    private var bottomRightCornerSize: CornerSize = allCornerSize
    private var bottomLeftCornerSize: CornerSize = allCornerSize

    constructor(context: Context?) : super(context) {}

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
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomMaterialCardView)
        cornerFamily =
            a.getInteger(R.styleable.CustomMaterialCardView_allCornerFamily, CornerFamily.ROUNDED)

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
        updateCardView()
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

    fun setCornerFamily(cornerFamily: Int) {
        this.cornerFamily = cornerFamily
        updateCardView()
    }

    fun setAllCorners(allCornerSize: Float) {
        this.allCornerSize = AbsoluteCornerSize(allCornerSize)
        this.topLeftCornerSize = AbsoluteCornerSize(allCornerSize)
        this.topRightCornerSize = AbsoluteCornerSize(allCornerSize)
        this.bottomLeftCornerSize = AbsoluteCornerSize(allCornerSize)
        this.bottomRightCornerSize = AbsoluteCornerSize(allCornerSize)
        updateCardView()
    }

    fun setAllCornersInPercent(allCornerSize: Float) {
        this.allCornerSize = RelativeCornerSize(allCornerSize)
        this.topLeftCornerSize = RelativeCornerSize(allCornerSize)
        this.topRightCornerSize = RelativeCornerSize(allCornerSize)
        this.bottomLeftCornerSize = RelativeCornerSize(allCornerSize)
        this.bottomRightCornerSize = RelativeCornerSize(allCornerSize)
        updateCardView()
    }

    fun setTopLeftCorner(topLeftCornerSize: Float) {
        this.topLeftCornerSize = AbsoluteCornerSize(topLeftCornerSize)
        updateCardView()
    }

    fun setTopLeftCornerInPercent(topLeftCornerSize: Float) {
        this.topLeftCornerSize = RelativeCornerSize(topLeftCornerSize)
        updateCardView()
    }

    fun setTopRightCorner(topRightCornerSize: Float) {
        this.topRightCornerSize = AbsoluteCornerSize(topRightCornerSize)
        updateCardView()
    }

    fun setTopRightCornerInPercent(topRightCornerSize: Float) {
        this.topRightCornerSize = RelativeCornerSize(topRightCornerSize)
        updateCardView()
    }

    fun setBottomLeftCorner(bottomLeftCornerSize: Float) {
        this.bottomLeftCornerSize = AbsoluteCornerSize(bottomLeftCornerSize)
        updateCardView()
    }

    fun setBottomLeftCornerInPercent(bottomLeftCornerSize: Float) {
        this.bottomLeftCornerSize = RelativeCornerSize(bottomLeftCornerSize)
        updateCardView()
    }

    fun setBottomRightCorner(bottomRightCornerSize: Float) {
        this.bottomRightCornerSize = AbsoluteCornerSize(bottomRightCornerSize)
        updateCardView()
    }

     fun setBottomRightCornerInPercent(bottomRightCornerSize: Float) {
        this.bottomRightCornerSize = RelativeCornerSize(bottomRightCornerSize)
        updateCardView()
    }

    private fun updateCardView() {
        shapeAppearanceModel = shapeAppearanceModel.toBuilder()
            .setTopLeftCorner(cornerFamily, topLeftCornerSize)
            .setTopRightCorner(cornerFamily, topRightCornerSize)
            .setBottomLeftCorner(cornerFamily, bottomLeftCornerSize)
            .setBottomRightCorner(cornerFamily, bottomRightCornerSize)
            .build()
    }
}