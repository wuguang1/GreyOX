//
//  AdaReportList
//
//  Created by 86139 on 2020-09-07 10:26:09
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;
import com.deepblue.greyox.bean.TaskReport
import com.deepblue.greyox.frg.position_FrgReportList

import com.deepblue.greyox.item.ReportList;
import com.mdx.framework.Frame

class AdaReportList(context: Context?, list: ArrayList<TaskReport>?) : MAdapter<TaskReport>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = ReportList(context)
        }
        try {
            (convertView as ReportList).set(item, position)
            convertView.setOnClickListener {
                position_FrgReportList = position
                this@AdaReportList.notifyDataSetChanged()
                Frame.HANDLES.sentAll("FrgReportList", 0, item)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}

