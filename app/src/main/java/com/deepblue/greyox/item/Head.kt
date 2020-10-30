//
//  Head
//
//  Created by 86139 on 2020-09-09 16:13:58
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.item;

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.deepblue.greyox.Const
import com.deepblue.greyox.F.showMenu
import com.deepblue.greyox.R
import kotlinx.android.synthetic.main.item_head.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Head(context: Context?) : BaseItem(context) {
    private var animation = AlphaAnimation(1F, 0F)

    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_head, this)

        mImageButton_menu.setOnClickListener {
            showMenu(context!!, this@Head)
        }

        mImageButton_back.setOnClickListener {
            (context as Activity).finish()
        }

        animation.duration = 500 // duration - half a second
        animation.interpolator = LinearInterpolator() // do not alter animation rate
        animation.repeatCount = Animation.INFINITE // Repeat animation infinitely
        animation.repeatMode = Animation.REVERSE //
//        iv_chat_head.animation = animation
    }

    fun setTitle(title: String) {
        mTextView_title.text = title
    }

    fun setback() {
        mImageButton_back.visibility = View.VISIBLE
        mImageButton_menu.visibility = View.GONE
        iv_head_logo.visibility = View.GONE
    }

    fun setNoBackNoMenu() {
        mImageButton_back.visibility = View.GONE
        mImageButton_menu.visibility = View.GONE
        iv_head_logo.visibility = View.GONE
    }


    fun refData() {
        if (Const.systemLocation) mImageView_location.visibility = View.VISIBLE else mImageView_location.visibility = View.GONE
        if (Const.system4G) mImageView_4g.visibility = View.VISIBLE else mImageView_4g.visibility = View.GONE
        if (Const.systemError) {
            mImageView_error.visibility = View.VISIBLE
            mImageView_error.animation = animation
        } else {
            mImageView_error.visibility = View.GONE
            mImageView_error.animation = null
            mImageView_error.clearAnimation()
        }

        mTextView_dl.text = Const.systemPower.toString() + "%"
        mBatteryView.setPower(Const.systemPower)
        if (Const.systemPower > 0) {
            mBatteryView.visibility = View.VISIBLE
            mTextView_dl.visibility = View.VISIBLE
        }
        var time = "00:00"
        val df: DateFormat = SimpleDateFormat("HH:mm")
        val date = Date(Const.systemTime)
        time = df.format(date)
        mTextView_time.text = time
    }


}