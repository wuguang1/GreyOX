package com.deepblue.greyox.frg

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ZoomControls
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.deepblue.greyox.Const
import com.deepblue.greyox.R
import com.deepblue.greyox.act.TitleActSpecial
import com.deepblue.greyox.bean.GetOXMapInfoModel2
import com.deepblue.greyox.bean.OXChangeTaskStatusReq
import com.deepblue.greyox.bean.Oxprogress
import com.deepblue.greyox.bean.TaskReportRes
import com.deepblue.greyox.util.BaiduMapUtil
import com.deepblue.greyox.util.BaiduMapUtil.loadBaiDuData
import com.deepblue.greyox.util.BaiduMapUtil.mHasRunPolylineColor
import com.deepblue.greyox.util.BaiduMapUtil.mHasRunPolylineWith
import com.deepblue.greyox.util.BaiduMapUtil.setLatLngBounds
import com.deepblue.greyox.view.ErrorDialog
import com.deepblue.greyox.view.TimeDownDialog
import com.deepblue.greyox.view.YesOrNODialog
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.msg2000.GetErrorHistoryRes
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_work.*
import kotlin.math.abs

class WorkFragment : BaseFrg() {
    private val mWorkMap by lazy { map_work.map }

    private var mMoveMarker: Marker? = null
    private var mHasRunPolyline: Overlay? = null

    private var allSelectMapPoints: ArrayList<LatLng> = ArrayList()
    private var isErrorStoped: Boolean = false   //是否因为故障暂停过
    private val adialog by lazy {
        ErrorDialog(context!!)
    }
    private val bdialog by lazy {
        YesOrNODialog(context!!)
    }
    private val edialog: TimeDownDialog by lazy {
        TimeDownDialog(context!!)
    }

    override fun create(var1: Bundle?) {
        setContentView(R.layout.frg_work)
        Const.hasRunPosints.clear()
    }

    override fun initView() {
        btn_work_chance.setOnClickListener(this)
        btn_work_start.setOnClickListener(this)
        btn_work_stop_continu.setOnClickListener(this)

        /*初始化地图设置*/
        var builder = MapStatus.Builder()
        mWorkMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
        val child = map_work.getChildAt(1)//设置地图 logo 显示/隐藏
        if (child != null && (child is ImageView || child is ZoomControls)) {
            child.visibility = View.INVISIBLE
        }
        map_work.showScaleControl(false)
        map_work.showZoomControls(false)
        mWorkMap.setIndoorEnable(true)
    }

