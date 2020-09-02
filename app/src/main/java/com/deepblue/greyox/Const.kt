package com.deepblue.greyox

import com.deepblue.library.planbmsg.bean.InitData
import com.deepblue.library.planbmsg.bean.UserInfo
import com.deepblue.library.planbmsg.push.InitDataRes

object Const {

    //机器人自检信息
    @JvmField
    var mInitData: InitData? = null

    //登录信息
    var user: UserInfo? = null
}