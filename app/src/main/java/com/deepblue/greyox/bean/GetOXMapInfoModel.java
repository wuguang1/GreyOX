//package com.deepblue.greyox.bean;
//
//import com.baidu.mapapi.map.Polyline;
//import com.baidu.mapapi.model.LatLng;
//import com.deepblue.greyox.util.BaiduMapUtil;
//import com.deepblue.library.planbmsg.Response;
//
//import java.util.List;
//
///**
// * 室外机器人地图信息上传(17001)
// */
//public class GetOXMapInfoModel  {
//
//    public List<MapInfoBean> map_info;
//
//    public static class MapInfoBean {
//        /**
//         * mapId : 1
//         * mapServerId : 12
//         * mapName : 上海长风公园
//         * min_pos : {"x":-22.553081512451172,"y":-22.41040802001953}
//         * max_pos : {"x":-22.553081512451172,"y":-22.41040802001953}
//         * resolution : 0.029999999329447746
//         * picture :
//         * greyPointList : [{"id":23,"x":-22.553081512451172,"y":-22.41040802001953},{"id":13,"x":-32.55308151245117,"y":-52.41040802001953}]
//         * greyAddrList : [{"jobAddr":"湖滨道路保洁作业","jobName":"路径1","lineIdList":[{"id":101},{"id":102},{"id":103}]},{"jobAddr":"内环线保洁作业","jobName":"路径2","lineIdList":[{"id":104},{"id":105}]},{"jobAddr":"外环线保洁作业","jobName":"路径3","lineIdList":[{"id":106}]}]
//         * greyLineList : [{"lineId":101,"lineServerId":12,"areaInfo":20,"distanceInfo":20,"timeInfo":0.5,"pathName":"作业任务101","prepointList":[{"x":315.223,"y":23.223},{"x":315.223,"y":21.223}],"path1List":[{"x":315.223,"y":23.223},{"x":315.223,"y":23.223}],"path2List":[{"x":315.223,"y":22.263},{"x":315.223,"y":22.213}]},{"lineId":103,"lineServerId":133,"areaInfo":23,"distanceInfo":23,"timeInfo":3.5,"pathName":"作业任务103","prepointList":[{"x":315.223,"y":23.223},{"x":315.223,"y":21.223}],"path1List":[{"x":315.223,"y":23.223},{"x":315.223,"y":23.223}],"path2List":[{"x":315.223,"y":22.263},{"x":315.223,"y":22.213}]},{"lineId":102,"lineServerId":13,"areaInfo":22,"distanceInfo":22,"timeInfo":1.5,"pathName":"作业任务102","prepointList":[{"x":315.223,"y":23.223},{"x":315.223,"y":21.223}],"path1List":[{"x":315.223,"y":23.223},{"x":315.223,"y":23.223}],"path2List":[{"x":315.223,"y":22.263},{"x":315.223,"y":22.213}]},{"lineId":104,"lineServerId":14,"areaInfo":24,"distanceInfo":24,"timeInfo":4.5,"pathName":"作业任务104","prepointList":[{"x":315.223,"y":23.223},{"x":315.223,"y":21.223}],"path1List":[{"x":315.223,"y":23.223},{"x":315.223,"y":23.223}],"path2List":[{"x":315.223,"y":22.263},{"x":315.223,"y":22.213}]},{"lineId":105,"lineServerId":15,"areaInfo":25,"distanceInfo":25,"timeInfo":5.5,"pathName":"作业任务105","prepointList":[{"x":315.223,"y":23.223},{"x":315.223,"y":21.223}],"path1List":[{"x":315.223,"y":23.223},{"x":315.223,"y":23.223}],"path2List":[{"x":315.223,"y":22.263},{"x":315.223,"y":22.213}]},{"lineId":106,"lineServerId":16,"areaInfo":26,"distanceInfo":26,"timeInfo":6.5,"pathName":"作业任务106","prepointList":[{"x":315.223,"y":23.223},{"x":315.223,"y":21.223}],"path1List":[{"x":315.223,"y":23.223},{"x":315.223,"y":23.223}],"path2List":[{"x":315.223,"y":22.263},{"x":315.223,"y":22.213}]}]
//         */
//
//        public int mapId;
//        public int mapServerId;
//        public String mapName;
//        public MinPosBean min_pos;
//        public MaxPosBean max_pos;
//        public double resolution;
//        public String picture;
//        public List<GreyPointListBean> greyPointList;
//        public List<GreyAddrListBean> greyAddrList;
//        public List<GreyLineListBean> greyLineList;
//
//        public static class MinPosBean {
//            /**
//             * x : -22.553081512451172
//             * y : -22.41040802001953
//             */
//
//            public double x;
//            public double y;
//        }
//
//        public static class MaxPosBean {
//            /**
//             * x : -22.553081512451172
//             * y : -22.41040802001953
//             */
//
//            public double x;
//            public double y;
//        }
//
//        public static class GreyPointListBean {
//            /**
//             * id : 23
//             * x : -22.553081512451172
//             * y : -22.41040802001953
//             */
//
//            public int id;
//            public double x;
//            public double y;
//        }
//
//        public static class GreyAddrListBean {
//            /**
//             * jobAddr : 湖滨道路保洁作业
//             * jobName : 路径1
//             * lineIdList : [{"id":101},{"id":102},{"id":103}]
//             */
//
//            public String jobAddr;
//            public String jobName;
//            public boolean isOXSelect = false;
//            public List<LineIdListBean> lineIdList;
//
//            public static class LineIdListBean {
//                /**
//                 * id : 101
//                 */
//
//                public int id;
//            }
//        }
//
//        public static class GreyLineListBean {
//            /**
//             * lineId : 101
//             * lineServerId : 12
//             * areaInfo : 20
//             * distanceInfo : 20
//             * timeInfo : 0.5
//             * pathName : 作业任务101
//             * prepointList : [{"x":315.223,"y":23.223},{"x":315.223,"y":21.223}]
//             * path1List : [{"x":315.223,"y":23.223},{"x":315.223,"y":23.223}]
//             * path2List : [{"x":315.223,"y":22.263},{"x":315.223,"y":22.213}]
//             */
//
//            public int lineId;
//            public int lineServerId;
//            public int areaInfo;
//            public int distanceInfo;
//            public double timeInfo;
//            public String pathName;
//            public List<PrepointListBean> prepointList;
//            public List<PrepointListBean> path1List;
//            public List<PrepointListBean> path2List;
//
//            public boolean isOXLineCheck = false;
//            public Polyline polyline;
//            public Polyline edgpolyline1;
//            public Polyline edgpolyline2;
//            public List<LatLng> map_poly_points;
//            public List<LatLng> map_edg1_points;
//            public List<LatLng> map_edg2_points;
//
//            public static class PrepointListBean {
//                /**
//                 * x : 315.223
//                 * y : 23.223
//                 */
//
//                public double x;
//                public double y;
//            }
//
//            public static class Path1ListBean {
//                /**
//                 * x : 315.223
//                 * y : 23.223
//                 */
//
//                public double x;
//                public double y;
//            }
//
//            public static class Path2ListBean {
//                /**
//                 * x : 315.223
//                 * y : 22.263
//                 */
//
//                public double x;
//                public double y;
//            }
//        }
//    }
//
//    public void initdata() {
//        for (int i = 0; i < this.map_info.size(); i++) {
//            for (int j = 0; j < this.map_info.get(i).greyLineList.size(); j++) {
//                this.map_info.get(i).greyLineList.get(j).map_poly_points = BaiduMapUtil.INSTANCE.loadBaiDuData2(this.map_info.get(i).greyLineList.get(j).prepointList);
//                this.map_info.get(i).greyLineList.get(j).map_edg1_points = BaiduMapUtil.INSTANCE.loadBaiDuData2(this.map_info.get(i).greyLineList.get(j).path1List);
//                this.map_info.get(i).greyLineList.get(j).map_edg2_points = BaiduMapUtil.INSTANCE.loadBaiDuData2(this.map_info.get(i).greyLineList.get(j).path2List);
//
//            }
//        }
//    }
//}
