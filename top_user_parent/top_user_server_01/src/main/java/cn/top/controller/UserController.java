package cn.top.controller;

import cn.top.domain.User;
import cn.top.util.AjaxResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public AjaxResult login(@RequestBody User user){
        return AjaxResult.me();
    }

    // /user/login2
    @RequestMapping(value = "/login2",method = RequestMethod.GET)
    public AjaxResult login(){
        return AjaxResult.me();
    }
}
