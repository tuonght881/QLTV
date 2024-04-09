/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.dao;

import com.QLTV.db.EntityDao;
import com.QLTV.db.JDBC;
import com.QLTV.entity.HoaDon;
import com.QLTV.entity.tongdoanhthu;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Tuong
 */
public class HoaDonDAO extends EntityDao<HoaDon, String> {
    String select_al = "select * from Hoadon order by idhoadon asc";
    String selectAll = "select * from HoaDon order by ngaytao desc";
    String insert = "insert into HoaDon values(?,?,?,?,?,?)";
    String mahoadon = "select top 1 * from hoadon order by idhoadon desc";
    String select_by_ID = "select * from HoaDon where idhoadon=?";
    String delete = "delete HoaDon where idhoadon=?";
    String timkiem = "select * from HoaDon where idhoadon like ? or manv like ? order by ngaytao desc";
    String update = "UPDATE hoadon SET manv = ?, ngaytao = ?, khachdua = ?, thoilai = ?, thanhtien = ? WHERE idhoadon = ?";
    String doanhthungay = "SELECT ISNULL(SUM(hd.thanhtien), 0) AS doanhthu FROM hoadon hd WHERE hd.ngaytao like ?";
    String doanhthuthang = "SELECT SUM(hd.thanhtien) AS doanhthu FROM hoadon hd WHERE MONTH(CONVERT(date, hd.ngaytao, 105)) = ?";
    String tongDT = "SELECT SUM(ban_sach) AS doanh_thu_ban_sach,SUM(thue_sach) AS doanh_thu_thue_sach,SUM(ban_sach) + SUM(thue_sach) AS tong_doanh_thu FROM (SELECT SUM(thanhtien) AS ban_sach, 0 AS thue_sach FROM hoadon UNION ALL SELECT 0 AS ban_sach, SUM(thanhtien + tienphat) AS thue_sach FROM donthue) AS combined_revenue";

    @Override
    public void insert(HoaDon entity) {
        JDBC.update(insert, entity.getIdhoadon(), entity.getManv(), entity.getNgaytao(), entity.getKhachdua(), entity.getThoilai(), entity.getThanhtien());
    }

    @Override
    public void update(HoaDon entity) {
        JDBC.update(update, entity.getManv(), entity.getNgaytao(), entity.getKhachdua(), entity.getThoilai(), entity.getThanhtien(), entity.getIdhoadon());
    }

    @Override
    public void delete(String entity) {
        JDBC.update(delete, entity);
    }

    @Override
    public List<HoaDon> selectAll() {
        return select_by_sql(selectAll);
    }
    public List<HoaDon> select_al() {
        return select_by_sql(select_al);
    }
    @Override
    public HoaDon select_byID(String key) {
        List<HoaDon> list = this.select_by_sql(select_by_ID, key);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    protected List<HoaDon> select_by_sql(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet r = JDBC.query(sql, args);
            while (r.next()) {
                HoaDon hd = new HoaDon();
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

    public List<HoaDon> timHD(String k) {
        return select_by_sql(timkiem, "%" + k + "%", "%" + k + "%");
    }

    public double doanhThuNgay(String ngay) {
        ResultSet rs = JDBC.query(doanhthungay, "%" + ngay + "%");
        double doanhthu = 0.0;
        try {
            if (rs.next()) {
                doanhthu = rs.getDouble("doanhthu");
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doanhthu;
    }

    public double doanhThuthang(int thang) {
        ResultSet rs = JDBC.query(doanhthuthang, thang);
        double doanhthu = 0.0;
        try {
            if (rs.next()) {
                doanhthu = rs.getDouble("doanhthu");
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doanhthu;
    }

    public List<tongdoanhthu> dthu() {
        List<tongdoanhthu> list = new ArrayList<>();
        double doanhThuBanSach = 0;
        double doanhThuThueSach = 0;
        double tongDoanhThu = 0;
        try {
            ResultSet r = JDBC.query(tongDT);
            while (r.next()) {
                doanhThuBanSach = r.getDouble("doanh_thu_ban_sach");
                doanhThuThueSach = r.getDouble("doanh_thu_thue_sach");
                tongDoanhThu = r.getDouble("tong_doanh_thu");
                tongdoanhthu dt = new tongdoanhthu(doanhThuBanSach, doanhThuThueSach, tongDoanhThu);
                list.add(dt);
            }
            r.getStatement().getConnection().close();
        } catch (Exception e) {
        }
        return list;
    }
}
