package com.quanlynhansu.dao;

import com.quanlynhansu.data.Database;
import com.quanlynhansu.model.HopDong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HopDongDAO {

    public List<HopDong> layDanhSachHopDong() {
        List<HopDong> ds = new ArrayList<>();
        String sql = "SELECT SoHD, LoaiHD, NgayKy, NgayHetHan, MaNV FROM HopDong";
        try (Connection conn = Database.openConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                HopDong hd = new HopDong(
                        rs.getString("SoHD"),
                        rs.getString("LoaiHD"),
                        rs.getDate("NgayKy"),
                        rs.getDate("NgayHetHan"),
                        rs.getString("MaNV")
                );
                ds.add(hd);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn hợp đồng: " + e.getMessage());
        }
        return ds;
    }

    public boolean kyHopDong(HopDong hd, String maHS, double bhyt, double bhxh) {
        String sqlHD = "INSERT INTO HopDong (SoHD, LoaiHD, NgayKy, NgayHetHan, MaNV) VALUES (?, ?, ?, ?, ?)";
        String sqlBH = "INSERT INTO HoSoBaoHiem (MaHS, SoHD, MucDongBHYT, MucDongBHXH) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.openConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstHD = conn.prepareStatement(sqlHD)) {
                pstHD.setString(1, hd.getSoHD());
                pstHD.setString(2, hd.getLoaiHD());
                pstHD.setDate(3, new java.sql.Date(hd.getNgayKy().getTime()));
                if (hd.getNgayHetHan() != null) {
                    pstHD.setDate(4, new java.sql.Date(hd.getNgayHetHan().getTime()));
                } else {
                    pstHD.setNull(4, java.sql.Types.DATE);
                }
                pstHD.setString(5, hd.getMaNV());
                pstHD.executeUpdate();
            }

            if ("Dài hạn".equalsIgnoreCase(hd.getLoaiHD()) && maHS != null && !maHS.isEmpty()) {
                try (PreparedStatement pstBH = conn.prepareStatement(sqlBH)) {
                    pstBH.setString(1, maHS);
                    pstBH.setString(2, hd.getSoHD());
                    pstBH.setDouble(3, bhyt);
                    pstBH.setDouble(4, bhxh);
                    pstBH.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi lưu hợp đồng/bảo hiểm: " + e.getMessage());
            return false;
        }
    }
}