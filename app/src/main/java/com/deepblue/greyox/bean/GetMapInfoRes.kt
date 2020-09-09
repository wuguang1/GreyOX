package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.bean.Map
import com.deepblue.library.planbmsg.bean.MapInfoBase
import com.deepblue.library.planbmsg.bean.MapPoint

/**
 * 室外机器人地图信息上传(17001)
 */
class GetMapInfoRes : Response() {

    init {
        json = Map()
    }

    fun getJson(): Map? {
        val map = JsonUtils.fromJson(json.toString(), Map::class.java)
        map?.resolution = map?.map_info?.resolution?:1.0
        return map
    }

}