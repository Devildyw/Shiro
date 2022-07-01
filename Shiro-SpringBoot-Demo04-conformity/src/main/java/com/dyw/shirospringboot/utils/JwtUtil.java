package com.dyw.shirospringboot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.shiro.codec.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Devil
 * @since 2022-07-01-16:42
 * <p>
 * Jwt工具类
 */
public class JwtUtil {
    private static final String SECRET_KEY = "comdywshirospringbootzIYEY7mxWNu49kYljNPMeva9Fjrwwqzw0bFlO0kPXZTCGaVcw0j";

    private static SecretKey generateSecretKey() {
        String secretKey = SECRET_KEY;
        byte[] key = Base64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec keySpec = new SecretKeySpec(key, 0, key.length, "HmacSHA256");
        return keySpec;
    }

    private static String createJwt(String iss, Map<String, Object> claims, String sessionId, long expireTime) {
        if (claims == null) {
            claims = new HashMap<>(16);
        }
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

        //获取当前时间
        long nowMillis = System.currentTimeMillis();

        //获取加密密钥
        SecretKey secretKey = generateSecretKey();

        //构建JWs
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)//设置内部负载
                .setId(sessionId)//设置id 这里用存储sessionId 后面也方便取出查找session
                .setIssuedAt(new Date(nowMillis))//设置签发时间
                .setSubject(iss)//设置Jwt主题
                .signWith(algorithm, secretKey);//设置Jwt第三部分的签名 采用加密密钥加加密算法加密
        if (expireTime >= 0) {
            //过期时间
            long expMillis = nowMillis + expireTime;
            jwtBuilder.setExpiration(new Date(expMillis));
        }

        //签发
        return jwtBuilder.compact();

    }


    public static String generateJwt(String iss, Integer id, String sessionId, long expireTime) {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("id", id);
        return createJwt(iss, map, sessionId, expireTime);
    }

    public static Claims verifyJwt(String token) {
        //获取加密密钥 以便解密
        SecretKey secretKey = generateSecretKey();

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

    }
}
