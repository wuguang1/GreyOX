//
//  FrgErrorList
//
//  Created by 86139 on 2020-09-07 09:55:37
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.frg;

import android.os.Bundle;
import android.widget.LinearLayout

import com.deepblue.greyox.R;

import android.widget.TextView;
import android.widget.ListView;
import com.deepblue.greyox.ada.AdaErrorList
import com.deepblue.greyox.bean.OXErrorListReq
import com.deepblue.greyox.bean.OXGetReportReq
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.bean.ErrorInfo
import com.deepblue.library.planbmsg.msg2000.GetErrorHistoryRes
import kotlinx.android.synthetic.main.frg_error_list.*


class FrgErrorList : BaseFrg() {
    var belong = "Motor"
    var getErrorHistoryRes: GetErrorHistoryRes? = null
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_error_list)
    }

    override fun initView() {
        mSwipeRefreshLayout.setOnRefreshListener {
            sendwebSocket(OXErrorListReq())
        }

        mTextView_dj.setOnClickListener {
            belong = "Motor"
            updateUI()
        }
        mTextView_dc.setOnClickListener {
            belong = "Battery"
            updateUI()
        }
        mTextView_cgq.setOnClickListener {
            belong = "Sensor"
            updateUI()
        }
        mTextView_clkzdy.setOnClickListener {
            belong = "VCU"
            updateUI()
        }
        mTextView_zdjsdy.setOnClickListener {
            belong = "ACU"
            updateUI()
        }
    }

    fun updateUI() {
        mTextView_dj.setBackgroundResource(0)
        mTextView_dc.setBackgroundResource(0)
        mTextView_cgq.setBackgroundResource(0)
        mTextView_clkzdy.setBackgroundResource(0)
        mTextView_zdjsdy.setBackgroundResource(0)


        var mMotors = ArrayList<ErrorInfo>()
        mMotors.add(ErrorInfo().apply { this.belong = "Motor" })
        var mBatterys = ArrayList<ErrorInfo>()
        mBatterys.add(ErrorInfo().apply { this.belong = "Battery" })
        var mSensors = ArrayList<ErrorInfo>()
        mSensors.add(ErrorInfo().apply { this.belong = "Sensor" })
        var mVCUs = ArrayList<ErrorInfo>()
        mVCUs.add(ErrorInfo().apply { this.belong = "VCU" })
        var mACUs = ArrayList<ErrorInfo>()
        mACUs.add(ErrorInfo().apply { this.belong = "ACU" })
        if (getErrorHistoryRes?.getJson()?.error_msgs != null) {
            for (item in getErrorHistoryRes?.getJson()?.error_msgs!!) {
                when (item.belong) {
                    "Motor" -> mMotors.add(item)
                    "Battery" -> mBatterys.add(item)
                    "Sensor" -> mSensors.add(item)
                    "VCU" -> mVCUs.add(item)
                    "ACU" -> mACUs.add(item)
                }
            }
        }
        when (belong) {
            "Motor" -> {
                mTextView_dj.setBackgroundResource(R.drawable.ic_tab)
                mListView.adapter = AdaErrorList(context, mMotors)
            }
            "Battery" -> {
                mTextView_dj.setBackgroundResource(R.drawable.ic_tab)
                mListView.adapter = AdaErrorList(context, mBatterys)
            }
            "Sensor" -> {
                mTextView_dj.setBackgroundResource(R.drawable.ic_tab)
                mListView.adapter = AdaErrorList(context, mSensors)
            }
            "VCU" -> {
                mTextView_dj.setBackgroundResource(R.drawable.ic_tab)
                mListView.adapter = AdaErrorList(context, mVCUs)
            }
            "ACU" -> {
                mTextView_dj.setBackgroundResource(R.drawable.ic_tab)
                mListView.adapter = AdaErrorList(context, mACUs)
            }
        }
    }

    override fun loaddata() {
        sendwebSocket(OXErrorListReq(),context,true)
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            12028 -> {
                mSwipeRefreshLayout.isRefreshing = false
                getErrorHistoryRes = JsonUtils.fromJson(obj.toString(), GetErrorHistoryRes::class.java)
                updateUI()
            }
            24002 -> {
                sendwebSocket(OXErrorListReq())
            }
        }
    }

    override fun setActionBar(actionBar: LinearLayout?) {
        super.setActionBar(actionBar)
        mHead?.setback()
    }

    override fun onSuccess(data: String?, method: String) {
    }

}