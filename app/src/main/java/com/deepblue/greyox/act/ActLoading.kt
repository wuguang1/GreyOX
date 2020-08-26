//
//  ActLoading
//
//  Created by 86139 on 2020-08-26 14:50:18
//  Copyright (c) 86139 All rights reserved.
/**
 *
 */
package com.deepblue.greyox.act

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import com.deepblue.greyox.R
import com.mdx.framework.activity.IndexAct
import com.mdx.framework.utility.Helper

class ActLoading : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = Color.BLUE; //写法一
        setContentView(R.layout.act_loading)
        loaddata()
    }


    fun loaddata() {
        Handler().postDelayed({

        }, 3000)
    }

}