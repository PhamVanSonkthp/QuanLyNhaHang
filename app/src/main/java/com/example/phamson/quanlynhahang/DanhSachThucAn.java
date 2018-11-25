package com.example.phamson.quanlynhahang;

import java.io.Serializable;

public class DanhSachThucAn implements Serializable {
    String hinhAnh;
    String name;
    String number;
    String price;

    public DanhSachThucAn() {
    }

    public DanhSachThucAn(String hinhAnh, String name, String number, String price) {
        this.hinhAnh = hinhAnh;
        this.name = name;
        this.number = number;
        this.price = price;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
