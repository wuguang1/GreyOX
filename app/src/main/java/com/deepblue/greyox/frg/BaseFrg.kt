//
//  BaseFrg
//
//  Created by 86139 on 2020-08-26 14:50:19
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.deepblue.greyox.frg;

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import com.baidu.mapapi.model.LatLng
import com.deepblue.greyox.Const
import com.deepblue.greyox.F
import com.deepblue.greyox.F.getJson
import com.deepblue.greyox.GreyOXApplication
import com.deepblue.greyox.R
import com.deepblue.greyox.bean.GetBatteryRes
import com.deepblue.greyox.bean.GetRealDateRes
import com.deepblue.greyox.bean.VoiceRes
import com.deepblue.greyox.item.Head
import com.deepblue.greyox.util.BaiduMapUtil
import com.deepblue.greyox.view.LoadingDialog
import com.deepblue.library.planbmsg.HeartbeatRes
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Request
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.msg1000.GetNetworkRes
import com.deepblue.library.planbmsg.msg2000.GetErrorHistoryRes
import com.deepblue.library.planbmsg.msg2000.LoginReq
import com.deepblue.library.planbmsg.push.InitDataRes
import com.google.gson.Gson
import com.mdx.framework.activity.MFragment
import org.jetbrains.anko.doAsync

abstract class BaseFrg : MFragment(), View.OnClickListener {
    val greyOXApplication by lazy { activity?.application as GreyOXApplication }
    val loadDialog by lazy { context?.let { LoadingDialog(it) } }
    var mHead: Head? = null
    private var mediaPlayer: MediaPlayer = MediaPlayer()

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
                                when (a.key) {
                                    GetRealDateRes.GPS_SIGNAL -> {
                                        Const.systemLocation = a.value >= 0
                                    }
                                    GetRealDateRes.LATITUDE -> Const.systemLatitude = a.value
                                    GetRealDateRes.LONGITUDE -> {
                                        Const.systemLongitude = a.value
                                        Const.systemLatLng = BaiduMapUtil.loadBaiDuData(LatLng(Const.systemLatitude, Const.systemLongitude))
                                    }
                                    GetRealDateRes.YAW_ANGLE -> {
                                        Const.systemYaw_angle = a.value
                                    }
                                }
                            }
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
            12003 -> {
                val res = JsonUtils.fromJson(obj.toString(), Response::class.java)
                if (res?.error_code == -2 && this !is LoginFragment) {
                    if (Const.user != null) {
                        val fromJson = Gson().fromJson(getJson("login"), LoginReq::class.java)
                        sendwebSocket(fromJson, context, true)
                    }
                }
            }

            12028 -> {
                val json = JsonUtils.fromJson(obj.toString(), GetErrorHistoryRes::class.java)?.getJson()
                Const.systemError = json?.error_msgs !== null && json.error_msgs.isNotEmpty()
            }
            24000 -> {
                val mInitDataRes = JsonUtils.fromJson(obj.toString(), InitDataRes::class.java)
                mInitDataRes?.let {
                    Const.mInitData = it.getJson()
                }
            }
            24012 -> {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                }

                val mVoiceRes = JsonUtils.fromJson(obj.toString(), VoiceRes::class.java)?.getJson()
                mVoiceRes?.let {
                    F.playVoice(
                        context!!, mediaPlayer, when (it.voiceContext) {
                            "请不要遮挡车顶的眼睛哦，谢谢" -> uri1
                            "车辆启动啦，请注意" -> uri3
                            "正在停车哦，请注意" -> uri4
                            "车辆左转，请注意" -> uri5
                            "车辆右转，请注意" -> uri6
                            "倒车啦，请注意" -> uri2
//                            "建议播放音乐" -> uri
                            else -> ""
                        }, it.playCnt.toInt(), it.playTime.toInt()
                    )
                }
            }
        }
    }

    val uri1 = "android.resource://" + "com.deepblue.greyox" + "/" + R.raw.voice_eye
    val uri2 = "android.resource://" + "com.deepblue.greyox" + "/" + R.raw.voice_daoche
    val uri3 = "android.resource://" + "com.deepblue.greyox" + "/" + R.raw.voice_start
    val uri4 = "android.resource://" + "com.deepblue.greyox" + "/" + R.raw.voice_stoping
    val uri5 = "android.resource://" + "com.deepblue.greyox" + "/" + R.raw.voice_left
    val uri6 = "android.resource://" + "com.deepblue.greyox" + "/" + R.raw.voice_right

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
        actionBar?.addView(mHead, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        greyOXApplication.webSocketClient?.loadDialog?.dismissDiaolog()
    }
}
