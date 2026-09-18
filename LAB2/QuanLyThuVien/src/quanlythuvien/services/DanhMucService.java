package quanlythuvien.services;

import quanlythuvien.data.DbConnection;
import quanlythuvien.models.Models.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DanhMucService {

    // --- 1. NHÂN VIÊN ---
    public DefaultTableModel layDanhSachNhanVien() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã NV", "Họ", "Tên", "Phái", "Ngày sinh", "Chức vụ", "Điện thoại"}, 0
        );
        String sql = "SELECT MaNhanVien, Ho, Ten, Phai, NgaySinh, ChucVu, SoDienThoai FROM dbo.NhanVien";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaNhanVien"), rs.getString("Ho"), rs.getString("Ten"),
                    rs.getString("Phai"), rs.getDate("NgaySinh"), rs.getString("ChucVu"),
                    rs.getString("SoDienThoai")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public boolean themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO dbo.NhanVien (MaNhanVien, Ho, Ten, Phai, NgaySinh, ChucVu, SoDienThoai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.maNhanVien);
            ps.setString(2, nv.ho);
            ps.setString(3, nv.ten);
            ps.setString(4, nv.phai);
            ps.setDate(5, new java.sql.Date(nv.ngaySinh.getTime()));
            ps.setString(6, nv.chucVu);
            ps.setString(7, nv.soDienThoai);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm nhân viên: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        String sql = "UPDATE dbo.NhanVien SET Ho=?, Ten=?, Phai=?, NgaySinh=?, ChucVu=?, SoDienThoai=? WHERE MaNhanVien=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.ho);
            ps.setString(2, nv.ten);
            ps.setString(3, nv.phai);
            ps.setDate(4, new java.sql.Date(nv.ngaySinh.getTime()));
            ps.setString(5, nv.chucVu);
            ps.setString(6, nv.soDienThoai);
            ps.setString(7, nv.maNhanVien);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật nhân viên: " + e.getMessage());
            return false;
        }
    }

    public boolean xoaNhanVien(String maNV) {
        String sql = "DELETE FROM dbo.NhanVien WHERE MaNhanVien=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa nhân viên (có thể đang có dữ liệu liên quan): " + e.getMessage());
            return false;
        }
    }

    // --- 2. THỂ LOẠI ---
    public DefaultTableModel layDanhSachTheLoai() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã Thể Loại", "Tên Thể Loại"}, 0);
        String sql = "SELECT MaTheLoai, TenTheLoai FROM dbo.TheLoai";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("MaTheLoai"), rs.getString("TenTheLoai")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }

    public boolean themTheLoai(TheLoai tl) {
        String sql = "INSERT INTO dbo.TheLoai (MaTheLoai, TenTheLoai) VALUES (?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tl.maTheLoai);
            ps.setString(2, tl.tenTheLoai);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm thể loại: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatTheLoai(TheLoai tl) {
        String sql = "UPDATE dbo.TheLoai SET TenTheLoai=? WHERE MaTheLoai=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tl.tenTheLoai);
            ps.setString(2, tl.maTheLoai);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật thể loại: " + e.getMessage());
            return false;
        }
    }

    public boolean xoaTheLoai(String maTL) {
        String sql = "DELETE FROM dbo.TheLoai WHERE MaTheLoai=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTL);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể xóa thể loại đang có đầu sách!");
            return false;
        }
    }

    // --- 3. NHÀ XUẤT BẢN ---
    public DefaultTableModel layDanhSachNXB() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã NXB", "Địa chỉ", "Điện thoại"}, 0);
        String sql = "SELECT MaNhaXuatBan, DiaChi, SoDienThoai FROM dbo.NhaXuatBan";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("MaNhaXuatBan"), rs.getString("DiaChi"), rs.getString("SoDienThoai")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }

    public boolean themNXB(NhaXuatBan nxb) {
        String sql = "INSERT INTO dbo.NhaXuatBan (MaNhaXuatBan, DiaChi, SoDienThoai) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nxb.maNhaXuatBan);
            ps.setString(2, nxb.diaChi);
            ps.setString(3, nxb.soDienThoai);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm NXB: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatNXB(NhaXuatBan nxb) {
        String sql = "UPDATE dbo.NhaXuatBan SET DiaChi=?, SoDienThoai=? WHERE MaNhaXuatBan=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nxb.diaChi);
            ps.setString(2, nxb.soDienThoai);
            ps.setString(3, nxb.maNhaXuatBan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật NXB: " + e.getMessage());
            return false;
        }
    }

    public boolean xoaNXB(String maNXB) {
        String sql = "DELETE FROM dbo.NhaXuatBan WHERE MaNhaXuatBan=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNXB);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể xóa NXB đang có đầu sách!");
            return false;
        }
    }
}