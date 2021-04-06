package com.example.chatuilib.customviews

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView

class CustomShapeableImageView : ShapeableImageView {

    var clickListener: OnClickListener? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun setOnClickListener(l: OnClickListener?) {
        clickListener = l
        super.setOnClickListener(l)
    }
}