//
//  ErrorList
//
//  Created by 86139 on 2020-09-07 09:57:57
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
import android.widget.ListView;


class  ErrorList(context: Context?) : BaseItem(context) {
    init {
        val flater = LayoutInflater.from(context)
        flater.inflate(R.layout.item_error_list, this)
    }

    fun set(item: Any?) {
        item?.run{
        }
    }

}