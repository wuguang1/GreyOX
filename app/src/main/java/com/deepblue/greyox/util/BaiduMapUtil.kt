package com.deepblue.greyox.util

import android.graphics.Color
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.model.LatLngBounds
import com.baidu.mapapi.utils.CoordinateConverter
import com.deepblue.greyox.bean.GetOXMapInfoModel2

object BaiduMapUtil {
    val mEdgePolylineWith = 3  //路沿宽度
    val mPolylineWith = 10  //路线宽度
    val mHasRunPolylineWith = 11   //已行驶路线宽度v
    val mEdgePolylineColor =  Color.parseColor("#40485F")   //路沿颜色
    val mPolylineColor = Color.parseColor("#28EECD") //路线颜色
    val mHasRunPolylineColor = Color.parseColor("#FF4538") //已行驶路线颜色

    val converter = CoordinateConverter().from(CoordinateConverter.CoordType.GPS)

    /**
     * 绘制Marker
     */
    fun drawMarker(mMap: BaiduMap, drawableId: Int, zindex: Int, latLng: LatLng): Marker {
        return mMap.addOverlay(
            MarkerOptions().flat(true)//marker突变是否平贴地面
                .anchor(0.5f, 0.5f)//设置 marker覆盖物与位置点的位置关系，默认（0.5f, 1.0f）水平居中，垂直下对齐
                .draggable(false) //是否可拖拽，默认不可拖拽
                .alpha(0.8f) //marker图标透明度，0~1.0，默认为1.0
                .animateType(MarkerOptions.MarkerAnimateType.jump) //marker出现的方式，从天上掉下
                .icon(BitmapDescriptorFactory.fromResource(drawableId))
                .zIndex(zindex)
                .position(latLng)
        ) as Marker
    }

    /**
     * 绘制折线
     */
    fun drawLine(mMap: BaiduMap, mWith: Int, mColor: Int, zIndex: Int, pointlist: List<LatLng>): Polyline {
        return mMap.addOverlay(
            PolylineOptions()
                .width(mWith)
                .color(mColor)
                .zIndex(zIndex)
                .points(pointlist)
        ) as Polyline
    }

    //转化成百度坐标体系
    fun loadBaiDuData(dataList: ArrayList<GetOXMapInfoModel2.MapInfoBean.GreyLineListBean.PrepointListBean>): ArrayList<LatLng> {
        val dealList = ArrayList<LatLng>()
        dataList.forEach {
            dealList.add(converter.coord(LatLng(it.x, it.y)).convert())
        }
        return dealList
    }

    fun loadBaiDuData2(dataList: List<GetOXMapInfoModel2.MapInfoBean.GreyLineListBean.PrepointListBean>): ArrayList<LatLng> {
        val dealList = ArrayList<LatLng>()
        dataList.forEach {
            dealList.add(LatLng(it.y, it.x))
        }
        return dealList
    }

    /**
     * 多个点，在Android里面显示合理的缩放级
     */
    fun setLatLngBounds(points: List<LatLng>, mMapView: MapView): MapStatusUpdate {
        var builder2 = LatLngBounds.Builder()
        for (p in points) {
            builder2 = builder2.include(p)
        }
        val latlngBounds = builder2.build()
        return MapStatusUpdateFactory.newLatLngBounds(latlngBounds, mMapView.width - 100, mMapView.height - 100)
    }
}