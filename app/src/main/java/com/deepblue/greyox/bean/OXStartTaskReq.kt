package com.deepblue.greyox.bean

import com.deepblue.library.planbmsg.Request
import com.deepblue.library.planbmsg.bean.TaskBasicInfo

/**
 * 一般任务格式
 */
class OXStartTaskReq {

    var task_basic_info = TaskBasicInfo()
    var rebackId = 0
    var lineIdList = ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean.LineIdListBean>()
}