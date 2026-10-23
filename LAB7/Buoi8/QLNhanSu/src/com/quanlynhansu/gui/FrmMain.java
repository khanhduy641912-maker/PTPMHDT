package com.quanlynhansu.gui;

import javax.swing.*;
import java.awt.*;

public class FrmMain extends JFrame {

    public FrmMain() {
        setTitle("HỆ THỐNG QUẢN LÝ NHÂN SỰ - CÔNG TY SƠN ĐÔNG Á");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblHeader = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ CÔNG TY SƠN ĐÔNG Á", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(new Color(0, 51, 153));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(lblHeader, BorderLayout.NORTH);

        JPanel pnlButtons = new JPanel(new GridLayout(6, 1, 10, 10));
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(10, 60, 30, 60));

        JButton btnNV = new JButton("1. Quản lý Hồ sơ Nhân viên & Thử việc");
        JButton btnHD = new JButton("2. Quản lý Hợp đồng Lao động & Bảo hiểm");
        JButton btnQDL = new JButton("3. Quản lý Quyết định Lương & Chức vụ");
        JButton btnQD = new JButton("4. Quản lý Quyết định Khen thưởng / Kỷ luật");
        JButton btnPT = new JButton("5. Quản lý Phụ trội & Tăng ca");
        JButton btnBL = new JButton("6. Quản lý Chấm công & Tính lương");

        pnlButtons.add(btnNV);
        pnlButtons.add(btnHD);
        pnlButtons.add(btnQDL);
        pnlButtons.add(btnQD);
        pnlButtons.add(btnPT);
        pnlButtons.add(btnBL);

        add(pnlButtons, BorderLayout.CENTER);

        btnNV.addActionListener(e -> openForm(new FrmNhanVien()));
        btnHD.addActionListener(e -> openForm(new FrmHopDongBaoHiem()));
        btnQDL.addActionListener(e -> openForm(new FrmQuyetDinhLuongChucVu()));
        btnQD.addActionListener(e -> openForm(new FrmQuyetDinh()));
        btnPT.addActionListener(e -> openForm(new FrmPhanCongPhuTroi()));
        btnBL.addActionListener(e -> openForm(new FrmTinhLuong()));
    }

    private void openForm(JFrame frame) {
        this.dispose();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmMain().setVisible(true));
    }
}