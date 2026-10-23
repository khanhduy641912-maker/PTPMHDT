package com.quanlynhansu.gui;

import com.quanlynhansu.dao.QuyetDinhLuongChucVuDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

public class FrmQuyetDinhLuongChucVu extends JFrame {

    private JTextField txtSoQDL, txtNgayHL_L, txtLuongCB, txtPhuCapL, txtPhuCapDH, txtMaNV_L;
    private JTextField txtSoQDCV, txtNgayHL_CV, txtMaCV, txtMaNV_CV;
    private JButton btnLuuL, btnLuuCV, btnTrove1, btnTrove2;
    private QuyetDinhLuongChucVuDAO dao;

    public FrmQuyetDinhLuongChucVu() {
        dao = new QuyetDinhLuongChucVuDAO();

        setTitle("QUẢN LÝ QUYẾT ĐỊNH LƯƠNG VÀ BỔ NHIỆM CHỨC VỤ");
        setSize(600, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tab = new JTabbedPane();

        // TAB LƯƠNG
        JPanel pnlL = new JPanel(new GridLayout(7, 2, 8, 8));
        pnlL.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlL.add(new JLabel("Số QĐ Lương:"));
        txtSoQDL = new JTextField("QDL01");
        pnlL.add(txtSoQDL);

        pnlL.add(new JLabel("Ngày hiệu lực (yyyy-MM-dd):"));
        txtNgayHL_L = new JTextField("2026-03-01");
        pnlL.add(txtNgayHL_L);

        pnlL.add(new JLabel("Mức lương cơ bản (VNĐ):"));
        txtLuongCB = new JTextField("9000000");
        pnlL.add(txtLuongCB);

        pnlL.add(new JLabel("Phụ cấp lương (VNĐ):"));
        txtPhuCapL = new JTextField("500000");
        pnlL.add(txtPhuCapL);

        pnlL.add(new JLabel("Phụ cấp độc hại (VNĐ):"));
        txtPhuCapDH = new JTextField("0");
        pnlL.add(txtPhuCapDH);

        pnlL.add(new JLabel("Mã Nhân viên:"));
        txtMaNV_L = new JTextField("NV01");
        pnlL.add(txtMaNV_L);

        btnLuuL = new JButton("Lưu QĐ Lương");
        btnTrove1 = new JButton("Trở về Menu chính");
        pnlL.add(btnLuuL);
        pnlL.add(btnTrove1);

        tab.addTab("Quyết định Lương", pnlL);

        // TAB CHỨC VỤ
        JPanel pnlCV = new JPanel(new GridLayout(5, 2, 8, 8));
        pnlCV.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlCV.add(new JLabel("Số QĐ Chức vụ:"));
        txtSoQDCV = new JTextField("QDCV01");
        pnlCV.add(txtSoQDCV);

        pnlCV.add(new JLabel("Ngày hiệu lực (yyyy-MM-dd):"));
        txtNgayHL_CV = new JTextField("2026-03-01");
        pnlCV.add(txtNgayHL_CV);

        pnlCV.add(new JLabel("Mã Chức vụ (CV01/CV02/CV03):"));
        txtMaCV = new JTextField("CV01");
        pnlCV.add(txtMaCV);

        pnlCV.add(new JLabel("Mã Nhân viên:"));
        txtMaNV_CV = new JTextField("NV01");
        pnlCV.add(txtMaNV_CV);

        btnLuuCV = new JButton("Lưu QĐ Chức Vụ");
        btnTrove2 = new JButton("Trở về Menu chính");
        pnlCV.add(btnLuuCV);
        pnlCV.add(btnTrove2);

        tab.addTab("Quyết định Bổ nhiệm Chức vụ", pnlCV);

        add(tab);

        btnLuuL.addActionListener(e -> {
            try {
                boolean ok = dao.lapQuyetDinhLuong(
                        txtSoQDL.getText().trim(),
                        txtNgayHL_L.getText().trim(),
                        Double.parseDouble(txtLuongCB.getText().trim()),
                        Double.parseDouble(txtPhuCapL.getText().trim()),
                        Double.parseDouble(txtPhuCapDH.getText().trim()),
                        txtMaNV_L.getText().trim()
                );
                if (ok) JOptionPane.showMessageDialog(this, "Lưu QĐ Lương thành công!");
                else JOptionPane.showMessageDialog(this, "Lỗi lưu QĐ Lương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLuuCV.addActionListener(e -> {
            try {
                boolean ok = dao.lapQuyetDinhChucVu(
                        txtSoQDCV.getText().trim(),
                        txtNgayHL_CV.getText().trim(),
                        txtMaCV.getText().trim(),
                        txtMaNV_CV.getText().trim()
                );
                if (ok) JOptionPane.showMessageDialog(this, "Lưu QĐ Bổ nhiệm Chức vụ thành công!");
                else JOptionPane.showMessageDialog(this, "Lỗi lưu QĐ Chức vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        ActionListener troVe = e -> {
            this.dispose();
            new FrmMain().setVisible(true);
        };
        btnTrove1.addActionListener(troVe);
        btnTrove2.addActionListener(troVe);
    }
}