package cn.top.controller;

import cn.top.client.RedisClient;
import cn.top.util.RedisUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisController  implements RedisClient {
    @Override
    @RequestMapping(value = "/set",method = RequestMethod.POST)
    public void setRedis(@RequestParam("key") String key, @RequestParam("value") String value) {
        RedisUtil.set(key,value);
    }

    @Override
    @RequestMapping(value = "/get/{key}",method = RequestMethod.GET)
    public String getRedis(@PathVariable("key") String key) {
        return RedisUtil.get(key);
    }
}
