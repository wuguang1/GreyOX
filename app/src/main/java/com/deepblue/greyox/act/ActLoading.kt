package com.deepblue.greyox.act

import android.app.Activity
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import com.deepblue.greyox.Const
import com.deepblue.greyox.F.play
import com.deepblue.greyox.R
import com.deepblue.greyox.frg.SelfCheckFragment
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.push.InitDataRes
import com.mdx.framework.activity.IndexAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.act_loading.*

class ActLoading : BaseAct() {
    var mediaPlayer: MediaPlayer? = null
    var timeOut = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = Color.BLUE; //写法一
        setContentView(R.layout.act_loading)
        loaddata()
    }

    override fun disposeMsg(type: Int, obj: Any) {
        super.disposeMsg(type, obj)
        when (type) {
            10999 -> {
                if (timeOut) {
                    timeOut = false
                    Helper.startActivity(this, SelfCheckFragment::class.java, IndexAct::class.java)
                    finish()
                }
            }
            24000 -> {
                val mInitDataRes = JsonUtils.fromJson(obj.toString(), InitDataRes::class.java)
                mInitDataRes?.let {
                    Const.mInitData = it.getJson()
                }
            }
        }
    }

    fun loaddata() {
        mediaPlayer = MediaPlayer()
        val uri = "android.resource://" + packageName.toString() + "/" + R.raw.welcome
        play(this.applicationContext, mediaPlayer!!, sv_welcome, uri)

        Handler().postDelayed({
            timeOut = true
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}