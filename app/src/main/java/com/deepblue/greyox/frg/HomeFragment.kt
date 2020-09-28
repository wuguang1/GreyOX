package com.deepblue.greyox.frg;

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.ZoomControls
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.model.LatLng
import com.deepblue.greyox.R
import com.deepblue.greyox.act.TitleActSpecial
import com.deepblue.greyox.ada.BaseAdapter
import com.deepblue.greyox.ada.HomeLineAdapter
import com.deepblue.greyox.ada.TaskDoubleAdapter
import com.deepblue.greyox.bean.*
import com.deepblue.greyox.util.BaiduMapUtil
import com.deepblue.greyox.util.BaiduMapUtil.drawMarker
import com.deepblue.greyox.util.BaiduMapUtil.drawMarker2
import com.deepblue.greyox.util.BaiduMapUtil.drawPointLine
import com.deepblue.greyox.util.BaiduMapUtil.getCustomStyleFilePath
import com.deepblue.greyox.util.BaiduMapUtil.loadBaiDuData
import com.deepblue.greyox.util.BaiduMapUtil.mPolylineColor
import com.deepblue.greyox.util.BaiduMapUtil.mPolylineWith
import com.deepblue.greyox.util.BaiduMapUtil.mRealTexture
import com.deepblue.greyox.util.BaiduMapUtil.setLatLngBounds
import com.deepblue.greyox.view.ErrorDialog
import com.deepblue.greyox.view.TimeDownDialog
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.EXECUTATION_TYPE_IMMEDIATELY
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_MODE_ONCE
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_PRIORITY_NORMAL
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_TYPE_CLEAN
import com.deepblue.library.planbmsg.msg2000.GetErrorHistoryRes
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_home.*
import org.jetbrains.anko.doAsync


class HomeFragment : BaseFrg() {
    companion object {
        var mGroupList = ArrayList<GetOXMapInfoModel2.MapInfoBean>()
        var mChildList = ArrayList<ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>>()
        var mLinesList = ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyLineListBean>()

        var mCurrentGroup = -1
        var mCurrentChlid = -1
        var mCurrentBackid = -1
    }

    private val mMap by lazy { map_home.map }

    private lateinit var mGetOXMapInfoModel2: GetOXMapInfoModel2
    private lateinit var mDoubleAdapter: TaskDoubleAdapter
    private lateinit var mLineAdapter: HomeLineAdapter
    private val edialog: TimeDownDialog by lazy {
        TimeDownDialog(context!!)
    }
    private val adialog: ErrorDialog by lazy {
        ErrorDialog(context!!)
    }

