package com.deepblue.greyox

import android.content.Context
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.deepblue.greyox.item.DialogLeft
import com.deepblue.greyox.pop.PopShowSet
import com.google.gson.Gson
import com.mdx.framework.Frame
import java.io.BufferedReader
import java.io.InputStreamReader


object F {

    fun init() {

    }

    fun <T> data2Model(data: String?, mclass: Class<T>): T {
        return Gson().fromJson(data, mclass)
    }

    fun getJson(key: String): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(Frame.CONTEXT)
        return sp.getString(key, "")
    }

    fun saveJson(key: String, json: String?) {
        val sp = PreferenceManager.getDefaultSharedPreferences(Frame.CONTEXT)
        sp.edit().putString(key, json).apply()
    }

    fun putShareP_BOOLEAN(key: String, boolean: Boolean) {
        val sp = PreferenceManager.getDefaultSharedPreferences(Frame.CONTEXT)
        sp.edit().putBoolean(key, boolean).apply()
    }

    fun getShareP_BOOLEAN(key: String): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(Frame.CONTEXT)
        return sp.getBoolean(key, false)
    }

    fun logOut(context: Context?, isShow: Boolean = true, isFromTask: Boolean = false) {

    }


    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Frame.CONTEXT.getResources().getDisplayMetrics()).toInt()
    }


    fun play(context: Context, mediaPlayer: MediaPlayer, surfaceView: SurfaceView, path: String) {
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                try {
                    mediaPlayer.run {
                        reset()
                        setDisplay(holder)
                        setDataSource(context, Uri.parse(path))
                        setOnPreparedListener { start() }
                        setOnCompletionListener { start() }
                        prepare()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
            }
        })
    }

    val mHandler = Handler()

    fun playVoice(context: Context, mediaPlayer: MediaPlayer, path: String, playCnt: Int, playDelay: Int) {
        if (path.isNotEmpty()) {
            var playCnt_ = playCnt
            if (mediaPlayer.isPlaying) {
                mHandler.removeCallbacksAndMessages(null)
                mediaPlayer.stop()
            }
            mediaPlayer.run {
                reset()
                setDataSource(context, Uri.parse(path))
                setOnPreparedListener { start() }
                setOnCompletionListener {
                    playCnt_--
                    if (playCnt_ > 0) {
                        mHandler.postDelayed({
                            start()
                        }, (playDelay * 1000).toLong())
                    }
                }
                prepare()
            }
        }else{
            mHandler.removeCallbacksAndMessages(null)
            mediaPlayer.stop()
        }
    }


    fun fileToJsonString(filename: String): String? {
        try {
            val assetManager: AssetManager = Frame.CONTEXT.assets //获得assets资源管理器（assets中的文件无法直接访问，可以使用AssetManager访问）

            val inputStreamReader = InputStreamReader(assetManager.open(filename), "UTF-8") //使用IO流读取json文件内容

            val br = BufferedReader(inputStreamReader) //使用字符高效流

            var line: String?
            val builder = StringBuilder()
            while (br.readLine().also { line = it } != null) {
                builder.append(line)
            }
            br.close()
            inputStreamReader.close()
            return builder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    //隐藏SystemUI
    fun hideNavigation() {
        try {
            val command =
                "LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui"
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            proc.waitFor()
        } catch (ex: java.lang.Exception) {
        }
    }

    fun showMenu(context: Context, view: View) {
        var mPopShowSet = PopShowSet(context, view, DialogLeft(context))
        mPopShowSet.show()
    }

}












