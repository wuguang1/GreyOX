package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response

/**
 * 查询机器人电池状态(11002)
 */
class GetBatteryRes : Response() {

    init {
        json = RobotBattery()
    }

    fun getJson(): RobotBattery? {
        return JsonUtils.fromJson(json.toString(), RobotBattery::class.java)
    }
}