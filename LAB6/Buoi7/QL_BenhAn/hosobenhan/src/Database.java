import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String SERVER_NAME = "LAPTOP-6VL0VPIP";
    private static final String DB_NAME = "QuanLyHoSoBenhAn";
    private static final String USER = "sa";
    private static final String PASS = "123";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            String url = "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DB_NAME 
                       + ";encrypt=true;trustServerCertificate=true;";
            
            conn = DriverManager.getConnection(url, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Chưa thêm thư viện mssql-jdbc-xxx.jar vào mục lib!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối CSDL SQL Server: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}