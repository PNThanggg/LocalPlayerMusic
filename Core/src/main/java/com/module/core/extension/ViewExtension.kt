package com.module.core.extension

import android.os.SystemClock
import android.view.View

fun View.toVisible(duration: Long = 300) {
    this.alpha = 0f

    this.visibility = View.VISIBLE

    this.animate().alpha(1f).setDuration(duration).start()
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.toGone(duration: Long = 300) {
    this.animate().alpha(0f).setDuration(duration).withEndAction {
        this.visibility = View.GONE
    }.start()
}

fun View.disableView() {
    this.isClickable = false
    this.postDelayed({ this.isClickable = true }, 300)
}

class SafeClickListener(
    private val interval: Int = 300,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {
    private var lastClickTime: Long = 0L

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < interval) {
            return
        }

        lastClickTime = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }
}

fun View.setOnSafeClick(
    onSafeClick: (View) -> Unit
) {
    setOnClickListener(SafeClickListener { v ->
        onSafeClick(v)
    })
}

fun View.setOnSafeClickListener(
    interval: Int,
    onSafeClick: (View) -> Unit
) {
    setOnClickListener(SafeClickListener(interval) { v ->
        onSafeClick(v)
    })
}
