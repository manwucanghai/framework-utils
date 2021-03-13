package com.zws.utils.tools;


import com.zws.utils.domain.GeoHash;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengws
 * @date 2020-10-17 14:31
 */
public class GeoHashUtils {

    /**
     * length 误差值范围
     * 1 2500km; 2 630km;3 78km; 4 30km
     * 5 2.4km; 6 610m; 7 76m; 8 19m
     */

    private static final Map<Integer, GeoHash> geoHashMap = new ConcurrentHashMap<>();

    /**
     * 获取经纬度geoHash。
     *
     * @param longitude
     * @param latitude
     * @param length    hash 值长度。
     * @return
     */
    public static String getGeoHash(double latitude, double longitude, int length) {
        GeoHash geoHash = geoHashMap.computeIfAbsent(length, k -> new GeoHash(length));
        return geoHash.getGeoHashBase32(latitude, longitude);
    }

    /**
     * 获取指定经纬度附近hash值
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param length    hash值长度
     * @param offset    偏移量范围。
     * @return
     */
    public static Set<String> getPoiGeoHash(
            double latitude, double longitude, int length, int offset) {
        GeoHash geoHash = geoHashMap.computeIfAbsent(length, k -> new GeoHash(length));
        return geoHash.getPoiGeoHashBase32(latitude, longitude, offset);
    }


    /**
     * 获取指定经纬度附近9宫格hash值
     *
     * @param latitude
     * @param longitude
     * @param length
     * @return
     */
    public static Set<String> getPoiGeoHash(
            double latitude, double longitude, int length) {
        return getPoiGeoHash(latitude, longitude, length, 1);
    }
}
