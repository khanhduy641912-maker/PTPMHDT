package services;

import data.Db;
import java.sql.*;

public class PhanCongService {
    public Model.KetQuaXuLy phanCong(String maPC, String maHDV, String loai, String targetId, String bd, String kt, double thuLao) {
        // Kiểm tra chồng chéo lịch HDV
        String sqlCheck = "SELECT COUNT(*) FROM PhanCongHDV WHERE MaHDV = ? AND NgayBatDau <= ? AND NgayKetThuc >= ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
            stmtCheck.setString(1, maHDV);
            stmtCheck.setDate(2, Date.valueOf(kt));
            stmtCheck.setDate(3, Date.valueOf(bd));
            ResultSet rs = stmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new Model.KetQuaXuLy(false, "Hướng dẫn viên bị trùng lịch làm việc!");
            }

            String sql = "INSERT INTO PhanCongHDV(MaPC, MaHDV, LoaiDoiTuong, MaChuyen, SoDKDoan, NgayBatDau, NgayKetThuc, ThuLaoTour) " +
                         "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, maPC);
                stmt.setString(2, maHDV);
                stmt.setString(3, loai);
                stmt.setString(4, loai.equals("LE") ? targetId : null);
                stmt.setString(5, loai.equals("DOAN") ? targetId : null);
                stmt.setDate(6, Date.valueOf(bd));
                stmt.setDate(7, Date.valueOf(kt));
                stmt.setDouble(8, thuLao);
                stmt.executeUpdate();
                return new Model.KetQuaXuLy(true, "Phân công Hướng dẫn viên thành công!");
            }
        } catch (SQLException e) {
            return new Model.KetQuaXuLy(false, "Lỗi: " + e.getMessage());
        }
    }
}