package com.quanlynhansu.gui;

import com.quanlynhansu.dao.LuongDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrmTinhLuong extends JFrame {

    private JTextField txtMaBL, txtThang, txtNam;
    private JTable tblLuong;
    private DefaultTableModel tableModel;
    private JButton btnTinhLuong, btnLuu, btnTrove;
    private LuongDAO luongDAO;
    private List<Object[]> dsLuongHienTai;

    public FrmTinhLuong() {
        luongDAO = new LuongDAO();
        dsLuongHienTai = new ArrayList<>();

        setTitle("BẢNG TÍNH VÀ CHỐT LƯƠNG HẰNG THÁNG TỰ ĐỘNG");
        setSize(950, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel top chọn tháng/năm
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Thời gian tính lương"));

        pnlTop.add(new JLabel("Mã Bảng Lương:"));
        txtMaBL = new JTextField("BL_T5_2026", 10);
        pnlTop.add(txtMaBL);

        pnlTop.add(new JLabel("Tháng:"));
        txtThang = new JTextField("5", 5);
        pnlTop.add(txtThang);

        pnlTop.add(new JLabel("Năm:"));
        txtNam = new JTextField("2026", 6);
        pnlTop.add(txtNam);

        btnTinhLuong = new JButton("Tính Lương Tự Động");
        btnTinhLuong.setBackground(new Color(0, 153, 76));
        btnTinhLuong.setForeground(Color.WHITE);
        pnlTop.add(btnTinhLuong);

        add(pnlTop, BorderLayout.NORTH);

        // Table hiển thị chi tiết bảng lương
        tableModel = new DefaultTableModel(new String[]{
                "Mã NV", "Họ và Tên", "Lương CB (VNĐ)", "Phụ Cấp (VNĐ)", "Ngày Công", "Giờ Phụ Trội", "Tổng Lương (VNĐ)", "Thực Lĩnh (VNĐ)"
        }, 0);
        tblLuong = new JTable(tableModel);
        add(new JScrollPane(tblLuong), BorderLayout.CENTER);

        // Panel bottom lưu & thoát
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnLuu = new JButton("Chốt & Lưu Bảng Lương");
        btnTrove = new JButton("Trở về Menu chính");

        pnlBottom.add(btnLuu);
        pnlBottom.add(btnTrove);
        add(pnlBottom, BorderLayout.SOUTH);

        btnTinhLuong.addActionListener(e -> tinhLuongToanBo());
        btnLuu.addActionListener(e -> chotLuong());
        btnTrove.addActionListener(e -> {
            this.dispose();
            new FrmMain().setVisible(true);
        });

        // Tự động tính lương ngay khi mở màn hình
        tinhLuongToanBo();
    }

    private void tinhLuongToanBo() {
        try {
            int thang = Integer.parseInt(txtThang.getText().trim());
            int nam = Integer.parseInt(txtNam.getText().trim());

            tableModel.setRowCount(0);
            dsLuongHienTai = luongDAO.tinhLuongTuDong(thang, nam);

            for (Object[] row : dsLuongHienTai) {
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Tháng hoặc Năm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chotLuong() {
        if (dsLuongHienTai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu bảng lương để chốt!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = luongDAO.luuBangLuongToanBo(
                txtMaBL.getText().trim(),
                Integer.parseInt(txtThang.getText().trim()),
                Integer.parseInt(txtNam.getText().trim()),
                dsLuongHienTai
        );

        if (ok) {
            JOptionPane.showMessageDialog(this, "Đã chốt và lưu toàn bộ Bảng lương vào CSDL SQL Server thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu bảng lương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}