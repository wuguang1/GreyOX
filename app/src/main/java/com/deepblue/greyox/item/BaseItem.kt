//
//  BaseItem
//
//  Created by 86139 on 2020-08-26 14:50:19
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.item;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout
import com.deepblue.greyox.GreyOXApplication
import com.deepblue.library.planbmsg.Request


open class BaseItem(context: Context?) : LinearLayout(context), View.OnClickListener {
    val greyOXApplication by lazy { context!!.applicationContext as GreyOXApplication }
    fun sendwebSocket(request: Request, context: Context? = getContext(), isShowLoading: Boolean = false, isCanceledOnTouchOutside: Boolean = false) {
        greyOXApplication.webSocketClient?.sendMessage(request, context, isShowLoading, isCanceledOnTouchOutside)
    }

    override fun onClick(v: View) {
    }
}