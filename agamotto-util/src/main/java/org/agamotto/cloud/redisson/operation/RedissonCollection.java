package org.agamotto.cloud.redisson.operation;

import org.redisson.api.RList;
import org.redisson.api.RListMultimap;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redisson集合操作
 *
 * @author: yxq
 * @date: 2021/8/26 15:33
 */

public class RedissonCollection {

    //@Resource
    private RedissonClient redissonClient;

    public RedissonCollection(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }


    /**
     * 获取map集合
     * @param key
     * @param <K>
     * @param <V>
     * @return
     */
    public <K,V> RMap<K, V> getMap(String key){
        return redissonClient.getMap(key);
    }

    /**
     * 设置map集合
     * @param key
     * @param data
     * @param time 缓存时间,单位毫秒,小于1-永久缓存
     * @return
     */
    public <K,V> void setMapValues(String key, Map<K, V> data, Long time){
        RMap<K,V> map = redissonClient.getMap(key);
        if(time != null && time > -1){
            map.expire(time, TimeUnit.MILLISECONDS);
        }
        map.putAll(data);
    }

    /**
     * 获取List集合
     * @param key
     * @return
     */
    public <T> RList<T> getList(String key){
        return redissonClient.getList(key);
    }

    /**
     * 设置List集合
     * @param key
     * @param data
     * @param time 缓存时间,单位毫秒,小于1-永久缓存
     * @return
     */
    public <T>void setListValues(String key, List<T> data, Long time){
        RList<T> list = redissonClient.getList(key);
        if(time != null && time > 0){
            list.expire(time, TimeUnit.MILLISECONDS);
        }
        list.addAll(data);
    }

    /**
     * 获取set集合
     * @param key
     * @return
     */
    public <T> RSet<T> getSet(String key){
        return redissonClient.getSet(key);
    }

    /**
     * 设置set集合
     * @param key
     * @param data
     * @param time 缓存时间,单位毫秒,小于1-永久缓存
     * @return
     */
    public <T>void setSetValues(String key, Set<T> data, Long time){
        RSet<T> set = redissonClient.getSet(key);
        if(time != null && time > 0){
            set.expire(time, TimeUnit.MILLISECONDS);
        }
        set.addAll(data);
    }

    /**
     * 获取RListMultimap
     * @param key
     * @return
     */
    public <K,V>RListMultimap<K,V> getListMultimap(String key){
        return redissonClient.getListMultimap(key);
    }

    /**
     * 设置RListMultimap
     * @param key
     * @param k
     * @param v
     * @param time 缓存时间,单位毫秒,小于1-永久缓存
     * @return
     */
    public <K,V>void setListMultimap(String key, K k, V v, Long time){
        RListMultimap<K,V> listMultimap = redissonClient.getListMultimap(key);
        if(time != null && time > 0){
            listMultimap.expire(time, TimeUnit.MILLISECONDS);
        }
        listMultimap.put(k, v);
    }

    /**
     * 设置RListMultimap
     * @param key
     * @param k
     * @param v
     * @param time 缓存时间,单位毫秒,小于1-永久缓存
     * @return
     */
    public <K,V>void setListMultimap(String key, K k, Iterable<? extends V> v, Long time){
        RListMultimap<K,V> listMultimap = redissonClient.getListMultimap(key);
        if(time != null && time > 0){
            listMultimap.expire(time, TimeUnit.MILLISECONDS);
        }
        listMultimap.putAll(k, v);
    }

}
