/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.HoaDon;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Tuong
 */
public class HoaDonDAO extends EntityDao<HoaDon, String>{
    String selectAll = "select * from HoaDon";
    String insert = "insert into HoaDon values(?,?,?,?,?,?)";
    String mahoadon = "select top 1 * from hoadon order by idhoadon desc";
    String select_by_ID = "select * from HoaDon where idhoadon=?";
    String delete = "delete HoaDon where idhoadon=?";
    String timkiem = "select * from HoaDon where idhoadon like ? or manv like ? order by ngaytao desc";
    String update = "UPDATE hoadon SET manv = ?, ngaytao = ?, khachdua = ?, thoilai = ?, thanhtien = ? WHERE idhoadon = ?";
    @Override
    public void insert(HoaDon entity) {
        JDBC.update(insert, entity.getIdhoadon(),entity.getManv(),entity.getNgaytao(),entity.getKhachdua(),entity.getThoilai(),entity.getThanhtien());
    }

    @Override
    public void update(HoaDon entity) {
        JDBC.update(update,entity.getManv(),entity.getNgaytao(),entity.getKhachdua(),entity.getThoilai(),entity.getThanhtien(), entity.getIdhoadon());
    }

    @Override
    public void delete(String entity) {
         JDBC.update(delete, entity);
    }

    @Override
    public List<HoaDon> selectAll() {
        return select_by_sql(selectAll);
    }

    @Override
    public HoaDon select_byID(String key) {
        List<HoaDon> list = this.select_by_sql(select_by_ID, key);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    @Override
    protected List<HoaDon> select_by_sql(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql,args);
            while(r.next()){
                HoaDon hd =  new HoaDon();
                hd.setIdhoadon(r.getString("idhoadon"));
                hd.setManv(r.getString("manv"));
                hd.setNgaytao(r.getString("ngaytao"));
                hd.setKhachdua(r.getDouble("khachdua"));
                hd.setThoilai(r.getDouble("thoilai"));
                hd.setThanhtien(r.getDouble("thanhtien"));
                list.add(hd);
            }
            r.getStatement().getConnection().close();
            
        } catch (Exception e) {
        }
        return list;
    }
    public String getmaHD() {
        ResultSet rs = JDBC.query(mahoadon);
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
            public List<HoaDon> timHD(String k){
        return select_by_sql(timkiem,"%"+k+"%","%"+k+"%");
    };
}
