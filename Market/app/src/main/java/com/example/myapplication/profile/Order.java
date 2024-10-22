package com.example.myapplication.profile;

public class Order {
    private String title;
    private String status;
    private double price;
    private int imageResourceId; // 假设图片是资源 ID

    public Order(String title, String status, double price, int imageResourceId) {
        this.title = title;
        this.status = status;
        this.price = price;
        this.imageResourceId = imageResourceId;
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
}

