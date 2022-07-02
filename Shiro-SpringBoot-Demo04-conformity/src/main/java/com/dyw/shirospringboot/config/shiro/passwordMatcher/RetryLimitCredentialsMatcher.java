package com.dyw.shirospringboot.config.shiro.passwordMatcher;

import com.dyw.shirospringboot.utils.ApplicationContextUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Devil
 * @since 2022-07-02-16:34
 *
 * 自定义密码匹配器 主要防止用户反复登录 导致恶意访问
 */
@Slf4j
@Component
@NoArgsConstructor
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

    private static final Long RETRY_LIMIT_NUM = 4L;

    public RetryLimitCredentialsMatcher(String hashAlgorithmName) {
        super(hashAlgorithmName);
    }

    public RedisTemplate getRedisTemplate(){
        return (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
    }
    /**
     * 这里就是密码匹配部分
     * @param token the {@code AuthenticationToken} submitted during the authentication attempt.
     * @param info  the {@code AuthenticationInfo} stored in the system matching the token principal
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        RedisTemplate redisTemplate = getRedisTemplate();

        //获取用户名 作为redis的key
        String loginName = (String) token.getPrincipal();

        //获取缓存 如果没有缓存则初始化缓存计数为0
        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(loginName, redisTemplate.getConnectionFactory());
        //获取缓存中的比较次数
        long retryCount = redisAtomicLong.get();

        //判断次数
        if (retryCount>RETRY_LIMIT_NUM){
            long nowMillis = System.currentTimeMillis();
            log.error("密码错误5次,请稍后再试");
            throw new ExcessiveAttemptsException("密码错误超过五次,请"+redisAtomicLong.getExpire()/60+"分钟后重试");
        }
        //没有超过
        //累加次数
        redisAtomicLong.incrementAndGet();
        //记录登录
        redisAtomicLong.expire(10, TimeUnit.MINUTES);

        //进行登录
        boolean flag = super.doCredentialsMatch(token, info);
        if (flag){
            //登录成功 清除缓存
            redisAtomicLong.expire(0,TimeUnit.MINUTES);
        }else{
            //如果没有登录成功 再次获取次数判断是达到五次
            retryCount = redisAtomicLong.get();
            if (retryCount>RETRY_LIMIT_NUM){
                redisAtomicLong.expire(10, TimeUnit.MINUTES);
                log.error("密码错误5次,请稍后再试");
                throw new ExcessiveAttemptsException("密码错误超过五次,请10分钟后重试");
            }
        }

        log.info("密码尝试次数"+retryCount);
        return flag;
    }
}
