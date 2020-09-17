package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.bean.CleanProInfo
import com.deepblue.library.planbmsg.bean.ScrubberReport

/**
 * 室外机器人地图信息上传(17001)
 */
class OxMapInfoRes : Response() {

    init {
        json = GetOXMapInfoModel2()
    }

    fun getJson(): GetOXMapInfoModel2? {
        val scrubberReport = JsonUtils.fromJson(json.toString(), GetOXMapInfoModel2::class.java) ?: return null
        return scrubberReport
    }
}