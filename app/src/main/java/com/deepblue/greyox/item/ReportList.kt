//
//  ReportList
//
//  Created by 86139 on 2020-09-07 10:26:09
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
import com.deepblue.greyox.bean.TaskReport
import com.deepblue.greyox.frg.position_FrgReportList
import kotlinx.android.synthetic.main.item_report_list.view.*


class ReportList(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_report_list, this)
    }

    fun set(item: TaskReport?, position: Int) {
        if (position == position_FrgReportList) {
            mTextView.setBackgroundColor(Color.parseColor("#800083FF"))
        } else {
            if (position % 2 == 0) mTextView.setBackgroundColor(Color.parseColor("#26ffffff")) else mTextView.setBackgroundColor(Color.parseColor("#00000000"))
        }
        item?.run {
            mTextView.text = taskName
        }
    }

}