    override fun loaddata() {
        allSelectMapPoints.clear()
        Const.selectRoute?.let {
//            val minPos = LatLng(it.min_pos.y, it.min_pos.x)
//            val maxPos = LatLng(it.max_pos.y, it.min_pos.x)
//            mWorkMap.setOnMapLoadedCallback(BaiduMap.OnMapLoadedCallback {
//                mWorkMap.animateMapStatus(setLatLngBounds(arrayListOf(minPos, maxPos), map_work))
//            })

            it.greyLineList.forEach { aa ->
                if (aa.isOXLineCheck) {
                    allSelectMapPoints.addAll(aa.map_poly_points)
                }
            }
            if (allSelectMapPoints.isNotEmpty()) {
                mMoveMarker = BaiduMapUtil.drawMarker(mWorkMap, R.drawable.ic_location, 10, allSelectMapPoints[0], false)
                BaiduMapUtil.drawPointLine(mWorkMap, BaiduMapUtil.mPolylineWith, BaiduMapUtil.mRealTexture, 8, allSelectMapPoints)
                mWorkMap.setOnMapLoadedCallback(BaiduMap.OnMapLoadedCallback {
                    mWorkMap.animateMapStatus(setLatLngBounds(allSelectMapPoints, map_work))
                })
            } else {
                Helper.toast("数据出错,请重试")
            }
        }
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            12028 -> {
                val json = JsonUtils.fromJson(obj.toString(), GetErrorHistoryRes::class.java)?.getJson()
                if (json?.error_msgs !== null && json.error_msgs.isNotEmpty()) {
                    isErrorStoped = true
                    adialog.setErrorData(json.error_msgs)
                    adialog.show()
                    adialog.setOnclickListener(View.OnClickListener { v ->
                        if (v.id == R.id.tv_Yes_center) {
                            adialog.dismiss()
                        }
                    })
                } else {
                    if (isErrorStoped) {
                        sendwebSocket(OXChangeTaskStatusReq().resume(0), context)
                    }
                }
            }
            17004 -> {
//                val mA = getDesBaiduLatLng(Const.systemLatitude, Const.systemLongitude)
                Const.systemLatLng?.let {
                    if (Const.hasRunPosints.size == 0)
                        Const.hasRunPosints.add(it)
                    moveLooper(it)
                    Const.hasRunPosints.add(it)
                }
            }
            24003 -> {
                val oxProRes = JsonUtils.fromJson(obj.toString(), Oxprogress::class.java)!!.getJson()
                if (oxProRes != null) {
                    activity?.runOnUiThread {
                        tv_work_taskname.text = oxProRes.taskName
                        tv_work_long.text = "${String.format("%.2f", oxProRes.planDistance)} KM"
                        tv_work_area.text = "${String.format("%.0f", oxProRes.planArea * 1000)} M²"
                        if (oxProRes.taskStartTime.isNotEmpty() && oxProRes.taskStartTime.contains(" ")) {
                            tv_work_starttime.text = oxProRes.taskStartTime.replace(" ", "\n")
                        }
                        tv_work_hasarea.text = "${String.format("%.2f", oxProRes.cleanArea)} M²"
                        tv_work_percent.text = "${oxProRes.donePercent} %"
                    }
                }
            }
            24011 -> {
                val taskReportRes = JsonUtils.fromJson(obj.toString(), TaskReportRes::class.java)?.getJson()
                if (taskReportRes != null) {
                    Helper.startActivity(context, FrgReportDetail::class.java, TitleActSpecial::class.java)
                    finish()
                }
            }
        }
    }

    private fun moveLooper(endPoint: LatLng) {
        object : Thread() {
            override fun run() {
                activity?.runOnUiThread {
                    try {
                        //更新小车位置
                        mMoveMarker?.position = endPoint
                        //更新小车方向
//                        iv_dirction.rotation = F.mModelStatus.mModelB?.data_yaw_angle?.toFloat()!!
//                        mMoveMarker?.rotate = (if (Const.systemYaw_angle > 0) 360 - Const.systemYaw_angle else kotlin.math.abs(Const.systemYaw_angle)).toFloat()
                        mMoveMarker?.rotate = (if (Const.systemYaw_angle > 0) (360 - Const.systemYaw_angle) else abs(Const.systemYaw_angle)).toFloat()
                        //设置小车已行驶路径
                        mHasRunPolyline?.remove()
                        if (Const.hasRunPosints.size > 1) {
                            val s = PolylineOptions()
                                .width(mHasRunPolylineWith)
                                .zIndex(9)
                                .color(mHasRunPolylineColor)
                                .points(Const.hasRunPosints)
                            mHasRunPolyline = mWorkMap.addOverlay(s)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    override fun setActionBar(actionBar: LinearLayout?) {
        super.setActionBar(actionBar)
        mHead?.setNoBackNoMenu()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_work_stop_continu -> {
                if (btn_work_stop_continu.isSelected) {
                    sendwebSocket(OXChangeTaskStatusReq().resume(0), context)
                    btn_work_stop_continu.text = "暂停"
                } else {
                    sendwebSocket(OXChangeTaskStatusReq().pause(0), context)
                    btn_work_stop_continu.text = "继续"
                }
                btn_work_stop_continu.isSelected = !btn_work_stop_continu.isSelected
            }
            R.id.btn_work_chance -> {
                sendwebSocket(OXChangeTaskStatusReq().stop(0), context, true)
            }
            R.id.btn_work_start -> {
                edialog.setOnDismissListener {
                    sendwebSocket(OXChangeTaskStatusReq().start(0), context, true)
                    btn_work_start.visibility = GONE
                }
                edialog.show()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            bdialog.show()
            bdialog.setTextValue("请确定是否退出任务")
            bdialog.setOnclickListener(View.OnClickListener { v ->
                if (v.id == R.id.tv_YesOrNo_left) {
                    bdialog.dismiss()
                }
                if (v.id == R.id.tv_YesOrNo_right) {
                    sendwebSocket(OXChangeTaskStatusReq().stop(0), context)
                    bdialog.dismiss()
                }
            })
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        map_work?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_work?.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        map_work?.let { it.onDestroy() }
        Const.selectRoute = null
    }
}
