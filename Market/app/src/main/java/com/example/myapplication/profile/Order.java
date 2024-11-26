package com.example.myapplication.profile;

public class Order {
    private String title;
    //Title 目前就是商品的名字
    private String status;
    private double price;
    private int imageResourceId;// 假设图片是资源 ID
    private long commodityId;

    public Order(String title, String status, double price, int imageResourceId, long commodityId) {
        this.title = title;
        this.status = status;
        this.price = price;
        this.imageResourceId = imageResourceId;
        this.commodityId = commodityId;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public long getCommodityId() {
        return commodityId;
    }
}

