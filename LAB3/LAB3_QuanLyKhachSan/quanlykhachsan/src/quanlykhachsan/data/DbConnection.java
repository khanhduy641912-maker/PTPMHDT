package quanlykhachsan.data;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    // 1. Chuỗi kết nối bằng Tên Server thực tế từ SSMS
    private static final String URL_SERVER_NAME = "jdbc:sqlserver://LAPTOP-6VL0VPIP;databaseName=QuanLyKhachSan;encrypt=false;trustServerCertificate=true;";
    // 2. Chuỗi kết nối dự phòng bằng IP/Localhost
    private static final String URL_LOCALHOST = "jdbc:sqlserver://127.0.0.1:1433;databaseName=QuanLyKhachSan;encrypt=false;trustServerCertificate=true;";
    
    private static final String USER = "sa";
    private static final String PASSWORD = "123";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Thử kết nối theo Tên Server LAPTOP-6VL0VPIP
            try {
                conn = DriverManager.getConnection(URL_SERVER_NAME, USER, PASSWORD);
            } catch (Exception e1) {
                // Nếu thất bại, thử kết nối qua Localhost/IP
                conn = DriverManager.getConnection(URL_LOCALHOST, USER, PASSWORD);
            }

            return conn;
        } catch (Exception e) {
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
            return null;
        }
    }
}