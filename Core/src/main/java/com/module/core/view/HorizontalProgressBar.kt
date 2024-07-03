package com.module.core.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.module.core.R

@SuppressLint("ResourceType")
class HorizontalProgressBar(
    context: Context, attributeSet: AttributeSet?
) : View(context, attributeSet) {
    private var maxProgress = 100
    private var progressValue = 0
    private var useRoundRect = false
    private var colorBackground = 0
    private var colorProgress = 0
    private var rectF = RectF()
    private var paint: Paint = Paint(1)
    private var path = Path()

    init {
        if (attributeSet != null) {
            val obtainStyledAttributes = context.obtainStyledAttributes(
                attributeSet,
                intArrayOf(R.attr.hpb_colorBackground, R.attr.hpb_colorProgress, R.attr.hpb_progress, R.attr.hpb_useRoundRect),
                0,
                0
            )

            try {
                colorBackground = obtainStyledAttributes.getColor(0, 1683075321)
                colorProgress = obtainStyledAttributes.getColor(1, -12942662)
                progressValue = obtainStyledAttributes.getInt(2, 0)
                useRoundRect = obtainStyledAttributes.getBoolean(3, false)
            } finally {
                obtainStyledAttributes.recycle()
            }
        } else {
            colorBackground = 1683075321
            colorProgress = -12942662
        }

        paint.style = Paint.Style.FILL
    }

    var progress: Int
        get() = progressValue
        set(value) {
            if (progressValue != value) {
                progressValue = 0.coerceAtLeast(value).coerceAtMost(maxProgress)
                invalidate()
            }
        }

    public override fun onDraw(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()

        val process = (progressValue.toFloat() / maxProgress.toFloat() * width).toInt()
        if (useRoundRect) {
            paint.color = colorBackground

            rectF.left = 0.0f
            rectF.top = 0.0f
            rectF.right = width
            rectF.bottom = height
            canvas.drawRoundRect(rectF, height / 2, height / 2, paint)
            path.addRect(0.0f, 0.0f, process.toFloat(), height, Path.Direction.CW)
            canvas.clipPath(path)
            paint.color = colorProgress
            canvas.drawRoundRect(rectF, height / 2, height / 2, paint)
        } else {
            paint.color = colorBackground
            canvas.drawRect(0.0f, 0.0f, width, height, paint)
            paint.color = colorProgress
            canvas.drawRect(0.0f, 0.0f, progressValue.toFloat() / maxProgress.toFloat() * width, height, paint)
        }
    }

    override fun setBackgroundColor(newBackgoundColor: Int) {
        colorBackground = newBackgoundColor
    }

    fun setForegroundColor(newForegroundColor: Int) {
        colorProgress = newForegroundColor
    }

    fun setMax(i: Int) {
        if (maxProgress != i) {
            maxProgress = 1.coerceAtLeast(i)
            invalidate()
        }
    }

    fun setUseRoundRect(z: Boolean) {
        useRoundRect = z
    }
}