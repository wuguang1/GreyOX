//
//  ErrorList
//
//  Created by 86139 on 2020-09-07 09:57:57
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.item;

import com.deepblue.greyox.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color
import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.TextView;
import android.widget.ListView;
import com.deepblue.library.planbmsg.bean.ErrorInfo
import kotlinx.android.synthetic.main.item_error_list.view.*
import kotlinx.android.synthetic.main.item_report_list.view.*


class ErrorList(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_error_list, this)
    }

    fun set(item: ErrorInfo?, position: Int) {
        item?.run {
            if (position % 2 == 0) mLinearLayout.setBackgroundColor(Color.parseColor("#26ffffff")) else mLinearLayout.setBackgroundColor(Color.parseColor("#00000000"))
            when (belong) {
                "Motor" -> mTextView_dj.text = "电机"
                "Battery" -> mTextView_dj.text = "电池"
                "Sensor" -> mTextView_dj.text = "传感器"
                "VCU" -> mTextView_dj.text = "车辆控制单元"
                "ACU" -> mTextView_dj.text = "自动驾驶控制单元"
            }

            if (position != 0) {
                mTextView_cwlx.text = if (type == "1") "故障" else "警告"
                mTextView_cgq.text = error_code
                mTextView_clkzdy.text = reason
            }
        }

    }

}