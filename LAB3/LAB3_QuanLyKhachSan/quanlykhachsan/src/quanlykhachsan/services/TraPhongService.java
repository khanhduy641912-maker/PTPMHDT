package quanlykhachsan.services;

import quanlykhachsan.data.DbConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class TraPhongService {

    // Lấy danh sách Phòng đang ở
    public DefaultTableModel layDanhSachPhongDangO() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Phòng", "Đơn giá/ngày"}, 0);
        String sql = "SELECT p.SoPhong, p.DonGiaNgay FROM dbo.Phong p WHERE p.TrangThai = N'Đang ở'";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), String.format("%,.0f", rs.getDouble(2))});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }

    // Lấy danh sách Tiện nghi lắp đặt
    public DefaultTableModel layDanhSachTienNghi() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Tiện nghi", "Loại", "Tình trạng"}, 0);
        String sql = "SELECT t.MaTienNghi, l.TenLoaiTN, t.TinhTrangHienTai " +
                     "FROM dbo.TienNghi t JOIN dbo.LoaiTienNghi l ON t.MaLoaiTN = l.MaLoaiTN";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }

    // Lấy danh sách Phiếu đền bù
    public DefaultTableModel layDanhSachTienNghiDenBu() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Tiện nghi đền bù", "Mức độ", "Số tiền"}, 0);
        String sql = "SELECT c.MaTienNghi, c.MucDoThietHai, c.SoTien FROM dbo.ChiTietPhieuDenBu c";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), String.format("%,.0f", rs.getDouble(3))});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }

    // Lấy danh sách Hóa đơn từ SQL Server
    public DefaultTableModel layDanhSachHoaDon() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Hóa đơn", "Phiếu đặt", "Tiền phòng", "Tiền dịch vụ", "Tổng tiền", "Trạng thái"}, 0);
        String sql = "SELECT SoHoaDon, SoPhieuDat, TienPhong, TienDichVu, TongTien, TrangThai FROM dbo.HoaDon";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("SoHoaDon"),
                    rs.getString("SoPhieuDat"),
                    String.format("%,.0f", rs.getDouble("TienPhong")),
                    String.format("%,.0f", rs.getDouble("TienDichVu")),
                    String.format("%,.0f", rs.getDouble("TongTien")),
                    rs.getString("TrangThai")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }

    // Lập Hóa Đơn
    public boolean lapHoaDon(String soHD, String soDat, int soNgay, String maNV) {
        String sqlTinhPhong = "SELECT ISNULL(SUM(p.DonGiaNgay), 0) * ? FROM dbo.ChiTietDatPhong c JOIN dbo.Phong p ON c.SoPhong = p.SoPhong WHERE c.SoPhieuDat = ?";
        String sqlTinhDV = "SELECT ISNULL(SUM(c.ThanhTien), 0) FROM dbo.PhieuSuDungDV h JOIN dbo.ChiTietPhieuSuDungDV c ON h.SoPhieuSDDV = c.SoPhieuSDDV WHERE h.SoPhieuDat = ?";
        String sqlHD = "INSERT INTO dbo.HoaDon (SoHoaDon, SoPhieuDat, NgayLap, MaNV, SoNgayTinhTien, TienPhong, TienDichVu, TrangThai) VALUES (?, ?, GETDATE(), ?, ?, ?, ?, N'Chưa thanh toán')";

        try (Connection conn = DbConnection.getConnection()) {
            double tienPhong = 0, tienDV = 0;
            try (PreparedStatement psP = conn.prepareStatement(sqlTinhPhong)) {
                psP.setInt(1, soNgay); psP.setString(2, soDat);
                ResultSet rs = psP.executeQuery();
                if (rs.next()) tienPhong = rs.getDouble(1);
            }
            try (PreparedStatement psDV = conn.prepareStatement(sqlTinhDV)) {
                psDV.setString(1, soDat);
                ResultSet rs = psDV.executeQuery();
                if (rs.next()) tienDV = rs.getDouble(1);
            }
            try (PreparedStatement psHD = conn.prepareStatement(sqlHD)) {
                psHD.setString(1, soHD); psHD.setString(2, soDat); psHD.setString(3, maNV);
                psHD.setInt(4, soNgay); psHD.setDouble(5, tienPhong); psHD.setDouble(6, tienDV);
                psHD.executeUpdate();
            }
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Trả phòng
    public String hoanTatTraPhong(String soDat) {
        String sqlCheckHD = "SELECT TrangThai FROM dbo.HoaDon WHERE SoPhieuDat = ?";
        String sqlUpdatePhieu = "UPDATE dbo.PhieuDatPhong SET TrangThai = N'Đã trả', NgayTraThucTe = GETDATE() WHERE SoPhieuDat = ?";
        String sqlUpdatePhong = "UPDATE dbo.Phong SET TrangThai = N'Trống' WHERE SoPhong IN (SELECT SoPhong FROM dbo.ChiTietDatPhong WHERE SoPhieuDat = ?)";

        try (Connection conn = DbConnection.getConnection()) {
            try (PreparedStatement psC = conn.prepareStatement(sqlCheckHD)) {
                psC.setString(1, soDat);
                ResultSet rs = psC.executeQuery();
                if (!rs.next()) return "Chưa lập hóa đơn cho phiếu đặt này!";
                if (!"Đã thanh toán".equals(rs.getString(1))) return "Hóa đơn chưa thanh toán đủ! Không thể hoàn tất trả phòng.";
            }

            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdatePhieu);
                 PreparedStatement ps2 = conn.prepareStatement(sqlUpdatePhong)) {
                ps1.setString(1, soDat); ps1.executeUpdate();
                ps2.setString(1, soDat); ps2.executeUpdate();
                conn.commit();
                return "OK";
            } catch (Exception ex) { conn.rollback(); throw ex; }
        } catch (Exception e) { return "Lỗi: " + e.getMessage(); }
    }
}