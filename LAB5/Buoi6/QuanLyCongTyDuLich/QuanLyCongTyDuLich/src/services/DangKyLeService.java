package services;

import data.Db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DangKyLeService {

    public List<Model.DangKyLeItem> layDanhSach() {
        List<Model.DangKyLeItem> list = new ArrayList<>();
        String sql = "SELECT SoDKLe, MaChuyen, TenNguoiDangKy, DienThoai, SoNguoi, ThanhTien, TrangThai FROM DangKyLe ORDER BY NgayDangKy DESC";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Model.DangKyLeItem(
                    rs.getString("SoDKLe"), rs.getString("MaChuyen"),
                    rs.getString("TenNguoiDangKy"), rs.getString("DienThoai"),
                    rs.getInt("SoNguoi"), rs.getDouble("ThanhTien"),
                    rs.getString("TrangThai")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Model.KetQuaXuLy dangKy(String soDK, String maChuyen, String maDiemBan, String tenKhach, String sdt, int soNguoi) {
        if (soDK.trim().isEmpty() || tenKhach.trim().isEmpty() || sdt.trim().isEmpty()) {
            return new Model.KetQuaXuLy(false, "Vui lòng nhập đầy đủ thông tin!");
        }
        if (soNguoi <= 0 || soNguoi >= 12) {
            return new Model.KetQuaXuLy(false, "Khách lẻ phải dưới 12 người (1 - 11 người)!");
        }

        try (Connection conn = Db.getConnection()) {
            // Lấy đơn giá tour từ chuyến lẻ
            double donGia = 0;
            String sqlGia = "SELECT t.DonGiaKhach FROM ChuyenLe c JOIN Tour t ON c.MaTour = t.MaTour WHERE c.MaChuyen = ? AND c.TrangThai = N'Mở đăng ký'";
            try (PreparedStatement stmtGia = conn.prepareStatement(sqlGia)) {
                stmtGia.setString(1, maChuyen);
                ResultSet rs = stmtGia.executeQuery();
                if (rs.next()) donGia = rs.getDouble("DonGiaKhach");
                else return new Model.KetQuaXuLy(false, "Chuyến đã đóng đăng ký hoặc không tồn tại!");
            }

            double thanhTien = donGia * soNguoi;
            String sql = "INSERT INTO DangKyLe(SoDKLe, MaChuyen, MaDiemBan, NgayDangKy, TenNguoiDangKy, DienThoai, SoNguoi, ThanhTien, DaThanhToan, TrangThai) " +
                         "VALUES(?, ?, ?, SYSDATETIME(), ?, ?, ?, ?, 1, N'Đã đăng ký')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, soDK.trim());
                stmt.setString(2, maChuyen);
                stmt.setString(3, maDiemBan);
                stmt.setString(4, tenKhach.trim());
                stmt.setString(5, sdt.trim());
                stmt.setInt(6, soNguoi);
                stmt.setDouble(7, thanhTien);
                stmt.executeUpdate();
                return new Model.KetQuaXuLy(true, "Đăng ký thành công! Thành tiền: " + String.format("%,.0f VNĐ", thanhTien));
            }
        } catch (SQLException e) {
            return new Model.KetQuaXuLy(false, "Lỗi: " + e.getMessage());
        }
    }
}