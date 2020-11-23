package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.bean.ErrorPath

/**
 * 语音播报(24012)
 */
class VoiceRes : Response() {

    init {
        json = Data()
    }

    fun getJson(): Data? {
        return JsonUtils.fromJson(json.toString(), Data::class.java)
    }

    class Data {
        var voiceContext = ""
        var playCnt = ""
        var playTime = ""
        var voiceType = 0
    }
}