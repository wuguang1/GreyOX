package com.deepblue.greyox.ada

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.deepblue.greyox.R
import com.deepblue.greyox.bean.GetOXMapInfoModel2
import com.deepblue.library.planbmsg.bean.ErrorInfo

class ErrorAdapter(mContext: Context, mDatas: MutableList<ErrorInfo>, mLayoutId: Int) :
    BaseAdapter<ErrorInfo>(mContext, mDatas, mLayoutId) {
    var a = Color.parseColor("#FF0000")
    var b = Color.parseColor("#F6BC10")
    override fun convert(mContext: Context, holder: BaseViewHolder, t: ErrorInfo, select_position: Int) {
        holder.setText(R.id.tv_item_belong, "所属模块 : ${t.belong}")
            .setText(R.id.tv_item_errorcode, "故障代码 : ${t.error_code}")
            .setText(R.id.tv_item_suggestion, "建议 : ${t.suggestion}")
        val tv_item_belong = holder.getView<TextView>(R.id.tv_item_belong)
        val tv_item_errorcode = holder.getView<TextView>(R.id.tv_item_errorcode)
        val tv_item_suggestion = holder.getView<TextView>(R.id.tv_item_suggestion)
        if (t.type == "1") {
            tv_item_belong.setTextColor(a)
            tv_item_errorcode.setTextColor(a)
            tv_item_belong.setTextColor(a)
        } else {
            tv_item_belong.setTextColor(b)
            tv_item_errorcode.setTextColor(b)
            tv_item_suggestion.setTextColor(b)
        }
    }

}