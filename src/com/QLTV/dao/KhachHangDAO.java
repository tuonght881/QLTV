/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.KhachHang;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuong
 */
public class KhachHangDAO extends EntityDao<KhachHang, String> {
    String findbysdt = "select * from KhachHang where sdt like ?";
    String selectAll = "select * from KhachHang order by idkhach desc";
    String insert = "insert into KhachHang values(?,?,?,?)";
    String update = "update KhachHang set hotenkhach = ?,sdt = ?,diemuytin = ? where idkhach=?";
    String delete = "delete KhachHang where idkhach=?";
    String select_by_ID = "select * from KhachHang where idkhach=?";
    String select_tenKH = "select hotenkhach from KhachHang";
    String timkiem = "select * from KhachHang where idkhach like ? or hotenkhach like ? order by idkhach desc";
    
    @Override
    public void insert(KhachHang entity) {
        JDBC.update(insert, entity.getIdkhach(),entity.getHotenkhach(),entity.getSdt(),entity.getDiemuytin());
    }

    @Override
    public void update(KhachHang entity) {
        JDBC.update(update, entity.getHotenkhach(),entity.getSdt(),entity.getDiemuytin(),entity.getIdkhach());
    }
    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<KhachHang> selectAll() {
        return select_by_sql(selectAll);
    }

    @Override
    public KhachHang select_byID(String key) {
        List<KhachHang> list = this.select_by_sql(select_by_ID, key);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    @Override
    protected List<KhachHang> select_by_sql(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql,args);
            while(r.next()){
                KhachHang kh =  new KhachHang();
                kh.setIdkhach(r.getString("idkhach"));
                kh.setHotenkhach(r.getString("hotenkhach"));
                kh.setSdt(r.getString("sdt"));
                kh.setDiemuytin(r.getInt("diemuytin"));
                list.add(kh);
            }
            r.getStatement().getConnection().close();
            
        } catch (Exception e) {
        }
        return list;
    }
    
    public List<KhachHang> timkiemTG(String k){
        return select_by_sql(timkiem,"%"+k+"%","%"+k+"%");
    };
    public List<KhachHang> select_ten() {
        return select_by_sql(select_tenKH);
    }
    public KhachHang select_bysdt(String key) {
        List<KhachHang> list = this.select_by_sql(findbysdt, key);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }
}
