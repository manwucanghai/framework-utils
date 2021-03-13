package com.zws.utils.tools;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.spec.KeySpec;

/**
 * @author zhengws
 */
public class DesUtils {

    private static SecretKey key;

    public static final String SECRET_KEY = "app.env.desKey";

    /**
     * 生成密钥的字符串
     */
    private static final String SECRET;

    static {
        SECRET = System.getProperty(SECRET_KEY, "Admin@Ni$JFg*");
    }

    static {
        try {
            KeySpec desKey = new DESKeySpec(SECRET.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(desKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对资源加密
     *
     * @param src 源文件
     * @return 加密后的文件
     */
    public static byte[] encrypt(byte[] src) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(src);
    }

    /**
     * 对资源解密
     *
     * @param src 需要解密的资源
     * @return 解密后的资源
     */
    public static byte[] decrypt(byte[] src) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(src);
    }
}
