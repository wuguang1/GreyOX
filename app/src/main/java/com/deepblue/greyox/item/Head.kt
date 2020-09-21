//
//  Head
//
//  Created by 86139 on 2020-09-09 16:13:58
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.item;

import com.deepblue.greyox.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.deepblue.greyox.Const
import com.deepblue.greyox.F.showMenu
import kotlinx.android.synthetic.main.item_head.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Head(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_head, this)

        mImageButton_menu.setOnClickListener {
            showMenu(context!!, this@Head)
        }
    }

    fun setTitle(title: String) {
        mTextView_title.text = title
    }

    fun refData() {
        if (Const.systemLocation) mImageView_location.visibility = View.VISIBLE else mImageView_location.visibility = View.GONE
        if (Const.system4G) mImageView_4g.visibility = View.VISIBLE else mImageView_4g.visibility = View.GONE
        if (Const.systemError) mImageView_error.visibility = View.VISIBLE else mImageView_error.visibility = View.GONE

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