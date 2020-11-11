package cn.top.controller;

import cn.top.client.FastDfsClient;
import cn.top.util.AjaxResult;
import cn.top.util.FastDfsApiOpr;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/common")
public class FastDfsController implements FastDfsClient {

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @Override
    public AjaxResult upload(@RequestBody MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            //获取原始名：
            String originalFilename = file.getOriginalFilename();
            //获取后缀名：
            String extName = FilenameUtils.getExtension(originalFilename);
            // "/"+fileIds[0]+"/"+fileIds[1];
            String groupNameAndFileName = FastDfsApiOpr.upload(bytes, extName);
            return AjaxResult.me().setSuccess(true).setMsg("亲，文件上传成功!").setObject(groupNameAndFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMsg("亲，文件上传失败!"+e.getMessage());
        }
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @Override
    public AjaxResult delete(@RequestParam("filePath") String filePath) {
        // filePath:   http://ip/groupName/fileName
        //    /groupName/fileName   groupName/fileName
        String filePath1  =filePath.substring(1);
        String  groupName= filePath1.substring(0, filePath1.indexOf("/"));
        String fileName=filePath1.substring(filePath1.indexOf("/")+1);
        int delete = FastDfsApiOpr.delete(groupName, fileName);
        if(delete==0){
            return AjaxResult.me().setSuccess(true).setMsg("删除成功!!");
        }else{
            return AjaxResult.me().setSuccess(false).setMsg("删除失败!!");
        }
    }

    public static void main(String[] args) {
        String filePath="/group1/M00/00/01/rBAHy1zYO26AEwFyAAB7VaQUqSY146.png";
       //  group1
       //  M00/00/01/rBAHy1zYLluAMw2BAC7Mdh1oFjs624.avi
        String groupName="";

        // filePath.indexOf("0"):第一个字符串出现的索引：从0开始
        // groupName.substring(0, 2)：ab  [)==>左闭右开
        // group1/M00/00/01/rBAHy1zYO26AEwFyAAB7VaQUqSY146.png:只有一个参数的时候：从这个开始截取到最后:filePath.substring(1)
        String filePath1  =filePath.substring(1);//   group1/M00/00/01/rBAHy1zYO26AEwFyAAB7VaQUqSY146.png
        // group1
        groupName= filePath1.substring(0, filePath1.indexOf("/"));
        String fileName=filePath1.substring(filePath1.indexOf("/")+1);


    }
}
