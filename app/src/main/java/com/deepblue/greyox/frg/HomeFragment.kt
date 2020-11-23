package com.deepblue.greyox.frg;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.ZoomControls
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.Marker
import com.baidu.mapapi.model.LatLng
import com.deepblue.greyox.Const
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
import com.deepblue.greyox.util.BaiduMapUtil.mPrePolylineWith
import com.deepblue.greyox.util.BaiduMapUtil.mPreTexture
import com.deepblue.greyox.util.BaiduMapUtil.mRealTexture
import com.deepblue.greyox.util.BaiduMapUtil.setLatLngBounds
import com.deepblue.greyox.view.ErrorDialog
import com.deepblue.greyox.view.TimeDownDialog
import com.deepblue.greyox.view.YesOrNODialog
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.EXECUTATION_TYPE_IMMEDIATELY
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_MODE_ONCE
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_PRIORITY_NORMAL
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_TYPE_CLEAN
import com.deepblue.library.planbmsg.msg1000.GetRobotInfoReq
import com.deepblue.library.planbmsg.msg2000.GetErrorHistoryRes
import com.mdx.framework.Frame
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_home.*
import kotlinx.android.synthetic.main.item_yesornodialog.*
import org.jetbrains.anko.doAsync
import kotlin.math.abs


class HomeFragment : BaseFrg() {
    companion object {
        var mGroupList = ArrayList<GetOXMapInfoModel2.MapInfoBean>()
        var mChildList = ArrayList<ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>>()
        var mLinesList = ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyLineListBean>()

        var mCurrentGroup = -1
        var mCurrentChlid = -1
        var mCurrentBackid = -1
    }

    private fun setTaskName() {
        var tempTaskname = ""
        if (mCurrentGroup != -1) {
            tempTaskname = mGroupList[mCurrentGroup].mapName
            if (mCurrentChlid != -1) {
                tempTaskname += " -- " + mChildList[mCurrentGroup][mCurrentChlid].jobAddr + "  :"
            }
        }
        mLinesList.forEach {
            if (it.isOXLineCheck) {
                tempTaskname += it.pathName + "、"
            }
        }
        tv_home_taskname.text = tempTaskname.substring(0, tempTaskname.length - 1)
    }

    private val mMap by lazy { map_home.map }
    private var mMoveMarker: Marker? = null

    private lateinit var mGetOXMapInfoModel2: GetOXMapInfoModel2
    private lateinit var mDoubleAdapter: TaskDoubleAdapter
    private lateinit var mLineAdapter: HomeLineAdapter
    private val edialog: TimeDownDialog by lazy {
        TimeDownDialog(context!!)
    }
    private val bdialog: YesOrNODialog by lazy {
        YesOrNODialog(context!!)
    }

    var customStyleFilePath = ""
    var isMapClear = false

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
//        mMap.setIndoorEnable(true)//开启室内地图  缩放比例到2

        /*       地图深色theme(报错TODO)       */
//        doAsync {
//            customStyleFilePath = getCustomStyleFilePath(context!!, "customConfigdir/custom_config_dark.json") ?: ""
//            if (customStyleFilePath.isNotEmpty()) {
//                map_home.setMapCustomStylePath(customStyleFilePath)
//                map_home.setMapCustomStyleEnable(true)
//            }
//        }

