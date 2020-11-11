package cn.top.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    private static JedisPool jedisPool;
    static {
        //连接池的配置：
        GenericObjectPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(5);
        poolConfig.setMaxWaitMillis(3000);
        poolConfig.setTestOnBorrow(true);
        //创建pool：
        // (GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password)
        jedisPool = new JedisPool(poolConfig, TopConstants.redisServerIp,6379,2000,"root");

    }

    /**
     * 设置一些字符串值
     * @param key
     * @param value
     */
    public static void set(String key,String value){
        Jedis jedis =null;
        try {
            jedis= jedisPool.getResource();
            jedis.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null){
                jedis.close();
            }

        }
    }

    /**
     * 获取指定 key 的值。如果 key 不存在，返回 nil
     * @param key
     * @return
     */
    public static String get(String key){
        Jedis jedis =null;
        try {
            jedis= jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if(jedis!=null){
                jedis.close();
            }

        }
    }

    /**
     * 设置key的到期时间
     * @param key
     * @param second
     * @return
     */
    public static long expire(String key, int second){
        Jedis jedis =null;
        try {
            jedis= jedisPool.getResource();
            return jedis.expire(key, second);
        } catch (Exception e) {
            e.printStackTrace();

            return 0;
        } finally {
            if(jedis!=null){
                jedis.close();
            }

        }
    }

    public static void main(String[] args) {
    }


}
