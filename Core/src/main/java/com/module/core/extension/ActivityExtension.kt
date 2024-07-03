package com.module.core.extension

import android.app.Activity
import android.app.Activity.OVERRIDE_TRANSITION_CLOSE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.module.core.R
import com.module.core.utils.CoreConstants
import com.module.core.utils.isRPlus
import com.module.core.utils.isTiramisuPlus
import com.module.core.utils.isUpsideDownCakePlus

/**
 * Ends the current activity with a sliding animation.
 *
 * This method closes the current activity and applies a slide transition animation. The specific animation
 * applied depends on the result of the `com.module.core.utils.isUpsideDownCakePlus` check:
 *
 * - If `com.module.core.utils.isUpsideDownCakePlus` returns true, a custom transition defined by `overrideActivityTransition`
 *   is applied. This transition uses the specified animations for closing the activity and supports
 *   additional customization such as changing the background color during the transition.
 * - If `com.module.core.utils.isUpsideDownCakePlus` returns false, the method falls back to using the deprecated
 *   `overridePendingTransition` method to apply a slide animation. This is necessary for compatibility
 *   with older versions of Android that do not support the newer transition methods.
 *
 * The method makes use of `@Suppress("DEPRECATION")` to avoid warnings about the deprecated
 * `overridePendingTransition` method, acknowledging that it's intentionally used for backward compatibility.
 *
 * @see `com.module.core.utils.isUpsideDownCakePlus` A method (presumably checking for a specific Android version or a custom condition).
 * @see `overrideActivityTransition` A custom method to override the activity transition with more options.
 */
fun Activity.finishWithSlide() {
    finish()
    if (isUpsideDownCakePlus()) {
        overrideActivityTransition(
            OVERRIDE_TRANSITION_CLOSE, R.anim.slide_in_left, R.anim.slide_out_right, Color.TRANSPARENT
        )
    } else {
        @Suppress("DEPRECATION")
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

/**
 * Hides the status bar and action bar in the activity.
 *
 * This method adjusts to the best practice based on the Android version of the device.
 * For devices running Android 11 (API level 30) and above, WindowInsetsController is used
 * to hide the status bars, which replaces the deprecated systemUiVisibility method. It also
 * sets the systemBarsBehavior to BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE allowing the status bars
 * to temporarily show when swiped from the edge. For compatibility with older Android versions,
 * the deprecated systemUiVisibility approach is used to hide the status bar, and the actionBar
 * is hidden using its hide() method.
 *
 * Note: Always ensure that the action bar is hidden if the status bar is also hidden to maintain
 * UI consistency and to adhere to Android's UI guidelines.
 */
fun Activity.hideStatusBar() {
    if (isRPlus()) {
        val controller = window.insetsController
        controller?.hide(WindowInsets.Type.statusBars())
        controller?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Ẩn action bar
        actionBar?.hide()
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }
}

/**
 * Makes the status bar transparent and allows the content to display under the system bars.
 *
 * This function hides the status bar and sets its color to transparent, enabling the application's content
 * to extend behind the system bars, providing a more immersive user experience. The system bars can still be
 * accessed by swiping from the edges of the screen.
 *
 * @receiver Activity The activity within which the system UI modifications are to be applied.
 */
fun Activity.makeStatusBarTransparentAndContentDisplayUnderneath() {
    WindowCompat.setDecorFitsSystemWindows(window, false)  // Allow content to display under status and navigation bars
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        // Uncomment the following line if you want to hide the status bar
        // controller.hide(WindowInsetsCompat.Type.statusBars())

        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.statusBarColor = Color.TRANSPARENT  // Make status bar transparent
    }
}

/**
 * Updates the status bar color and adjusts the appearance based on contrast.
 *
 * This function sets the color of the status bar to the specified color and adjusts the appearance
 * of the status bar content (icons and text) based on the contrast color of the provided color. For
 * Android R (API 30) and above, it uses the `WindowInsetsController` to set the system bar appearance,
 * and for older versions, it manipulates the `systemUiVisibility` flags.
 *
 * @receiver Activity The activity where the status bar color is being updated.
 * @param color The color to set for the status bar.
 */
fun Activity.updateStatusBarColor(color: Int) {
    window.statusBarColor = color

    if (isRPlus()) {
        val controller = window.insetsController
        if (color.getContrastColor() == CoreConstants.DARK_GREY) {
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            controller?.setSystemBarsAppearance(
                0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    } else {
        @Suppress("DEPRECATION")
        if (color.getContrastColor() == CoreConstants.DARK_GREY) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.addBit(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility.removeBit(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }
}

fun Activity.registerBroadcastReceiver(
    broadcastReceiver: BroadcastReceiver,
    intentFilter: IntentFilter,
    isExported: Boolean = true
) {
    if (isTiramisuPlus()) {
        val flag = if (isExported) {
            Context.RECEIVER_EXPORTED
        } else {
            Context.RECEIVER_NOT_EXPORTED
        }
        registerReceiver(
            broadcastReceiver,
            intentFilter,
            flag
        )
    } else {
        registerReceiver(broadcastReceiver, intentFilter)
    }
}