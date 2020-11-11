package cn.top.client;

import cn.top.util.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value="common-provider",fallback = FastDfsFall.class)
public interface FastDfsClient {

    //上传和下载和删除：
    @RequestMapping(value = "/common/upload",method = RequestMethod.POST)
    AjaxResult upload(@RequestBody MultipartFile file);

    //下载：页面直接使用http://ip/groupname/filename


    //删除：
    @RequestMapping(value = "/common/delete",method = RequestMethod.GET)
    AjaxResult delete(@RequestParam("filePath") String filePath);





}
