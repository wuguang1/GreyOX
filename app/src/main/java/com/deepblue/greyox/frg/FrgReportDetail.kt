//
//  FrgReportDetail
//
//  Created by 86139 on 2020-09-07 13:22:52
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.frg;

import android.annotation.SuppressLint
import android.os.Bundle;
import android.view.View

import com.deepblue.greyox.R;

import android.widget.TextView;
import android.widget.Button;
import com.deepblue.greyox.ada.AdaReportList
import com.deepblue.greyox.bean.GetTaskReportsRes
import com.deepblue.greyox.bean.OXGetReportReq
import com.deepblue.greyox.bean.TaskReport
import com.deepblue.library.planbmsg.JsonUtils
import com.mdx.framework.Frame
import kotlinx.android.synthetic.main.frg_report_detail.*


class FrgReportDetail : BaseFrg() {
    private var getTaskReportsRes: GetTaskReportsRes? = null
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_report_detail)
    }

    override fun initView() {

        mButton_sure.setOnClickListener {
            Frame.HANDLES.sentAll("HomeFragment", 10009, "10009")
            finish()
        }
    }

    override fun loaddata() {
        sendwebSocket(OXGetReportReq(0, 0), context, true)
    }

    @SuppressLint("SetTextI18n")
    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            17003 -> {
                getTaskReportsRes = JsonUtils.fromJson(obj.toString(), GetTaskReportsRes::class.java)
                if (getTaskReportsRes?.getJson()?.reports != null && getTaskReportsRes?.getJson()?.reports!!.size > 0) {
                    var obj = getTaskReportsRes?.getJson()?.reports!![0]
                    if (obj.taskEndTime.split(" ").isNotEmpty())
                        mTextView_time.text = obj.taskStartTime + "-" + obj.taskEndTime.split(" ")[1]
                    mTextView_name.text = obj.taskName
                    mTextView_czy.text = getString(R.string.i_czy) + obj.operater
                    mTextView_ghzylc.text = getString(R.string.i_ghzylc) + String.format("%.2f", obj.planDistance) + "KM"
                    mTextView_ghzymj.text = getString(R.string.i_ghqszymj) + String.format("%.2f", obj.planArea) + "M²"
                    mTextView_ghzysc.text = getString(R.string.I_ghqszysc) + String.format("%.1f", obj.planCostTime) + "H"

                    mTextView_sjzylc.text = getString(R.string.i_sjqszylc) + String.format("%.2f", obj.actualDistance) + "KM"
                    mTextView_sjzymj.text = getString(R.string.i_sjqszymj) + String.format("%.2f", obj.actualArea) + "M²"
                    mTextView_sjzysc.text = getString(R.string.i_sjqszysc) + String.format("%.2f", obj.actualCostTime) + "H"

                    mTextView_sjwcd.text = getString(R.string.i_sjqszywcd) + obj.finishPercent + "%"
                    mTextView_wqsmj.text = getString(R.string.i_wqszymj) + String.format("%.2f", obj.unfinishArea) + "M²"

                    mTextView_dcnh.text = getString(R.string.i_dcqszynh) + String.format("%.1f", obj.costEnergy) + "KWH"
                    mTextView_sydl.text = getString(R.string.i_sydcdl) + obj.remainingBattery + "%"

                    mTextView_zlc.text = getString(R.string.i_zqslc) + String.format("%.2f", getTaskReportsRes?.getJson()?.totalDistance) + "KM"
                    mTextView_znh.text = getString(R.string.i_zqszynh) + String.format("%.2f", getTaskReportsRes?.getJson()?.totalCostEnergy) + "KWH"
                }
            }
        }
    }

    override fun onSuccess(data: String?, method: String) {
    }

}