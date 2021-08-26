package org.agamotto.cloud.redisson.operation;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redisson对象操作
 *
 * @author: yxq
 * @date: 2021/8/26 15:24
 */
@Component
public class RedissonObject {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 获取对象值
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getValue(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 获取对象空间
     * @param key
     * @param <T>
     * @return
     */
    public <T> RBucket<T> getBucket(String key) {
        return redissonClient.getBucket(key);
    }

    /**
     * 设置对象的值
     *
     * @param key  键
     * @param value 值
     * @param time  缓存时间,单位毫秒,小于1-永久缓存
     * @return
     */
    public <T> void setValue(String key, T value, Long time) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if(time == null || time < 1){
            bucket.set(value);
        }else {
            bucket.set(value, time, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 如果值已经存在则则不设置
     *
     * @param key  键
     * @param value 值
     * @param time  缓存时间,单位毫秒,小于1-永久缓存
     * @return true-设置成功;false-值存在,不设置
     */
    public <T> Boolean trySetValue(String key, T value, Long time) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        boolean b;
        if(time == null || time < 1){
            b = bucket.trySet(value);
        }else {
            b = bucket.trySet(value, time, TimeUnit.MILLISECONDS);
        }
        return b;
    }

    /**
     * 删除对象
     *
     * @param key 键
     * @return true-删除成功,false-不成功
     */
    public Boolean delete(String key) {
        return redissonClient.getBucket(key).delete();
    }

}
