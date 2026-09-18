package quanlythuvien.services;

import quanlythuvien.data.DbConnection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ThongKeService {

    // 1. Lấy các chỉ số tổng quan (KPI) thực tế từ Database
    public int[] layThongKeTongQuan() {
        int[] result = new int[3]; // [0]: TongLuotMuon, [1]: SachQuahan, [2]: TongTienPhat
        
        String sqlMuon = "SELECT COUNT(*) FROM dbo.ChiTietPhieuMuon";
        String sqlQuaHan = "SELECT COUNT(*) FROM dbo.ChiTietPhieuMuon ct JOIN dbo.PhieuMuon pm ON ct.MaPhieuMuon = pm.MaPhieuMuon WHERE ct.NgayTraThucTe IS NULL AND pm.NgayHenTra < GETDATE()";
        String sqlPhat = "SELECT ISNULL(SUM(PhiPhat), 0) FROM dbo.PhieuPhat";

        try (Connection conn = DbConnection.getConnection()) {
            // Lượt mượn
            try (PreparedStatement ps = conn.prepareStatement(sqlMuon); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) result[0] = rs.getInt(1);
            }
            // Quá hạn
            try (PreparedStatement ps = conn.prepareStatement(sqlQuaHan); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) result[1] = rs.getInt(1);
            }
            // Tổng phí phạt
            try (PreparedStatement ps = conn.prepareStatement(sqlPhat); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) result[2] = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 2. Lấy danh sách chi tiết các phiếu phạt
    public DefaultTableModel layChiTietPhat() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã phiếu phạt", "Ngày phạt", "Mã độc giả", "Mã sách", "Tên sách", "Lý do phạt", "Phí phạt"}, 0
        );
        String sql = "SELECT pp.MaPhieuPhat, pp.NgayPhat, pm.MaDocGia, ct.MaDauSach, s.TenSach, pp.LyDo, pp.PhiPhat " +
                     "FROM dbo.PhieuPhat pp " +
                     "JOIN dbo.ChiTietPhieuMuon ct ON ct.MaChiTiet = pp.MaChiTiet " +
                     "JOIN dbo.PhieuMuon pm ON pm.MaPhieuMuon = ct.MaPhieuMuon " +
                     "JOIN dbo.DauSach s ON s.MaDauSach = ct.MaDauSach " +
                     "ORDER BY pp.NgayPhat DESC";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaPhieuPhat"),
                    rs.getDate("NgayPhat"),
                    rs.getString("MaDocGia"),
                    rs.getString("MaDauSach"),
                    rs.getString("TenSach"),
                    rs.getString("LyDo"),
                    String.format("%,.0f VNĐ", rs.getDouble("PhiPhat"))
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi tải thống kê phạt: " + e.getMessage());
        }
        return model;
    }
}