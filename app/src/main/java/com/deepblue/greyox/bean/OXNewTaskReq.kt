package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.Request

class OXNewTaskReq : Request {

    constructor(task: OXStartTaskReq) : super(7002, "室外机器人任务下发请求（7002）") {
        json = task
    }
}