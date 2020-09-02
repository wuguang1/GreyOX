package com.deepblue.greyox.frg

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.deepblue.greyox.R
import com.mdx.framework.activity.IndexAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_selfcheck.*

class SelfCheckFragment : BaseFrg() {
    val rotate by lazy {
        RotateAnimation(
            0f,
            359f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
    }
    val rotate2 by lazy {
        RotateAnimation(
            0f,
            359f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
    }

    var currentProcess = 0

    var runnable1: Runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            currentProcess += 2
            if (currentProcess <= 100) {
                activity?.runOnUiThread {
                    tv_selfcheck_pro.text = "$currentProcess%"
                }
                if (currentProcess == 100) {
                    handler.removeCallbacks(this)
                    do100Percent()
                }
            }
            handler.postDelayed(this, 100)
        }
    }

    override fun create(var1: Bundle?) {
        setContentView(R.layout.frg_selfcheck)
    }

    override fun initView() {
        val drawable: AnimatedVectorDrawable = (iv_selfcheck_center_bg.drawable) as AnimatedVectorDrawable
        if (drawable.isRunning) drawable.stop() else drawable.start()

        rotate.interpolator = LinearInterpolator() //设置插值器
        rotate.duration = 3000 //设置动画持续周期
        rotate.repeatCount = Animation.INFINITE //设置重复次数
        rotate.fillAfter = true //动画执行完后是否停留在执行完的状态
        iv_selfcheck_center.animation = rotate

        rotate2.interpolator = LinearInterpolator()
        rotate2.duration = 3000
        rotate2.repeatCount = Animation.INFINITE
        rotate2.fillAfter = true
        iv_vcu.animation = rotate2
        iv_sensor.animation = rotate2
        iv_ACU.animation = rotate2
        iv_aa.animation = rotate2
        iv_network.animation = rotate2
    }

    override fun loaddata() {
        handler.post(runnable1)
    }

    private fun do100Percent() {
        iv_vcu.setImageResource(R.mipmap.icon_selfcheck_right)
        iv_sensor.setImageResource(R.mipmap.icon_selfcheck_right)
        iv_ACU.setImageResource(R.mipmap.icon_selfcheck_right)
        iv_aa.setImageResource(R.mipmap.icon_selfcheck_right)
        iv_network.setImageResource(R.mipmap.icon_selfcheck_right)
        cleanAllAnimator()

        handler.postDelayed({
            Helper.startActivity(context, LoginFragment::class.java, IndexAct::class.java)
            finish()
        },2000)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (iv_vcu != null || iv_sensor != null || iv_ACU != null || iv_aa != null || iv_network != null) {
            cleanAllAnimator()
        }
    }

    private fun cleanAllAnimator() {
        iv_vcu.clearAnimation()
        iv_sensor.clearAnimation()
        iv_ACU.clearAnimation()
        iv_aa.clearAnimation()
        iv_network.clearAnimation()
    }
}