//
//  BaseFrg
//
//  Created by 86139 on 2020-08-26 14:50:19
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.frg;

import android.util.Log
import android.view.View;
import android.widget.LinearLayout

import com.mdx.framework.activity.MFragment;

abstract class BaseFrg : MFragment(), View.OnClickListener {


    final override fun initV(view: View) {
        initView()
        loaddata()
    }

    abstract fun initView()
    abstract fun loaddata()
    override fun onClick(v: View) {
    }

    open fun onSuccess(data: String?, method: String) {


    }
    override fun setActionBar(actionBar: LinearLayout?) {
//        mHead = Head(context)
//        mHead.canGoBack()
//        actionBar?.addView(mHead, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}