    var customStyleFilePath = ""
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_home)
    }

    override fun initView() {
        btn_home_start.setOnClickListener(this)

        initRecycleView()
        initExpandList()
        var builder = MapStatus.Builder()
//        mMap.compassPosition = Point(110, 265) //指南针位置
        mMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
//        mMap.mapType = BaiduMap.MAP_TYPE_SATELLITE //地图卫星
//        mMap.isTrafficEnabled = true  //交通
        val child = map_home.getChildAt(1)//设置地图 logo 显示/隐藏
        if (child != null && (child is ImageView || child is ZoomControls)) {
            child.visibility = View.INVISIBLE
        }
        map_home.showScaleControl(false)//比例尺 显示/隐藏
        map_home.showZoomControls(false)//缩放按钮 显示/隐藏
        mMap.setIndoorEnable(true)//开启室内地图  缩放比例到2

        /*       地图深色theme(报错TODO)       */
        doAsync {
            customStyleFilePath = getCustomStyleFilePath(context!!, "customConfigdir/custom_config_dark.json") ?: ""
            if (customStyleFilePath.isNotEmpty()) {
                map_home.setMapCustomStylePath(customStyleFilePath)
                map_home.setMapCustomStyleEnable(true)
            }
        }

        /*        百度地图Maker点击事件监听        */
        mMap.setOnMarkerClickListener {
            mGetOXMapInfoModel2.map_info[mCurrentGroup].greyPointList.forEach { aa ->
                aa.mSelectMarker?.remove()
                if (aa.mMarker == it) {
                    mCurrentBackid = aa.id
                    aa.mSelectMarker = drawMarker2(mMap, R.mipmap.ic_map_todown, 10, aa.mMarker.position, true)
                }
            }
            return@setOnMarkerClickListener true
        }

    }

    private fun initAdapter(contexts: Context) {
        mLineAdapter = HomeLineAdapter(contexts, mLinesList, R.layout.adapter_homeline)
        recycleview_line.adapter = mLineAdapter
        mLineAdapter.notifyDataSetChanged()
        mLineAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val greyLineListBean = mLinesList[position]
                greyLineListBean.isOXLineCheck = !greyLineListBean.isOXLineCheck
                if (greyLineListBean.isOXLineCheck) {
                    /*   绘制地图   */
                    if (greyLineListBean.map_poly_points != null && greyLineListBean.map_poly_points.size > 0) {
                        val drawPointLine = drawPointLine(mMap, mPolylineWith, mRealTexture, 8, greyLineListBean.map_poly_points)
                        greyLineListBean.polyline = drawPointLine
                    }
//                    if (greyLineListBean.map_edg1_points != null && greyLineListBean.map_edg1_points.size > 0) {
//                        val edgLine1 = BaiduMapUtil.drawLine(
//                            mMap, mEdgePolylineWith, mEdgePolylineColor, 8,
//                            greyLineListBean.map_edg1_points
//                        )
//                        greyLineListBean.edgpolyline1 = edgLine1
//                    }
//                    if (greyLineListBean.map_edg2_points != null && greyLineListBean.map_edg2_points.size > 0) {
//                        val edgLine2 = BaiduMapUtil.drawLine(
//                            mMap, mEdgePolylineWith, mEdgePolylineColor, 8,
//                            greyLineListBean.map_edg2_points
//                        )
//                        greyLineListBean.edgpolyline2 = edgLine2
//                    }
//                    /*   定位缩放地图   */
                    val list = ArrayList<LatLng>()
                    mMap.animateMapStatus(setLatLngBounds(greyLineListBean.map_poly_points, map_home))
                } else {
                    greyLineListBean.polyline?.remove()
//                    greyLineListBean.edgpolyline1?.remove()
//                    greyLineListBean.edgpolyline2?.remove()
                }
                mLineAdapter.notifyDataSetChanged()
//                if (false) {
//                    mMap.setOnMapLoadedCallback(OnMapLoadedCallback {
//                        mMap.animateMapStatus(setLatLngBounds(loadBaiDuData2(mLinesList[position].path1List), map_home))
//                    })
//                } else {
//                    mMap.animateMapStatus(setLatLngBounds(loadBaiDuData2(mLinesList[position].path1List), map_home))
//                }
            }
        })
    }

    override fun loaddata() {
        greyOXApplication.isStartRequestError = true
        sendwebSocket(GetMapInfoReq().reqUpload(), context, true)

//        //TODO
//        val jsonbuilder = F.fileToJsonString("test2.json")
//
//        mGetOXMapInfoModel2 = JsonUtils.fromJson(jsonbuilder!!, OxMapInfoRes::class.java)?.getJson()!!
//        mGetOXMapInfoModel2.initdata()
//        mGroupList.clear()
//        mChildList.clear()
//        mGroupList.addAll(mGetOXMapInfoModel2.map_info)
//
//        mGroupList.forEach {
//            mChildList.add(it.greyAddrList as ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>)
//        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_home_start -> {
                if (mCurrentGroup < 0 && mCurrentChlid < 0) {
                    Helper.toast("请选择地图")
                    return
                }
                if (mGroupList[mCurrentGroup].greyPointList.size <= 0) {
                    Helper.toast("该地图没有预置返回点")
                    return
                }
                edialog.setOnDismissListener {
                    val oxStartTaskReq = OXStartTaskReq()
                    oxStartTaskReq.task_basic_info.task_id = 0
                    oxStartTaskReq.task_basic_info.task_type = TASK_TYPE_CLEAN
                    oxStartTaskReq.task_basic_info.task_status = TASK_MODE_ONCE
                    oxStartTaskReq.task_basic_info.task_mode = 0
                    oxStartTaskReq.task_basic_info.task_name =
                        mGroupList[mCurrentGroup].greyAddrList[mCurrentChlid].jobAddr + "/" + mGroupList[mCurrentGroup].greyAddrList[mCurrentChlid].jobName
                    oxStartTaskReq.task_basic_info.executation_type = EXECUTATION_TYPE_IMMEDIATELY
                    oxStartTaskReq.task_basic_info.task_priority = TASK_PRIORITY_NORMAL
                    oxStartTaskReq.task_basic_info.map_id = mGroupList[mCurrentGroup].mapId
                    mLinesList.forEach {
                        if (it.isOXLineCheck) {
                            val lineIdListBean = GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean.LineIdListBean()
                            lineIdListBean.id = it.lineId
                            oxStartTaskReq.lineIdList.add(lineIdListBean)
                        }
                    }
                    if (mCurrentBackid == -1) {

                    }
                    oxStartTaskReq.rebackId = mCurrentBackid
                    if (oxStartTaskReq.lineIdList.size <= 0) {
                        Helper.toast("请选择任务")
                    } else {
                        sendwebSocket(OXNewTaskReq(oxStartTaskReq), context, true)
                    }
                }
                edialog.show()
            }
        }
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            17001 -> {
                mGetOXMapInfoModel2 = JsonUtils.fromJson(obj.toString(), OxMapInfoRes::class.java)?.getJson()!!
                mGetOXMapInfoModel2.initdata()
                mGroupList.clear()
                mChildList.clear()
                mGroupList.addAll(mGetOXMapInfoModel2.map_info)

                mGroupList.forEach {
                    mChildList.add(it.greyAddrList as ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>)
                }
            }
            17002 -> {
                val res = JsonUtils.fromJson(obj.toString(), OxStartTaskRes::class.java)?.getJson()
                res?.let {
                    if (res.status == OxStartTaskRes.ALLOWSTART) {
                        Helper.toast("新建任务成功")
                        Helper.startActivity(context, WorkFragment::class.java, TitleActSpecial::class.java)
                    } else {
                        Helper.toast("任务新建失败,请检查机器人故障列表")
                    }
                }
            }
            12028 -> {
                val json = JsonUtils.fromJson(obj.toString(), GetErrorHistoryRes::class.java)?.getJson()
                if (json?.error_msgs !== null && json.error_msgs.isNotEmpty()) {
                    adialog.setErrorData(json.error_msgs)
                    adialog.show()
                    adialog.setOnclickListener(View.OnClickListener { v ->
                        if (v.id == R.id.tv_Yes_center) {
                            adialog.dismiss()
                        }
                    })
                }
            }
        }
    }

    private fun initRecycleView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycleview_line.layoutManager = layoutManager
        initAdapter(context!!)
    }

    private fun initExpandList() {
        mDoubleAdapter = TaskDoubleAdapter(mGroupList, mChildList, context)
        expandableListView_home_task.setAdapter(mDoubleAdapter)

        expandableListView_home_task.setOnGroupClickListener { parent, v, groupPosition, id ->
            val groupExpanded = parent.isGroupExpanded(groupPosition)
            if (groupExpanded) {
                parent.collapseGroup(groupPosition)
            } else {
                parent.expandGroup(groupPosition, true)
            }
            mDoubleAdapter.setIndicatorState(groupPosition, groupExpanded)
            true
        }
        expandableListView_home_task.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            mCurrentGroup = groupPosition
            mCurrentChlid = childPosition
            /*   更新当前位置   */
            mChildList.forEach {
                it.forEach { aa ->
                    aa.isOXSelect = false
                }
            }
            mChildList[groupPosition][childPosition].isOXSelect = true
            expandableListView_home_task.collapseGroup(groupPosition)
            expandableListView_home_task.expandGroup(groupPosition)
            /* expandableListView子item选中状态   ------    点击子item  更新右侧预置line列表 */
            mLinesList.clear()
            val greyLineList = mGetOXMapInfoModel2.map_info[groupPosition].greyLineList//字典
            val lineIdList = mChildList[groupPosition][childPosition].lineIdList
            lineIdList.forEach { _lineId ->
                greyLineList.forEach { _greyLinebean ->
                    _greyLinebean.isOXLineCheck = false
                    if (_greyLinebean.lineId == _lineId.id) {
                        mLinesList.add(_greyLinebean)
                    }
                }
            }
            mLineAdapter.notifyDataSetChanged()

            mMap.clear()
//            val minPos = LatLng(mGetOXMapInfoModel2.map_info[groupPosition].min_pos.y, mGetOXMapInfoModel2.map_info[groupPosition].min_pos.x)
//            val maxPos = LatLng(mGetOXMapInfoModel2.map_info[groupPosition].max_pos.y, mGetOXMapInfoModel2.map_info[groupPosition].max_pos.x)
//            mMap.animateMapStatus(setLatLngBounds(arrayListOf(minPos, maxPos), map_home))
            if (mGetOXMapInfoModel2.map_info[groupPosition].allPoints != null && mGetOXMapInfoModel2.map_info[groupPosition].allPoints.isNotEmpty())
                mMap.animateMapStatus(setLatLngBounds(mGetOXMapInfoModel2.map_info[groupPosition].allPoints, map_home))
            mGetOXMapInfoModel2.map_info[groupPosition].greyPointList.forEach {
                val drawMarker = drawMarker(mMap, R.mipmap.icon_map_point, 10, loadBaiDuData(LatLng(it.y, it.x)), true)
                drawMarker.isClickable = true
                it.mMarker = drawMarker
//                val drawMarker2 = drawMarker2(mMap, R.mipmap.ic_map_todown, 10, loadBaiDuData(LatLng(it.y, it.x)), true)
            }
            true
        }
        expandableListView_home_task.setOnGroupExpandListener(object : ExpandableListView.OnGroupExpandListener {
            override fun onGroupExpand(groupPosition: Int) {
                mCurrentGroup = groupPosition
                val count: Int = mDoubleAdapter.groupCount
                for (i in 0 until count) {
                    if (i != groupPosition) {
                        expandableListView_home_task.collapseGroup(i)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        map_home?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_home?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRealTexture?.recycle()
        mMap?.clear()
        map_home?.onDestroy()
    }
}