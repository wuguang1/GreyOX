package com.deepblue.greyox.frg

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.deepblue.greyox.R
import com.deepblue.greyox.frg.BaseFrg
import com.deepblue.library.planbmsg.HeartbeatReq
import com.deepblue.library.planbmsg.msg2000.ConnectWifiReq
import com.mdx.framework.activity.IndexAct
import com.mdx.framework.utility.Helper

class SelfCheckFragment : BaseFrg() {
    override fun create(var1: Bundle?) {
        setContentView(R.layout.frg_selfcheck)
    }

    override fun initView() {
//        Handler().postDelayed({
//            Helper.startActivity(context, LoginFragment::class.java, IndexAct::class.java)
////            finish()
//        }, 10000)

        sendwebSocket(HeartbeatReq(), context, isShowLoading = true, isCanceledOnTouchOutside = true)

        Handler().postDelayed({ sendwebSocket(ConnectWifiReq("",""), context, isShowLoading = true, isCanceledOnTouchOutside = true) }, 5000)
    }

    override fun loaddata() {
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
        }
    }
}