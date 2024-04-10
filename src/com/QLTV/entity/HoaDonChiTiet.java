/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

/**
 *
 * @author Tuong
 */
public class HoaDonChiTiet {
    int idhoadonct;
    int idhoadon;
    String idsach;
    int soluong;

    public HoaDonChiTiet() {
    }

    public HoaDonChiTiet(int idhoadonct, int idhoadon, String idsach, int soluong) {
        this.idhoadonct = idhoadonct;
        this.idhoadon = idhoadon;
        this.idsach = idsach;
        this.soluong = soluong;
    }

    public int getIdhoadonct() {
        return idhoadonct;
    }

    public void setIdhoadonct(int idhoadonct) {
        this.idhoadonct = idhoadonct;
    }

    public int getIdhoadon() {
        return idhoadon;
    }

    public void setIdhoadon(int idhoadon) {
        this.idhoadon = idhoadon;
    }

    public String getIdsach() {
        return idsach;
    }

    public void setIdsach(String idsach) {
        this.idsach = idsach;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

   
}
