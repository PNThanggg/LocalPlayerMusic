package com.module.core.extension

import java.util.Locale

fun String.fileName(): String {
    return this.substring(this.lastIndexOf("/") + 1)
}

fun String?.capitalize(): String? {
    return this?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}