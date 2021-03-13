package com.zws.utils.tools;


import com.zws.utils.exception.AppException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密工具类
 *
 */
public class Md5Utils {
    /**
     * md5算法，返回32位md5值
     *
     * @param text
     * @return
     */
    public static String encryptMd5(String text) {
        String md5;
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            md5 = (new HexBinaryAdapter()).marshal(md5Digest.digest(text.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(e);
        }
        md5 = md5.toLowerCase();
        return md5;
    }
}
