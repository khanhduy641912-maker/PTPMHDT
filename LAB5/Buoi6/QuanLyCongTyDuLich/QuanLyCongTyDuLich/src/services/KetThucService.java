package services;

import data.Db;
import java.sql.*;

public class KetThucService {
    public Model.KetQuaXuLy thanhToanDoan(String soTT, String soDK, double soTien, String ghiChu) {
        String sql = "INSERT INTO ThanhToanDoan(SoTT, SoDKDoan, NgayThanhToan, SoTien, GhiChu) VALUES(?, ?, SYSDATETIME(), ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, soTT);
            stmt.setString(2, soDK);
            stmt.setDouble(3, soTien);
            stmt.setString(4, ghiChu);
            stmt.executeUpdate();
            return new Model.KetQuaXuLy(true, "Ghi nhận thanh toán thành công!");
        } catch (SQLException e) {
            return new Model.KetQuaXuLy(false, "Lỗi: " + e.getMessage());
        }
    }
}