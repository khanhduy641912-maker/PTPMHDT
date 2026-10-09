package services;

import data.Db;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChuyenLeService {

    public List<Model.ChuyenLeItem> layDanhSachChuyen() {
        List<Model.ChuyenLeItem> list = new ArrayList<>();
        String sql = "SELECT c.MaChuyen, t.TenTour, c.NgayDi, c.NgayVe, c.DiaDiemDon, c.TrangThai " +
                     "FROM ChuyenLe c JOIN Tour t ON c.MaTour = t.MaTour ORDER BY c.NgayDi DESC";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Model.ChuyenLeItem(
                    rs.getString("MaChuyen"), rs.getString("TenTour"),
                    rs.getString("NgayDi"), rs.getString("NgayVe"),
                    rs.getString("DiaDiemDon"), rs.getString("TrangThai")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Model.ComboItem> layChuyenMoCombo() {
        List<Model.ComboItem> list = new ArrayList<>();
        String sql = "SELECT c.MaChuyen, t.TenTour, c.NgayDi FROM ChuyenLe c JOIN Tour t ON c.MaTour = t.MaTour WHERE c.TrangThai = N'Mở đăng ký'";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Model.ComboItem(
                    rs.getString("MaChuyen"),
                    rs.getString("MaChuyen") + " - " + rs.getString("TenTour") + " (" + rs.getString("NgayDi") + ")"
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Model.KetQuaXuLy themChuyen(String maChuyen, String maTour, String ngayDiStr, String diaDiemDon) {
        if (maChuyen.trim().isEmpty() || maTour.trim().isEmpty() || diaDiemDon.trim().isEmpty()) {
            return new Model.KetQuaXuLy(false, "Vui lòng điền đầy đủ thông tin!");
        }
        try (Connection conn = Db.getConnection()) {
            // Lấy số ngày của tour để tính ngày về
            int soNgay = 0;
            String sqlTour = "SELECT SoNgay FROM Tour WHERE MaTour = ?";
            try (PreparedStatement stmtTour = conn.prepareStatement(sqlTour)) {
                stmtTour.setString(1, maTour);
                ResultSet rs = stmtTour.executeQuery();
                if (rs.next()) soNgay = rs.getInt("SoNgay");
                else return new Model.KetQuaXuLy(false, "Tour không tồn tại!");
            }

            LocalDate ngayDi = LocalDate.parse(ngayDiStr);
            LocalDate ngayVe = ngayDi.plusDays(soNgay - 1);

            String sql = "INSERT INTO ChuyenLe(MaChuyen, MaTour, NgayDi, NgayVe, DiaDiemDon, TrangThai) VALUES(?, ?, ?, ?, ?, N'Mở đăng ký')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, maChuyen.trim());
                stmt.setString(2, maTour);
                stmt.setDate(3, Date.valueOf(ngayDi));
                stmt.setDate(4, Date.valueOf(ngayVe));
                stmt.setString(5, diaDiemDon.trim());
                stmt.executeUpdate();
                return new Model.KetQuaXuLy(true, "Tạo chuyến thành công! Ngày về dự kiến: " + ngayVe);
            }
        } catch (Exception e) {
            return new Model.KetQuaXuLy(false, "Lỗi: " + e.getMessage());
        }
    }

    public Model.KetQuaXuLy dongDangKy(String maChuyen) {
        String sql = "UPDATE ChuyenLe SET TrangThai = N'Đóng đăng ký' WHERE MaChuyen = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maChuyen);
            int row = stmt.executeUpdate();
            return row > 0 ? new Model.KetQuaXuLy(true, "Đã đóng đăng ký chuyến!") : new Model.KetQuaXuLy(false, "Không tìm thấy chuyến!");
        } catch (SQLException e) {
            return new Model.KetQuaXuLy(false, "Lỗi: " + e.getMessage());
        }
    }
}