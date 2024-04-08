/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.DonThue;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuong
 */
public class DonThueDAO extends EntityDao<DonThue, String> {
    
    String madonthue = "select top 1 * from donthue order by trangthai asc";
    String selectAll = "select * from donthue order by trangthai asc";
    String insert = "insert into DonThue values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    String update = "UPDATE donthue SET idkhach = ?, manv = ?, ngaytao = ?, ngaythue = ?, ngaytradukien = ?, ngaytra = ?,trangthai=?,tienphat = ?,tongtiendambao = ?, khachdua= ? ,thoilai= ? ,thanhtien = ? WHERE iddonthue = ?;";
    String delete = "delete donthue where iddonthue=?";
    String select_by_ID = "select * from DonThue where iddonthue=?";
    String doanhthuthuengay = "SELECT ISNULL(SUM(dthue.thanhtien), 0) AS doanhthuthue FROM donthue dthue WHERE dthue.ngaytao like ?";
    String doanhthuthueTONG = "SELECT ISNULL(SUM(dthue.thanhtien+ dthue.tienphat), 0) AS doanhthuthue FROM donthue dthue WHERE dthue.ngaytao like ?";
    String doanhthuthuethang = "SELECT SUM(dt.thanhtien + dt.tienphat) AS doanhthu_thue_sach FROM donthue dt WHERE MONTH(CONVERT(date, dt.ngaytao, 105)) = ?";
    String timkiem ="select * from donthue where idkhach in (select idkhach from khachhang where sdt like ? ) order by trangthai asc";
    @Override
    public void insert(DonThue entity) {
        JDBC.update(insert, entity.getIddonthue(), entity.getIdkhach(),entity.getManv(),entity.getNgaytao(),entity.getNgaythue(),entity.getNgaytradukien(),entity.getNgaytra(),entity.getTrangthai(),entity.getTienphat(),entity.getTongtiendambao(),entity.getKhachdua(),entity.getThoilai(), entity.getThanhtien());
    }

    @Override
    public void update(DonThue entity) {
        JDBC.update(update, entity.getIdkhach(), entity.getManv(), entity.getNgaytao(), entity.getNgaythue(), entity.getNgaytradukien(), entity.getNgaytra(), entity.getTrangthai(),entity.getTienphat(), entity.getTongtiendambao(), entity.getKhachdua(), entity.getThoilai(), entity.getThanhtien(), entity.getIddonthue());
    }

    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<DonThue> selectAll() {
        return select_by_sql(selectAll);
    }
    public List<DonThue> timDthue(String sdt){
        return select_by_sql(timkiem, "%"+sdt+"%");
    }
    @Override
    public DonThue select_byID(String key) {
        List<DonThue> list = this.select_by_sql(select_by_ID, key);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    protected List<DonThue> select_by_sql(String sql, Object... args) {
        List<DonThue> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql, args);
            while (r.next()) {
                DonThue dthue = new DonThue();
                dthue.setIddonthue(r.getString("iddonthue"));
                dthue.setIdkhach(r.getString("idkhach"));
                dthue.setManv(r.getString("manv"));
                dthue.setNgaytao(r.getString("ngaytao"));
                dthue.setNgaythue(r.getString("ngaythue"));
                dthue.setNgaytradukien(r.getString("ngaytradukien"));
                dthue.setNgaytra(r.getString("ngaytra"));
                dthue.setTrangthai(r.getBoolean("trangthai"));
                dthue.setTienphat(r.getDouble("tienphat"));
                dthue.setTongtiendambao(r.getDouble("tongtiendambao"));
                dthue.setKhachdua(r.getDouble("khachdua"));
                dthue.setThoilai(r.getDouble("thoilai"));
                dthue.setThanhtien(r.getDouble("thanhtien"));
                list.add(dthue);
            }
            r.getStatement().getConnection().close();

        } catch (Exception e) {
        }
        return list;
    }

    public String getIDdonthue() {
        ResultSet rs = JDBC.query(madonthue);
        String mahoadon = "";
        try {
            while (rs.next()) {
                mahoadon = rs.getString("iddonthue");
            }
            rs.getStatement().getConnection().close();
            return mahoadon;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double doanhThuthueNgay(String ngay) {
        ResultSet rs = JDBC.query(doanhthuthuengay, "%" + ngay + "%");
        double doanhthuthue = 0.0;
        try {
            if (rs.next()) {
                doanhthuthue = rs.getDouble("doanhthuthue");
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doanhthuthue;
    }

    public double doanhThuthueNgayTong(String ngay) {
        ResultSet rs = JDBC.query(doanhthuthueTONG, "%" + ngay + "%");
        double doanhthuthue = 0.0;
        try {
            if (rs.next()) {
                doanhthuthue = rs.getDouble("doanhthuthue");
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doanhthuthue;
    }

    public double doanhThuthuethangTong(int thang) {
        ResultSet rs = JDBC.query(doanhthuthuethang, thang);
        double doanhthuthue = 0.0;
        try {
            if (rs.next()) {
                doanhthuthue = rs.getDouble("doanhthu_thue_sach");
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doanhthuthue;
    }
}
