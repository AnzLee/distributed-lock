package com.github.anzlee.lock.redisson;

import com.github.anzlee.lock.configuration.RedissonConfig;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <pre>redisson锁管理
 * ClassName: DistributedRedisLock
 * Package: com.trs.media.com.github.anzlee.lock.redisson
 * Description:
 * Datetime: Created by AnzLee on 2020-03-26   15:40.
 * </pre>
 *
 * @author <a href="https://github.com/anzlee">Anzlee</a>
 */
@Component
public class DistributedRedisLock {

    private static Redisson redisson;

    private static final String LOCK_TITLE = "redisDistributedLock_";

    @Autowired
    public void RedissonManager(RedissonConfig redissonConfig) {
        //从配置类中获取redisson对象
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redissonConfig.getHost() + ":" + redissonConfig.getPort()).setPassword(redissonConfig.getPassword());
        redisson = (Redisson) Redisson.create(config);
    }

    /**
     * 加锁
     * @param lockName
     * @return
     */
    public boolean lock(String lockName){
        //声明key对象
        String key = LOCK_TITLE + lockName;
        //获取锁对象
        RLock mylock = redisson.getLock(key);
        //加锁，并且设置锁过期时间，防止死锁的产生
        mylock.lock(2, TimeUnit.MINUTES);
        System.err.println("======lock======"+Thread.currentThread().getName());
        //加锁成功
        return  true;
    }

    /**
     * 锁的释放
     * @param lockName
     */
    public void unlock(String lockName){
        //必须是和加锁时的同一个key
        String key = LOCK_TITLE + lockName;
        //获取所对象
        RLock mylock = redisson.getLock(key);
        //释放锁（解锁）
        mylock.unlock();
        System.err.println("======unlock======"+Thread.currentThread().getName());
    }
}
