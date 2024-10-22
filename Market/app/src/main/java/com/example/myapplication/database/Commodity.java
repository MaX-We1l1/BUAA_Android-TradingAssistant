package com.example.myapplication.database;

import org.litepal.crud.LitePalSupport;

public class Commodity extends LitePalSupport {
    private long id; //编号
    private String commodityName; //名称
    // TODO 添加商品图片
    private Float price; //价格
    private String releaseDate; //发布日期
    private String description; //商品描述
    private Type type; //商品类别
    private String sellerName;
    private String buyerName;

    public Commodity() {
    }

    // set && get
    public String getBuyerName() {
        return buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPrice() {
        return price;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
