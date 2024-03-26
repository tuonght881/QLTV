/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

/**
 *
 * @author Tuong
 */
public class Sach {
    String idsach;
    String tensach;
    String idtheloai;
    String idtacgia;
    String idvitri;
    int sl;
    Double giaban;
    Double giathue1ngay;
    Boolean trangthaisach;
    String anhsach;

    public Sach() {
    }

    public Sach(String idsach, String tensach, String idtheloai, String idtacgia, String idvitri, int sl, Double giaban, Double giathue1ngay, Boolean trangthaisach, String anhsach) {
        this.idsach = idsach;
        this.tensach = tensach;
        this.idtheloai = idtheloai;
        this.idtacgia = idtacgia;
        this.idvitri = idvitri;
        this.sl = sl;
        this.giaban = giaban;
        this.giathue1ngay = giathue1ngay;
        this.trangthaisach = trangthaisach;
        this.anhsach = anhsach;
    }

    public String getIdsach() {
        return idsach;
    }

    public void setIdsach(String idsach) {
        this.idsach = idsach;
    }

    public String getTensach() {
        return tensach;
    }

    public void setTensach(String tensach) {
        this.tensach = tensach;
    }

    public String getIdtheloai() {
        return idtheloai;
    }

    public void setIdtheloai(String idtheloai) {
        this.idtheloai = idtheloai;
    }

    public String getIdtacgia() {
        return idtacgia;
    }

    public void setIdtacgia(String idtacgia) {
        this.idtacgia = idtacgia;
    }

    public String getIdvitri() {
        return idvitri;
    }

    public void setIdvitri(String idvitri) {
        this.idvitri = idvitri;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public Double getGiaban() {
        return giaban;
    }

    public void setGiaban(Double giaban) {
        this.giaban = giaban;
    }

    public Double getGiathue1ngay() {
        return giathue1ngay;
    }

    public void setGiathue1ngay(Double githue1ngay) {
        this.giathue1ngay = githue1ngay;
    }

    public Boolean getTrangthaisach() {
        return trangthaisach;
    }

    public void setTrangthaisach(Boolean trangthaisach) {
        this.trangthaisach = trangthaisach;
    }

    public String getAnhsach() {
        return anhsach;
    }

    public void setAnhsach(String anhsach) {
        this.anhsach = anhsach;
    }

    
}
