package com.example.myapplication.database;

public class Comment {
    private long id;
    private long commodityId;
    private String userName;
    private String title;      // 收藏标题
    private String description;// 收藏描述
    private String date;       // 收藏日期
    private int imageResource;

    public Comment() {

    }

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

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

}
