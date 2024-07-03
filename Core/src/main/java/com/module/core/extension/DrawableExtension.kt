package com.module.core.extension

import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.module.core.utils.isQPlus

/**
 * Applies a color filter to this drawable.
 *
 * @param color The color to apply as a filter.
 *
 * This function applies a color filter to the drawable object. Depending on the Android API level,
 * it utilizes either a BlendModeColorFilter or a PorterDuffColorFilter for compatibility.
 *
 * @see BlendModeColorFilter
 * @see PorterDuffColorFilter
 * @see android.os.Build.VERSION.SDK_INT
 */
fun Drawable.applyColorFilter(color: Int) {
    mutate()
    colorFilter = if (isQPlus()) {
        BlendModeColorFilter(color, BlendMode.SRC_IN)
    } else {
        PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}

/**
 * Converts this drawable to a bitmap.
 *
 * @return The converted bitmap.
 *
 * This function converts the drawable to a bitmap. If the drawable's intrinsic width or height is
 * less than or equal to zero, a 1x1 ARGB_8888 bitmap is created. Otherwise, a bitmap with the
 * dimensions equal to the intrinsic width and height of the drawable is created.
 *
 * If the drawable is a BitmapDrawable and the underlying bitmap is not null, the function
 * directly returns the bitmap without further processing.
 *
 * @see Bitmap
 * @see Canvas
 * @see BitmapDrawable
 */
fun Drawable.convertToBitmap(): Bitmap {
    val bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    }

    if (this is BitmapDrawable) {
        if (this.bitmap != null) {
            return this.bitmap
        }
    }

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}
