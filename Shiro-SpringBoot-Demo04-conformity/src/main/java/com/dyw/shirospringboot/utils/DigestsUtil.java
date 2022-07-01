package com.dyw.shirospringboot.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Devil
 * @Description 加密工具类 使用Shiro自带API
 * @since 2022-07-01-14:33
 */
public class DigestsUtil {
    /**
     * 算法名称
     */
    public static final String ALGORITHM_NAME = "MD5";


    /**
     * 散列次数
     */
    public static final Integer HASH_ITERATIONS = 512;

    /**
     * MD5加密 这里采用Shiro自带的API进行加密
     *
     * @param input 输入对象
     * @param salt  盐
     * @return 散列后字段
     */
    public static String md5Hash(String input, String salt) {
        return new SimpleHash(ALGORITHM_NAME, input, salt, HASH_ITERATIONS).toString();
    }

    /**
     * 生成随机盐 如果是其他的随机数生成器也可以
     *
     * @return String
     */
    private static String generateSalt() {
        SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        return generator.nextBytes().toHex();
    }

    /**
     * slat加盐加密密码 返回加密时用的盐和密文密码
     *
     * @param password 明文密码
     * @return map 加密时用的盐和密文密码
     */
    public static Map<String, String> encryptPassword(String password) {
        HashMap<String, String> map = new HashMap<>();
        String salt = generateSalt();
        password = md5Hash(password, salt);
        //封装map
        map.put("salt", salt);
        map.put("password", password);
        return map;
    }

}
