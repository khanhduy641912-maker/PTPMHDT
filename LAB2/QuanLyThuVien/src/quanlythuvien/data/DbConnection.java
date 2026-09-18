package quanlythuvien.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    // Địa chỉ kết nối lấy đúng theo tên Server Laptop của bạn
    private static final String URL = "jdbc:sqlserver://LAPTOP-6VL0VPIP:1433;databaseName=QuanLyThuVienDB;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa"; 
    
    private static final String PASSWORD = "123"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy Driver JDBC SQL Server!", e);
        }
    }
}