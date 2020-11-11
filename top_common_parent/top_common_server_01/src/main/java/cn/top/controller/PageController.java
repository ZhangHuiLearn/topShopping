package cn.top.controller;

import cn.top.client.PageClient;
import cn.top.util.TopConstants;
import cn.top.util.VelocityUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/common")
public class PageController implements PageClient {

    @RequestMapping(value = "/page",method = RequestMethod.POST)
    @Override
    public void createPage(@RequestBody Map<String, Object> map) {
        //获取参数：
        //调用封装的工具类：
        Object model=map.get(TopConstants.PAGE_MODEL);
        String templateFilePathAndName=(String)map.get(TopConstants.PAGE_TEMPLATEFILEPATHANDNAME);
        String targetFilePathAndName=(String)map.get(TopConstants.PAGE_TARGETFILEPATHANDNAME);
        VelocityUtils.staticByTemplate(model,templateFilePathAndName,targetFilePathAndName);
    }
}
