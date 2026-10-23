package com.quanlynhansu.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String SERVER_NAME = "LAPTOP-6VL0VPIP";
    private static final String DATABASE_NAME = "QuanLyNhanSu";
    private static final String USER = "sa";
    private static final String PASSWORD = "123"; 

    private static final String CONNECTION_STRING = "jdbc:sqlserver://" + SERVER_NAME + ":1433;"
            + "databaseName=" + DATABASE_NAME + ";"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    public static Connection openConnection() {
        Connection conn = null;
        try {
            // Nạp Driver JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Mở kết nối
            conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
            System.out.println("Kết nối SQL Server thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
        }
        return conn;
    }

    // Hàm main để chạy kiểm tra nhanh
    public static void main(String[] args) {
        Connection conn = openConnection();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}