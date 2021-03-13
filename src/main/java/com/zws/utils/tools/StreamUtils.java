package com.zws.utils.tools;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author zhengws
 * @time 2020/7/13 17:38
 * @description
 **/
@Slf4j
public class StreamUtils {
    /**
     * InputStream 流内容读取.
     * @param inputStream
     * @return
     */
    public static String read(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        try {
            String line = null;
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }
}
