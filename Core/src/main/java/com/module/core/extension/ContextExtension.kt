package com.module.core.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Point
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.module.core.R
import com.module.core.utils.isTiramisuPlus

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

fun Context.showToast(id: Int, length: Int = Toast.LENGTH_LONG) {
    showToast(getString(id), length)
}

fun Context.getFileName(uri: Uri): String {
    var name = "Unknown"
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0) {
                name = it.getString(nameIndex)
            }
        }
    }
    return name
}

fun Context.getFileSize(uri: Uri): Long {
    var fileSize: Long = 0
    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            if (sizeIndex != -1) {
                fileSize = it.getLong(sizeIndex)
            }
        }
    }
    return fileSize
}

fun Context.getFileDuration(uri: Uri): Long {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(this, uri)
        val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        durationString?.toLong() ?: 0
    } catch (e: Exception) {
        0
    } finally {
        retriever.release()
    }
}

fun Context.showToast(msg: String, length: Int = Toast.LENGTH_LONG) {
    try {
        if (isOnMainThread()) {
            doToast(this, msg, length)
        } else {
            Handler(Looper.getMainLooper()).post {
                doToast(this, msg, length)
            }
        }
    } catch (e: Exception) {
        Log.e("App", "toast: ${e.message}")
    }
}

@SuppressLint("StringFormatInvalid")
fun Context.showErrorToast(msg: String, length: Int = Toast.LENGTH_LONG) {
    showToast(String.format(getString(R.string.error), msg), length)
}

fun Context.showErrorToast(exception: Exception, length: Int = Toast.LENGTH_LONG) {
    showErrorToast(exception.toString(), length)
}

private fun doToast(context: Context, message: String, length: Int) {
    if (context is Activity) {
        if (!context.isFinishing && !context.isDestroyed) {
            Toast.makeText(context, message, length).show()
        }
    } else {
        Toast.makeText(context, message, length).show()
    }
}

val Context.actionBarHeight: Int
    get() {
        val styledAttributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val actionBarHeight = styledAttributes.getDimension(0, 0f)
        styledAttributes.recycle()
        return actionBarHeight.toInt()
    }

val Context.windowManager: WindowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

@Suppress("DEPRECATION")
val Context.usableScreenSize: Point
    get() {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        return size
    }

@Suppress("DEPRECATION")
val Context.realScreenSize: Point
    get() {
        val size = Point()
        windowManager.defaultDisplay.getRealSize(size)
        return size
    }

fun Context.getProperBackgroundColor() = resources.getColor(R.color.you_background_color, theme)

fun Context.sendEmail(toEmail: String, feedBackString: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    val data: Uri = Uri.parse(
        "mailto:"
                + toEmail
                + "?subject=" + feedBackString + "&body=" + ""
    )
    intent.data = data
    try {
        startActivity(intent)
    } catch (ex: Exception) {
        showToast("Not have email app to send email!")
        ex.printStackTrace()
    }
}

fun Context.shareApp() {
    val subject = "Install app: "
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    val shareBody = "https://play.google.com/store/apps/details?id=$packageName"
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
    this.startActivity(Intent.createChooser(sharingIntent, "Share to"))
}

fun Context.openDeviceSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }

    try {
        startActivity(intent)
    } catch (e: Exception) {
        showErrorToast(e)
    }
}

fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasReadAudioPermission(): Boolean {
    return if (isTiramisuPlus()) {
        checkPermission(android.Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}