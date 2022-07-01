package com.dyw.shiro.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Devil
 * @since 2022-06-21-15:06
 */
public class DigestsUtil {
    public static final String SHA256 = "SHA-256";

    public static final Integer ITERATIONS =512;

    /**
     * SHA256加密方法
     * @param input 需要散列字符串
     * @param salt 盐字符串
     * @return String
     */
    private static String sha256(String input, String salt){
        return new SimpleHash(SHA256,input,salt,ITERATIONS).toString();
    }

    /**
     * 随机获得salt字符串
     * @return String
     */
    private static String generateSalt(){
        SecureRandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        return randomNumberGenerator.nextBytes().toHex();
    }

    /**
     * 生成密码字符密文和salt密文
     * @param passwordPlain 需要散列的字符串
     * @return map
     */
    public static Map<String,String> entryPtPassword(String passwordPlain){
        HashMap<String, String> map = new HashMap<>(16);
        String salt = generateSalt();
        String password = sha256(passwordPlain, salt);
        map.put("salt",salt);
        map.put("password",password);
        return map;
    }

}
