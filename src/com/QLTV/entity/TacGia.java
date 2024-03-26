/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

/**
 *
 * @author Tuong
 */
public class TacGia {
    String idtg;
    String tentg;
    Boolean trangthaitg;

    public TacGia() {
    }

    public TacGia(String idtg, String tentg, Boolean trangthaitg) {
        this.idtg = idtg;
        this.tentg = tentg;
        this.trangthaitg = trangthaitg;
    }

    public String getIdtg() {
        return idtg;
    }

    public void setIdtg(String idtg) {
        this.idtg = idtg;
    }

    public String getTentg() {
        return tentg;
    }

    public void setTentg(String tentg) {
        this.tentg = tentg;
    }

    public Boolean getTrangthaitg() {
        return trangthaitg;
    }

    public void setTrangthaitg(Boolean trangthaitg) {
        this.trangthaitg = trangthaitg;
    }
    
    
}
