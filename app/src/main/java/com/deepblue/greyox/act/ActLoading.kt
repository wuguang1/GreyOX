package com.deepblue.greyox.act

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import com.deepblue.greyox.R
import com.deepblue.greyox.ui.fragment.LoginFragment
import com.deepblue.greyox.ui.fragment.SelfCheckFragment
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
            Helper.startActivity(this, SelfCheckFragment::class.java, IndexAct::class.java)
//            if (mModellogin == null) {
//                Helper.startActivity(this, FrgLogin::class.java, IndexAct::class.java)
//            } else {
//                Helper.startActivity(this, FrgHome::class.java, IndexAct::class.java)
//            }
            finish()
        }, 10000)
    }

}