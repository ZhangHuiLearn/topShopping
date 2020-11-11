package cn.top.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.text.SimpleDateFormat;

/**
 * 玩家
 */
@TableName("t_user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String password;
    private String openId;
    private Long telphone;
    private String loginIp;
    private Long loginTime;
    private String registerIp;
    private Long registerTime;
    private String status;


    public User() {
    }

    public User(Long id, String name, String password, String openId, Long telphone, String loginIp, Long loginTime, String registerIp, Long registerTime, String status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.openId = openId;
        this.telphone = telphone;
        this.loginIp = loginIp;
        this.loginTime = loginTime;
        this.registerIp = registerIp;
        this.registerTime = registerTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public User setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public Long getTelphone() {
        return telphone;
    }

    public User setTelphone(Long telphone) {
        this.telphone = telphone;
        return this;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public User setLoginIp(String loginIp) {
        this.loginIp = loginIp;
        return this;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public String getLoginTimeStr() {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        return  this.loginTime==null?"":sdf.format(this.loginTime);
    }

    public User setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
        return this;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public User setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
        return this;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public String getRegisterTimeStr() {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        return  this.registerTime==null?"":sdf.format(this.registerTime);
    }

    public User setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public User setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", openId='" + openId + '\'' +
                ", telphone=" + telphone +
                ", loginIp='" + loginIp + '\'' +
                ", loginTime=" + loginTime +
                ", registerIp='" + registerIp + '\'' +
                ", registerTime=" + registerTime +
                ", status='" + status + '\'' +
                '}';
    }
}
