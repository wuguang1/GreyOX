package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.Request

class OXChangeTaskStatusReq : Request(4002, "任务状态修改") {

    companion object {
        const val DELETE = 1
        const val PAUSE = 2
        const val RESUME = 3
        const val STOP = 5
    }

    fun delete(task_id: Int): OXChangeTaskStatusReq {
        number = DELETE
        json = Data("delete", task_id)
        return this
    }

    fun pause(task_id: Int): OXChangeTaskStatusReq {
        number = PAUSE
        json = Data("pause", task_id)
        return this
    }

    fun resume(task_id: Int): OXChangeTaskStatusReq {
        number = RESUME
        json = Data("continue", task_id)
        return this
    }

    fun stop(task_id: Int): OXChangeTaskStatusReq {
        number = STOP
        json = Data("cancel", task_id)
        return this
    }

    fun cancel_lpower(task_id: Int): OXChangeTaskStatusReq {
        number = STOP
        json = Data("cancel_lpower", task_id)
        return this
    }

    fun cancel_people(task_id: Int): OXChangeTaskStatusReq {
        number = STOP
        json = Data("cancel_people", task_id)
        return this
    }

    fun cancel_error(task_id: Int): OXChangeTaskStatusReq {
        number = STOP
        json = Data("cancel_fault", task_id)
        return this
    }

    fun cancel_stop(task_id: Int): OXChangeTaskStatusReq {
        number = STOP
        json = Data("cancel_emerbutton", task_id)
        return this
    }

    /**
     * 数据区
     * @param task_id 任务ID
     * @param status 任务状态
     */
    class Data(val status: String, val task_id: Int? = null)
}