package quanlykhachsan.services;

import quanlykhachsan.data.DbConnection;
import java.sql.*;

import javax.swing.table.DefaultTableModel;

public class DichVuService {

    // Ghi nhận dịch vụ & TỰ ĐỘNG CỘNG DỒN số lượng nếu dùng cùng dịch vụ trong ngày
    public boolean ghiNhanDichVu(String soPhieuDat, String soPhong, String maDV, int soLuong, String maNV) {
        String sqlCheckPhieu = "SELECT SoPhieuSDDV FROM dbo.PhieuSuDungDV WHERE SoPhieuDat = ? AND SoPhong = ? AND NgaySuDung = CAST(GETDATE() AS DATE)";
        String sqlTaoPhieu = "INSERT INTO dbo.PhieuSuDungDV (SoPhieuSDDV, SoPhieuDat, SoPhong, NgaySuDung, MaNV) VALUES (?, ?, ?, CAST(GETDATE() AS DATE), ?)";
        String sqlCheckCT = "SELECT COUNT(*) FROM dbo.ChiTietPhieuSuDungDV WHERE SoPhieuSDDV = ? AND MaDV = ?";
        String sqlUpdateCT = "UPDATE dbo.ChiTietPhieuSuDungDV SET SoLuong = SoLuong + ? WHERE SoPhieuSDDV = ? AND MaDV = ?";
        String sqlInsertCT = "INSERT INTO dbo.ChiTietPhieuSuDungDV (SoPhieuSDDV, MaDV, SoLuong, DonGia) " +
                             "VALUES (?, ?, ?, (SELECT DonGia FROM dbo.DichVu WHERE MaDV = ?))";

        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);
            String soSDDV = null;

            // 1. Kiểm tra xem ngày hôm nay phòng này đã có đầu phiếu DV chưa
            try (PreparedStatement ps1 = conn.prepareStatement(sqlCheckPhieu)) {
                ps1.setString(1, soPhieuDat);
                ps1.setString(2, soPhong);
                ResultSet rs = ps1.executeQuery();
                if (rs.next()) {
                    soSDDV = rs.getString(1);
                }
            }

            // Nếu chưa có thì tạo đầu phiếu dịch vụ mới
            if (soSDDV == null) {
                soSDDV = "SD" + (System.currentTimeMillis() % 1000000);
                try (PreparedStatement ps2 = conn.prepareStatement(sqlTaoPhieu)) {
                    ps2.setString(1, soSDDV);
                    ps2.setString(2, soPhieuDat);
                    ps2.setString(3, soPhong);
                    ps2.setString(4, maNV);
                    ps2.executeUpdate();
                }
            }

            // 2. Kiểm tra xem Mã DV này đã có trong phiếu ngày hôm nay chưa
            boolean daCo = false;
            try (PreparedStatement ps3 = conn.prepareStatement(sqlCheckCT)) {
                ps3.setString(1, soSDDV);
                ps3.setString(2, maDV);
                ResultSet rs = ps3.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) daCo = true;
            }

            // Nếu ĐÃ CÓ $\rightarrow$ CỘNG DỒN số lượng; Nếu CHƯA CÓ $\rightarrow$ Tạo dòng chi tiết mới
            if (daCo) {
                try (PreparedStatement psUp = conn.prepareStatement(sqlUpdateCT)) {
                    psUp.setInt(1, soLuong);
                    psUp.setString(2, soSDDV);
                    psUp.setString(3, maDV);
                    psUp.executeUpdate();
                }
            } else {
                try (PreparedStatement psIns = conn.prepareStatement(sqlInsertCT)) {
                    psIns.setString(1, soSDDV);
                    psIns.setString(2, maDV);
                    psIns.setInt(3, soLuong);
                    psIns.setString(4, maDV);
                    psIns.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

        // Bổ sung hàm lấy tất cả phiếu dịch vụ từ SQL Server
    public DefaultTableModel layDanhSachSuDungDV() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Số phiếu", "Phòng", "Ngày", "Dịch vụ", "Số lượng", "Đơn giá", "Thành tiền"}, 0
        );
        String sql = "SELECT p.SoPhieuSDDV, p.SoPhong, p.NgaySuDung, d.TenDV, c.SoLuong, c.DonGia, c.ThanhTien " +
                    "FROM dbo.PhieuSuDungDV p " +
                    "JOIN dbo.ChiTietPhieuSuDungDV c ON p.SoPhieuSDDV = c.SoPhieuSDDV " +
                    "JOIN dbo.DichVu d ON c.MaDV = d.MaDV " +
                    "ORDER BY p.NgaySuDung DESC";

        try (Connection conn = DbConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("SoPhieuSDDV"),
                    rs.getString("SoPhong"),
                    rs.getDate("NgaySuDung"),
                    rs.getString("TenDV"),
                    rs.getInt("SoLuong"),
                    String.format("%,.0f", rs.getDouble("DonGia")),
                    String.format("%,.0f", rs.getDouble("ThanhTien"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}