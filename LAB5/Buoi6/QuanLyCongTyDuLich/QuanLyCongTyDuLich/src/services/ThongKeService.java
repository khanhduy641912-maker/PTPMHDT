package services;

import data.Db;
import java.sql.*;

public class ThongKeService {
    public double tinhLuongHDV(String maHDV, int thang, int nam) {
        String sql = "SELECT h.LuongCoBan + ISNULL(SUM(p.ThuLaoTour), 0) AS TongLuong " +
                     "FROM HuongDanVien h LEFT JOIN PhanCongHDV p ON h.MaHDV = p.MaHDV " +
                     "AND MONTH(p.NgayKetThuc) = ? AND YEAR(p.NgayKetThuc) = ? " +
                     "WHERE h.MaHDV = ? GROUP BY h.LuongCoBan";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            stmt.setString(3, maHDV);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("TongLuong");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}