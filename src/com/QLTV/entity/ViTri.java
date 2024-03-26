/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

/**
 *
 * @author Tuong
 */
public class ViTri {
    String idvitri;
    String tenvt;
    Boolean trangthaivt;

    public ViTri(String idvitri, String tenvt, Boolean trangthaivt) {
        this.idvitri = idvitri;
        this.tenvt = tenvt;
        this.trangthaivt = trangthaivt;
    }

    

    public ViTri() {
    }

    public String getIdvitri() {
        return idvitri;
    }

    public void setIdvitri(String idvitri) {
        this.idvitri = idvitri;
    }

    public String getTenvt() {
        return tenvt;
    }

    public void setTenvt(String tenvt) {
        this.tenvt = tenvt;
    }

    public Boolean getTrangthaivt() {
        return trangthaivt;
    }

    public void setTrangthaivt(Boolean trangthaivt) {
        this.trangthaivt = trangthaivt;
    }

    
}
