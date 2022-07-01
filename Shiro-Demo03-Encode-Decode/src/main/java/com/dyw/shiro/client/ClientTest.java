package com.dyw.shiro.client;

import com.dyw.shiro.utils.DigestsUtil;
import com.dyw.shiro.utils.EncodesUtil;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Devil
 * @since 2022-06-21-14:49
 *
 * Shiro自带编码解码测试
 */
public class ClientTest {
    public static void main(String[] args) {
        System.out.println("======16进制编码测试======");
        String val = "hello";
        String encodeHex = EncodesUtil.encodeHex(val.getBytes(StandardCharsets.UTF_8));
        System.out.println(encodeHex);
        String decodeHex = new String(EncodesUtil.decodeHex(encodeHex));
        System.out.println(decodeHex);

        System.out.println("======base64编码测试======");
        String encodeBase64 = EncodesUtil.encodeBase64(val.getBytes(StandardCharsets.UTF_8));
        System.out.println(encodeBase64);
        String decodeBase64 = new String(EncodesUtil.decodeBase64(encodeBase64));
        System.out.println(decodeBase64);

        System.out.println("======测试密码含盐加密======");
        Map<String, String> stringStringMap = DigestsUtil.entryPtPassword("123456");
        System.out.println(stringStringMap.toString());
    }
}
