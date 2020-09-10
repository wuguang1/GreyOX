//
//  BaseFrg
//
//  Created by 86139 on 2020-08-26 14:50:19
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.frg;

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.deepblue.greyox.Const.mInitData
import com.deepblue.greyox.F.hideNavigation
import com.deepblue.greyox.GreyOXApplication
import com.deepblue.greyox.item.DialogLeft
import com.deepblue.greyox.item.Head
import com.deepblue.greyox.pop.PopShowSet
import com.deepblue.greyox.view.LoadingDialog
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Request
import com.deepblue.library.planbmsg.msg2000.GetAllUsersRes
import com.deepblue.library.planbmsg.push.InitDataRes
import com.mdx.framework.activity.MFragment

abstract class BaseFrg : MFragment(), View.OnClickListener {
    val greyOXApplication by lazy { activity?.application as GreyOXApplication }
    val loadDialog by lazy { context?.let { LoadingDialog(it) } }
    lateinit var mHead: Head
    fun isHeadInit() = ::mHead.isInitialized
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

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            24000 -> {
                val mInitDataRes = JsonUtils.fromJson(obj.toString(), InitDataRes::class.java)
                mInitDataRes?.let {
                    mInitData = it.getJson()
                }
            }
        }
    }

    fun sendwebSocket(request: Request, context: Context? = getContext(), isShowLoading: Boolean = false, isCanceledOnTouchOutside: Boolean = false) {
        greyOXApplication.webSocketClient?.sendMessage(request, context, isShowLoading, isCanceledOnTouchOutside)
    }

    fun showMenu() {
        var mPopShowSet = PopShowSet(context!!, mHead, DialogLeft(context))
        mPopShowSet.show()
    }

    fun showLoading() {
        if (loadDialog != null && !loadDialog!!.isShowing) {
            loadDialog!!.show()
        }
    }

    fun dismissLoading() {
        if (loadDialog != null && loadDialog!!.isShowing) {
            loadDialog!!.dismissDiaolog()
        }
    }

    override fun setActionBar(actionBar: LinearLayout?) {
        mHead = Head(context)
//        mHead.canGoBack()
        actionBar?.addView(mHead, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        greyOXApplication.webSocketClient?.loadDialog?.dismissDiaolog()
    }
}
