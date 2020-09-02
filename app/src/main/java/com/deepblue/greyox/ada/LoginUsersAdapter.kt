package com.deepblue.greyox.ada

import android.content.Context
import com.deepblue.greyox.R
import com.deepblue.library.planbmsg.bean.UserInfo

class LoginUsersAdapter(mContext: Context, mDatas: MutableList<UserInfo>, mLayoutId: Int) :
    BaseAdapter<UserInfo>(mContext, mDatas, mLayoutId) {
    override fun convert(mContext: Context, holder: BaseViewHolder, t: UserInfo, select_position: Int) {
        holder.setText(R.id.tv_name,t.name)
    }

}