package quanlythuvien.services;

import quanlythuvien.data.DbConnection;
import quanlythuvien.models.Models.DocGia;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DocGiaService {

    public DefaultTableModel layDanhSachDocGia() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã DG", "Họ", "Tên", "Phái", "SĐT", "Địa chỉ", "Email", "Hạn thẻ"}, 0
        );
        String sql = "SELECT dg.MaDocGia, dg.Ho, dg.Ten, dg.Phai, dg.SoDienThoai, dg.DiaChi, dg.Email, t.HanSuDung " +
                     "FROM dbo.DocGia dg " +
                     "OUTER APPLY ( " +
                     "   SELECT TOP 1 HanSuDung FROM dbo.TheDocGia x " +
                     "   WHERE x.MaDocGia = dg.MaDocGia ORDER BY x.TrangThai DESC, x.HanSuDung DESC " +
                     ") t ORDER BY dg.MaDocGia";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaDocGia"), rs.getString("Ho"), rs.getString("Ten"),
                    rs.getString("Phai"), rs.getString("SoDienThoai"), rs.getString("DiaChi"),
                    rs.getString("Email"), rs.getDate("HanSuDung")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi tải độc giả: " + e.getMessage());
        }
        return model;
    }

    public boolean themDocGia(DocGia dg) {
        String sql = "INSERT INTO dbo.DocGia (MaDocGia, Ho, Ten, NgaySinh, Phai, SoDienThoai, DiaChi, Email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dg.maDocGia);
            ps.setString(2, dg.ho);
            ps.setString(3, dg.ten);
            ps.setDate(4, new java.sql.Date(dg.ngaySinh.getTime()));
            ps.setString(5, dg.phai);
            ps.setString(6, dg.soDienThoai);
            ps.setString(7, dg.diaChi);
            ps.setString(8, dg.email);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm độc giả: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatDocGia(DocGia dg) {
        String sql = "UPDATE dbo.DocGia SET Ho=?, Ten=?, Phai=?, SoDienThoai=?, DiaChi=?, Email=? WHERE MaDocGia=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dg.ho);
            ps.setString(2, dg.ten);
            ps.setString(3, dg.phai);
            ps.setString(4, dg.soDienThoai);
            ps.setString(5, dg.diaChi);
            ps.setString(6, dg.email);
            ps.setString(7, dg.maDocGia);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật độc giả: " + e.getMessage());
            return false;
        }
    }

    public boolean xoaDocGia(String maDG) {
        // 1. Kiểm tra xem độc giả có phiếu mượn sách nào chưa
        String sqlCheckMuon = "SELECT COUNT(*) FROM dbo.PhieuMuon WHERE MaDocGia = ?";
        String sqlDeleteThe = "DELETE FROM dbo.TheDocGia WHERE MaDocGia = ?";
        String sqlDeleteDG = "DELETE FROM dbo.DocGia WHERE MaDocGia = ?";

        try (Connection conn = DbConnection.getConnection()) {
            // Kiểm tra mượn sách
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheckMuon)) {
                psCheck.setString(1, maDG);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(null, "Không thể xóa độc giả này vì đã có lịch sử mượn/trả sách!");
                        return false;
                    }
                }
            }

            // Xóa thẻ độc giả trước (nếu có)
            try (PreparedStatement psThe = conn.prepareStatement(sqlDeleteThe)) {
                psThe.setString(1, maDG);
                psThe.executeUpdate();
            }

            // Tiến hành xóa độc giả
            try (PreparedStatement psDG = conn.prepareStatement(sqlDeleteDG)) {
                psDG.setString(1, maDG);
                return psDG.executeUpdate() > 0;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa độc giả: " + e.getMessage());
            return false;
        }
    }
    // Nghiệp vụ Cấp thẻ thư viện / Gia hạn thẻ cho độc giả
    public boolean capTheMoi(String maDG) {
        // 1. Kiểm tra xem độc giả đã có thẻ đang hoạt động và còn hạn sử dụng hay chưa
        String sqlCheck = "SELECT COUNT(*) FROM dbo.TheDocGia WHERE MaDocGia = ? AND TrangThai = 1 AND HanSuDung >= GETDATE()";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
            
            psCheck.setString(1, maDG);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null, 
                        "Độc giả này hiện đã có thẻ và thẻ vẫn còn hạn sử dụng!", 
                        "Thông báo cấp thẻ", 
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }

            // 2. Nếu thẻ cũ đã hết hạn, hủy kích hoạt thẻ cũ (TrangThai = 0)
            String sqlDisableOld = "UPDATE dbo.TheDocGia SET TrangThai = 0 WHERE MaDocGia = ? AND TrangThai = 1";
            try (PreparedStatement psDisable = conn.prepareStatement(sqlDisableOld)) {
                psDisable.setString(1, maDG);
                psDisable.executeUpdate();
            }

            // 3. Tiến hành cấp thẻ mới thời hạn 1 năm với Mã thẻ duy nhất
            String maThe = "THE_" + maDG + "_" + (System.currentTimeMillis() % 10000000);
            String sqlInsert = "INSERT INTO dbo.TheDocGia (MaThe, MaDocGia, NgayCap, HanSuDung, DaDongLePhi, TrangThai) " +
                               "VALUES (?, ?, GETDATE(), DATEADD(year, 1, GETDATE()), 1, 1)";
            
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setString(1, maThe);
                psInsert.setString(2, maDG);
                return psInsert.executeUpdate() > 0;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi hệ thống khi cấp thẻ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}