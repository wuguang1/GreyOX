//
//  BaseAct
//
//  Created by 86139 on 2020-09-02 10:41:16
//  Copyright (c) 86139 All rights reserved.
/**
 *
 */
package com.deepblue.greyox.act

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.mdx.framework.Frame
import com.mdx.framework.utility.handle.MHandler

abstract class BaseAct : Activity(), View.OnClickListener {
    var handler = MHandler()
    val className = this.javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler.setId(className)
        handler.setMsglisnener { msg ->
            when (msg.what) {
                201 -> this@BaseAct.disposeMsg(msg.arg1, msg.obj)
                0 -> finish()
            }
        }
        if (Frame.HANDLES.get(className).size > 0) {
            Frame.HANDLES.get(className).forEach {
                Frame.HANDLES.remove(it)
            }
        }
        Frame.HANDLES.add(handler)
    }

    override fun onDestroy() {
        Frame.HANDLES.remove(this.handler)
        super.onDestroy()
    }

    open fun disposeMsg(type: Int, obj: Any) {}
    override fun onClick(v: View) {}
}