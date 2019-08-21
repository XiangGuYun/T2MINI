package com.yp.payment.entity;

public class DepositorInfo {
    String name;
    String phone;
    double totalPrice;

    public DepositorInfo(String name, String phone, double totalPrice) {
        this.name = name;
        this.phone = phone;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
