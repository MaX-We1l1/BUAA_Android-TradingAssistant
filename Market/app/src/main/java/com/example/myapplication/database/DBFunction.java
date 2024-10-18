package com.example.myapplication.database;

import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class DBFunction {
    public static final String TAG = "from db";

    public static User findUserByName(String username) {
        List<User> users = LitePal.where("username = ?", username).find(User.class);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }
    
    public static void addUser(String username, String password, String registerDate) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRegisterTime(registerDate);
        user.save();
    }

    //是否允许登录
    public static boolean isAllowLogin(String username, String password) {
        return LitePal.isExist(User.class, "username = ? and password = ?", username, password);
    }
    
    //保证用户名唯一
    public static boolean isUsernameExist(String username) {
        return LitePal.isExist(User.class, "username = ?", username);
    }

    //修改密码
    public static void changePassword(String username, String newPassword) {
        User updateUser = new User();
        updateUser.setPassword(newPassword);
        updateUser.updateAll("username = ?", username);
    }

    public static void cancelUser(String username) {
        long num = 0;
        User user = findUserByName(username);
        if (user != null) {
            User updateUser = new User();
            updateUser.setUsername("用户已注销_" + user.getId());
            updateUser.updateAll("username = ?", username);
        }
    }

    public static String getUserBirthday(String username) {
        return LitePal.where("username = ?", username).find(User.class).get(0).getBirthday();
    }


    //拿到username的当前性别，返回"男"或者"女"
    public static String getGender(String username) {
        return LitePal.where("username = ?", username).find(User.class).get(0).getSex();
    }

    //个性签名
    public static String getPersonalSign(String username) {
        return LitePal.where("username = ?", username).find(User.class).get(0).getPersonalitySign();
    }

    public static String getUserRegisterTime(String username) {
        return LitePal.where("username = ?", username).find(User.class).get(0).getRegisterTime();
    }

    public static void setUserGender(String username, String newGender) {
        User updateUser = new User();
        updateUser.setSex(newGender);
        updateUser.updateAll("username = ?", username);
    }


    //将用户的出生日期设置为参数date（是类似于“2023-11-28”这样的格式的，不需要再进行格式解析，直接set即可）
    public static void setUserBirthday(String username, String date) {
        User updateUser = new User();
        updateUser.setBirthday(date);
        updateUser.updateAll("username = ?", username);
    }

    //设置一个用户的个性签名为newSign
    public static void setPersonalSign(String username, String newSign) {
        User updateUser = new User();
        updateUser.setPersonalitySign(newSign);
        updateUser.updateAll("username = ?", username);
    }

    public static void addStar(String username, Type commodityType) {
        Hobby hobby = new Hobby();
        hobby.setUserByName(username);
        hobby.setCommodityType(commodityType);
        hobby.save();
    }

    //    取消用户对某项运动的收藏，保证这个sportName在收藏列表里
    public static void cancelStar(String username, String sportName) {
        User user = findUserByName(username);
        if (user != null) {
            LitePal.deleteAll(Hobby.class, "user_id=? and sportname=?", String.valueOf(user.getId()), sportName);
        } else {
            Log.w(DBFunction.TAG, "不存在username用户，cancelStar失败");
        }
    }

    public static void addRecordToDB(TradeRecord oneTradeRecord) {
        TradeRecord tradeRecord = new TradeRecord();
        tradeRecord.setBuyer(oneTradeRecord.getBuyer());
        tradeRecord.setSeller(oneTradeRecord.getSeller());
        tradeRecord.setDate(oneTradeRecord.getDate());
        tradeRecord.setNote(oneTradeRecord.getNote());
        tradeRecord.save();
    }
}
