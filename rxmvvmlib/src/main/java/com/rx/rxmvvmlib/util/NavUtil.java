package com.rx.rxmvvmlib.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

/**
 * Created by wuwei
 * 2019/12/27
 * 佛祖保佑       永无BUG
 */
public class NavUtil {
    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
    public static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名

    /**
     * 检查地图应用是否安装
     *
     * @return
     */
    public static boolean isGdMapInstalled(Context context) {
        return isInstallPackage(context, PN_GAODE_MAP);
    }

    public static boolean isBaiduMapInstalled(Context context) {
        return isInstallPackage(context, PN_BAIDU_MAP);
    }

    public static boolean isTencentMapInstalled(Context context) {
        return isInstallPackage(context, PN_TENCENT_MAP);
    }

    private static boolean isInstallPackage(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfoList = manager.getInstalledPackages(0);
        if (packageInfoList != null) {
            for (int i = 0; i < packageInfoList.size(); i++) {
                String package_name = packageInfoList.get(i).packageName;
                if (package_name.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 百度转高德
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    /**
     * 高德、腾讯转百度
     *
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    private static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 打开高德地图导航功能
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openGaoDeNavi(Context context, String sname, double slat, double slon, String dname, double dlat, double dlon) {
        try {
            String uriString = null;
            StringBuilder builder = new StringBuilder("amapuri://route/plan?sourceApplication=maxuslife");
            if (slat != 0) {
                builder.append("&sname=").append(sname)
                        .append("&slat=").append(slat)
                        .append("&slon=").append(slon);
            }
            builder.append("&dlat=").append(dlat)
                    .append("&dlon=").append(dlon)
                    .append("&dname=").append(dname)
                    .append("&dev=0")
                    .append("&t=0");
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_GAODE_MAP);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 打开腾讯地图
     * params 参考http://lbs.qq.com/uri_v1/guide-route.html
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     *                驾车：type=drive，policy有以下取值
     *                0：较快捷
     *                1：无高速
     *                2：距离
     *                policy的取值缺省为0
     *                &from=" + dqAddress + "&fromcoord=" + dqLatitude + "," + dqLongitude + "
     */
    public static void openTencentMap(Context context, String sname, double slat, double slon, String dname, double dlat, double dlon) {
        try {
            String uriString = null;
            StringBuilder builder = new StringBuilder("qqmap://map/routeplan?type=walk&policy=0&referer=ZHCBZ-S373G-OJXQG-IJQRG-UXX7E-ANBO5");
            if (slat != 0) {
                builder.append("&from=").append(sname)
                        .append("&fromcoord=").append(slat)
                        .append(",")
                        .append(slon);
            }
            builder.append("&to=").append(dname)
                    .append("&tocoord=").append(dlat)
                    .append(",")
                    .append(dlon);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_TENCENT_MAP);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 打开百度地图导航功能(默认坐标点是高德地图，需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openBaiDuNavi(Context context, String sname, double slat, double slon, String dname, double dlat, double dlon) {
        try {

            String uriString = null;
            //终点坐标转换
            //        此方法需要百度地图的BaiduLBS_Android.jar包
            //        LatLng destination = new LatLng(dlat,dlon);
            //        LatLng destinationLatLng = GCJ02ToBD09(destination);
            //        dlat = destinationLatLng.latitude;
            //        dlon = destinationLatLng.longitude;

            double destination[] = gaoDeToBaidu(dlat, dlon);
            dlat = destination[0];
            dlon = destination[1];

            StringBuilder builder = new StringBuilder("baidumap://map/direction?mode=walk&");
            if (slat != 0) {
                //起点坐标转换

                //            LatLng origin = new LatLng(slat,slon);
                //            LatLng originLatLng = GCJ02ToBD09(origin);
                //            slat = originLatLng.latitude;
                //            slon = originLatLng.longitude;

                double[] origin = gaoDeToBaidu(slat, slon);
                slat = origin[0];
                slon = origin[1];

                builder.append("origin=latlng:")
                        .append(slat)
                        .append(",")
                        .append(slon)
                        .append("|name:")
                        .append(sname);
            }
            builder.append("&destination=latlng:")
                    .append(dlat)
                    .append(",")
                    .append(dlon)
                    .append("|name:")
                    .append(dname);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_BAIDU_MAP);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }
}
