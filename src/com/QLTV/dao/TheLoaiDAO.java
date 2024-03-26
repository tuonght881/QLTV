/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.TheLoai;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuong
 */
public class TheLoaiDAO extends EntityDao<TheLoai, String> {

    String selectAll = "select * from TheLoai order by trangthaitl desc";
    String insert = "insert into TheLoai values(?,?,?)";
    String select_by_ID = "select * from TheLoai where idtheloai=?";
    String select_by_theloai = "select * from TheLoai where tentheloai = ?";
    String update = "update TheLoai set tentheloai = ?,trangthaitl = ? where idtheloai=?";
    String delete = "delete TheLoai where idtheloai=?";
    String timkiem = "select * from TheLoai where idtheloai like ? or tentheloai like ? order by trangthaitl desc";

    @Override
    public void insert(TheLoai entity) {
        JDBC.update(insert, entity.getIdtheloai(),entity.getTentheloai(),entity.getTrangthaitl());
    }

    @Override
    public void update(TheLoai entity) {
        JDBC.update(update, entity.getTentheloai(),entity.getTrangthaitl(),entity.getIdtheloai());
    }

    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<TheLoai> selectAll() {
        return select_by_sql(selectAll);
    }

    @Override
    public TheLoai select_byID(String key) {
        List<TheLoai> list = this.select_by_sql(select_by_ID, key);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    @Override
    protected List<TheLoai> select_by_sql(String sql, Object... args) {
        List<TheLoai> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql, args);
            while (r.next()) {
                TheLoai tg = new TheLoai();
                tg.setIdtheloai(r.getString("idtheloai"));
                tg.setTentheloai(r.getString("tentheloai"));
                tg.setTrangthaitl(r.getBoolean("trangthaitl"));
                list.add(tg);
            }
            r.getStatement().getConnection().close();

        } catch (Exception e) {
        }
        return list;
    }
    public List<TheLoai> timkiemTL(String k){
        return select_by_sql(timkiem,"%"+k+"%","%"+k+"%");
    };
        public TheLoai select_byTheLoai(String key) {
        List<TheLoai> list = this.select_by_sql(select_by_theloai, key);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }
}
