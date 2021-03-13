package com.zws.utils.tools;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author zhengws
 * @date 2020-09-12 15:28
 */
@Slf4j
public class StringUtils {

    public static boolean isBlank(String content) {
        if (content == null || "".equals(content)) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String content) {
        return !isBlank(content);
    }

    /**
     * 去除字符串中的所有空格.
     *
     * @param content
     * @return
     */
    public static String trimSpace(String content) {
        if (content == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : content.toCharArray()) {
            if (c != 32) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 获取inputstream中的内容。
     *
     * @param inputStream
     * @return
     */
    public static String transform(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * 字符串拼接
     *
     * @param iterator
     * @param separator
     * @return
     */
    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object first = iterator.next();
            if (!iterator.hasNext()) {
                return Objects.toString(first, "");
            } else {
                StringBuilder buf = new StringBuilder(256);
                if (first != null) {
                    buf.append(first);
                }

                while (iterator.hasNext()) {
                    if (separator != null) {
                        buf.append(separator);
                    }

                    Object obj = iterator.next();
                    if (obj != null) {
                        buf.append(obj);
                    }
                }

                return buf.toString();
            }
        }
    }
}
