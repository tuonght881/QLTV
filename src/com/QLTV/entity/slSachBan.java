/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

/**
 *
 * @author Tuong
 */
public class slSachBan {

    String idSach;
    String tenSach;
    int soLuongBan;

    public slSachBan() {
    }
    
    
    // Constructor
    public slSachBan(String idSach, String tenSach, int soLuongBan) {
        this.idSach = idSach;
        this.tenSach = tenSach;
        this.soLuongBan = soLuongBan;
    }

    // Getters
    public String getIdSach() {
        return idSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public int getSoLuongBan() {
        return soLuongBan;
    }
}
