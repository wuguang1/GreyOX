package com.deepblue.greyox.util

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.model.LatLngBounds
import com.baidu.mapapi.utils.CoordinateConverter
import com.deepblue.greyox.bean.GetOXMapInfoModel2
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


object BaiduMapUtil {
    val mEdgePolylineWith = 3  //路沿宽度
    val mPolylineWith = 10  //路线宽度
    val mHasRunPolylineWith = 10  //已行驶路线宽度
    val mEdgePolylineColor = Color.parseColor("#40485F")   //路沿颜色
    val mPolylineColor = Color.parseColor("#40485F") //路线颜色
    val mHasRunPolylineColor = Color.parseColor("#28EECD") //已行驶路线颜色

    val converter = CoordinateConverter().from(CoordinateConverter.CoordType.GPS)

    val mHasTexture by lazy { BitmapDescriptorFactory.fromAsset("ic_hastoute.png") }
    val mRealTexture by lazy { BitmapDescriptorFactory.fromAsset("ic_realroute.png") }

    /**
     * 绘制Marker
     */
    fun drawMarker(mMap: BaiduMap, drawableId: Int, zindex: Int, latLng: LatLng, animateType: Boolean): Marker {
        return mMap.addOverlay(
            MarkerOptions().flat(true)//marker突变是否平贴地面
                .anchor(0.5f, 0.5f)//设置 marker覆盖物与位置点的位置关系，默认（0.5f, 1.0f）水平居中，垂直下对齐
                .draggable(false) //是否可拖拽，默认不可拖拽
                .alpha(0.8f) //marker图标透明度，0~1.0，默认为1.0
                .animateType(if (animateType) MarkerOptions.MarkerAnimateType.drop else MarkerOptions.MarkerAnimateType.none) //marker出现的方式，从天上掉下
                .icon(BitmapDescriptorFactory.fromResource(drawableId))
                .zIndex(zindex)
                .position(latLng)
        ) as Marker
    }

    fun drawMarker2(mMap: BaiduMap, drawableId: Int, zindex: Int, latLng: LatLng, animateType: Boolean): Marker {
        return mMap.addOverlay(
            MarkerOptions().flat(true)//marker突变是否平贴地面
                .anchor(0.5f, 10f)//设置 marker覆盖物与位置点的位置关系，默认（0.5f, 1.0f）水平居中，垂直下对齐
                .draggable(false) //是否可拖拽，默认不可拖拽
                .alpha(0.8f) //marker图标透明度，0~1.0，默认为1.0
                .animateType(if (animateType) MarkerOptions.MarkerAnimateType.grow else MarkerOptions.MarkerAnimateType.none) //marker出现的方式，从天上掉下
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

    fun drawPointLine(mMap: BaiduMap, mWith: Int, color: BitmapDescriptor, zIndex: Int, pointlist: List<LatLng>): Polyline {
        return mMap.addOverlay(
            PolylineOptions().width(mWith)
                .points(pointlist)
                .zIndex(zIndex)
                .dottedLine(true)
                .customTextureList(arrayOf(color).toMutableList())
                .textureIndex(arrayOf(0).toMutableList())
        ) as Polyline
    }

    //转化成百度坐标体系
    fun loadBaiDuData(dataList: List<GetOXMapInfoModel2.MapInfoBean.GreyLineListBean.PrepointListBean>): ArrayList<LatLng> {
        val dealList = ArrayList<LatLng>()
        dataList.forEach {
            dealList.add(converter.coord(LatLng(it.y, it.x)).convert())
        }
        return dealList
    }

    //转化成百度坐标体系
    fun loadBaiDuData(oneLatLng: LatLng): LatLng {
        return converter.coord(oneLatLng).convert()
    }

    fun getDesBaiduLatLng(latlng: LatLng): LatLng {
        return converter.coord(latlng).convert()
    }

    fun getDesBaiduLatLng(lat: Double, lng: Double): LatLng {
        return converter.coord(LatLng(lat, lng)).convert()
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

    fun getCustomStyleFilePath(context: Context, customStyleFileName: String): String? {
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        var parentPath: String? = null
        try {
            inputStream = context.assets.open("customConfigdir/$customStyleFileName")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            parentPath = context.filesDir.absolutePath
            val customStyleFile = File("$parentPath/$customStyleFileName")
            if (customStyleFile.exists()) {
                customStyleFile.delete()
            }
            customStyleFile.createNewFile()
            outputStream = FileOutputStream(customStyleFile)
            outputStream.write(buffer)
        } catch (e: IOException) {
            Log.e("CustomMapDemo", "Copy custom style file failed", e)
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                Log.e("CustomMapDemo", "Close stream failed", e)
                return null
            }
        }
        return "$parentPath/$customStyleFileName"
    }
}