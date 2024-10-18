package com.example.myapplication.database;

import org.litepal.crud.LitePalSupport;

public class Commodity extends LitePalSupport {
    private long id; //编号，期望递增
    private String commodityName; //名称
    // TODO 添加商品图片
    private Float price; //价格
    private String releaseDate; //发布日期
    private String description; //商品描述
    private Type type; //商品类别

    // set && get
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
