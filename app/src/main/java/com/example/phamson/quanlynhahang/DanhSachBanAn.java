package com.example.phamson.quanlynhahang;

import java.io.Serializable;

public class DanhSachBanAn implements Serializable {
    String tenBan;

    public DanhSachBanAn(String tenBan) {
        this.tenBan = tenBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }
}
