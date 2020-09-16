package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.bean.CleanProInfo
import com.deepblue.library.planbmsg.bean.ScrubberReport

class TaskReportRes : Response() {

    init {
        json = ScrubberReport()
    }

    fun getJson(): TaskReport? {
        val scrubberReport = JsonUtils.fromJson(json.toString(), TaskReport::class.java) ?: return null
        return scrubberReport
    }
}