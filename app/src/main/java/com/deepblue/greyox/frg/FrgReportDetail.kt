//
//  FrgReportDetail
//
//  Created by 86139 on 2020-09-07 13:22:52
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.frg;

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
import kotlinx.android.synthetic.main.frg_report_detail.*


class FrgReportDetail : BaseFrg() {
    private var getTaskReportsRes: GetTaskReportsRes? = null
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_report_detail)
    }

    override fun initView() {

        mButton_sure.setOnClickListener {
            finish()
        }
    }

    override fun loaddata() {
        sendwebSocket(OXGetReportReq(0, 0))
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            7003 -> {
                getTaskReportsRes = JsonUtils.fromJson(obj.toString(), GetTaskReportsRes::class.java)
                if (getTaskReportsRes?.getJson()?.reports != null && getTaskReportsRes?.getJson()?.reports!!.size > 0) {
                    var obj = getTaskReportsRes?.getJson()?.reports!![0]
                    mTextView_time.text = obj.taskStartTime + "-" + obj.taskEndTime
                    mTextView_name.text = obj.taskName
                    mTextView_czy.text = getString(R.string.i_czy) + obj.operater
                    mTextView_ghzylc.text = getString(R.string.i_ghzylc) + obj.planDistance + "KM"
                    mTextView_ghzymj.text = getString(R.string.i_ghqszymj) + obj.planArea + "㎡"
                    mTextView_ghzysc.text = getString(R.string.I_ghqszysc) + obj.planCostTime + "H"

                    mTextView_sjzylc.text = getString(R.string.i_sjqszylc) + obj.actualDistance + "KM"
                    mTextView_sjzymj.text = getString(R.string.i_sjqszymj) + obj.actualArea + "㎡"
                    mTextView_sjzysc.text = getString(R.string.i_sjqszysc) + obj.actualCostTime + "H"

                    mTextView_sjwcd.text = getString(R.string.i_sjqszywcd) + obj.finishPercent + "%"
                    mTextView_wqsmj.text = getString(R.string.i_wqszymj) + obj.unfinishArea + "㎡"

                    mTextView_dcnh.text = getString(R.string.i_dcqszynh) + obj.costEnergy + "KWH"
                    mTextView_sydl.text = getString(R.string.i_sydcdl) + obj.remainingBattery + "%"

                    mTextView_zlc.text = getString(R.string.i_zqslc) + getTaskReportsRes?.getJson()?.totalDistance + "KM"
                    mTextView_znh.text = getString(R.string.i_zqszynh) + getTaskReportsRes?.getJson()?.totalCostEnergy + "KWH"
                }
            }
        }
    }

    override fun onSuccess(data: String?, method: String) {
    }

}