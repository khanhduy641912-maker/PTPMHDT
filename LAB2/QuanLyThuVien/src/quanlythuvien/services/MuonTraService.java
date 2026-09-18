package quanlythuvien.services;

import quanlythuvien.data.DbConnection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

public class MuonTraService {

    // 1. Lấy danh sách sách đang mượn kèm tính toán Số ngày trễ & Phí phạt dự kiến
    public DefaultTableModel laySachDangMuon(String maDocGia) {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã CTPM", "Mã phiếu", "Mã sách", "Tên sách", "Ngày mượn", "Hẹn trả", "Trễ (ngày)", "Phạt dự kiến"}, 0
        );
        String sql = "SELECT ct.MaChiTiet, pm.MaPhieuMuon, ct.MaDauSach, s.TenSach, pm.NgayMuon, pm.NgayHenTra, " +
                     "DATEDIFF(day, pm.NgayHenTra, GETDATE()) AS SoNgayTre " +
                     "FROM dbo.PhieuMuon pm " +
                     "JOIN dbo.ChiTietPhieuMuon ct ON ct.MaPhieuMuon = pm.MaPhieuMuon " +
                     "JOIN dbo.DauSach s ON s.MaDauSach = ct.MaDauSach " +
                     "WHERE pm.MaDocGia = ? AND ct.NgayTraThucTe IS NULL " +
                     "ORDER BY pm.NgayHenTra";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDocGia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int ngayTre = rs.getInt("SoNgayTre");
                    if (ngayTre < 0) ngayTre = 0;
                    double phiPhat = ngayTre * 5000.0;

