package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.bean.CleanProInfo
import com.deepblue.library.planbmsg.bean.ScrubberReport

/**
 * 下发任务(17002)
 */
class OxStartTaskRes : Response() {

    companion object {
        val ALLOWSTART = 0
        val NOTSTART = 1
    }

    init {
        json = Data()
    }

    fun getJson(): Data? {
        return JsonUtils.fromJson(json.toString(), Data::class.java)
    }

    class Data {
        var status = NOTSTART
    }
}