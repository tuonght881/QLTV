/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.QLTV.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author tuong
 */
public class JDBC {

    static String Driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String url = "jdbc:sqlserver://localhost:1433;databaseName=QL_ThuVien;encrypt=true;trustServerCertificate=true";
    static String user = "sa";
    static String pass = "123456";

    // nạp driver
    static {
        try {
            Class.forName(Driver);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // viết PrepareStatement chạy câu lệnh sql 
    // String sql là chứa câu lệnh sql
    // Object... args là mình có thể truyền vô nhiều biến với nhiều kiểu dữ khác nhau
    // args là danh sách các giá trị được cung cấp cho các tham số trong câu lệnh SQL
    // throws SQLException thông báo lỗi sai cú pháp sql 
    // gọi các thủ tục lưu stored procedures và functions
    // Sử dụng để thực hiện các câu truy vấn SQL động hoặc có tham số
    // chạy hết các giá trị được cung cấp cho các tham số trong câu lệnh SQL
    public static PreparedStatement getStmt(String sql, Object... args) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pass);
        PreparedStatement stmt;
        if (sql.trim().startsWith("{")) {
            stmt = conn.prepareCall(sql);
        } else {
            stmt = conn.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            stmt.setObject(i + 1, args[i]);
        }

        return stmt;
    }

    // dùng để thực hiện select hoặc các thủ tục lưu truy vấn dữ liệu
    public static ResultSet query(String sql, Object... args) {
        try {
            PreparedStatement stmt = JDBC.getStmt(sql, args);
            return stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // dùng để select đối tượng liên quan
    // ví dụ select * from nhanvien where maNV = "maNV01"
    public static Object values(String sql, Object... args) {
        try {
            ResultSet rs = JDBC.query(sql, args);
            if (rs.next()) {
                return rs.getObject(0);
            }
            rs.getStatement().getConnection().close();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // dùng để sử dụng các thao tác như insert, update, delete hoặc thủ lục lưu tác động dữ liệu
    public static int update(String sql, Object... args) {
        try {
            PreparedStatement stmt = JDBC.getStmt(sql, args);

            try {
                return stmt.executeUpdate();
            } finally {
                stmt.getConnection().close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
