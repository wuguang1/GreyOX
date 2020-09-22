package com.deepblue.greyox.pop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout.LayoutParams
import android.widget.PopupWindow
import com.deepblue.greyox.R
import com.deepblue.greyox.item.DialogLeft
import com.mdx.framework.activity.BaseActivity
import kotlin.math.roundToInt


class PopShowSet(var context: Context, val view: View , popview: View) {
    private val popwindow: PopupWindow

    val isShow: Boolean
        get() = popwindow.isShowing

    init {
        (popview as DialogLeft).set(this)
        popwindow = PopupWindow(popview, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        popwindow.setBackgroundDrawable(BitmapDrawable(context.resources))
        popwindow.isTouchable = true
        popwindow.isOutsideTouchable = true
        popwindow.isFocusable = true
        popwindow.animationStyle = R.style.MyActivitySiwtchAnimation;
    }

    fun setOnDismissListener(l: PopupWindow.OnDismissListener) {
        popwindow.setOnDismissListener(l)
    }

    @SuppressLint("NewApi")
    fun show() {
        popwindow.showAsDropDown(view, 0, -view.height)
    }

    fun hide() {
        popwindow.dismiss()
    }


}
