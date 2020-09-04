package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.Request
import com.deepblue.library.planbmsg.bean.TaskBasicInfo
import com.deepblue.library.planbmsg.bean.TaskRange
import java.io.Serializable

/**
 * 一般任务格式
 */
class OXStartTaskReq : Request(7002, "室外机器人任务下发请求（7002）") {

    var task_basic_info = TaskBasicInfo()
    var rebackId = 0
    var lineIdList = ArrayList<GetOXMapInfoModel.MapInfoBean.GreyAddrListBean.LineIdListBean>()
}