/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.entity.ViTri;
import com.QLTV.db.JDBC;
import com.QLTV.db.EntityDao;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
/**
 *
 * @author Tuong
 */
public class ViTriDAO extends EntityDao<ViTri, String> {

    String selectAll = "select * from ViTri order by trangthaivt desc";
    String insert = "insert into ViTri values(?,?,?)";
    String update = "update ViTri set tenvt = ?,trangthaivt = ? where idvitri=?";
    String delete = "delete ViTri where idvitri=?";
    String select_by_ID = "select * from ViTri where idvitri=?";
    String select_by_tenVT="select * from ViTri where tenvt = ?";
    String timkiem = "select * from ViTri where idvitri like ? or tenvt like ? order by trangthaivt desc";
    String selectvt_hd = "select * from ViTri where trangthaivt = 1";
    @Override
    public void insert(ViTri entity) {
        JDBC.update(insert, entity.getIdvitri(), entity.getTenvt(), entity.getTrangthaivt());
    }

    @Override
    public void update(ViTri entity) {
        JDBC.update(update, entity.getTenvt(), entity.getTrangthaivt(), entity.getIdvitri());
    }

    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<ViTri> selectAll() {
        return select_by_sql(selectAll);
    }
    public List<ViTri> selectVT_hd() {
        return select_by_sql(selectvt_hd);
    }
    @Override
    public ViTri select_byID(String key) {
        List<ViTri> list = this.select_by_sql(select_by_ID, key);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    protected List<ViTri> select_by_sql(String sql, Object... args) {
        List<ViTri> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql, args);
            while (r.next()) {
                ViTri vt = new ViTri();
                vt.setIdvitri(r.getString("idvitri"));
                vt.setTenvt(r.getString("tenvt"));
                vt.setTrangthaivt(r.getBoolean("trangthaivt"));
                list.add(vt);
            }
            r.getStatement().getConnection().close();

        } catch (Exception e) {
        }
        return list;
    }

    public List<ViTri> timkiemTvt(String k) {
        return select_by_sql(timkiem, "%" + k + "%", "%" + k + "%");
    };
    public ViTri select_byTenVT(String key) {
        List<ViTri> list = this.select_by_sql(select_by_tenVT, key);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
