/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.DonThue;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuong
 */
public class DonThueDAO extends EntityDao<DonThue, String> {

    String madonthue = "select top 1 * from donthue order by idhoadon desc";
    String selectAll = "select * from HoaDon";

    @Override
    public void insert(DonThue entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(DonThue entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DonThue> selectAll() {
        return select_by_sql(selectAll);
    }

    @Override
    public DonThue select_byID(String key) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected List<DonThue> select_by_sql(String sql, Object... args) {
        List<DonThue> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql, args);
            while (r.next()) {
                DonThue dthue = new DonThue();
                dthue.setIddonthue(r.getString("iddonthue"));
                dthue.setIdkhach(r.getString("idkhach"));
                dthue.setManv(r.getString("manv"));
                dthue.setNgaytao(r.getString("ngaytao"));
                dthue.setNgaythue(r.getString("ngaythue"));
                dthue.setNgaytradukien(r.getString("ngaytradukien"));
                dthue.setNgaytra(r.getString("ngaytra"));
                dthue.setTienphat(r.getDouble("tienphat"));
                dthue.setTiendambao(r.getDouble("tiendambao"));
                dthue.setKhachdua(r.getDouble("thoilai"));
                dthue.setThoilai(r.getDouble("thoilai"));
                dthue.setThanhtien(r.getDouble("thanhtien"));
                list.add(dthue);
            }
            r.getStatement().getConnection().close();

        } catch (Exception e) {
        }
        return list;
    }

    public String getIDdonthue() {
        ResultSet rs = JDBC.query(madonthue);
        String mahoadon = "";
        try {
            while (rs.next()) {
                mahoadon = rs.getString("idhoadon");
            }
            rs.getStatement().getConnection().close();
            return mahoadon;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
