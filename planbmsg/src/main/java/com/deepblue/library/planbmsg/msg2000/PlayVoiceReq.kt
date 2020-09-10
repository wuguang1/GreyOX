package com.deepblue.library.planbmsg.msg2000

import com.deepblue.library.planbmsg.Request

class PlayVoiceReq(voice_text: String) : Request(2026, "播放语音") {
    init {
        json = Data(voice_text)
    }

    class Data(val voice_text: String)
}