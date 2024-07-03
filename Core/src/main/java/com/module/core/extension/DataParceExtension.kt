package com.module.core.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.module.core.utils.isTiramisuPlus
import java.io.Serializable

inline fun <reified T : Serializable> Bundle.getDataSerializable(key: String, clazz: Class<T>): T? {
    return if (isTiramisuPlus()) {
        getSerializable(key, clazz)
    } else {
        @Suppress("DEPRECATION") getSerializable(key) as? T
    }
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    isTiramisuPlus() -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    isTiramisuPlus() -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Activity.getParcelable(): T? = when {
    isTiramisuPlus() -> intent?.extras?.getParcelable(T::class.java.name, T::class.java)
    else -> @Suppress("DEPRECATION") intent?.extras?.getParcelable(T::class.java.name) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    isTiramisuPlus() -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    isTiramisuPlus() -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

inline fun <reified T> pushBundle(data: T): Bundle {
    val bundle = Bundle()
    when (data) {
        is Long -> bundle.putLong(T::class.java.name, data)
        is Int -> bundle.putInt(T::class.java.name, data)
        is Float -> bundle.putFloat(T::class.java.name, data)
        is Boolean -> bundle.putBoolean(T::class.java.name, data)
        is String -> bundle.putString(T::class.java.name, data.toString())
        is Parcelable -> bundle.putParcelable(T::class.java.name, data)
        is Serializable -> bundle.putSerializable(T::class.java.name, data)
        else -> bundle.putString(T::class.java.name, data.toString())
    }
    return bundle
}

fun <T> pushBundle(key: String, data: T): Bundle {
    val bundle = Bundle()
    when (data) {
        is Long -> bundle.putLong(key, data)
        is Int -> bundle.putInt(key, data)
        is Float -> bundle.putFloat(key, data)
        is Boolean -> bundle.putBoolean(key, data)
        is String -> bundle.putString(key, data.toString())
        is Parcelable -> bundle.putParcelable(key, data)
        is Serializable -> bundle.putSerializable(key, data)
        else -> bundle.putString(key, data.toString())
    }
    return bundle
}