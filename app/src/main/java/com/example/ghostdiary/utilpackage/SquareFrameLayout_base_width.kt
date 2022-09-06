package com.example.ghostdiary.utilpackage

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class SquareFrameLayout_base_width : FrameLayout {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 여기가 핵심!
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
//        super.onMeasure(heightMeasureSpec,heightMeasureSpec )
    }
}
