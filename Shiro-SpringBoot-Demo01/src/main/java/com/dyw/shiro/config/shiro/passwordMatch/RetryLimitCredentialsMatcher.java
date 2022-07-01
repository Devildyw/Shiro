package com.dyw.shiro.config.shiro.passwordMatch;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author Devil
 * @since 2022-06-28-18:25
 */
@NoArgsConstructor
@Slf4j
@Component
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {
    @Resource
    private RedisTemplate redisTemplate;

    private static Long RETRY_LIMIT_NUM = 4L;

    public static RedisTemplate redisTemplateSelf;


    @PostConstruct
    public void getRedisTemplate(){
        this.redisTemplateSelf = redisTemplate;
    }

    public RetryLimitCredentialsMatcher(String hashAlgorithmName){
        super(hashAlgorithmName);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        log.info("进入自定义密码比较器");
        //获得用户名
        String loginName = (String) token.getPrincipal();
        //获得缓存
        log.info(loginName);
        RedisAtomicLong atomicLong = new RedisAtomicLong(loginName, redisTemplateSelf.getConnectionFactory());
        long retryFlag = atomicLong.get();

        //判断次数
        if (retryFlag>RETRY_LIMIT_NUM){
            //超过次数设计10分钟后重试
            atomicLong.expire(10, TimeUnit.MINUTES);
            log.error("密码错误5次，请10分钟以后再试");
            throw new ExcessiveAttemptsException("密码错误5次，请10分钟以后再试");
        }
        //没有超过
        //累加次数
        atomicLong.incrementAndGet();
        atomicLong.expire(10,TimeUnit.MINUTES);
        //密码校验
        boolean flag = super.doCredentialsMatch(token, info);
        log.info("{}",atomicLong.get());
        //如果登录成功 清除缓存
        if (flag){
            atomicLong.expire(0,TimeUnit.MILLISECONDS);
        }
        return flag;
    }


}
