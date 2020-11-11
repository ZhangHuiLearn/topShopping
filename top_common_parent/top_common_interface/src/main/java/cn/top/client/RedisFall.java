package cn.top.client;

import org.springframework.stereotype.Component;

@Component
public class RedisFall implements RedisClient {

    @Override
    public void setRedis(String key, String value) {

    }

    @Override
    public String getRedis(String key) {
        return null;
    }
}
