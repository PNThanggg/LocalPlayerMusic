package com.module.core.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.module.core.R
import kotlin.math.abs

class ShadowLayout : FrameLayout {
    private var h: Int = 1
    private var w: Int = 1

    private var mShadowColor = 0
        set(value) {
            field = value
            setBackgroundCompat(w, h)
        }

    private var mShadowRadius = 0f
        set(value) {
            field = value
            setBackgroundCompat(w, h)
        }

    private var mCornerRadius = 0f
        set(value) {
            field = value
            setBackgroundCompat(w, h)
        }

    private var mDx = 0f
        set(value) {
            field = value
            setBackgroundCompat(w, h)
        }

    private var mDy = 0f
        set(value) {
            field = value
            setBackgroundCompat(w, h)
        }

    private val mInvalidateShadowOnSizeChanged = true
    private var mForceInvalidateShadow = false

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    override fun getSuggestedMinimumWidth(): Int {
        return 0
    }

    override fun getSuggestedMinimumHeight(): Int {
        return 0
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && (background == null || mInvalidateShadowOnSizeChanged || mForceInvalidateShadow)) {
            mForceInvalidateShadow = false
            setBackgroundCompat(w, h)
        }
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        if (mForceInvalidateShadow) {
            mForceInvalidateShadow = false
            setBackgroundCompat(right - left, bottom - top)
        }
        w = right - left
        h = bottom - top
    }

    private fun initView(
        context: Context,
        attrs: AttributeSet?
    ) {
        if (attrs != null) {
            initAttributes(context, attrs)
        }
        val xPadding = (mShadowRadius + abs(mDx)).toInt()
        val yPadding = (mShadowRadius + abs(mDy)).toInt()
        setPadding(xPadding, yPadding, xPadding, yPadding)
    }

    private fun setBackgroundCompat(
        w: Int,
        h: Int
    ) {
        val shadowWidth = w + (mShadowRadius * 2).toInt()
        val shadowHeight = h + (mShadowRadius * 2).toInt()

        val shadowBitmap = createShadowBitmap(
            shadowWidth,
            shadowHeight,
            mCornerRadius,
            mShadowRadius,
            mDx,
            mDy,
            mShadowColor,
        )

        val drawable = BitmapDrawable(resources, shadowBitmap).apply {
            // set bounds so that bitmap is drawn in the center
            val left = mShadowRadius.toInt()
            val right = left + w
            val top = mShadowRadius.toInt()
            val bottom = top + h
            bounds = Rect(left, top, right, bottom)
        }

        // set the shadow drawable as background without changing the view size
        background = drawable
    }


    private fun initAttributes(
        context: Context,
        attrs: AttributeSet
    ) {
        val attr = getTypedArray(context, attrs, R.styleable.ShadowLayout)
        try {
            mCornerRadius = attr.getDimension(
                R.styleable.ShadowLayout_cornerRadius,
                resources.getDimension(R.dimen.def_corner_radius)
            )
            mShadowRadius = attr.getDimension(
                R.styleable.ShadowLayout_shadowRadius2,
                resources.getDimension(R.dimen.def_shadow_radius)
            )
            mDx = attr.getDimension(R.styleable.ShadowLayout_dx, 0f)
            mDy = attr.getDimension(R.styleable.ShadowLayout_dy, 0f)
            mShadowColor = attr.getColor(
                R.styleable.ShadowLayout_shadowColor,
                resources.getColor(R.color.black, context.theme)
            )
        } finally {
            attr.recycle()
        }
    }

    private fun getTypedArray(
        context: Context,
        attributeSet: AttributeSet,
        attr: IntArray
    ): TypedArray {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
    }

    private fun createShadowBitmap(
        shadowWidth: Int,
        shadowHeight: Int,
        cornerRadius: Float,
        shadowRadius: Float,
        dx: Float,
        dy: Float,
        shadowColor: Int,
    ): Bitmap {
        val output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val shadowRect = RectF(
            shadowRadius,
            shadowRadius,
            shadowWidth - shadowRadius,
            shadowHeight - shadowRadius
        )

        if (dy > 0) {
            shadowRect.top += dy
            shadowRect.bottom -= dy
        } else if (dy < 0) {
            shadowRect.top += abs(dy)
            shadowRect.bottom -= abs(dy)
        }

        if (dx > 0) {
            shadowRect.left += dx
            shadowRect.right -= dx
        } else if (dx < 0) {
            shadowRect.left += abs(dx)
            shadowRect.right -= abs(dx)
        }

        val shadowPaint = Paint()
        shadowPaint.isAntiAlias = true
        shadowPaint.color = Color.TRANSPARENT
        shadowPaint.style = Paint.Style.FILL
        if (!isInEditMode) {
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor)
        }
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint)
        return output
    }
}