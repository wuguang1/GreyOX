//
//  Test
//
//  Created by 86139 on 2020-08-26 14:50:18
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
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


class  Test(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_test, this)
    }

    fun set(item: Any?) {
        item?.run{
        }
    }

}