                    model.addRow(new Object[]{
                        rs.getString("MaChiTiet"),
                        rs.getString("MaPhieuMuon"),
                        rs.getString("MaDauSach"),
                        rs.getString("TenSach"),
                        rs.getDate("NgayMuon"),
                        rs.getDate("NgayHenTra"),
                        ngayTre > 0 ? ngayTre + " ngày" : "Đúng hạn",
                        String.format("%,.0f VNĐ", phiPhat)
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    // 2. Mượn NHIỀU sách cùng lúc (Danh sách các mã đầu sách)
    public boolean lapPhieuMuonNhieuSach(String maDocGia, String maNhanVien, List<String> dsMaSach) {
        if (dsMaSach == null || dsMaSach.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Chưa chọn sách nào để mượn!");
            return false;
        }

        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // BR01: Kiểm tra thẻ độc giả
            String sqlCheckThe = "SELECT COUNT(*) FROM dbo.TheDocGia WHERE MaDocGia = ? AND TrangThai = 1 AND HanSuDung >= GETDATE()";
            try (PreparedStatement ps = conn.prepareStatement(sqlCheckThe)) {
                ps.setString(1, maDocGia);
                ResultSet rs = ps.executeQuery();
                if (!rs.next() || rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(null, "Thẻ độc giả không hợp lệ hoặc đã hết hạn!");
                    return false;
                }
            }

            // BR02: Kiểm tra tổng số sách đang mượn + số sách chuẩn bị mượn (Tối đa 3 cuốn)
            String sqlCheckCount = "SELECT COUNT(*) FROM dbo.ChiTietPhieuMuon ct JOIN dbo.PhieuMuon pm ON ct.MaPhieuMuon = pm.MaPhieuMuon WHERE pm.MaDocGia = ? AND ct.NgayTraThucTe IS NULL";
            int soSachDangMuon = 0;
            try (PreparedStatement ps = conn.prepareStatement(sqlCheckCount)) {
                ps.setString(1, maDocGia);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    soSachDangMuon = rs.getInt(1);
                }
            }

            if (soSachDangMuon + dsMaSach.size() > 3) {
                JOptionPane.showMessageDialog(null, "Độc giả đang mượn " + soSachDangMuon + " cuốn. Không thể mượn thêm " + dsMaSach.size() + " cuốn nữa (Tối đa 3 cuốn/độc giả)!");
                return false;
            }

            // Kiểm tra tồn kho cho tất cả sách chọn
            String sqlCheckTon = "SELECT SoLuongHienCo, TenSach FROM dbo.DauSach WHERE MaDauSach = ?";
            for (String maSach : dsMaSach) {
                try (PreparedStatement ps = conn.prepareStatement(sqlCheckTon)) {
                    ps.setString(1, maSach);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next() || rs.getInt("SoLuongHienCo") <= 0) {
                        String tenSach = rs.isBeforeFirst() ? rs.getString("TenSach") : maSach;
                        JOptionPane.showMessageDialog(null, "Cuốn sách [" + tenSach + "] đã hết hàng trong kho!");
                        return false;
                    }
                }
            }

            // Tạo PhieuMuon chung cho đợt mượn này
            String maPhieu = "PM_" + (System.currentTimeMillis() % 1000000);
            String sqlPM = "INSERT INTO dbo.PhieuMuon (MaPhieuMuon, MaDocGia, MaNhanVien, NgayMuon, NgayHenTra) VALUES (?, ?, ?, GETDATE(), DATEADD(day, 14, GETDATE()))";
            try (PreparedStatement ps = conn.prepareStatement(sqlPM)) {
                ps.setString(1, maPhieu);
                ps.setString(2, maDocGia);
                ps.setString(3, maNhanVien);
                ps.executeUpdate();
            }

            // Tạo các ChiTietPhieuMuon & Trừ tồn kho
            String sqlCT = "INSERT INTO dbo.ChiTietPhieuMuon (MaChiTiet, MaPhieuMuon, MaDauSach) VALUES (?, ?, ?)";
            String sqlUpdateKho = "UPDATE dbo.DauSach SET SoLuongHienCo = SoLuongHienCo - 1 WHERE MaDauSach = ?";

            int index = 0;
            for (String maSach : dsMaSach) {
                String maCT = "CT_" + (System.currentTimeMillis() % 1000000) + "_" + (index++);
                try (PreparedStatement ps = conn.prepareStatement(sqlCT)) {
                    ps.setString(1, maCT);
                    ps.setString(2, maPhieu);
                    ps.setString(3, maSach);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(sqlUpdateKho)) {
                    ps.setString(1, maSach);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi mượn sách: " + e.getMessage());
            return false;
        }
    }

    // 3. Trả 1 sách
    public boolean traSach(String maChiTiet, String maDauSach, int soNgayTre) {
        try (Connection conn = DbConnection.getConnection()) {
            conn.setAutoCommit(false);

            String sqlTra = "UPDATE dbo.ChiTietPhieuMuon SET NgayTraThucTe = GETDATE(), TinhTrangTra = ? WHERE MaChiTiet = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlTra)) {
                ps.setString(1, soNgayTre > 0 ? "Trả trễ " + soNgayTre + " ngày" : "Bình thường");
                ps.setString(2, maChiTiet);
                ps.executeUpdate();
            }

            if (soNgayTre > 0) {
                double phiPhat = soNgayTre * 5000.0;
                String maPhat = "PP_" + (System.currentTimeMillis() % 1000000);
                String sqlPhat = "INSERT INTO dbo.PhieuPhat (MaPhieuPhat, MaChiTiet, MaNhanVien, NgayPhat, LyDo, PhiPhat) VALUES (?, ?, 'NV001', GETDATE(), ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlPhat)) {
                    ps.setString(1, maPhat);
                    ps.setString(2, maChiTiet);
                    ps.setString(3, "Quá hạn " + soNgayTre + " ngày");
                    ps.setDouble(4, phiPhat);
                    ps.executeUpdate();
                }
            }

            String sqlUpdateKho = "UPDATE dbo.DauSach SET SoLuongHienCo = SoLuongHienCo + 1 WHERE MaDauSach = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateKho)) {
                ps.setString(1, maDauSach);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi trả sách: " + e.getMessage());
            return false;
        }
    }
}