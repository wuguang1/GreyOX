package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.Request
import com.deepblue.library.planbmsg.bean.TaskBasicInfo
import com.deepblue.library.planbmsg.bean.TaskRange
import java.io.Serializable

/**
 * 一般任务格式
 */
class OXGetReportReq(from: Long = 0, to: Long = 0) : Request(7003, "获取灰犀牛任务报告（7003）") {

    init {
        json = Data(from, to)
    }

    class Data(val from: Long, val to: Long)
}