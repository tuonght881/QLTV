/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.Sach;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

/**
 *
 * @author Tuong
 */
public class SachDAO extends EntityDao<Sach, String>{
    String selectAll = "select * from Sach order by trangthaiSach desc";
    String insert = "insert into Sach values(?,?,?,?,?,?,?,?,?,?)";
    String select_by_ID = "select * from Sach where idsach=?";
    String update = "UPDATE sach SET tensach = ?, idtheloai = ?, idtacgia = ?, idvitri = ?, sl = ?, giaban = ?, giathue1ngay = ?, trangthaiSach = ?, anhsach = ? WHERE idsach = ?";
    String delete = "delete Sach where idsach=?";
    String timkiem = "select * from Sach where idsach like ? or tensach like ? order by trangthaiSach desc";
    String getIDSach = "select idsach from Sach where tensach like ?";
    String getNameSach = "select tensach from Sach where idsach like ?";
    String getGiaBan = "select giaban from Sach where idsach like ?";
    @Override
    public void insert(Sach entity) {
        JDBC.update(insert, entity.getIdsach(), entity.getTensach(),entity.getIdtheloai(),entity.getIdtacgia(),entity.getIdvitri(),entity.getSl(),entity.getGiaban(),entity.getGiathue1ngay(),entity.getTrangthaisach(),entity.getAnhsach());
    }

    @Override
    public void update(Sach entity) {
        JDBC.update(update, entity.getTensach(),entity.getIdtheloai(),entity.getIdtacgia(),entity.getIdvitri(),entity.getSl(),entity.getGiaban(),entity.getGiathue1ngay(),entity.getTrangthaisach(),entity.getAnhsach(),entity.getIdsach());
    }

    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<Sach> selectAll() {
        return select_by_sql(selectAll);
    }

    @Override
    public Sach select_byID(String key) {
        List<Sach> list = this.select_by_sql(select_by_ID, key);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    @Override
    protected List<Sach> select_by_sql(String sql, Object... args) {
        List<Sach> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql, args);
            while (r.next()) {
                Sach sach = new Sach();
                sach.setIdsach(r.getString("idsach"));
                sach.setTensach(r.getString("tensach"));
                sach.setIdtheloai(r.getString("idtheloai"));
                sach.setIdtacgia(r.getString("idtacgia"));
                sach.setIdvitri(r.getString("idvitri"));
                sach.setSl(r.getInt("sl"));
                sach.setGiaban(r.getDouble("giaban"));
                sach.setGiathue1ngay(r.getDouble("giathue1ngay"));
                sach.setTrangthaisach(r.getBoolean("trangthaisach"));
                sach.setAnhsach(r.getString("anhsach"));
                list.add(sach);
            }
            r.getStatement().getConnection().close();

        } catch (Exception e) {
        }
        return list;
    }
        public List<Sach> timkiemSach(String k){
        return select_by_sql(timkiem,"%"+k+"%","%"+k+"%");
    };
        
    public String getMasach(String idsach){
        ResultSet rs = JDBC.query(getIDSach, "%"+idsach+"%");
        List<String> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(rs.getString("idsach"));
            }
        return list.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getTenSach(String name){
        ResultSet rs = JDBC.query(getNameSach, "%"+name+"%");
        List<String> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(rs.getString("tensach"));
            }
        return list.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getGiaBan(String name){
        ResultSet rs = JDBC.query(getGiaBan, "%"+name+"%");
        List<String> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(rs.getString("giaban"));
            }
        return list.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
