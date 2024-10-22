package com.example.myapplication.profile;

public class Address {
    private String name;  // 地址名称或收货人姓名
    private String detail;  // 详细地址
    private String phoneNumber;  // 联系电话
    private boolean isDefault;  // 是否为默认地址

    // 构造函数
    public Address(String name, String detail, String phoneNumber, boolean isDefault) {
        this.name = name;
        this.detail = detail;
        this.phoneNumber = phoneNumber;
        this.isDefault = isDefault;
    }

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}

