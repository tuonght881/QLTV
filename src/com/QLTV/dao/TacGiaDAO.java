/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.TacGia;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuong
 */
public class TacGiaDAO extends EntityDao<TacGia, String> {

    String selectAll = "select * from TacGia order by trangthaitg desc";
    String insert = "insert into TacGia values(?,?,?)";
    String update = "update TacGia set tentg = ?,trangthaitg = ? where idtg=?";
    String delete = "delete TacGia where idtg=?";
    String select_by_ID = "select * from TacGia where idtg=?";
    String select_by_tacgia ="select * from TacGia where tentg=?";
    String timkiem = "select * from tacgia where idtg like ? or tentg like ? order by trangthaitg desc";
    String select_tG_hd = "select * from TacGia where trangthaitg = 1 ";
    @Override
    public void insert(TacGia entity) {
        JDBC.update(insert, entity.getIdtg(),entity.getTentg(),entity.getTrangthaitg());
    }

    @Override
    public void update(TacGia entity) {
        JDBC.update(update, entity.getTentg(),entity.getTrangthaitg(),entity.getIdtg());
    }
    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<TacGia> selectAll() {
        return select_by_sql(selectAll);
    }
    public List<TacGia> selecttg_hd() {
        return select_by_sql(select_tG_hd);
    }
    @Override
    public TacGia select_byID(String key) {
        List<TacGia> list = this.select_by_sql(select_by_ID, key);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    @Override
    protected List<TacGia> select_by_sql(String sql, Object... args) {
        List<TacGia> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql,args);
            while(r.next()){
                TacGia tg =  new TacGia();
                tg.setIdtg(r.getString("idtg"));
                tg.setTentg(r.getString("tentg"));
                tg.setTrangthaitg(r.getBoolean("trangthaitg"));
                list.add(tg);
            }
            r.getStatement().getConnection().close();
            
        } catch (Exception e) {
        }
        return list;
    }
    
    public List<TacGia> timkiemTG(String k){
        return select_by_sql(timkiem,"%"+k+"%","%"+k+"%");
    };
    
        public TacGia select_byTenTG(String key) {
        List<TacGia> list = this.select_by_sql(select_by_tacgia, key);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }
}
