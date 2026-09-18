package quanlythuvien.services;

import quanlythuvien.data.DbConnection;
import quanlythuvien.models.Models.DauSach;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class SachService {

    public DefaultTableModel layDanhSachSach(String tuKhoa) {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã sách", "Tên sách", "Năm XB", "Số lượng", "Mã TL", "Mã NXB"}, 0
        );
        String sql = "SELECT s.MaDauSach, s.TenSach, s.NamXuatBan, s.SoLuongHienCo, s.MaTheLoai, s.MaNhaXuatBan " +
                     "FROM dbo.DauSach s " +
                     "WHERE (? = '' OR s.MaDauSach LIKE ? OR s.TenSach LIKE ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String key = (tuKhoa == null) ? "" : tuKhoa.trim();
            ps.setString(1, key);
            ps.setString(2, "%" + key + "%");
            ps.setString(3, "%" + key + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("MaDauSach"),
                        rs.getString("TenSach"),
                        rs.getInt("NamXuatBan"),
                        rs.getInt("SoLuongHienCo"),
                        rs.getString("MaTheLoai"),
                        rs.getString("MaNhaXuatBan")
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi tải danh sách sách: " + e.getMessage());
        }
        return model;
    }

    public boolean themSach(DauSach s) {
        String sql = "INSERT INTO dbo.DauSach (MaDauSach, TenSach, NamXuatBan, SoLuongHienCo, MaTheLoai, MaNhaXuatBan) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.maDauSach);
            ps.setString(2, s.tenSach);
            ps.setInt(3, s.namXuatBan);
            ps.setInt(4, s.soLuongHienCo);
            ps.setString(5, s.maTheLoai);
            ps.setString(6, s.maNhaXuatBan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm đầu sách: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatSach(DauSach s) {
        String sql = "UPDATE dbo.DauSach SET TenSach=?, NamXuatBan=?, SoLuongHienCo=?, MaTheLoai=?, MaNhaXuatBan=? WHERE MaDauSach=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.tenSach);
            ps.setInt(2, s.namXuatBan);
            ps.setInt(3, s.soLuongHienCo);
            ps.setString(4, s.maTheLoai);
            ps.setString(5, s.maNhaXuatBan);
            ps.setString(6, s.maDauSach);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật sách: " + e.getMessage());
            return false;
        }
    }

    public boolean xoaSach(String maSach) {
        String sql = "DELETE FROM dbo.DauSach WHERE MaDauSach=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSach);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể xóa đầu sách đã phát sinh mượn/trả!");
            return false;
        }
    }
}