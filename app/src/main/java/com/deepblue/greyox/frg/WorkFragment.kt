package com.deepblue.greyox.frg

import android.os.Bundle
import android.view.View
import com.deepblue.greyox.R
import com.deepblue.greyox.bean.GetOXMapInfoModel2
import com.deepblue.greyox.bean.OXChangeTaskStatusReq
import com.deepblue.greyox.bean.Oxprogress
import com.deepblue.greyox.bean.TaskReportRes
import com.deepblue.greyox.frg.HomeFragment.Companion.MAPINFO
import com.deepblue.library.planbmsg.JsonUtils
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_work.*

class WorkFragment : BaseFrg() {
    private lateinit var data: GetOXMapInfoModel2.MapInfoBean
    override fun create(var1: Bundle?) {
        setContentView(R.layout.frg_work)
        data = activity?.intent?.getSerializableExtra(MAPINFO) as GetOXMapInfoModel2.MapInfoBean
    }

    override fun initView() {
        btn_work_chance.setOnClickListener(this)
        btn_work_stop_continu.setOnClickListener(this)
    }

    override fun loaddata() {
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            24003 -> {
                val oxProRes = JsonUtils.fromJson(obj.toString(), Oxprogress::class.java)!!.getJson()
                if (oxProRes != null) {
                    activity?.runOnUiThread {
                        tv_work_taskname.text = oxProRes.taskName
                        tv_work_long.text = "${oxProRes.planDistance} KM"
                        tv_work_area.text = "${oxProRes.planArea} m²"
                        tv_work_starttime.text = oxProRes.taskStartTime
                        tv_work_hasarea.text = "${oxProRes.cleanArea} m²"
                        tv_work_percent.text = "${oxProRes.donePercent} %"
                    }
                }
            }
            24011 -> {
                val taskReportRes = JsonUtils.fromJson(obj.toString(), TaskReportRes::class.java)?.getJson()
                if (taskReportRes != null) {
                    Helper.startActivity(context, FrgReportDetail::class.java, TitleAct::class.java)
                    finish()
                }
            }
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_work_stop_continu -> {
                if (btn_work_stop_continu.isSelected) {
                    sendwebSocket(OXChangeTaskStatusReq().resume(0), context)
                } else {
                    sendwebSocket(OXChangeTaskStatusReq().pause(0), context)
                }
                btn_work_stop_continu.isSelected = !btn_work_stop_continu.isSelected
            }
            R.id.btn_work_chance -> {
                sendwebSocket(OXChangeTaskStatusReq().stop(0), context)
            }
        }
    }

}
