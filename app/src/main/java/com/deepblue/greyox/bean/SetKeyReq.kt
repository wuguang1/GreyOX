package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.Request

/**
 * 室外机器人地图信息上传
 * "key_system_command", "start" | "quit" | "shutdown" | "restart"
 */
class SetKeyReq(io_states: ArrayList<KeySet>) : Request(2030, "设置机器人信息-通用") {

    init {
        json = Data(io_states)
    }

    /**
     * 数据区
     * @param io_states 控制端口列表
     */
    class Data(val key_value: ArrayList<KeySet>)
}
