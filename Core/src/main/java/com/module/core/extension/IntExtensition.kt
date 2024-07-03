package com.module.core.extension

import android.content.res.Resources
import android.graphics.Color
import com.module.core.utils.CoreConstants.DARK_GREY
import java.text.DecimalFormat
import java.util.Locale
import java.util.Random
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

fun Int.toHex() = String.format("#%06X", 0xFFFFFF and this).uppercase(Locale.ROOT)

fun Int.getContrastColor(): Int {
    val y = (299 * Color.red(this) + 587 * Color.green(this) + 114 * Color.blue(this)) / 1000
    return if (y >= 149 && this != Color.BLACK) DARK_GREY else Color.WHITE
}


fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt()
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)
    return Color.argb(alpha, red, green, blue)
}

fun Int.getFormattedDuration(forceShowHours: Boolean = false): String {
    val sb = StringBuilder(8)
    val hours = this / 3600
    val minutes = this % 3600 / 60
    val seconds = this % 60

    if (this >= 3600) {
        sb.append(String.format(Locale.getDefault(), "%02d", hours)).append(":")
    } else if (forceShowHours) {
        sb.append("0:")
    }

    sb.append(String.format(Locale.getDefault(), "%02d", minutes))
    sb.append(":").append(String.format(Locale.getDefault(), "%02d", seconds))
    return sb.toString()
}

fun Int.formatSize(): String {
    if (this <= 0) {
        return "0 B"
    }

    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (log10(toDouble()) / log10(1024.0)).toInt()
    return "${DecimalFormat("#,##0.#").format(this / 1024.0.pow(digitGroups.toDouble()))} ${units[digitGroups]}"
}

fun Int.addBitIf(add: Boolean, bit: Int) =
    if (add) {
        addBit(bit)
    } else {
        removeBit(bit)
    }

fun Int.removeBit(bit: Int) = addBit(bit) - bit

fun Int.addBit(bit: Int) = this or bit

fun Int.flipBit(bit: Int) = if (this and bit == 0) addBit(bit) else removeBit(bit)

fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start

fun Int.darkenColor(factor: Int = 8): Int {
    if (this == Color.WHITE || this == Color.BLACK) {
        return this
    }

    var hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    val hsl = hsv2hsl(hsv)
    hsl[2] -= factor / 100f
    if (hsl[2] < 0)
        hsl[2] = 0f
    hsv = hsl2hsv(hsl)
    return Color.HSVToColor(hsv)
}

fun Int.lightenColor(factor: Int = 8): Int {
    if (this == Color.WHITE || this == Color.BLACK) {
        return this
    }

    var hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    val hsl = hsv2hsl(hsv)
    hsl[2] += factor / 100f
    if (hsl[2] < 0)
        hsl[2] = 0f
    hsv = hsl2hsv(hsl)
    return Color.HSVToColor(hsv)
}

private fun hsl2hsv(hsl: FloatArray): FloatArray {
    val hue = hsl[0]
    var sat = hsl[1]
    val light = hsl[2]
    sat *= if (light < .5) light else 1 - light
    return floatArrayOf(hue, 2f * sat / (light + sat), light + sat)
}

private fun hsv2hsl(hsv: FloatArray): FloatArray {
    val hue = hsv[0]
    val sat = hsv[1]
    val value = hsv[2]

    val newHue = (2f - sat) * value
    var newSat = sat * value / if (newHue < 1f) newHue else 2f - newHue
    if (newSat > 1f)
        newSat = 1f

    return floatArrayOf(hue, newSat, newHue / 2f)
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Int.hours: Int
    get() = this / 3600

val Int.minutes: Int
    get() = (this % 3600) / 60

val Int.seconds: Int
    get() = this % 60

