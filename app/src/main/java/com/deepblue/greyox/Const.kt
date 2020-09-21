package com.deepblue.greyox

import com.deepblue.library.planbmsg.bean.InitData
import com.deepblue.library.planbmsg.bean.RobotLoc
import com.deepblue.library.planbmsg.bean.UserInfo
import com.deepblue.library.planbmsg.push.InitDataRes

object Const {

    //机器人自检信息
    @JvmField
    var mInitData: InitData? = null

    //登录信息
    var user: UserInfo? = null

    //机器人是否联网
    @JvmField
    var system4G: Boolean = false

    //机器人GPS信号
    @JvmField
    var systemLocation: Boolean = false

    //机器人电量
    @JvmField
    var systemPower: Int = 0

    //机器人有故障
    @JvmField
    var systemError: Boolean = false

    //机器人时间
    @JvmField
    var systemTime: Long = 0

}