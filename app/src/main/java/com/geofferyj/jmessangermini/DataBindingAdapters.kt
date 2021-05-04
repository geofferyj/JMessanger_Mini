package com.geofferyj.jmessangermini

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter


@BindingAdapter("android:background")
fun setBG(view: View, bg: Int) {
    view.setBackgroundResource(bg)
}

@BindingAdapter("android:src")
fun setImage(imageView: ImageView, src: Int) {
    imageView.setImageResource(src)
}


@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    "paddingImeSystemWindowInsets",
    requireAll = false
)
fun applySystemWindows(
    view: View,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean,
    applyIme: Boolean
) {
    view.doOnApplyWindowInsets { v, insets, padding ->
        val left = if (applyLeft) insets.getInsets(WindowInsetsCompat.Type.systemBars()).left else 0
        val top = if (applyTop) insets.getInsets(WindowInsetsCompat.Type.systemBars()).top else 0
        val right =
            if (applyRight) insets.getInsets(WindowInsetsCompat.Type.systemBars()).right else 0
        val bottom =
            if (applyBottom) insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom else 0
        val ime = if (applyIme) insets.getInsets(WindowInsetsCompat.Type.ime()).bottom else 0

        v.setPadding(
            padding.left + left,
            padding.top + top,
            padding.right + right,
            padding.bottom + if (ime != 0) ime else bottom
        )
    }
}


fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, InitialPadding) -> Unit) {
    // Create a snapshot of the view's padding state
    val initialPadding = recordInitialPaddingForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View){

            }
        })
    }
}

data class InitialPadding(
    val left: Int, val top: Int,
    val right: Int, val bottom: Int
)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)


@BindingAdapter("android:layout_alignParentEnd")
fun setLayoutAlignParentEnd(view: View, value: Boolean) {
    val params = view.layoutParams as RelativeLayout.LayoutParams
    val check = if (value) RelativeLayout.TRUE else 0
    params.addRule(RelativeLayout.ALIGN_PARENT_END, check)


    view.layoutParams = params
}