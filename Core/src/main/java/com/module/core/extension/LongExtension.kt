package com.module.core.extension

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

fun Long.toSize(): String {
    if (this <= 0) return "0"
    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#.00").format(this / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}

fun Long.toDuration(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

fun Long.formatDateToHumanReadable(): String? {
    return SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(Date(this))
}

fun Long.getFormattedDuration(forceShowHours: Boolean = false): String {
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
