package services;

import data.Db;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DangKyDoanService {

    public List<Model.DangKyDoanItem> layDanhSach() {
        List<Model.DangKyDoanItem> list = new ArrayList<>();
        String sql = "SELECT d.SoDKDoan, k.TenCoQuanDaiDien, t.TenTour, d.NgayDi, d.NgayKetThucDuKien, d.SoNguoi, d.TienCoc, d.TongTienDuKien, d.TrangThai " +
                     "FROM DangKyDoan d JOIN DoanKhach k ON d.MaDoan = k.MaDoan JOIN Tour t ON d.MaTour = t.MaTour ORDER BY d.NgayDangKy DESC";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Model.DangKyDoanItem(
                    rs.getString("SoDKDoan"), rs.getString("TenCoQuanDaiDien"), rs.getString("TenTour"),
                    rs.getString("NgayDi"), rs.getString("NgayKetThucDuKien"), rs.getInt("SoNguoi"),
                    rs.getDouble("TienCoc"), rs.getDouble("TongTienDuKien"), rs.getString("TrangThai")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Model.KetQuaXuLy dangKy(String soDK, String maDoan, String tenCoQuan, String diaChi, String sdt, String nguoiDaiDien, String maTour, String ngayDiStr, int soNguoi, String diaDiemDon, boolean muaBaoHiem, double tienCoc) {
        if (soDK.trim().isEmpty() || maDoan.trim().isEmpty() || tenCoQuan.trim().isEmpty()) {
            return new Model.KetQuaXuLy(false, "Vui lòng điền thông tin đoàn!");
        }
        if (soNguoi <= 12) {
            return new Model.KetQuaXuLy(false, "Đoàn phải trên 12 người!");
        }
        if (tienCoc <= 0) {
            return new Model.KetQuaXuLy(false, "Đoàn phải đặt cọc tiền trước!");
        }

        try (Connection conn = Db.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu Transaction

            int soNgay = 0; double donGia = 0;
            String sqlTour = "SELECT SoNgay, DonGiaKhach FROM Tour WHERE MaTour = ?";
            try (PreparedStatement stmtT = conn.prepareStatement(sqlTour)) {
                stmtT.setString(1, maTour);
                ResultSet rs = stmtT.executeQuery();
                if (rs.next()) {
                    soNgay = rs.getInt("SoNgay");
                    donGia = rs.getDouble("DonGiaKhach");
                }
            }

            LocalDate ngayDi = LocalDate.parse(ngayDiStr);
            LocalDate ngayKT = ngayDi.plusDays(soNgay - 1);
            double tongTien = donGia * soNguoi;

            if (tienCoc > tongTien) {
                conn.rollback();
                return new Model.KetQuaXuLy(false, "Tiền cọc không vượt quá tổng tiền dự kiến!");
            }

            // 1. Lưu Đoàn khách
            String sqlDoan = "IF EXISTS(SELECT 1 FROM DoanKhach WHERE MaDoan = ?) " +
                             "UPDATE DoanKhach SET TenCoQuanDaiDien=?, DiaChi=?, DienThoai=?, NguoiDaiDien=? WHERE MaDoan=? " +
                             "ELSE INSERT INTO DoanKhach VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement stmtD = conn.prepareStatement(sqlDoan)) {
                stmtD.setString(1, maDoan);
                stmtD.setString(2, tenCoQuan); stmtD.setString(3, diaChi); stmtD.setString(4, sdt); stmtD.setString(5, nguoiDaiDien); stmtD.setString(6, maDoan);
                stmtD.setString(7, maDoan); stmtD.setString(8, tenCoQuan); stmtD.setString(9, diaChi); stmtD.setString(10, sdt); stmtD.setString(11, nguoiDaiDien);
                stmtD.executeUpdate();
            }

            // 2. Lưu Đăng ký đoàn
            String sqlDK = "INSERT INTO DangKyDoan(SoDKDoan, MaDoan, MaTour, NgayDangKy, NgayDi, NgayKetThucDuKien, SoNguoi, DiaDiemDon, MuaBaoHiem, TienCoc, DaThanhToanCoc, TongTienDuKien, TrangThai) " +
                           "VALUES(?, ?, ?, SYSDATETIME(), ?, ?, ?, ?, ?, ?, 1, ?, N'Đã đăng ký')";
            try (PreparedStatement stmtDK = conn.prepareStatement(sqlDK)) {
                stmtDK.setString(1, soDK); stmtDK.setString(2, maDoan); stmtDK.setString(3, maTour);
                stmtDK.setDate(4, Date.valueOf(ngayDi)); stmtDK.setDate(5, Date.valueOf(ngayKT));
                stmtDK.setInt(6, soNguoi); stmtDK.setString(7, diaDiemDon); stmtDK.setBoolean(8, muaBaoHiem);
                stmtDK.setDouble(9, tienCoc); stmtDK.setDouble(10, tongTien);
                stmtDK.executeUpdate();
            }

            conn.commit();
            return new Model.KetQuaXuLy(true, "Lập phiếu đoàn thành công! Tổng dự kiến: " + String.format("%,.0f VNĐ", tongTien));
        } catch (Exception e) {
            return new Model.KetQuaXuLy(false, "Lỗi Transaction: " + e.getMessage());
        }
    }

    public Model.KetQuaXuLy huyDangKy(String soDK) {
        try (Connection conn = Db.getConnection()) {
            conn.setAutoCommit(false);
            // Gỡ phân công HDV
            try (PreparedStatement p1 = conn.prepareStatement("DELETE FROM PhanCongHDV WHERE SoDKDoan = ?")) {
                p1.setString(1, soDK);
                p1.executeUpdate();
            }
            // Đổi trạng thái phiếu
            try (PreparedStatement p2 = conn.prepareStatement("UPDATE DangKyDoan SET TrangThai = N'Hủy - mất cọc' WHERE SoDKDoan = ?")) {
                p2.setString(1, soDK);
                p2.executeUpdate();
            }
            conn.commit();
            return new Model.KetQuaXuLy(true, "Đã hủy phiếu. Đoàn bị mất tiền cọc theo quy định!");
        } catch (SQLException e) {
            return new Model.KetQuaXuLy(false, "Lỗi: " + e.getMessage());
        }
    }
}