package com.zws.utils.domain;


import com.zws.utils.exception.AppException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhengws
 * @date on 2020/11/13
 */
public class GeoHash {

    private static final double MINLAT = -90;
    private static final double MAXLAT = 90;
    private static final double MINLNG = -180;
    private static final double MAXLNG = 180;

    /**
     * 1 2500km; 2 630km;3 78km; 4 30km
     * 5 2.4km; 6 610m; 7 76m; 8 19m
     */
    private int latLength = 20; //纬度转化为二进制长度
    private int lngLength = 20; //经度转化为二进制长度

    private double minLat;//每格纬度的单位大小
    private double minLng;//每个经度的倒下
    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public GeoHash(int length) {
        setHashLength(length);
    }

    /**
     * @param length
     * @return
     * @Author:lulei
     * @Description: 设置经纬度转化为geohash长度
     */
    private void setHashLength(int length) {
        if (length < 1) {
            throw new AppException("length can't low zero");
        }
        latLength = (length * 5) / 2;
        if (length % 2 == 0) {
            lngLength = latLength;
        } else {
            lngLength = latLength + 1;
        }
        setMinLatLng();
    }

    /**
     * @Author:lulei
     * @Description: 设置经纬度的最小单位
     */
    private void setMinLatLng() {
        minLat = MAXLAT - MINLAT;
        for (int i = 0; i < latLength; i++) {
            minLat /= 2.0;
        }
        minLng = MAXLNG - MINLNG;
        for (int i = 0; i < lngLength; i++) {
            minLng /= 2.0;
        }
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 获取附近9个gen哈希值，offset 为偏移多少个。
     */
    public Set<String> getPoiGeoHashBase32(double latitude, double longitude, int offset) {
        if (offset < 0) {
            throw new AppException("offset can't low zero");
        }
        double leftLat = latitude - offset * minLat;
        double rightLat = latitude + offset * minLat;
        double upLng = longitude - offset * minLng;
        double downLng = longitude + offset * minLng;
        Set<String> results = new HashSet<>();
        String hash;
        for (double lat = leftLat; lat <= rightLat; lat += minLat) {
            for (double lng = upLng; lng <= downLng; lng += minLng) {
                hash = getGeoHashBase32(lat, lng);
                if (hash != null) {
                    results.add(hash);
                }
            }
        }
        return results;
    }


    /**
     * @param lat
     * @param lng
     * @return
     * @Author:lulei
     * @Description: 获取经纬度的base32字符串
     */
    public String getGeoHashBase32(double lat, double lng) {
        boolean[] bools = getGeoBinary(lat, lng);
        if (bools == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bools.length; i = i + 5) {
            boolean[] base32 = new boolean[5];
            for (int j = 0; j < 5; j++) {
                base32[j] = bools[i + j];
            }
            char cha = getBase32Char(base32);
            if (' ' == cha) {
                return null;
            }
            sb.append(cha);
        }
        return sb.toString();
    }

    /**
     * @param base32
     * @return
     * @Author:lulei
     * @Description: 将五位二进制转化为base32
     */
    private char getBase32Char(boolean[] base32) {
        if (base32 == null || base32.length != 5) {
            return ' ';
        }
        int num = 0;
        for (boolean bool : base32) {
            num <<= 1;
            if (bool) {
                num += 1;
            }
        }
        return CHARS[num % CHARS.length];
    }

    /**
     * @param lat
     * @param lng
     * @return
     * @Author:lulei
     * @Description: 获取坐标的geo二进制字符串
     */
    private boolean[] getGeoBinary(double lat, double lng) {
        boolean[] latArray = getHashArray(lat, MINLAT, MAXLAT, latLength);
        boolean[] lngArray = getHashArray(lng, MINLNG, MAXLNG, lngLength);
        return merge(latArray, lngArray);
    }

    /**
     * @param latArray
     * @param lngArray
     * @return
     * @Author:lulei
     * @Description: 合并经纬度二进制
     */
    private boolean[] merge(boolean[] latArray, boolean[] lngArray) {
        if (latArray == null || lngArray == null) {
            return null;
        }
        boolean[] result = new boolean[lngArray.length + latArray.length];
        Arrays.fill(result, false);
        for (int i = 0; i < lngArray.length; i++) {
            result[2 * i] = lngArray[i];
        }
        for (int i = 0; i < latArray.length; i++) {
            result[2 * i + 1] = latArray[i];
        }
        return result;
    }

    /**
     * @return
     * @Author:lulei
     * @Description: 将数字转化为geohash二进制字符串
     */
    private boolean[] getHashArray(double value, double min, double max, int length) {
        if (value < min || value > max) {
            return null;
        }
        if (length < 1) {
            return null;
        }
        boolean[] result = new boolean[length];
        for (int i = 0; i < length; i++) {
            double mid = (min + max) / 2.0;
            if (value > mid) {
                result[i] = true;
                min = mid;
            } else {
                result[i] = false;
                max = mid;
            }
        }
        return result;
    }
}