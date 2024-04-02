/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.entity;

import java.util.Date;

/**
 *
 * @author Tuong
 */
public class DonThue {
    String iddonthue;
    String idkhach;
    String manv;
    String ngaytao;
    String ngaythue;
    String ngaytradukien;
    String ngaytra;
    Double tienphat;
    Double tongtiendambao;
    Double khachdua;
    Double thoilai;
    Double thanhtien;

    public DonThue() {
    }

    public DonThue(String iddonthue, String idkhach, String manv, String ngaytao, String ngaythue, String ngaytradukien, String ngaytra, Double tienphat, Double tongtiendambao, Double khachdua, Double thoilai, Double thanhtien) {
        this.iddonthue = iddonthue;
        this.idkhach = idkhach;
        this.manv = manv;
        this.ngaytao = ngaytao;
        this.ngaythue = ngaythue;
        this.ngaytradukien = ngaytradukien;
        this.ngaytra = ngaytra;
        this.tienphat = tienphat;
        this.tongtiendambao = tongtiendambao;
        this.khachdua = khachdua;
        this.thoilai = thoilai;
        this.thanhtien = thanhtien;
    }

    public String getIddonthue() {
        return iddonthue;
    }

    public void setIddonthue(String iddonthue) {
        this.iddonthue = iddonthue;
    }

    public String getIdkhach() {
        return idkhach;
    }

    public void setIdkhach(String idkhach) {
        this.idkhach = idkhach;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }

    public String getNgaythue() {
        return ngaythue;
    }

    public void setNgaythue(String ngaythue) {
        this.ngaythue = ngaythue;
    }

    public String getNgaytradukien() {
        return ngaytradukien;
    }

    public void setNgaytradukien(String ngaytradukien) {
        this.ngaytradukien = ngaytradukien;
    }

    public String getNgaytra() {
        return ngaytra;
    }

    public void setNgaytra(String ngaytra) {
        this.ngaytra = ngaytra;
    }

    public Double getTienphat() {
        return tienphat;
    }

    public void setTienphat(Double tienphat) {
        this.tienphat = tienphat;
    }

    public Double getTongtiendambao() {
        return tongtiendambao;
    }

    public void setTongtiendambao(Double tongtiendambao) {
        this.tongtiendambao = tongtiendambao;
    }

    public Double getKhachdua() {
        return khachdua;
    }

    public void setKhachdua(Double khachdua) {
        this.khachdua = khachdua;
    }

    public Double getThoilai() {
        return thoilai;
    }

    public void setThoilai(Double thoilai) {
        this.thoilai = thoilai;
    }

    public Double getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(Double thanhtien) {
        this.thanhtien = thanhtien;
    }

    
}
