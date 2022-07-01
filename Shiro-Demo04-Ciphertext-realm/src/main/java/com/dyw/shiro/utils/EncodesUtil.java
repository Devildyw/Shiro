package com.dyw.shiro.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;

/**
 * @author Devil
 * @since 2022-06-21-14:49
 *
 * 封装base64和16进制编码解码工具类
 */
public class EncodesUtil {

    /**
     * Hex-byte[]-->String转换
     * @param input 输入数组
     * @return String
     */
    public static String encodeHex(byte[] input){
        return Hex.encodeToString(input);
    }

    /**
     * Hex-String-->byte[]转换
     * @param input 输入字符串
     * @return byte[]
     */
    public static byte[] decodeHex(String  input){
        return Hex.decode(input);
    }

    /**
     * base64-byte[]-->String转换
     * @param input 输入数组
     * @return String
     */
    public static String encodeBase64(byte[] input){
        return Base64.encodeToString(input);
    }

    /**
     * Base64-String-->byte[]转换
     * @param input 输入字符串
     * @return byte[]
     */
    public static byte[] decodeBase64(String input){
        return Base64.decode(input);
    }
}
