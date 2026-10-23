package com.quanlynhansu.dao;

import com.quanlynhansu.data.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LuongDAO {

    // Tự động truy vấn CSDL để tính lương cho tất cả nhân viên trong tháng
    public List<Object[]> tinhLuongTuDong(int thang, int nam) {
        List<Object[]> dsLuong = new ArrayList<>();
        
        // Cú pháp SQL tổng hợp dữ liệu từ 6 bảng: NhanVien, QuyetDinhLuong, ChiTietChamCong, ChiTietPhuTroi, HoSoBaoHiem
        String sql = "SELECT nv.MaNV, nv.HoTen, " +
                     "ISNULL(qdl.MucLuongCoBan, 0) AS LuongCB, " +
                     "ISNULL(qdl.PhuCapLuong, 0) + ISNULL(qdl.PhuCapDocHai, 0) AS PhuCap, " +
                     "ISNULL(ctcc.SoNgayLamViec, 0) AS SoNgay, " +
                     "ISNULL(ctpt.GioQuyDoi, 0) AS GioPhuTroi, " +
                     "ISNULL(bh.MucDongBHYT + bh.MucDongBHXH, 0) AS TienBaoHiem " +
                     "FROM NhanVien nv " +
                     "LEFT JOIN QuyetDinhLuong qdl ON nv.MaNV = qdl.MaNV " +
                     "LEFT JOIN (SELECT ct.MaNV, ct.SoNgayLamViec FROM ChiTietChamCong ct JOIN BangChamCong b ON ct.MaBCC = b.MaBCC WHERE b.Thang = ? AND b.Nam = ?) ctcc ON nv.MaNV = ctcc.MaNV " +
                     "LEFT JOIN (SELECT ct.MaNV, SUM(ct.GioQuyDoi) AS GioQuyDoi FROM ChiTietPhuTroi ct JOIN PhieuPhuTroi p ON ct.SoPhieuPT = p.SoPhieuPT WHERE MONTH(p.NgayPhuTroi) = ? AND YEAR(p.NgayPhuTroi) = ? GROUP BY ct.MaNV) ctpt ON nv.MaNV = ctpt.MaNV " +
                     "LEFT JOIN HopDong hd ON nv.MaNV = hd.MaNV " +
                     "LEFT JOIN HoSoBaoHiem bh ON hd.SoHD = bh.SoHD";

        try (Connection conn = Database.openConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, thang);
            pst.setInt(2, nam);
            pst.setInt(3, thang);
            pst.setInt(4, nam);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String maNV = rs.getString("MaNV");
                String hoTen = rs.getString("HoTen");
                double luongCB = rs.getDouble("LuongCB");
                double phuCap = rs.getDouble("PhuCap");
                double soNgay = rs.getDouble("SoNgay");
                double gioPhuTroi = rs.getDouble("GioPhuTroi");
                double tienBH = rs.getDouble("TienBaoHiem");

                // Công thức tính toán: 
                // Lương công = (Lương CB / 22) * Số ngày làm
                // Lương phụ trội = (Lương CB / 22 / 8) * Giờ quy đổi (đã x1.5)
                double luongCong = (soNgay > 0) ? (luongCB / 22.0) * soNgay : 0;
                double luongPhuTroi = (luongCB / 22.0 / 8.0) * gioPhuTroi;
                
                double tongLuong = luongCong + phuCap + luongPhuTroi;
                double thucLanh = tongLuong - tienBH;

                dsLuong.add(new Object[]{
                        maNV, hoTen, Math.round(luongCB), Math.round(phuCap),
                        soNgay, gioPhuTroi, Math.round(tongLuong), Math.round(thucLanh)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLuong;
    }

    // Chốt và lưu bảng lương vào CSDL
    public boolean luuBangLuongToanBo(String maBL, int thang, int nam, List<Object[]> dsLuong) {
        String sql = "INSERT INTO BangLuong (MaBL, Thang, Nam, TongLuong, ThucLanh, MaNV) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.openConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                int stt = 1;
                for (Object[] row : dsLuong) {
                    pst.setString(1, maBL + "_" + stt++);
                    pst.setInt(2, thang);
                    pst.setInt(3, nam);
                    pst.setDouble(4, Double.parseDouble(row[6].toString())); // TongLuong
                    pst.setDouble(5, Double.parseDouble(row[7].toString())); // ThucLanh
                    pst.setString(6, row[0].toString()); // MaNV
                    pst.addBatch();
                }
                pst.executeBatch();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}