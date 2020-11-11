package cn.top.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value="common-provider",fallback = PageFall.class)
public interface PageClient {

    @RequestMapping(value = "/common/page",method = RequestMethod.POST)
    void createPage(@RequestBody Map<String,Object> map);


}
