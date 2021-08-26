package org.agamotto.cloud.redisson.operation;

import org.redisson.api.RBinaryStream;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 操作对象二进制
 *
 * @author: yxq
 * @date: 2021/8/26 15:50
 */
@Component
public class RedissonBinary {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 获取输出流
     * @param key
     * @return
     */
    public OutputStream getOutputStream(String key) {
        RBinaryStream binaryStream = redissonClient.getBinaryStream(key);
        return binaryStream.getOutputStream();
    }

    /**
     * 获取输入流
     * @param key
     * @return
     */
    public InputStream getInputStream(String key) {
        RBinaryStream binaryStream = redissonClient.getBinaryStream(key);
        return binaryStream.getInputStream();
    }
    /**
     * 获取输入流
     * @param key
     * @return
     */
    public InputStream getValue(String key,OutputStream stream) throws IOException {
        RBinaryStream binaryStream = redissonClient.getBinaryStream(key);
        InputStream inputStream = binaryStream.getInputStream();
        byte[] buff=new byte[1024];
        int len;
        while ((len=inputStream.read(buff))!=-1){
            stream.write(buff,0,len);
        }
        return binaryStream.getInputStream();
    }

    /**
     * 获取对象空间
     *
     * @param key
     * @return
     */
    public RBinaryStream getBucket(String key) {
        return redissonClient.getBinaryStream(key);
    }

    /**
     * 设置对象的值
     *
     * @param key  键
     * @param value 值
     * @return
     */
    public void setValue(String key, InputStream value) throws IOException {
        RBinaryStream binaryStream = redissonClient.getBinaryStream(key);
        binaryStream.delete();
        OutputStream outputStream = binaryStream.getOutputStream();
        byte[] buff = new byte[1024];
        int len;
        while ((len=value.read(buff))!=-1){
            outputStream.write(buff,0,len);
        }
    }

    /**
     * 删除对象
     *
     * @param key 键
     * @return true 删除成功,false 不成功
     */
    public Boolean delete(String key) {
        RBinaryStream binaryStream = redissonClient.getBinaryStream(key);
        return binaryStream.delete();
    }
}
