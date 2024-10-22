package com.example.myapplication.database;

import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class Hobby extends LitePalSupport {
    private long id;
    private long commodityId;
    private String userName;
    //private User user; //foreign key refer to User	（该记录的用户）

    //  外键约束

    public User getUser() {
        User user = DBFunction.findUserByName(userName);
        if (user != null) {
            return user;
        } else {
            throw new NullPointerException("外键约束异常：没有找到对应的user");
        }
    }

    // 普通 get & set 方法

    public long getId() {
        return id;
    }

    public long getCommodityId() {
        return commodityId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCommodityId(long commodityId) {
        this.commodityId = commodityId;
    }
}
