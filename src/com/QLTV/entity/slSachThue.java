/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

/**
 *
 * @author Tuong
 */
public class slSachThue {

    String idSach;
    String tenSach;
    int soLuongThue;

    public slSachThue() {
    }

    public slSachThue(String idSach, String tenSach, int soLuongThue) {
        this.idSach = idSach;
        this.tenSach = tenSach;
        this.soLuongThue = soLuongThue;
    }

    public String getIdSach() {
        return idSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public int getSoLuongThue() {
        return soLuongThue;
    }
    
    
    
}
