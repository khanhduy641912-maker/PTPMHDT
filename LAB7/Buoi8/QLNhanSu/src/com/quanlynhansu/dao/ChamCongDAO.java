package com.quanlynhansu.dao;

import com.quanlynhansu.data.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChamCongDAO {

    // Lấy danh sách nhân viên theo phòng ban để chấm công
    public List<Object[]> layDanhSachNhanVienTheoPhongBan(String maPB) {
        List<Object[]> ds = new ArrayList<>();
        String sql = "SELECT MaNV, HoTen FROM NhanVien WHERE MaPB = ?";
        try (Connection conn = Database.openConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, maPB);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ds.add(new Object[]{rs.getString("MaNV"), rs.getString("HoTen"), 22.0}); // Mặc định 22 ngày công
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Lưu bảng chấm công và danh sách chi tiết
    public boolean luuBangChamCongHoanChinh(String maBCC, int thang, int nam, String maPB, List<Object[]> chiTiet) {
        String sqlBCC = "IF NOT EXISTS (SELECT 1 FROM BangChamCong WHERE MaBCC = ?) " +
                        "INSERT INTO BangChamCong (MaBCC, Thang, Nam, MaPB) VALUES (?, ?, ?, ?)";
        String sqlCT = "INSERT INTO ChiTietChamCong (MaBCC, MaNV, SoNgayLamViec) VALUES (?, ?, ?)";

        try (Connection conn = Database.openConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstBCC = conn.prepareStatement(sqlBCC)) {
                pstBCC.setString(1, maBCC);
                pstBCC.setString(2, maBCC);
                pstBCC.setInt(3, thang);
                pstBCC.setInt(4, nam);
                pstBCC.setString(5, maPB);
                pstBCC.executeUpdate();
            }

            try (PreparedStatement pstCT = conn.prepareStatement(sqlCT)) {
                for (Object[] row : chiTiet) {
                    pstCT.setString(1, maBCC);
                    pstCT.setString(2, row[0].toString()); // MaNV
                    pstCT.setDouble(3, Double.parseDouble(row[2].toString())); // SoNgay
                    pstCT.addBatch();
                }
                pstCT.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}