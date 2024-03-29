/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.DonThueChiTiet;
import java.util.List;

/**
 *
 * @author Tuong
 */
public class DonThueChiTietDAO extends EntityDao<DonThueChiTiet, String> {

    String insert = "insert into DonThuect values(?,?,?,?)";
    String delete = "delete donthuect where iddonthuect=?";

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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected List<DonThueChiTiet> select_by_sql(String sql, Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
