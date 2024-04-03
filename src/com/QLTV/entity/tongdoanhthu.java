/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

/**
 *
 * @author Tuong
 */
public class tongdoanhthu {

    double doanhThuBanSach;
    double doanhThuThueSach;
    double tongDoanhThu;

    public tongdoanhthu(double doanhThuBanSach, double doanhThuThueSach, double tongDoanhThu) {
        this.doanhThuBanSach = doanhThuBanSach;
        this.doanhThuThueSach = doanhThuThueSach;
        this.tongDoanhThu = tongDoanhThu;
    }

    public double getDoanhThuBanSach() {
        return doanhThuBanSach;
    }

    public void setDoanhThuBanSach(double doanhThuBanSach) {
        this.doanhThuBanSach = doanhThuBanSach;
    }

    public double getDoanhThuThueSach() {
        return doanhThuThueSach;
    }

    public void setDoanhThuThueSach(double doanhThuThueSach) {
        this.doanhThuThueSach = doanhThuThueSach;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }
}
