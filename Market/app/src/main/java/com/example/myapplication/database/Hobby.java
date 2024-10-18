package com.example.myapplication.database;

import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class Hobby extends LitePalSupport {
    private long id;
    private Type commodityType;
    private long user_id;
    private User user; //foreign key refer to User	（该记录的用户）

    //  外键约束
    public int getUserId() {
        return (int) user_id;
    }

    public User getUser() {
        User user = LitePal.find(User.class, user_id);
        if (user != null) {
            return user;
        } else {
            throw new NullPointerException("外键约束异常：没有找到对应的user");
        }
    }

    public boolean setUserByName(String userName) {
        User user = DBFunction.findUserByName(userName);
        if (user != null) {
            this.user = user;
            return true;
        } else {
            Log.w(DBFunction.TAG, "数据不符合外键约束！插入数据失败");
            return false;
        }
    }
    // 普通 get & set 方法

    public long getId() {
        return id;
    }

    public Type getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(Type commodityType) {
        this.commodityType = commodityType;
    }
}
