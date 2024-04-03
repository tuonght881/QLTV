/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.DonThueChiTiet;
import com.QLTV.entity.slSachThue;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;


/**
 *
 * @author Tuong
 */
public class DonThueChiTietDAO extends EntityDao<DonThueChiTiet, String> {

    String insert = "insert into DonThuect values(?,?,?,?)";
    String delete = "delete donthuect where iddonthuect=?";
    String select_by_ID = "select * from donthuect where iddonthuect = ?";
    String select_by_hoadon = "select * from DonThueCT where iddonthue = ?";
    String slThue = "SELECT s.idsach, s.tensach, ISNULL(SUM(dtct.soluong),0) AS soluong_thue FROM sach s LEFT JOIN donthuect dtct ON s.idsach = dtct.idsach GROUP BY s.idsach, s.tensach;";

    @Override
    public void insert(DonThueChiTiet entity) {
        JDBC.update(insert, entity.getIddonthue(), entity.getIdsach(), entity.getSoluong(), entity.getTiendambao());
    }

    @Override
    public void update(DonThueChiTiet entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<DonThueChiTiet> selectAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public DonThueChiTiet select_byID(String key) {
        List<DonThueChiTiet> list = this.select_by_sql(select_by_ID, key);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    protected List<DonThueChiTiet> select_by_sql(String sql, Object... args) {
        List<DonThueChiTiet> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql, args);
            while (r.next()) {
                DonThueChiTiet hd = new DonThueChiTiet();
                hd.setIddonthuect(r.getInt("iddonthuect"));
                hd.setIddonthue(r.getString("iddonthue"));
                hd.setIdsach(r.getString("idsach"));
                hd.setSoluong(r.getInt("soluong"));
                hd.setTiendambao(r.getDouble("tiendambao"));
                list.add(hd);
            }
            r.getStatement().getConnection().close();

        } catch (Exception e) {
        }
        return list;
    }

    public List<DonThueChiTiet> select_by_HD(String key) {
        return select_by_sql(select_by_hoadon, key);
    }
    public List<slSachThue> slSachthue() {
        List<slSachThue> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(slThue);
            while (r.next()) {
                String idSach = r.getString("idsach");
                String tenSach = r.getString("tensach");
                int soLuongthue = r.getInt("soluong_thue");
                slSachThue sBan = new slSachThue(idSach, tenSach, soLuongthue);
                list.add(sBan);
            }
            r.getStatement().getConnection().close();
        } catch (Exception e) {
        }
        return list;
    }
}
