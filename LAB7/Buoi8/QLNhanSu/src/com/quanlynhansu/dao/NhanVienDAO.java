package com.quanlynhansu.dao;

import com.quanlynhansu.data.Database;
import com.quanlynhansu.model.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    
    // Lấy toàn bộ danh sách nhân viên để đổ lên bảng (JTable)
    public List<NhanVien> layDanhSachNhanVien() {
        List<NhanVien> danhSach = new ArrayList<>();
        String sql = "SELECT MaNV, HoTen, NgaySinh, DiaChi, MaPB FROM NhanVien";
        
        try (Connection conn = Database.openConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setMaPB(rs.getString("MaPB"));
                
                danhSach.add(nv);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn danh sách nhân viên: " + e.getMessage());
        }
        return danhSach;
    }

    // Thêm mới nhân viên vào CSDL
    public boolean themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (MaNV, HoTen, NgaySinh, DiaChi, MaPB) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = Database.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nv.getMaNV());
            pstmt.setString(2, nv.getHoTen());
            if (nv.getNgaySinh() != null) {
                pstmt.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            } else {
                pstmt.setNull(3, java.sql.Types.DATE);
            }
            pstmt.setString(4, nv.getDiaChi());
            pstmt.setString(5, nv.getMaPB());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi thêm nhân viên: " + e.getMessage());
            return false;
        }
    }
}