package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.Request

/**
 * 室外机器人地图信息上传
 */
class GetMapInfoReq : Request(7001, "室外机器人地图信息上传") {

    companion object {
        const val REQUEST_UPLOAD = 1 //请求上传
    }

    fun reqUpload(): GetMapInfoReq {
        json = Data(REQUEST_UPLOAD)
        return this
    }

    class Data(val status: Int)
}
