package com.deepblue.greyox.view

import android.app.Dialog
import android.content.Context
import android.view.View
import com.deepblue.greyox.R
import com.deepblue.greyox.ada.ErrorAdapter
import com.deepblue.library.planbmsg.bean.ErrorInfo
import kotlinx.android.synthetic.main.item_yesdialog.*

/**
 * @author wg
 */
class ErrorDialog : Dialog {

    private var mAdapter: ErrorAdapter
    private var mdataList = ArrayList<ErrorInfo>()

    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.loadingDialogStyle) {
        setContentView(R.layout.item_yesdialog)
        mAdapter = ErrorAdapter(context, mdataList, R.layout.adapter_error)
        error_recyc.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

    fun setErrorData(mDatas: MutableList<ErrorInfo>) {
        mdataList.clear()
        mdataList.addAll(mDatas)
        mAdapter.notifyDataSetChanged()
    }

    fun setOnclickListener(listener: View.OnClickListener) {
        tv_Yes_center.setOnClickListener(listener)
    }
}
