package com.dyw.shirospringboot.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author shengyu
 * @since 2022/3/21 12:16
 */
public class TokenUtils {
    private static final String SEPARATOR = "j891h9nsdon120jdnklsndo19";
    private static final String SECRETKEY = "comdywshirospringbootzIYEY7mxWNu49kYljNPMeva9Fjrwwqzw0bFlO0kPXZTCGaVcw0j";

    public static String createToken() {
        try {
            //生成code
            EncrypDES encrypDES = new EncrypDES(SECRETKEY);
            String code = UUID.randomUUID().toString().replaceAll("-", "");
            //生成时间戳
            DateTimeFormatter fmDate = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
            LocalDateTime today = LocalDateTime.now();
            String date = today.format(fmDate);
            //返回token
            return encrypDES.encrypt(date + SEPARATOR + code);
        } catch (Exception e) {
            throw new RuntimeException("token获取异常");
        }
    }
}
