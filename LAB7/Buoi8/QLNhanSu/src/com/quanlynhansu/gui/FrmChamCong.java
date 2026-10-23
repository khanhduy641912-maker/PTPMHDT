package com.quanlynhansu.gui;

import com.quanlynhansu.dao.ChamCongDAO;

import javax.swing.*;
import java.awt.*;

public class FrmChamCong extends JFrame {

    private JTextField txtMaBCC, txtThang, txtNam, txtMaPB, txtMaNV, txtSoNgay;
    private JButton btnLuu;
    private ChamCongDAO chamCongDAO;

    public FrmChamCong() {
        chamCongDAO = new ChamCongDAO();

        setTitle("QUẢN LÝ CHẤM CÔNG THÁNG");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pnl = new JPanel(new GridLayout(7, 2, 8, 8));
        pnl.setBorder(BorderFactory.createTitledBorder("Nhập thông tin chấm công"));

        pnl.add(new JLabel("Mã bảng chấm công:"));
        txtMaBCC = new JTextField("BCC01");
        pnl.add(txtMaBCC);

        pnl.add(new JLabel("Tháng:"));
        txtThang = new JTextField("5");
        pnl.add(txtThang);

        pnl.add(new JLabel("Năm:"));
        txtNam = new JTextField("2026");
        pnl.add(txtNam);

        pnl.add(new JLabel("Mã Phòng ban:"));
        txtMaPB = new JTextField("PB01");
        pnl.add(txtMaPB);

        pnl.add(new JLabel("Mã Nhân viên:"));
        txtMaNV = new JTextField("NV01");
        pnl.add(txtMaNV);

        pnl.add(new JLabel("Số ngày làm việc thực tế:"));
        txtSoNgay = new JTextField("22.0");
        pnl.add(txtSoNgay);

        btnLuu = new JButton("Lưu Bảng Chấm Công");
        pnl.add(btnLuu);

        add(pnl);

        btnLuu.addActionListener(e -> {
            try {
                boolean ok = chamCongDAO.chamCongThang(
                        txtMaBCC.getText().trim(),
                        Integer.parseInt(txtThang.getText().trim()),
                        Integer.parseInt(txtNam.getText().trim()),
                        txtMaPB.getText().trim(),
                        txtMaNV.getText().trim(),
                        Double.parseDouble(txtSoNgay.getText().trim())
                );
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Chấm công thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi chấm công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}