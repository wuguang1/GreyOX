package com.deepblue.greyox.ada

import android.content.Context
import android.widget.ImageView
import com.deepblue.greyox.R
import com.deepblue.greyox.bean.GetOXMapInfoModel

class HomeLineAdapter(mContext: Context, mDatas: MutableList<GetOXMapInfoModel.MapInfoBean.GreyLineListBean>, mLayoutId: Int) :
    BaseAdapter<GetOXMapInfoModel.MapInfoBean.GreyLineListBean>(mContext, mDatas, mLayoutId) {
    override fun convert(mContext: Context, holder: BaseViewHolder, t: GetOXMapInfoModel.MapInfoBean.GreyLineListBean, select_position: Int) {
        holder.setText(R.id.tv_line_item_name, t.pathName)
        var iv = holder.getView<ImageView>(R.id.iv_line_item_check)
        if(t.isOXLineCheck){
            iv.setImageResource(R.drawable.ic_line_pre)
        }else{
            iv.setImageResource(R.drawable.ic_line_nor)
        }
    }

}