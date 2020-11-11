package cn.top.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="common-provider",fallback = RedisFall.class)
public interface RedisClient {
    @RequestMapping(value = "/redis/set",method = RequestMethod.POST)
    void setRedis(@RequestParam("key") String key,@RequestParam("value") String value);

    @RequestMapping(value = "/redis/get/{key}",method = RequestMethod.GET)
    String getRedis(@PathVariable("key") String key);
}
