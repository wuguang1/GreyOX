package com.deepblue.greyox

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.deepblue.greyox.bean.GetOxInfoReq
import com.deepblue.greyox.bean.OXErrorListReq
import com.deepblue.greyox.bean.OXRealdataReq
import com.deepblue.greyox.websocket.WebSocketClient3
import com.deepblue.library.planbmsg.HeartbeatReq
import com.deepblue.library.planbmsg.msg1000.GetBatteryReq
import com.deepblue.library.planbmsg.msg1000.GetNetworkReq
import com.deepblue.library.planbmsg.msg1000.GetRobotLocReq
import com.deepblue.library.planbmsg.msg1000.GetRobotStatusReq
import com.deepblue.library.planbmsg.msg5000.GetScrubberStatusReq
import com.deepblue.library.planbmsg.msg5000.GetScrubberWorkModeReq
import com.mdx.framework.Frame
import com.tencent.bugly.crashreport.CrashReport
import org.jetbrains.anko.doAsync

class GreyOXApplication : Application() {
    companion object {
        //val hostUrl = "ws://192.168.8.109:12235"
//        val hostUrl = "ws://192.168.120.17:12235"
        val hostUrl = "ws://192.168.8.199:12235"
        val DEFAULT_STATUS: Int = 0  //0默认
        val DISCONNECT_STATUS: Int = 1  //1断连
        val CONNECT_STATUS: Int = 2     //2续连
    }

    var webSocketClient: WebSocketClient3? = null

    var isDestory: Boolean = true
    var tims = 0
    var heartTimes: Int = 0
    var lockTime: Int = 0
    var connect_status: Int = DEFAULT_STATUS

    override fun onCreate() {
        super.onCreate()
        Frame.init(applicationContext)
        initBaiduSDK()
        CrashReport.initCrashReport(applicationContext, "02108abf99", false)
        webSocketClient = WebSocketClient3.getInstance(hostUrl)

        doAsync {
            while (isDestory) {
                Thread.sleep(2000)
                if (connect_status != DISCONNECT_STATUS) {
                    tims++
                    heartTimes++
                    lockTime++
                    webSocketClient!!.sendMessage(HeartbeatReq())
                    if (heartTimes > 10 && connect_status > 0) {
                        Frame.HANDLES.sentAll(10002, "stopHeart")
                        heartTimes = 0
                        continue
                    }
                    if (lockTime > 30) {
                        Frame.HANDLES.sentAll(10002, "backLoack")
                        lockTime = 0
                    }
                    if (webSocketClient!!.isConnected()) {
//                        Thread.sleep(100)
//                        webSocketClient!!.sendMessage(OXRealdataReq())
                        Thread.sleep(100)
                        webSocketClient!!.sendMessage(OXErrorListReq())
                        if (tims % 5 == 2) {
                            Thread.sleep(100)
                            webSocketClient!!.sendMessage(GetOxInfoReq())
                            Thread.sleep(100)
                            webSocketClient!!.sendMessage(GetNetworkReq())
                            Thread.sleep(100)
                            webSocketClient!!.sendMessage(GetBatteryReq())
                        }
                    }
                }

            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initBaiduSDK() {
        SDKInitializer.initialize(this)
//        SDKInitializer.setCoordType(CoordType.GCJ02)//国测局坐标
        SDKInitializer.setCoordType(CoordType.BD09LL)// 百度坐标
    }
}