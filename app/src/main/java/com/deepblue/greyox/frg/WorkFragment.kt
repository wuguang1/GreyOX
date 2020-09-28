package com.deepblue.greyox.frg

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.msg2000.GetErrorHistoryRes
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_work.*

class WorkFragment : BaseFrg() {
    private val mWorkMap by lazy { map_work.map }

    private var mMoveMarker: Marker? = null
    private var mHasRunPolyline: Overlay? = null

    private var data: GetOXMapInfoModel2.MapInfoBean? = null
    private var allSelectMapPoints: ArrayList<LatLng> = ArrayList()
    private var isErrorStoped: Boolean = false   //是否因为故障暂停过
    private val adialog: ErrorDialog by lazy {
        ErrorDialog(context!!)
    }

    override fun create(var1: Bundle?) {
        setContentView(R.layout.frg_work)
        data = HomeFragment.mGroupList[HomeFragment.mCurrentGroup]
        Const.hasRunPosints.clear()
    }

    override fun initView() {
        btn_work_chance.setOnClickListener(this)
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
        sendwebSocket(OXChangeTaskStatusReq().start(0), context)

        allSelectMapPoints.clear()
        data?.let {
//            val minPos = LatLng(it.min_pos.y, it.min_pos.x)
//            val maxPos = LatLng(it.max_pos.y, it.min_pos.x)
//            mWorkMap.setOnMapLoadedCallback(BaiduMap.OnMapLoadedCallback {
//                mWorkMap.animateMapStatus(setLatLngBounds(arrayListOf(minPos, maxPos), map_work))
//            })

            it.greyLineList.forEach { aa ->
                if (aa.isOXLineCheck) {
                    allSelectMapPoints.addAll(aa.map_poly_points)
                    mMoveMarker = BaiduMapUtil.drawMarker(mWorkMap, R.drawable.ic_location, 10, aa.map_poly_points[0], false)
                }
            }
            BaiduMapUtil.drawPointLine(mWorkMap, BaiduMapUtil.mPolylineWith, BaiduMapUtil.mRealTexture, 8, allSelectMapPoints)
            mWorkMap.setOnMapLoadedCallback(BaiduMap.OnMapLoadedCallback {
                mWorkMap.animateMapStatus(setLatLngBounds(allSelectMapPoints, map_work))
            })
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
                val mA = loadBaiDuData(LatLng(Const.systemLatitude, Const.systemLongitude))
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
                        tv_work_area.text = "${oxProRes.planArea} M²"
                        if (oxProRes.taskStartTime.isNotEmpty() && oxProRes.taskStartTime.contains(" ")) {
                            tv_work_starttime.text = oxProRes.taskStartTime.replace(" ", "\n")
                        }
                        tv_work_hasarea.text = "${oxProRes.cleanArea} M²"
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
                sendwebSocket(OXChangeTaskStatusReq().stop(0), context)
            }
        }
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
    }
}
