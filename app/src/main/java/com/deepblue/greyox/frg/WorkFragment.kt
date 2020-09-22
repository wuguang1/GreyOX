package com.deepblue.greyox.frg

import android.os.Bundle
import android.util.Log
import android.view.View
import com.baidu.mapapi.map.Marker
import com.baidu.mapapi.map.Overlay
import com.baidu.mapapi.map.PolylineOptions
import com.baidu.mapapi.model.LatLng
import com.deepblue.greyox.Const
import com.deepblue.greyox.R
import com.deepblue.greyox.bean.GetOXMapInfoModel2
import com.deepblue.greyox.bean.OXChangeTaskStatusReq
import com.deepblue.greyox.bean.Oxprogress
import com.deepblue.greyox.bean.TaskReportRes
import com.deepblue.greyox.frg.HomeFragment.Companion.MAPINFO
import com.deepblue.greyox.util.BaiduMapUtil
import com.deepblue.greyox.util.BaiduMapUtil.mHasRunPolylineColor
import com.deepblue.greyox.util.BaiduMapUtil.mHasRunPolylineWith
import com.deepblue.library.planbmsg.JsonUtils
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_work.*

class WorkFragment : BaseFrg() {
    private val mMap by lazy { map_work.map }
//    private lateinit var data: GetOXMapInfoModel2.MapInfoBean
    private var mMoveMarker: Marker? = null

    private var mHasRunPolyline: Overlay? = null

    override fun create(var1: Bundle?) {
        setContentView(R.layout.frg_work)
//        data = activity?.intent?.getSerializableExtra(MAPINFO) as GetOXMapInfoModel2.MapInfoBean
        Const.hasRunPosints.clear()
    }

    override fun initView() {
        btn_work_chance.setOnClickListener(this)
        btn_work_stop_continu.setOnClickListener(this)
//        data.greyLineList.forEach {
//            if (it.isOXLineCheck) {
//                mMoveMarker = BaiduMapUtil.drawMarker(mMap, R.drawable.ic_location, 10, it.map_poly_points[0], false)
//                return
//            }
//        }
    }

    override fun loaddata() {
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            17004 -> {
//                val mA = getDesBaiduLatLng(Const.systemLatitude, Const.systemLongitude)
                val mA = LatLng(Const.systemLatitude, Const.systemLongitude)
                if (Const.hasRunPosints.size == 0)
                    Const.hasRunPosints.add(mA)
                moveLooper(mA)
                Const.hasRunPosints.add(mA)
            }
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

    private fun moveLooper(endPoint: LatLng) {
        object : Thread() {
            override fun run() {
                mMoveMarker?.position = endPoint
                activity?.runOnUiThread {
                    try {
                        //更新小车方向
//                        iv_dirction.rotation = F.mModelStatus.mModelB?.data_yaw_angle?.toFloat()!!
                        mMoveMarker?.rotate = (if (Const.systemYaw_angle > 0) 360 - Const.systemYaw_angle else kotlin.math.abs(Const.systemYaw_angle)).toFloat()
                        Log.e("websocket_route_angle", "route方向====(" + mMoveMarker?.rotate + ")")
                        //设置小车已行驶路径
                        mHasRunPolyline?.remove()
                        if (Const.hasRunPosints.size > 1) {
                            val s = PolylineOptions()
                                .width(mHasRunPolylineWith)
                                .zIndex(9)
                                .color(mHasRunPolylineColor)
                                .points(Const.hasRunPosints)
                            mHasRunPolyline = mMap.addOverlay(s)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
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
