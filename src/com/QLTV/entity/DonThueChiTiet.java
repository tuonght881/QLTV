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
    int iddonthue;
    String idsach;
    int soluong;
    Double tiendambao;

    public DonThueChiTiet() {
    }

    public DonThueChiTiet(int iddonthuect, int iddonthue, String idsach, int soluong, Double tiendambao) {
        this.iddonthuect = iddonthuect;
        this.iddonthue = iddonthue;
        this.idsach = idsach;
        this.soluong = soluong;
        this.tiendambao = tiendambao;
    }

    public int getIddonthuect() {
        return iddonthuect;
    }

    public void setIddonthuect(int iddonthuect) {
        this.iddonthuect = iddonthuect;
    }

    public int getIddonthue() {
        return iddonthue;
    }

    public void setIddonthue(int iddonthue) {
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

    public Double getTiendambao() {
        return tiendambao;
    }

    public void setTiendambao(Double tiendambao) {
        this.tiendambao = tiendambao;
    }
    
}
