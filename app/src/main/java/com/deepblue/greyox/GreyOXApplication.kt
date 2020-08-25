package com.deepblue.greyox

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.mdx.framework.Frame
import com.tencent.bugly.crashreport.CrashReport

class GreyOXApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Frame.init(applicationContext)
        initBaiduSDK()
        CrashReport.initCrashReport(applicationContext, "02108abf99", false)
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