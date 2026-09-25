package quanlykhachsan.services;

import quanlykhachsan.data.DbConnection;
import java.sql.*;

public class PhongTienNghiService {

    // Lập phiếu lắp đặt & kiểm tra quy tắc UNIQUE (MaTienNghi + NgayLap)
    public String lapPhieuLapDat(String soPhieu, String maTN, String soPhong, String maNV) {
        String sqlCheck = "SELECT COUNT(*) FROM dbo.PhieuLapDat WHERE MaTienNghi = ? AND NgayLap = CAST(GETDATE() AS DATE)";
        String sqlInsert = "INSERT INTO dbo.PhieuLapDat (SoPhieuLapDat, MaTienNghi, SoPhong, NgayLap, TinhTrang, MaNV) VALUES (?, ?, ?, CAST(GETDATE() AS DATE), N'Tốt', ?)";

        try (Connection conn = DbConnection.getConnection()) {
            // Kiểm tra quy tắc: Trong 1 ngày, 1 thiết bị chỉ được lắp cho 1 phòng duy nhất
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setString(1, maTN);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return "Thiết bị [" + maTN + "] đã được lắp cho một phòng khác trong ngày hôm nay!";
                }
            }

            // Thực hiện thêm phiếu lắp đặt
            try (PreparedStatement psIns = conn.prepareStatement(sqlInsert)) {
                psIns.setString(1, soPhieu);
                psIns.setString(2, maTN);
                psIns.setString(3, soPhong);
                psIns.setString(4, maNV);
                psIns.executeUpdate();
                return "OK";
            }
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}