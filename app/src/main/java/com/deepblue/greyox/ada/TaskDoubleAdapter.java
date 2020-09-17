package com.deepblue.greyox.ada;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepblue.greyox.R;
import com.deepblue.greyox.bean.GetOXMapInfoModel2;

import java.util.ArrayList;

public class TaskDoubleAdapter extends BaseExpandableListAdapter {
    //数据
    private ArrayList<GetOXMapInfoModel2.MapInfoBean> group;
    private ArrayList<ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>> child;

    private Context context;
    private GroupViewHolder groupViewHolder;
    private ChildViewHolder childViewHolder;

    public TaskDoubleAdapter(ArrayList<GetOXMapInfoModel2.MapInfoBean> group, ArrayList<ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyAddrListBean>> child, Context context) {
        this.group = group;
        this.child = child;
        this.context = context;
        this.mIndicators = new SparseArray<>();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    /***
     * 一级列表个数
     * @return
     */
    @Override
    public int getGroupCount() {
        return group.size();
    }

    /***
     * 每个二级列表的个数
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    /***
     * 一级列表中单个item
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    /***
     * 二级列表中单个item
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /***
     * 每个item的id是否固定，一般为true
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /***
     * 填充一级列表
     * @param groupPosition
     * @param isExpanded 是否展开
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.doubleadapter_task, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv_group = convertView.findViewById(R.id.tv_doubleadapter_task);
            groupViewHolder.iv_group = convertView.findViewById(R.id.iv_doubleadapter_task);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        //设置显示数据
        groupViewHolder.tv_group.setText(group.get(groupPosition).mapName);
        mIndicators.put(groupPosition, groupViewHolder.iv_group);
        setIndicatorState(groupPosition, isExpanded);
        return convertView;
    }

    /***
     * 填充二级列表
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.doubleadapter_task2, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tv_child = convertView.findViewById(R.id.tv_doubleadapter_task2);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tv_child.setText(child.get(groupPosition).get(childPosition).jobName);
        setSelectState(groupPosition, childPosition, childViewHolder.tv_child);
        return convertView;
    }

    /***
     * 二级列表中每个能否被选中，如果有点击事件一定要设为true
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    class GroupViewHolder {
        TextView tv_group;
        ImageView iv_group;
    }

    class ChildViewHolder {
        TextView tv_child;
    }

    private SparseArray<ImageView> mIndicators;

    public void setIndicatorState(int groupPosition, boolean isExpanded) {
        if (isExpanded) {
            mIndicators.get(groupPosition).setImageResource(R.mipmap.icon_todown);
        } else {
            mIndicators.get(groupPosition).setImageResource(R.mipmap.icon_toright);
        }
    }

    public void setSelectState(int groupPosition_aa, int chlidPosition_aa, TextView tv) {
        if (child.get(groupPosition_aa).get(chlidPosition_aa).isOXSelect) {
            tv.setBackgroundResource(R.drawable.expandlist_chlid2);
        } else {
            tv.setBackgroundResource(R.drawable.expandlist_chlid1);
        }
    }

}
