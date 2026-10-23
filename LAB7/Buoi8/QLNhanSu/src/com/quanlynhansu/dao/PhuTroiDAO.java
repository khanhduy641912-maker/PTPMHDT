package com.quanlynhansu.dao;

import com.quanlynhansu.data.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhuTroiDAO {

    // Lấy danh sách phiếu phụ trội đã lập
    public List<Object[]> layDanhSachPhieuPhuTroi() {
        List<Object[]> ds = new ArrayList<>();
        String sql = "SELECT p.SoPhieuPT, p.NgayPhuTroi, p.HinhThuc, ct.MaNV, nv.HoTen, ct.SoGio, ct.GioQuyDoi " +
                     "FROM PhieuPhuTroi p " +
                     "JOIN ChiTietPhuTroi ct ON p.SoPhieuPT = ct.SoPhieuPT " +
                     "JOIN NhanVien nv ON ct.MaNV = nv.MaNV";

        try (Connection conn = Database.openConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ds.add(new Object[]{
                        rs.getString("SoPhieuPT"),
                        rs.getDate("NgayPhuTroi"),
                        rs.getString("HinhThuc"),
                        rs.getString("MaNV"),
                        rs.getString("HoTen"),
                        rs.getDouble("SoGio"),
                        rs.getDouble("GioQuyDoi")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Lưu phiếu phụ trội master-detail
    public boolean luuPhieuPhuTroiDayDu(String soPhieuPT, String ngayPT, String hinhThuc, List<Object[]> chiTietNV) {
        String sqlPhieu = "IF NOT EXISTS (SELECT 1 FROM PhieuPhuTroi WHERE SoPhieuPT = ?) " +
                          "INSERT INTO PhieuPhuTroi (SoPhieuPT, NgayPhuTroi, HinhThuc) VALUES (?, ?, ?)";
        String sqlCT = "INSERT INTO ChiTietPhuTroi (SoPhieuPT, MaNV, SoGio, GioQuyDoi) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.openConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstP = conn.prepareStatement(sqlPhieu)) {
                pstP.setString(1, soPhieuPT);
                pstP.setString(2, soPhieuPT);
                pstP.setDate(3, Date.valueOf(ngayPT));
                pstP.setString(4, hinhThuc);
                pstP.executeUpdate();
            }

            try (PreparedStatement pstCT = conn.prepareStatement(sqlCT)) {
                for (Object[] row : chiTietNV) {
                    double soGio = Double.parseDouble(row[1].toString());
                    double gioQuyDoi = soGio * 1.5; // Hệ số quy đổi 1.5

                    pstCT.setString(1, soPhieuPT);
                    pstCT.setString(2, row[0].toString()); // MaNV
                    pstCT.setDouble(3, soGio);
                    pstCT.setDouble(4, gioQuyDoi);
                    pstCT.addBatch();
                }
                pstCT.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}