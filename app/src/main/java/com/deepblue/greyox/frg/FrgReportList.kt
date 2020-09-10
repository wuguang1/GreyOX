//
//  FrgReportList
//
//  Created by 86139 on 2020-09-07 11:04:34
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.frg;

import android.os.Bundle;
import android.view.View

import com.deepblue.greyox.R;

import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepblue.greyox.ada.AdaReportList
import com.deepblue.greyox.bean.GetTaskReportsRes
import com.deepblue.greyox.bean.OXGetReportReq
import com.deepblue.greyox.bean.TaskReport
import com.deepblue.library.planbmsg.JsonUtils
import kotlinx.android.synthetic.main.frg_report_list.*

var position_FrgReportList = -1

class FrgReportList : BaseFrg() {
    private var getTaskReportsRes: GetTaskReportsRes? = null
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_report_list)
        position_FrgReportList = -1
    }

    override fun initView() {
    }

    override fun loaddata() {
        sendwebSocket(OXGetReportReq(0, System.currentTimeMillis() / 1000))
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            0 -> {
                mLinearLayout_nocontent.visibility = View.GONE
                mLinearLayout_content.visibility = View.GONE
                mTextView_name.text = (obj as TaskReport).taskName
                mTextView_time.text = obj.taskStartTime + "-" + obj.taskEndTime
                mTextView_czy.text = getString(R.string.i_czy) + obj.operater
                mTextView_ghzylc.text = getString(R.string.i_ghzylc) + obj.planDistance + "KM"
                mTextView_sjqslc.text = getString(R.string.i_sjqszylc) + obj.actualDistance + "KM"
                mTextView_ghmj.text = getString(R.string.i_ghqszymj) + obj.planArea + "㎡"
                mTextView_sjmj.text = getString(R.string.i_sjqszymj) + obj.actualArea + "㎡"
                mTextView_sjwcd.text = getString(R.string.i_sjqszywcd) + obj.finishPercent + "%"
                mTextView_wqsmj.text = getString(R.string.i_wqszymj) + obj.unfinishArea + "㎡"
                mTextView_ghzysc.text = getString(R.string.I_ghqszysc) + obj.planCostTime + "H"
                mTextView_sjzysc.text = getString(R.string.i_sjqszysc) + obj.actualCostTime + "H"
                mTextView_dcxh.text = getString(R.string.i_dcqszynh) + obj.costEnergy + "KWH"
                mTextView_zqsnh.text = getString(R.string.i_zqszynh) + getTaskReportsRes?.getJson()?.totalCostEnergy + "KWH"
                mTextView_sydl.text = getString(R.string.i_sydcdl) + obj.remainingBattery + "%"
                mTextView_zqslc.text = getString(R.string.i_zqslc) + getTaskReportsRes?.getJson()?.totalDistance + "KM"

            }
            7003 -> {
                getTaskReportsRes = JsonUtils.fromJson(obj.toString(), GetTaskReportsRes::class.java)
                mListView.adapter = AdaReportList(context, getTaskReportsRes?.getJson()?.reports)
            }
        }
    }

    override fun onSuccess(data: String?, method: String) {
    }

}