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
import com.deepblue.greyox.Const
import com.deepblue.greyox.act.TitleActSpecial
import com.deepblue.greyox.bean.GetMapInfoReq
import com.deepblue.greyox.bean.KeySet
import com.deepblue.greyox.bean.SetKeyReq
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
        mTextView_name.text = Const.user?.name ?: ""
        mLinearLayout_zybg.setOnClickListener {
            Helper.startActivity(context, FrgReportList::class.java, TitleActSpecial::class.java)
            mPopShowSet?.hide()
        }
        mLinearLayout_gzlb.setOnClickListener {
            Helper.startActivity(context, FrgErrorList::class.java, TitleActSpecial::class.java)
            mPopShowSet?.hide()
        }
        mLinearLayout_set.setOnClickListener { mPopShowSet?.hide() }

        mTextView_reset.setOnClickListener {
            Helper.toast("开发中,请等待...")
        }
        mTextView_close.setOnClickListener {
            Helper.toast("请关闭电源")
            var req = ArrayList<KeySet>()
            req.add(KeySet("data_system_command","shutdown"))
            sendwebSocket(SetKeyReq(req), context)
        }

    }

    fun set(mPopShowSet: PopShowSet?) {
        this.mPopShowSet = mPopShowSet

    }

}