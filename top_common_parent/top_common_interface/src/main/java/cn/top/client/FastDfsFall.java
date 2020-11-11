package cn.top.client;

import cn.top.util.AjaxResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FastDfsFall implements  FastDfsClient{

    @Override
    public AjaxResult upload(MultipartFile file) {
        return AjaxResult.me().setSuccess(false).setMsg("亲，现在我们的服务很忙，亲稍后在试哦!!!!");
    }

    @Override
    public AjaxResult delete(String filePath) {
        return AjaxResult.me().setSuccess(false).setMsg("亲，现在我们的服务很忙，亲稍后在试哦!!!!");
    }
}
