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
import com.deepblue.greyox.F
import com.deepblue.greyox.R
import com.deepblue.greyox.ada.BaseAdapter
import com.deepblue.greyox.ada.HomeLineAdapter
import com.deepblue.greyox.ada.TaskDoubleAdapter
import com.deepblue.greyox.bean.GetMapInfoReq
import com.deepblue.greyox.bean.GetOXMapInfoModel2
import com.deepblue.greyox.bean.OXStartTaskReq
import com.deepblue.greyox.bean.OxMapInfoRes
import com.deepblue.greyox.util.BaiduMapUtil
import com.deepblue.greyox.util.BaiduMapUtil.drawMarker
import com.deepblue.greyox.util.BaiduMapUtil.mEdgePolylineColor
import com.deepblue.greyox.util.BaiduMapUtil.mEdgePolylineWith
import com.deepblue.greyox.util.BaiduMapUtil.mPolylineColor
import com.deepblue.greyox.util.BaiduMapUtil.mPolylineWith
import com.deepblue.greyox.util.BaiduMapUtil.setLatLngBounds
import com.deepblue.greyox.view.TimeDownDialog
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.EXECUTATION_TYPE_IMMEDIATELY
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_MODE_ONCE
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_PRIORITY_NORMAL
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_TYPE_CLEAN
import com.mdx.framework.activity.IndexAct
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_home.*


class HomeFragment : BaseFrg() {
    companion object {
        var MAPINFO = "mapinfo"
    }

    private val mMap by lazy { map_home.map }

    var mGroupList = ArrayList<GetOXMapInfoModel2.MapInfoBean>()
    var mChildList = ArrayList<ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>>()
    var mLinesList = ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyLineListBean>()

    var mCurrentGroup = -1
    var mCurrentChlid = -1

    private lateinit var mGetOXMapInfoModel2: GetOXMapInfoModel2
    private lateinit var mDoubleAdapter: TaskDoubleAdapter
    private lateinit var mLineAdapter: HomeLineAdapter
    private val edialog: TimeDownDialog by lazy {
        TimeDownDialog(context!!)
    }

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

    }

    private fun initAdapter(contexts: Context) {
        mLineAdapter = HomeLineAdapter(contexts, mLinesList, R.layout.adapter_homeline)
        recycleview_line.adapter = mLineAdapter
        mLineAdapter.notifyDataSetChanged()
        mLineAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                mLinesList[position].isOXLineCheck = !mLinesList[position].isOXLineCheck
                if (mLinesList[position].isOXLineCheck) {
                    /*   绘制地图   */
                    if (mLinesList[position].map_poly_points != null && mLinesList[position].map_poly_points.size > 0) {
                        val drawLine = BaiduMapUtil.drawLine(
                            mMap, mPolylineWith, mPolylineColor, 8,
                            mLinesList[position].map_poly_points
                        )
                        mLinesList[position].polyline = drawLine
                    }
                    if (mLinesList[position].map_edg1_points != null && mLinesList[position].map_edg1_points.size > 0) {
                        val edgLine1 = BaiduMapUtil.drawLine(
                            mMap, mEdgePolylineWith, mEdgePolylineColor, 8,
                            mLinesList[position].map_edg1_points
                        )
                        mLinesList[position].edgpolyline1 = edgLine1
                    }
                    if (mLinesList[position].map_edg2_points != null && mLinesList[position].map_edg2_points.size > 0) {
                        val edgLine2 = BaiduMapUtil.drawLine(
                            mMap, mEdgePolylineWith, mEdgePolylineColor, 8,
                            mLinesList[position].map_edg2_points
                        )
                        mLinesList[position].edgpolyline2 = edgLine2
                    }
//                    /*   定位缩放地图   */
//                    val list = ArrayList<LatLng>()
//                    mMap.animateMapStatus(setLatLngBounds(mLinesList[position].map_poly_points, map_home))
                } else {
                    mLinesList[position].polyline?.remove()
                    mLinesList[position].edgpolyline1?.remove()
                    mLinesList[position].edgpolyline2?.remove()
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
        sendwebSocket(GetMapInfoReq().reqUpload(), context, true)

        //TODO
        val jsonbuilder = F.fileToJsonString("test2.json")

        mGetOXMapInfoModel2 = JsonUtils.fromJson(jsonbuilder!!, OxMapInfoRes::class.java)?.getJson()!!
        mGetOXMapInfoModel2.initdata()
        mGroupList.clear()
        mChildList.clear()
        mGroupList.addAll(mGetOXMapInfoModel2.map_info)

        mGroupList.forEach {
            mChildList.add(it.greyAddrList as ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>)
        }
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
                    oxStartTaskReq.task_basic_info.task_name = mGroupList[mCurrentGroup].mapName
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
                    oxStartTaskReq.rebackId = mGroupList[mCurrentGroup].greyPointList[0].id
                    if (oxStartTaskReq.lineIdList.size <= 0) {
                        Helper.toast("请选择任务")
                    } else {
                        sendwebSocket(oxStartTaskReq, context, true)
                        //TODO
                        Helper.startActivity(context, WorkFragment::class.java, TitleAct::class.java, MAPINFO, mGroupList[mCurrentGroup])
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
//                mGetOXMapInfoModel2 = JsonUtils.fromJson(obj.toString(), OxMapInfoRes::class.java)?.getJson()!!
//                mGetOXMapInfoModel2.initdata()
//                mGroupList.clear()
//                mChildList.clear()
//                mGroupList.addAll(mGetOXMapInfoModel2.map_info)
//
//                mGroupList.forEach {
//                    mChildList.add(it.greyAddrList as ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>)
//                }
            }
            17002 -> {
                val res = JsonUtils.fromJson(obj.toString(), Response::class.java)
                if (res?.error_code == 0) {
                    Helper.toast("新建任务成功")
                    Helper.startActivity(context, WorkFragment::class.java, TitleAct::class.java, MAPINFO, mGroupList[mCurrentGroup])
                } else {
                    Helper.toast("任务新建失败,请检查机器人状态")
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
            val minPos = LatLng(mGetOXMapInfoModel2.map_info[groupPosition].min_pos.y, mGetOXMapInfoModel2.map_info[groupPosition].min_pos.x)
            val maxPos = LatLng(mGetOXMapInfoModel2.map_info[groupPosition].max_pos.y, mGetOXMapInfoModel2.map_info[groupPosition].max_pos.x)
            mMap.animateMapStatus(setLatLngBounds(arrayListOf(minPos, maxPos), map_home))
            mGetOXMapInfoModel2.map_info[groupPosition].greyPointList.forEach {
                val drawMarker = drawMarker(mMap, R.mipmap.icon_map_point, 10, LatLng(it.y, it.x))
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
}