package com.deepblue.greyox.frg;

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.ZoomControls
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.Polyline
import com.baidu.mapapi.map.PolylineOptions
import com.baidu.mapapi.model.LatLng
import com.deepblue.greyox.F
import com.deepblue.greyox.R
import com.deepblue.greyox.ada.BaseAdapter
import com.deepblue.greyox.ada.HomeLineAdapter
import com.deepblue.greyox.ada.TaskDoubleAdapter
import com.deepblue.greyox.bean.GetOXMapInfoModel
import com.deepblue.greyox.bean.OXStartTaskReq
import com.deepblue.greyox.util.BaiduMapUtil
import com.deepblue.greyox.view.TimeDownDialog
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.Response
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.EXECUTATION_TYPE_IMMEDIATELY
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_MODE_ONCE
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_PRIORITY_NORMAL
import com.deepblue.library.planbmsg.bean.TaskBasicInfo.Companion.TASK_TYPE_CLEAN
import com.deepblue.library.planbmsg.msg7000.GetMapInfoReq
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_home.*


class HomeFragment : BaseFrg() {
    private val mMap by lazy { map_home.map }

    private val mEdgePolylineWith = 3  //路沿宽度
    private val mEdgePolylineColor = Color.parseColor("#0080FF")  //路沿颜色


    var mGroupList = ArrayList<GetOXMapInfoModel.MapInfoBean>()
    var mChildList = ArrayList<ArrayList<GetOXMapInfoModel.MapInfoBean.GreyAddrListBean>>()
    var mLinesList = ArrayList<GetOXMapInfoModel.MapInfoBean.GreyLineListBean>()

    var mCurrentGroup = -1
    var mCurrentChlid = -1

    private lateinit var mGetOXMapInfoModel: GetOXMapInfoModel
    private lateinit var mDoubleAdapter: TaskDoubleAdapter
    private lateinit var mLineAdapter: HomeLineAdapter

    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_home)
    }

    override fun initView() {
        val p1 = LatLng(31.209933, 121.608515)
        val p2 = LatLng(30.905841, 121.927665)
        val p3 = LatLng(31.049502, 121.432088)
        val p4 = LatLng(31.160318, 121.434962)
        val p5 = LatLng(34.283806, 117.198051)
        val p6 = LatLng(29.545097, 106.568581)
        val p7 = LatLng(34.358342, 108.922285)
        val points: MutableList<LatLng> = ArrayList()
        points.add(p1)
        points.add(p2)
        points.add(p3)
        points.add(p4)
//        points.add(p5)
//        points.add(p6)
//        points.add(p7)


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

//        var builder1 = LatLngBounds.Builder()
//        for (p in points) {
//            builder1 = builder1.include(p)
//        }
//        val mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder1.build())//在合适视野范围内显示所有的点
//        mMap.setMapStatus(mapStatusUpdate)
//        val msu = MapStatusUpdateFactory.zoomTo(13F)
//        mMap.setMapStatus(msu)

//        mMap.addOverlay(BaiduMapUtil().Polyline(points))
//        if (isFrist) {
        mMap.setOnMapLoadedCallback(OnMapLoadedCallback {
            mMap.animateMapStatus(BaiduMapUtil().setLatLngBounds(points, map_home))
        })
//        } else {
//            mMap.animateMapStatus(BaiduMapUtil().setLatLngBounds(points, map_home));
//        }
    }

    private fun initAdapter(contexts: Context) {
        mLineAdapter = HomeLineAdapter(contexts, mLinesList, R.layout.adapter_homeline)
        recycleview_line.adapter = mLineAdapter
        mLineAdapter.notifyDataSetChanged()
        mLineAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                mLinesList[position].isOXLineCheck = !mLinesList[position].isOXLineCheck
                mLineAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun loaddata() {
        sendwebSocket(GetMapInfoReq().reqUpload(), context = context)

        val jsonbuilder = F.fileToJsonString("test.json")

        mGetOXMapInfoModel = JsonUtils.fromJson(jsonbuilder!!, GetOXMapInfoModel::class.java)!!
        mGroupList.clear()
        mChildList.clear()
        mGroupList.addAll(mGetOXMapInfoModel.map_info)

        mGroupList.forEach {
            mChildList.add(it.greyAddrList as ArrayList<GetOXMapInfoModel.MapInfoBean.GreyAddrListBean>)
        }
    }

    private val edialog: TimeDownDialog by lazy {
        TimeDownDialog(context!!)
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
                            oxStartTaskReq.lineIdList.add(GetOXMapInfoModel.MapInfoBean.GreyAddrListBean.LineIdListBean(it.lineId))
                        }
                    }
                    oxStartTaskReq.rebackId = mGroupList[mCurrentGroup].greyPointList[0].id
                    if (oxStartTaskReq.lineIdList.size <= 0) {
                        Helper.toast("请选择任务")
                    } else {
                        sendwebSocket(oxStartTaskReq, context, false)
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
                val mGetOXMapInfoModel = JsonUtils.fromJson(obj.toString(), GetOXMapInfoModel::class.java)
            }
            17002 -> {
                val res = JsonUtils.fromJson(obj.toString(), Response::class.java)
                if (res?.error_code == 0) {
                    Helper.toast("新建任务成功")
//                    Helper.startActivity(context, HomeFragment::class.java, IndexAct::class.java)
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
            val greyLineList = mGetOXMapInfoModel.map_info[groupPosition].greyLineList//字典
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