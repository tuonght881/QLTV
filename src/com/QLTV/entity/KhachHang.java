/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

public class KhachHang {
    String idkhach;
    String hotenkhach;
    String sdt;
    int diemuytin;

    public KhachHang() {
    }

    public KhachHang(String idkhach, String hotenkhach, String sdt, int diemuytin) {
        this.idkhach = idkhach;
        this.hotenkhach = hotenkhach;
        this.sdt = sdt;
        this.diemuytin = diemuytin;
    }

    public String getIdkhach() {
        return idkhach;
    }

    public void setIdkhach(String idkhach) {
        this.idkhach = idkhach;
    }

    public String getHotenkhach() {
        return hotenkhach;
    }

    public void setHotenkhach(String hotenkhach) {
        this.hotenkhach = hotenkhach;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getDiemuytin() {
        return diemuytin;
    }

    public void setDiemuytin(int diemuytin) {
        this.diemuytin = diemuytin;
    }

    
}
