package com.module.core.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable

@SuppressLint("UseCompatLoadingForDrawables")
fun Resources.getColoredBitmap(
    context: Context, resourceId: Int, newColor: Int
): Bitmap {
    val drawable = getDrawable(resourceId, context.theme)
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.applyColorFilter(newColor)
    drawable.draw(canvas)
    return bitmap
}

fun Resources.getColoredDrawable(
    context: Context, drawableId: Int, colorId: Int, alpha: Int = 255
) = getColoredDrawableWithColor(
    context = context, drawableId = drawableId, color = getColor(colorId, context.theme), alpha = alpha
)

@SuppressLint("UseCompatLoadingForDrawables")
fun Resources.getColoredDrawableWithColor(context: Context, drawableId: Int, color: Int, alpha: Int = 255): Drawable {
    val drawable = getDrawable(drawableId, context.theme)
    drawable.mutate().applyColorFilter(color)
    drawable.mutate().alpha = alpha
    return drawable
}

fun Resources.hasNavBar(): Boolean {
    val id = getIdentifier("config_showNavigationBar", "bool", "android")
    return id > 0 && getBoolean(id)
}

@SuppressLint("InternalInsetResource")
fun Resources.getNavBarHeight(): Int {
    val id = getIdentifier("navigation_bar_height", "dimen", "android")
    return if (id > 0 && hasNavBar()) {
        getDimensionPixelSize(id)
    } else 0
}
