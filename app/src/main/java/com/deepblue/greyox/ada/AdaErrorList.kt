//
//  AdaErrorList
//
//  Created by 86139 on 2020-09-07 09:57:57
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.deepblue.greyox.item.ErrorList;
import com.deepblue.library.planbmsg.bean.ErrorInfo

class AdaErrorList(context: Context?, list: List<ErrorInfo>?) : MAdapter<ErrorInfo>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = ErrorList(context)
        }
        try {
            (convertView as ErrorList).set(item, position)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}

