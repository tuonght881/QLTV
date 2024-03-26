/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

import java.util.Date;

/**
 *
 * @author Tuong
 */
public class HoaDon {
    String idhoadon;
    String manv;
    String ngaytao;
    Double khachdua;
    Double thoilai;
    Double thanhtien;

    public HoaDon() {
    }

    public HoaDon(String idhoadon, String manv, String ngaytao, Double khachdua, Double thoilai, Double thanhtien) {
        this.idhoadon = idhoadon;
        this.manv = manv;
        this.ngaytao = ngaytao;
        this.khachdua = khachdua;
        this.thoilai = thoilai;
        this.thanhtien = thanhtien;
    }

    public String getIdhoadon() {
        return idhoadon;
    }

    public void setIdhoadon(String idhoadon) {
        this.idhoadon = idhoadon;
    }


    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }

    public Double getKhachdua() {
        return khachdua;
    }

    public void setKhachdua(Double khachdua) {
        this.khachdua = khachdua;
    }

    public Double getThoilai() {
        return thoilai;
    }

    public void setThoilai(Double thoilai) {
        this.thoilai = thoilai;
    }

    public Double getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(Double thanhtien) {
        this.thanhtien = thanhtien;
    }

   
}
