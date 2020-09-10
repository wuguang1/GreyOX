package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response

class GetTaskReportsRes : Response() {

    init {
        json = Data()
    }

    fun getJson(): Data? {
        return JsonUtils.fromJson(json.toString(), Data::class.java)
    }

    class Data {
        val reports = ArrayList<TaskReport>()
        var totalDistance: Long? = null
        var totalCostEnergy: Long? = null
    }
}