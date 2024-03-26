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
    String iddonthuect;
    String iddonthue;
    String idsach;
    int soluong;

    public DonThueChiTiet() {
    }

    public DonThueChiTiet(String iddonthuect, String iddonthue, String idsach, int soluong) {
        this.iddonthuect = iddonthuect;
        this.iddonthue = iddonthue;
        this.idsach = idsach;
        this.soluong = soluong;
    }

    public String getIddonthuect() {
        return iddonthuect;
    }

    public void setIddonthuect(String iddonthuect) {
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
