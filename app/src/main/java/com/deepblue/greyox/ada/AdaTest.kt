//
//  AdaTest
//
//  Created by 86139 on 2020-08-26 14:50:18
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.deepblue.greyox.ada;

import com.mdx.framework.adapter.MAdapter;
import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.deepblue.greyox.item.Test;

class AdaTest (context: Context, list: List<String>) : MAdapter<String>(context, list) {


    override fun getview(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val item = get(position)
        if (convertView == null) {
            convertView = Test(context)
        }
        try {
            (convertView as Test).set(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView
    }
}

