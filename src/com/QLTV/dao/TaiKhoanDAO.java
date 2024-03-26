/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.TaiKhoan;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuong
 */
public class TaiKhoanDAO extends EntityDao<TaiKhoan, String> {

    String selectAll = "select * from TaiKhoan order by trangthai desc";
    String insert = "insert into TaiKhoan values(?,?,?,?,?,?,?,?,?)";
    String update = "update TaiKhoan set matkhau = ?,vaitro = ?,trangthai = ?,hoten=?,gioitinh=?,sdt=?,ngaysinh=?,diachi=? where manv=?";
    String delete = "delete TaiKhoan where manv=?";
    String select_by_ID = "select * from TaiKhoan where manv=?";
    String timkiem = "select * from TaiKhoan where manv like ? or hoten like ? order by trangthai desc";

    @Override
    public void insert(TaiKhoan entity) {
        JDBC.update(insert, entity.getManv(), entity.getMatkhau(), entity.getVaitro(), entity.getTrangthai(), entity.getHoten(), entity.getGioitinh(), entity.getSdt(), entity.getNgaysinh(), entity.getDiachi());
    }

    @Override
    public void update(TaiKhoan entity) {
        JDBC.update(update, entity.getMatkhau(), entity.getVaitro(), entity.getTrangthai(), entity.getHoten(), entity.getGioitinh(), entity.getSdt(), entity.getNgaysinh(), entity.getDiachi(), entity.getManv());
    }

    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<TaiKhoan> selectAll() {
        return select_by_sql(selectAll);
    }

    @Override
    public TaiKhoan select_byID(String key) {
        List<TaiKhoan> list = this.select_by_sql(select_by_ID, key);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    protected List<TaiKhoan> select_by_sql(String sql, Object... args) {
        List<TaiKhoan> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql, args);
            while (r.next()) {
                TaiKhoan kh = new TaiKhoan();
                kh.setManv(r.getString("manv"));
                kh.setMatkhau(r.getString("matkhau"));
                kh.setVaitro(r.getBoolean("vaitro"));
                kh.setTrangthai(r.getBoolean("trangthai"));
                kh.setHoten(r.getString("hoten"));
                kh.setGioitinh(r.getBoolean("gioitinh"));
                kh.setSdt(r.getString("sdt"));
                kh.setNgaysinh(r.getString("ngaysinh"));
                kh.setDiachi(r.getString("diachi"));
                list.add(kh);
            }
            r.getStatement().getConnection().close();

        } catch (Exception e) {
        }
        return list;
    }

    public List<TaiKhoan> timkiemTK(String k) {
        return select_by_sql(timkiem, "%" + k + "%", "%" + k + "%");
    }
;
}
