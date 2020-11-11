package cn.top.controller;

import cn.top.domain.User;
import cn.top.service.IUserService;
import cn.top.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public IUserService userService;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public AjaxResult login(@RequestBody User user){
        if (user != null && user.getId() != null) {
            User selectUser = userService.selectById(user.getId());
            if (selectUser != null && selectUser.getPassword().equals(user.getPassword())) {
                return AjaxResult.me().setObject(selectUser);
            }
        }
        return AjaxResult.me().setSuccess(false).setMsg("操作失败");
    }

    // /user/login2
    @RequestMapping(value = "/login2",method = RequestMethod.GET)
    public AjaxResult login(){
        return AjaxResult.me();
    }
}
