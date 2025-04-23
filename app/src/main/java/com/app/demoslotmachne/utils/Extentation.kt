package com.app.demoslotmachne.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.app.demoslotmachne.R


fun TextView.fadeText(newText: String, duration: Long = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                text = newText
                animate()
                    .alpha(1f)
                    .setDuration(duration)
                    .setListener(null)
            }
        })
}

fun TextView.fadeIn(duration: Long = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .setListener(null)
}

fun spToPx(context: Context, sp: Float): Int {
    val scale = context.resources.displayMetrics.scaledDensity
    return (sp * scale + 0.5f).toInt()
}

fun openChromeTab(context: Context, url: String) {
    val colorInt = ContextCompat.getColor(context, R.color.btn_color) // your toolbar color

    val customTabsIntent = CustomTabsIntent.Builder()
        .setToolbarColor(colorInt)
        .setShowTitle(true)
        .build()

    customTabsIntent.intent.setPackage("com.android.chrome") // Force Chrome
    customTabsIntent.launchUrl(context, Uri.parse(url))
}