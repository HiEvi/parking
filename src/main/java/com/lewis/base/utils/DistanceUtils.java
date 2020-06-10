package com.lewis.base.utils;

import java.math.BigDecimal;

public class DistanceUtils {
    /**  
          * 生成以中心点为中心的四方形经纬度  
          *   
          * @param lat 纬度  
          * @param lon 精度  
          * @param raidus 半径（以米为单位）  
          * @return  
          */
    public static BigDecimal[] getAround(BigDecimal lat,BigDecimal lon,int radius){
        BigDecimal minLat1;
        BigDecimal minLng1;
        BigDecimal maxLat1;
        BigDecimal maxLng1;

        Double latutude = lat.doubleValue();
        Double longitude = lon.doubleValue();

//        Double latutude = lat;
//        Double longitude = lon;

        Double degree = (24901*1609)/360.0;
        Double raidusMile = Double.valueOf(radius);

        Double dpmLat = 1/degree;
        Double radiusLat = dpmLat * raidusMile;

        Double minLat = latutude - radiusLat;
        Double maxLat = latutude + radiusLat;

        Double mpdLng = degree * Math.cos(latutude * (Math.PI/180));
        Double dpmLng = 1 / mpdLng;

        Double radiusLng = dpmLng * raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;

        minLat1 = new BigDecimal(minLat);
        minLng1 = new BigDecimal(minLng);
        maxLat1 = new BigDecimal(maxLat);
        maxLng1 = new BigDecimal(maxLng);

//        System.out.println(minLat1);
//        System.out.println(minLng1);
//        System.out.println(maxLat1);
//        System.out.println(maxLng1);

        return  new BigDecimal[]{minLat1,minLng1,maxLat1,maxLng1};
    }
    /**  
          * 计算中心经纬度与目标经纬度的距离（米）  
          *   
          * @param centerLon  
          *            中心精度  
          * @param centerLan  
          *            中心纬度  
          * @param targetLon  
          *            需要计算的精度  
          * @param targetLan  
          *            需要计算的纬度  
          * @return 米  
          */
    public static double distance(double centerLon, double centerLat, double targetLon, double targetLat){
        double jl_jd = 102834.74258026089786013677476285;// 每经度单位米
        double jl_wd = 111712.69150641055729984301412873;// 每纬度单位米

        double b = Math.abs((centerLat - targetLat) * jl_jd);
        double a = Math.abs((centerLon - targetLon) * jl_wd);

        return Math.sqrt((a * a + b * b));
    }
}
