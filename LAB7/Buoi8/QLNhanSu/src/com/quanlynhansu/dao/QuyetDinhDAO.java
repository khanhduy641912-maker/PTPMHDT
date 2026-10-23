package com.quanlynhansu.dao;

import com.quanlynhansu.data.Database;
import java.sql.*;

public class QuyetDinhDAO {

    public boolean lapKhenThuong(String soQDKT, String ngayQD, String noiDung, double mucThuong, String maNV, String maPB, String soPhieuChi) {
        String sqlQD = "INSERT INTO QuyetDinhKhenThuong (SoQDKT, NgayQD, NoiDung, MucThuong, MaNV, MaPB) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlPC = "INSERT INTO PhieuChiKhenThuong (SoPhieuChi, NgayChi, SoTien, SoQDKT) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.openConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstQD = conn.prepareStatement(sqlQD)) {
                pstQD.setString(1, soQDKT);
                pstQD.setDate(2, Date.valueOf(ngayQD));
                pstQD.setString(3, noiDung);
                pstQD.setDouble(4, mucThuong);
                pstQD.setString(5, maNV.isEmpty() ? null : maNV);
                pstQD.setString(6, maPB.isEmpty() ? null : maPB);
                pstQD.executeUpdate();
            }

            try (PreparedStatement pstPC = conn.prepareStatement(sqlPC)) {
                pstPC.setString(1, soPhieuChi);
                pstPC.setDate(2, Date.valueOf(ngayQD));
                pstPC.setDouble(3, mucThuong);
                pstPC.setString(4, soQDKT);
                pstPC.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi lập quyết định khen thưởng: " + e.getMessage());
            return false;
        }
    }

    // Hàm bổ sung cho Quyết định Kỷ luật & Phiếu thu
    public boolean lapKyLuat(String soQDKL, String ngayQD, String noiDung, double mucPhat, String maNV, String maPB, String soPhieuThu) {
        String sqlQD = "INSERT INTO QuyetDinhKyLuat (SoQDKL, NgayQD, NoiDung, MucPhat, MaNV, MaPB) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlPT = "INSERT INTO PhieuThuNopPhat (SoPhieuThu, NgayThu, SoTien, SoQDKL) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.openConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstQD = conn.prepareStatement(sqlQD)) {
                pstQD.setString(1, soQDKL);
                pstQD.setDate(2, Date.valueOf(ngayQD));
                pstQD.setString(3, noiDung);
                pstQD.setDouble(4, mucPhat);
                pstQD.setString(5, maNV.isEmpty() ? null : maNV);
                pstQD.setString(6, maPB.isEmpty() ? null : maPB);
                pstQD.executeUpdate();
            }

            try (PreparedStatement pstPT = conn.prepareStatement(sqlPT)) {
                pstPT.setString(1, soPhieuThu);
                pstPT.setDate(2, Date.valueOf(ngayQD));
                pstPT.setDouble(3, mucPhat);
                pstPT.setString(4, soQDKL);
                pstPT.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi lập quyết định kỷ luật: " + e.getMessage());
            return false;
        }
    }
}