package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.bean.CleanProInfo

class Oxprogress : Response() {

    init {
        json = CleanProInfo()
    }

    fun getJson(): OxProInfo? {
        val info = JsonUtils.fromJson(json.toString(), OxProInfo::class.java) ?: return null
        return info
    }
}
