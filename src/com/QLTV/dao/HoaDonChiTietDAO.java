/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.HoaDonChiTiet;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

/**
 *
 * @author Tuong
 */
public class HoaDonChiTietDAO extends EntityDao<HoaDonChiTiet, String> {

    String selectAll = "select * from HoaDonChiTiet";
    String select_by_hoadon = "select * from HoaDonChiTiet where idhoadon = ?";
    String insert = "insert into HoaDonChiTiet values(?,?,?)";
    String select_by_ID = "select * from HoaDonChiTiet where idhoadonct = ?";
    String delete = "delete HoaDonChiTiet where idhoadonct=?";
    @Override
    public void insert(HoaDonChiTiet entity) {
        JDBC.update(insert, entity.getIdhoadon(), entity.getIdsach(), entity.getSoluong());
    }

    @Override
    public void update(HoaDonChiTiet entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<HoaDonChiTiet> selectAll() {
        return select_by_sql(selectAll);
    }

    @Override
    public HoaDonChiTiet select_byID(String key) {
        List<HoaDonChiTiet> list = this.select_by_sql(select_by_ID, key);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    protected List<HoaDonChiTiet> select_by_sql(String sql, Object... args) {
        List<HoaDonChiTiet> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql, args);
            while (r.next()) {
                HoaDonChiTiet hdct = new HoaDonChiTiet();
                hdct.setIdhoadonct(r.getInt("idhoadonct"));
                hdct.setIdhoadon(r.getString("idhoadon"));
                hdct.setIdsach(r.getString("idsach"));
                hdct.setSoluong(r.getInt("soluong"));
                list.add(hdct);
            }
            r.getStatement().getConnection().close();

        } catch (Exception e) {
        }
        return list;
    }

    public List<HoaDonChiTiet> select_by_HD(String key) {
        return select_by_sql(select_by_hoadon, key);
    }
    public HoaDonChiTiet select_byID_int(int key) {
        List<HoaDonChiTiet> list = this.select_by_sql(select_by_ID, key);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
        public void delete_int(int entity) {
        JDBC.update(delete, entity);
    }}
