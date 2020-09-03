package com.deepblue.greyox.frg;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepblue.greyox.F
import com.deepblue.greyox.R
import com.deepblue.greyox.ada.BaseAdapter
import com.deepblue.greyox.ada.HomeLineAdapter
import com.deepblue.greyox.ada.TaskDoubleAdapter
import com.deepblue.greyox.bean.GetOXMapInfoModel
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.msg7000.GetMapInfoReq
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_home.*
import okhttp3.internal.notifyAll


class HomeFragment : BaseFrg() {
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
        btn_home_start.setOnClickListener(this)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycleview_line.layoutManager = layoutManager
        initAdapter(context!!)

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
            val greyLineList = mGetOXMapInfoModel.map_info[groupPosition].greyLineList
            val lineIdList = mChildList[groupPosition][childPosition].lineIdList
            lineIdList.forEach { _lineId ->
                greyLineList.forEach { _greyLinebean ->
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
        mGroupList.addAll(mGetOXMapInfoModel!!.map_info)

        mGroupList.forEach {
            mChildList.add(it.greyAddrList as ArrayList<GetOXMapInfoModel.MapInfoBean.GreyAddrListBean>)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_home_start -> {
                if(mCurrentGroup != -1 && mCurrentChlid != -1){
                    Log.e("tag", "mapname--${mGroupList[mCurrentGroup].mapName};areaname---${mGroupList[mCurrentGroup].greyAddrList[mCurrentChlid].jobAddr}")
                    mLinesList.forEach {
                        if(it.isOXLineCheck){
                            Log.e("tag", "lines---${it.pathName}")
                        }
                    }
                }else{
                    Helper.toast("请选择任务")
                }
            }
        }
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            17001 -> {
                val mGetOXMapInfoModel = JsonUtils.fromJson(obj.toString(), GetOXMapInfoModel::class.java)
            }
        }
    }
}