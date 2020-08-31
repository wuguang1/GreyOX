package com.deepblue.greyox.frg

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.deepblue.greyox.R
import com.deepblue.greyox.frg.BaseFrg
import com.deepblue.library.planbmsg.HeartbeatReq
import com.mdx.framework.Frame
import com.mdx.framework.activity.IndexAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_login.*

class LoginFragment : BaseFrg() {
    override fun create(var1: Bundle?) {
        setContentView(R.layout.frg_login)
        Frame.HANDLES.closeWidthOut("LoginFragment")
    }

    override fun initView() {
        btn_login.setOnClickListener(this)
    }

    override fun loaddata() {
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_login -> {
//                Helper.startActivity(context, HomeFragment::class.java, IndexAct::class.java)
//                finish()
            }
        }
    }
}