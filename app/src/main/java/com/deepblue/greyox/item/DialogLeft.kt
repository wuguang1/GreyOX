//
//  DialogLeft
//
//  Created by 86139 on 2020-09-09 15:26:43
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
import android.widget.TextView;
import android.widget.LinearLayout;
import com.deepblue.greyox.frg.FrgErrorList
import com.deepblue.greyox.frg.FrgReportList
import com.deepblue.greyox.pop.PopShowSet
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.item_dialog_left.view.*


class DialogLeft(context: Context?) : BaseItem(context) {
    var mPopShowSet: PopShowSet? = null

    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_dialog_left, this)
        mLinearLayout_zybg.setOnClickListener {
            Helper.startActivity(context, FrgReportList::class.java, TitleAct::class.java)
            mPopShowSet?.hide()
        }
        mLinearLayout_gzlb.setOnClickListener {
            Helper.startActivity(context, FrgErrorList::class.java, TitleAct::class.java)
            mPopShowSet?.hide()
        }
        mLinearLayout_set.setOnClickListener { mPopShowSet?.hide() }

    }

    fun set(mPopShowSet: PopShowSet?) {
        this.mPopShowSet = mPopShowSet

    }

}