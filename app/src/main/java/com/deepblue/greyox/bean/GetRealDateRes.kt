package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response

/**
 * 室外机器人实时数据上传（7004）
 */
class GetRealDateRes : Response() {

    companion object {
        var LATITUDE = "data_latitude"
        var LONGITUDE = "data_longitude"
        var YAW_ANGLE = "data_yaw_angle"     // 车头方向
        var GPS_SIGNAL = "data_gps_signal"   // GPS状态信息   -1: offline   0-100
    }

    init {
        json = Data()
    }

    fun getJson(): Data? {
        return JsonUtils.fromJson(json.toString(), Data::class.java)
    }

    class Data {
        var realdatainfo: List<RealdatainfoBean>? = null

        class RealdatainfoBean {
            /**
             * key : latitude
             * value : 31.81944
             */
            var key: String = ""
            var value = 0.0
        }
    }
}