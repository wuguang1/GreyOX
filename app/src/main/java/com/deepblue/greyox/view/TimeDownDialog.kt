package com.deepblue.greyox.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.deepblue.greyox.R
import kotlinx.android.synthetic.main.dialog_countdown.*


/**
 * @author wg
 */
class TimeDownDialog : Dialog {
    private var countTime = 3300
    private var countDownTimer: CountDownTimer? = null

    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.loadingDialogStyle) {
        setContentView(R.layout.dialog_countdown)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(true)
        val attr: WindowManager.LayoutParams = window!!.attributes
        attr.height = ViewGroup.LayoutParams.MATCH_PARENT
        attr.width = ViewGroup.LayoutParams.MATCH_PARENT
        attr.gravity = Gravity.CENTER //设置dialog 在布局中的位置
        window!!.setBackgroundDrawableResource(R.color.trangray)

    }

    override fun show() {
        super.show()
        countTime = 3300
        countDownTimer = object : CountDownTimer(countTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                count_down_tv.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                dismiss()
            }
        }
        countDownTimer!!.start()
    }
}
