package com.bluesky.jeecg.entity;

import java.util.Date;

/**
 * User: bluesky
 * Date: 14-1-20
 * Time: 下午2:12
 */
public class UserEntity {

    private String username;
    private String password;
    private Date logintime;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }
}
