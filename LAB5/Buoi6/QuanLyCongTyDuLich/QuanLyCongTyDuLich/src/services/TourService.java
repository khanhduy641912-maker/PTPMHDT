package services;

import data.Db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourService {

    public List<Model.TourItem> layDanhSachTour() {
        List<Model.TourItem> list = new ArrayList<>();
        String sql = "SELECT MaTour, TenTour, SoNgay, SoDem, DonGiaKhach FROM Tour ORDER BY MaTour";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Model.TourItem(
                    rs.getString("MaTour"),
                    rs.getString("TenTour"),
                    rs.getInt("SoNgay"),
                    rs.getInt("SoDem"),
                    rs.getDouble("DonGiaKhach")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Model.ComboItem> layTourCombo() {
        List<Model.ComboItem> list = new ArrayList<>();
        String sql = "SELECT MaTour, TenTour FROM Tour WHERE DangMoBan = 1 ORDER BY MaTour";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Model.ComboItem(
                    rs.getString("MaTour"),
                    rs.getString("MaTour") + " - " + rs.getString("TenTour")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Model.KetQuaXuLy themTour(String ma, String ten, int soNgay, int soDem, double donGia) {
        if (ma.trim().isEmpty() || ten.trim().isEmpty()) {
            return new Model.KetQuaXuLy(false, "Mã tour và tên tour không được để trống!");
        }
        if (soNgay <= 0 || soDem < 0 || donGia < 0) {
            return new Model.KetQuaXuLy(false, "Số ngày, số đêm hoặc đơn giá không hợp lệ!");
        }
        String sql = "INSERT INTO Tour(MaTour, TenTour, SoNgay, SoDem, DonGiaKhach, DangMoBan) VALUES(?, ?, ?, ?, ?, 1)";
        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ma.trim());
            stmt.setString(2, ten.trim());
            stmt.setInt(3, soNgay);
            stmt.setInt(4, soDem);
            stmt.setDouble(5, donGia);
            stmt.executeUpdate();
            return new Model.KetQuaXuLy(true, "Thêm Tour thành công!");
        } catch (SQLException e) {
            return new Model.KetQuaXuLy(false, "Lỗi CSDL: " + e.getMessage());
        }
    }
}