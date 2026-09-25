package quanlykhachsan.services;

import quanlykhachsan.data.DbConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ThongKeService {

    public DefaultTableModel layThongKeTongHop() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Số phiếu đặt", "Đang ở", "Hóa đơn đã thanh toán", "Tổng doanh thu"}, 0
        );
        String sql = "SELECT " +
                     "(SELECT COUNT(*) FROM dbo.PhieuDatPhong) AS SoPhieu, " +
                     "(SELECT COUNT(*) FROM dbo.Phong WHERE TrangThai = N'Đang ở') AS DangO, " +
                     "(SELECT COUNT(*) FROM dbo.HoaDon WHERE TrangThai = N me'Đã thanh toán') AS SoHD, " +
                     "(SELECT ISNULL(SUM(TongTien), 0) FROM dbo.HoaDon WHERE TrangThai = N'Đã thanh toán') AS DoanhThu";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("SoPhieu"),
                    rs.getInt("DangO"),
                    rs.getInt("SoHD"),
                    String.format("%,.0f VNĐ", rs.getDouble("DoanhThu"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}