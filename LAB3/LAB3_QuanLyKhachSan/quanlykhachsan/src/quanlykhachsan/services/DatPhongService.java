package quanlykhachsan.services;

import quanlykhachsan.data.DbConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DatPhongService {

    // 1. Lấy danh sách phòng trống để hiển thị lên ComboBox/Table
    public DefaultTableModel layDanhSachPhongTrong() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Số phòng", "Khu vực", "Sức chứa", "Đơn giá/ngày", "Trạng thái"}, 0
        );
        String sql = "SELECT p.SoPhong, k.TenKhuVuc, p.SoNguoiToiDa, p.DonGiaNgay, p.TrangThai " +
                     "FROM dbo.Phong p JOIN dbo.KhuVuc k ON p.MaKhuVuc = k.MaKhuVuc " +
                     "WHERE p.TrangThai = N'Trống'";

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

    // 2. Tạo Đặt Phòng với kiểm tra sức chứa & kiểm tra trùng lịch đặt
    public String taoDatPhong(String soPhieu, String maKhach, String maNV, String soPhong, int soNguoi, double tienCoc) {
        // Kiểm tra 1: Sức chứa phòng
        String sqlCheckSucChua = "SELECT SoNguoiToiDa, TrangThai FROM dbo.Phong WHERE SoPhong = ?";
        // Kiểm tra 2: Lịch đặt trùng trong khoảng thời gian
        String sqlCheckTrungLich = "SELECT COUNT(*) FROM dbo.ChiTietDatPhong c " +
                                   "JOIN dbo.PhieuDatPhong p ON c.SoPhieuDat = p.SoPhieuDat " +
                                   "WHERE c.SoPhong = ? AND p.TrangThai IN (N'Đã đặt', N'Đang ở')";

        String sqlPhieu = "INSERT INTO dbo.PhieuDatPhong (SoPhieuDat, MaKhach, MaNVLeTan, NgayLap, NgayNhan, NgayTraDuKien, TienCoc, KenhDat, TrangThai) " +
                          "VALUES (?, ?, ?, GETDATE(), CAST(GETDATE() AS DATE), DATEADD(day, 2, CAST(GETDATE() AS DATE)), ?, N'Trực tiếp', N'Đã đặt')";
        String sqlChiTiet = "INSERT INTO dbo.ChiTietDatPhong (SoPhieuDat, SoPhong, SoNguoi) VALUES (?, ?, ?)";
        String sqlUpdatePhong = "UPDATE dbo.Phong SET TrangThai = N'Đã đặt' WHERE SoPhong = ?";

        try (Connection conn = DbConnection.getConnection()) {
            // Step 1: Kiểm tra sức chứa
            try (PreparedStatement ps1 = conn.prepareStatement(sqlCheckSucChua)) {
                ps1.setString(1, soPhong);
                ResultSet rs1 = ps1.executeQuery();
                if (rs1.next()) {
                    int sucChuaMax = rs1.getInt("SoNguoiToiDa");
                    if (soNguoi > sucChuaMax) {
                        return "Số người ở (" + soNguoi + ") vượt quá sức chứa tối đa (" + sucChuaMax + ") của phòng " + soPhong + "!";
                    }
                } else {
                    return "Phòng " + soPhong + " không tồn tại trong hệ thống!";
                }
            }

            // Step 2: Kiểm tra trùng lịch
            try (PreparedStatement ps2 = conn.prepareStatement(sqlCheckTrungLich)) {
                ps2.setString(1, soPhong);
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next() && rs2.getInt(1) > 0) {
                    return "Phòng " + soPhong + " đã có người đặt/đang ở trong khoảng thời gian này!";
                }
            }

            // Step 3: Thực thi Transaction đặt phòng
            conn.setAutoCommit(false);
            try (PreparedStatement psPhieu = conn.prepareStatement(sqlPhieu);
                 PreparedStatement psCT = conn.prepareStatement(sqlChiTiet);
                 PreparedStatement psUp = conn.prepareStatement(sqlUpdatePhong)) {

                // Lưu phiếu đặt
                psPhieu.setString(1, soPhieu);
                psPhieu.setString(2, maKhach);
                psPhieu.setString(3, maNV);
                psPhieu.setDouble(4, tienCoc);
                psPhieu.executeUpdate();

                // Lưu chi tiết đặt
                psCT.setString(1, soPhieu);
                psCT.setString(2, soPhong);
                psCT.setInt(3, soNguoi);
                psCT.executeUpdate();

                // Cập nhật trạng thái phòng sang 'Đã đặt'
                psUp.setString(1, soPhong);
                psUp.executeUpdate();

                conn.commit();
                return "OK";
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        } catch (Exception e) {
            return "Lỗi hệ thống: " + e.getMessage();
        }
    }
        // Lấy danh sách tất cả Phiếu đặt phòng từ SQL Server
    public DefaultTableModel layDanhSachPhieuDat() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Số phiếu", "Khách", "Ngày nhận", "Ngày trả dự kiến", "Cọc", "Kênh", "Trạng thái"}, 0
        );
        String sql = "SELECT p.SoPhieuDat, k.HoTen, p.NgayNhan, p.NgayTraDuKien, p.TienCoc, p.KenhDat, p.TrangThai " +
                    "FROM dbo.PhieuDatPhong p JOIN dbo.KhachHang k ON p.MaKhach = k.MaKhach";

        try (Connection conn = DbConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("SoPhieuDat"),
                    rs.getString("HoTen"),
                    rs.getDate("NgayNhan"),
                    rs.getDate("NgayTraDuKien"),
                    String.format("%,.0f", rs.getDouble("TienCoc")),
                    rs.getString("KenhDat"),
                    rs.getString("TrangThai")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}