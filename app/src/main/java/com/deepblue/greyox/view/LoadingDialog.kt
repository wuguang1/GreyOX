package com.deepblue.greyox.view

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.deepblue.greyox.R
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.dialog_load.*

class LoadingDialog @JvmOverloads constructor(
    context: Context,
    theme: Int = R.style.loadingDialogStyle,
    string: String = context.getString(R.string.str_pleasewait),
    delayMillis: Long = 30000
) : Dialog(context, theme) {

    var currentType = 0
    private var delayMillis_: Long = 30000
    private var mhandler = Handler()

    init {
        setContentView(R.layout.dialog_load)
        window!!.attributes.gravity = Gravity.CENTER
        window!!.attributes.dimAmount = 0f

        this.delayMillis_ = delayMillis
        mTextView_loaddialog.text = string
    }

    override fun show() {
        super.show()
        mhandler.postDelayed({
            if (isShowing) {
                try {
                    var context_: Context? = when (context) {
                        is FragmentActivity -> context
                        is ContextWrapper -> (context as ContextWrapper).baseContext
                        else -> null
                    }
                    if (context_ != null && !(context_ as FragmentActivity).isFinishing && !(context_ as FragmentActivity).isDestroyed) {
                        Helper.toast("请求超时")
                        dismiss()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, delayMillis_)
    }

    override fun onStop() {
        super.onStop()
//        Helper.toast(currentType.toString())
        mhandler.removeCallbacksAndMessages(null)
    }

    fun dismissDiaolog() {
        if (isShowing) dismiss()
        mhandler.removeCallbacksAndMessages(null)
    }
}