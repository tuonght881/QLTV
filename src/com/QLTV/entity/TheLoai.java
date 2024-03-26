/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

/**
 *
 * @author Tuong
 */
public class TheLoai {
    String idtheloai;
    String tentheloai;
    Boolean trangthaitl;

    public TheLoai(String idtheloai, String tentheloai, Boolean trangthaitl) {
        this.idtheloai = idtheloai;
        this.tentheloai = tentheloai;
        this.trangthaitl = trangthaitl;
    }

    public TheLoai() {
    }

    public String getIdtheloai() {
        return idtheloai;
    }

    public void setIdtheloai(String idtheloai) {
        this.idtheloai = idtheloai;
    }

    public String getTentheloai() {
        return tentheloai;
    }

    public void setTentheloai(String tentheloai) {
        this.tentheloai = tentheloai;
    }

    public Boolean getTrangthaitl() {
        return trangthaitl;
    }

    public void setTrangthaitl(Boolean trangthaitl) {
        this.trangthaitl = trangthaitl;
    }
    
    
}
