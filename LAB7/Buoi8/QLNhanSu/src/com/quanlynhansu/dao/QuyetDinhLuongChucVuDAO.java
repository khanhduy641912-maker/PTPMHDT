package com.quanlynhansu.dao;

import com.quanlynhansu.data.Database;
import java.sql.*;

public class QuyetDinhLuongChucVuDAO {

    public boolean lapQuyetDinhLuong(String soQDL, String ngayHL, double luongCB, double phuCapL, double phuCapDH, String maNV) {
        String sql = "INSERT INTO QuyetDinhLuong (SoQDL, NgayHieuLuc, MucLuongCoBan, PhuCapLuong, PhuCapDocHai, MaNV) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.openConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, soQDL);
            pst.setDate(2, Date.valueOf(ngayHL));
            pst.setDouble(3, luongCB);
            pst.setDouble(4, phuCapL);
            pst.setDouble(5, phuCapDH);
            pst.setString(6, maNV);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean lapQuyetDinhChucVu(String soQDCV, String ngayHL, String maCV, String maNV) {
        String sql = "INSERT INTO QuyetDinhChucVu (SoQDCV, NgayHieuLuc, MaCV, MaNV) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.openConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, soQDCV);
            pst.setDate(2, Date.valueOf(ngayHL));
            pst.setString(3, maCV);
            pst.setString(4, maNV);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}