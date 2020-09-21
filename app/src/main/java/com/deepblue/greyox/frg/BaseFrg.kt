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
import com.deepblue.greyox.Const
import com.deepblue.greyox.Const.mInitData
import com.deepblue.greyox.F.hideNavigation
import com.deepblue.greyox.GreyOXApplication
import com.deepblue.greyox.bean.GetBatteryRes
import com.deepblue.greyox.bean.GetRealDateRes
import com.deepblue.greyox.item.DialogLeft
import com.deepblue.greyox.item.Head
import com.deepblue.greyox.pop.PopShowSet
import com.deepblue.greyox.view.LoadingDialog
import com.deepblue.library.planbmsg.HeartbeatRes
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Request
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.msg1000.GetNetworkRes
import com.deepblue.library.planbmsg.msg2000.GetAllUsersRes
import com.deepblue.library.planbmsg.msg2000.GetErrorHistoryRes
import com.deepblue.library.planbmsg.push.InitDataRes
import com.mdx.framework.activity.MFragment
import kotlinx.android.synthetic.main.frg_error_list.*
import org.jetbrains.anko.doAsync

abstract class BaseFrg : MFragment(), View.OnClickListener {
    val greyOXApplication by lazy { activity?.application as GreyOXApplication }
    val loadDialog by lazy { context?.let { LoadingDialog(it) } }
    var mHead: Head? = null
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
            17004 -> {
                doAsync {
                    val mGetRealDateRes = JsonUtils.fromJson(obj.toString(), GetRealDateRes::class.java)?.getJson()
                    mGetRealDateRes?.let {
                        if (it.realdatainfo != null && it.realdatainfo!!.isNotEmpty()) {
                            it.realdatainfo!!.forEach { a ->
                                Const.systemLocation = false
                                if (a.key.isNotEmpty() && a.key == GetRealDateRes.GPS_SIGNAL && a.value > -1) {
                                    Const.systemLocation = true
                                }
                            }
                        } else {
                            Const.systemLocation = false
                        }
                    }
                }
            }
            11002 -> {
                val robotBattery =
                    JsonUtils.fromJson(obj.toString(), GetBatteryRes::class.java)
                Const.systemPower = robotBattery?.getJson()!!.battery_level
            }
            11008 -> {
                val network =
                    JsonUtils.fromJson(obj.toString(), GetNetworkRes::class.java)
                Const.system4G = network?.getJson()!!.network_status
            }
            10999 -> {
                val heartbeatRes =
                    JsonUtils.fromJson(obj.toString(), HeartbeatRes::class.java)
                val json = heartbeatRes?.getJson()
                Const.systemTime = json?.time!! * 1000
                mHead?.refData()
            }
            12028 -> {
                var getErrorHistoryRes = JsonUtils.fromJson(obj.toString(), GetErrorHistoryRes::class.java)
                if (getErrorHistoryRes?.getJson()?.error_msgs !== null && getErrorHistoryRes?.getJson()?.error_msgs?.size!! > 0) {
                    Const.systemError = true
                }
            }
        }
    }

    fun sendwebSocket(request: Request, context: Context? = getContext(), isShowLoading: Boolean = false, isCanceledOnTouchOutside: Boolean = false) {
        greyOXApplication.webSocketClient?.sendMessage(request, context, isShowLoading, isCanceledOnTouchOutside)
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
