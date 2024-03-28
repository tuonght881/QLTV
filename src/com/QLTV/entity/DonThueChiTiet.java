/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

/**
 *
 * @author Tuong
 */
public class DonThueChiTiet {
    int iddonthuect;
    String iddonthue;
    String idsach;
    int soluong;

    public DonThueChiTiet() {
    }

    public DonThueChiTiet(int iddonthuect, String iddonthue, String idsach, int soluong) {
        this.iddonthuect = iddonthuect;
        this.iddonthue = iddonthue;
        this.idsach = idsach;
        this.soluong = soluong;
    }

    public int getIddonthuect() {
        return iddonthuect;
    }

    public void setIddonthuect(int iddonthuect) {
        this.iddonthuect = iddonthuect;
    }

    public String getIddonthue() {
        return iddonthue;
    }

    public void setIddonthue(String iddonthue) {
        this.iddonthue = iddonthue;
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
