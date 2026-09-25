package quanlykhachsan.services;

import quanlykhachsan.data.DbConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DanhMucService {

    public DefaultTableModel layDanhSachPhong() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Số phòng", "Khu vực", "Sức chứa", "Đơn giá", "Trạng thái"}, 0
        );
        String sql = "SELECT p.SoPhong, k.TenKhuVuc, p.SoNguoiToiDa, p.DonGiaNgay, p.TrangThai " +
                     "FROM dbo.Phong p JOIN dbo.KhuVuc k ON p.MaKhuVuc = k.MaKhuVuc";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("SoPhong"),
                    rs.getString("TenKhuVuc"),
                    rs.getInt("SoNguoiToiDa"),
                    String.format("%,.0f VNĐ", rs.getDouble("DonGiaNgay")),
                    rs.getString("TrangThai")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    // BỔ SUNG HÀM TÌM KIẾM DỊCH VỤ / DANH MỤC VÀO DanhMucService.java
public DefaultTableModel timKiemDichVu(String keyword) {
    DefaultTableModel model = new DefaultTableModel(
        new String[]{"Mã dịch vụ", "Tên dịch vụ", "Đơn vị tính", "Đơn giá"}, 0
    );
    String sql = "SELECT MaDV, TenDV, DonViTinh, DonGia FROM dbo.DichVu " +
                 "WHERE MaDV LIKE ? OR TenDV LIKE ?";

    try (Connection conn = DbConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        String searchPattern = "%" + keyword + "%";
        ps.setString(1, searchPattern);
        ps.setString(2, searchPattern);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaDV"),
                    rs.getString("TenDV"),
                    rs.getString("DonViTinh"),
                    String.format("%,.0f VNĐ", rs.getDouble("DonGia"))
                });
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return model;
}
}