        /*        百度地图Maker点击事件监听        */
        mMap.setOnMarkerClickListener {
            if (mMoveMarker != it) {
                mGetOXMapInfoModel2.map_info[mCurrentGroup].greyPointList.forEach { aa ->
                    aa.mSelectMarker?.remove()
                    if (aa.mMarker == it) {
                        mCurrentBackid = aa.id
                        aa.mSelectMarker = drawMarker2(mMap, R.mipmap.ic_map_todown, 10, aa.mMarker.position, true)
                    }
                }
            }
            return@setOnMarkerClickListener true
        }

    }

    val templist = ArrayList<LatLng>()
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
                        val startMarker =
                            drawMarker(mMap, R.mipmap.ic_startpoint, 9, greyLineListBean.map_poly_points[0], false)
                        val endMarker =
                            drawMarker(mMap, R.mipmap.ic_endpoint, 9, greyLineListBean.map_poly_points[greyLineListBean.map_poly_points.size - 1], false)
                        greyLineListBean.startMarker = startMarker
                        greyLineListBean.endMarker = endMarker
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
                    templist.addAll(greyLineListBean.map_poly_points)
                    mMap.animateMapStatus(setLatLngBounds(templist, map_home))
                } else {
                    try {
                        templist.removeAll(greyLineListBean.map_poly_points)
                        if (templist.size > 2)
                            mMap.animateMapStatus(setLatLngBounds(templist, map_home))
                        greyLineListBean.polyline?.remove()
//                    greyLineListBean.edgpolyline1?.remove()
//                    greyLineListBean.edgpolyline2?.remove()
                        greyLineListBean.startMarker?.remove()
                        greyLineListBean.endMarker?.remove()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                mLineAdapter.notifyDataSetChanged()

                /*设置底部Textview Taskname*/
                setTaskName()
            }
        })
    }

    override fun loaddata() {
        sendwebSocket(GetMapInfoReq().reqUpload(), context, true)
        sendwebSocket(GetRobotInfoReq(), context, true)
        /*       模拟数据使用        */
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

    override fun onResume() {
        super.onResume()
        map_home?.onResume()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_home_start -> {
//                if (Const.systemError) {
//                    bdialog.setSingleBtn("车体出现硬件故障")
//                    bdialog.setOnclickListener(View.OnClickListener { v ->
//                        if (v.id == R.id.tv_YesOrNo_center) {
//                            bdialog.dismiss()
//                            Helper.startActivity(context, FrgErrorList::class.java, TitleActSpecial::class.java)
//                        }
//                        if (v.id == R.id.iv_YesOrNo_close) {
//                            bdialog.dismiss()
//                        }
//                    })
//                    bdialog.show()
//                    return
//                }
                if (mCurrentGroup < 0 && mCurrentChlid < 0) {
                    Helper.toast("请选择地图")
                    return
                }
//                edialog.setOnDismissListener {
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
                oxStartTaskReq.rebackId = if (mCurrentBackid == -1) {
                    mGroupList[mCurrentGroup].greyPointList[0].id
                } else {
                    mCurrentBackid
                }

                if (oxStartTaskReq.lineIdList.size <= 0) {
                    Helper.toast("请选择任务")
                } else {
                    sendwebSocket(OXNewTaskReq(oxStartTaskReq), context, true)
                }
//                }
//                edialog.show()
            }
        }
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            11000 -> {
            }
            10009 -> {
                mMap.clear()
                mLinesList.clear()
                mLineAdapter.notifyDataSetChanged()
                sendwebSocket(GetMapInfoReq().reqUpload(), context, true)

                /*关闭所有父级菜单*/
                val count: Int = mDoubleAdapter.groupCount
                for (i in 0 until count) {
                    expandableListView_home_task.collapseGroup(i)
                }
            }
            17001 -> {
                activity?.runOnUiThread {
                    mGetOXMapInfoModel2 = JsonUtils.fromJson(obj.toString(), OxMapInfoRes::class.java)?.getJson()!!
                    mGetOXMapInfoModel2.initdata()
                    mGroupList.clear()
                    mChildList.clear()
                    mGroupList.addAll(mGetOXMapInfoModel2.map_info)

                    mGroupList.forEach {
                        mChildList.add(it.greyAddrList as ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>)
                    }
                    mDoubleAdapter.notifyDataSetChanged()

                    mMoveMarker =  drawMarker(mMap, R.drawable.ic_location, 10, loadBaiDuData(LatLng(Const.systemLatitude, Const.systemLongitude)), false)
                }
            }
            17002 -> {
                val res = JsonUtils.fromJson(obj.toString(), OxStartTaskRes::class.java)?.getJson()
                res?.let {
                    if (res.status == OxStartTaskRes.ALLOWSTART) {
                        Helper.toast("新建任务成功")
                        Const.selectRoute = mGroupList[mCurrentGroup]
                        Helper.startActivity(context, WorkFragment::class.java, TitleActSpecial::class.java)
                    } else {
                        Helper.toast("任务新建失败,请检查机器人故障列表")
                    }
                }
            }
            17004 -> {
                activity?.runOnUiThread {
                    try {

                        Const.systemLatLng?.let {
                            if (isMapClear) {
                                isMapClear = false
                                mMoveMarker = null
                                mMoveMarker = drawMarker(mMap, R.drawable.ic_location, 10, it, false)
                            } else {
                                mMoveMarker?.position = it
                            }
                            mMoveMarker?.rotate = (if (Const.systemYaw_angle > 0) (360 - Const.systemYaw_angle) else abs(Const.systemYaw_angle)).toFloat()
                        }
                    } catch (e: Exception) {
                    }

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

            templist.clear()
            mMap.clear()
            isMapClear = true

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
                        drawPointLine(mMap, mPrePolylineWith, mPreTexture, 8, _greyLinebean.map_poly_points)
                    }
                }
            }
            mLineAdapter.notifyDataSetChanged()

//            val minPos = LatLng(mGetOXMapInfoModel2.map_info[groupPosition].min_pos.y, mGetOXMapInfoModel2.map_info[groupPosition].min_pos.x)
//            val maxPos = LatLng(mGetOXMapInfoModel2.map_info[groupPosition].max_pos.y, mGetOXMapInfoModel2.map_info[groupPosition].max_pos.x)
//            mMap.animateMapStatus(setLatLngBounds(arrayListOf(minPos, maxPos), map_home))
            /*      因为没有max min_point  所以将所有点聚合----->地图显示合理缩放比列      */
            if (mGetOXMapInfoModel2.map_info[groupPosition].allPoints != null && mGetOXMapInfoModel2.map_info[groupPosition].allPoints.isNotEmpty()) {
                mMap.animateMapStatus(setLatLngBounds(mGetOXMapInfoModel2.map_info[groupPosition].allPoints, map_home))
            }
            /*        绘制预置点      */
            if (mGetOXMapInfoModel2.map_info[groupPosition].greyPointList.isNotEmpty()) {
                mGetOXMapInfoModel2.map_info[groupPosition].greyPointList.forEach {
                    if (it.id != -1) {
                        val drawMarker = drawMarker(mMap, R.mipmap.icon_map_point, 10, loadBaiDuData(LatLng(it.y, it.x)), true)
                        drawMarker.isClickable = true
                        it.mMarker = drawMarker
                    }
                }
                //默认第一个预置点选中
                if (mGetOXMapInfoModel2.map_info[groupPosition].greyPointList[0].id != -1) {
                    mGetOXMapInfoModel2.map_info[groupPosition].greyPointList[0].mSelectMarker =
                        drawMarker2(
                            mMap,
                            R.mipmap.ic_map_todown,
                            10,
                            loadBaiDuData(
                                LatLng(
                                    mGetOXMapInfoModel2.map_info[groupPosition].greyPointList[0].y,
                                    mGetOXMapInfoModel2.map_info[groupPosition].greyPointList[0].x
                                )
                            ),
                            true
                        )
                }
            }

            /*设置显示底部Textview Taskname*/
            setTaskName()
            true
        }/*     二级列表 父级同时之能选中一个      */
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