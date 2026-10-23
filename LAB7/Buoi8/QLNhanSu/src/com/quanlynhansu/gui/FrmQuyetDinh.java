package com.quanlynhansu.gui;

import com.quanlynhansu.dao.QuyetDinhDAO;

import javax.swing.*;
import java.awt.*;

public class FrmQuyetDinh extends JFrame {

    // Tab Khen Thuong
    private JTextField txtSoQDKT, txtNgayKT, txtNoiDungKT, txtMucThuong, txtMaNV_KT, txtMaPB_KT, txtSoPhieuChi;
    private JButton btnLuuKT, btnTroveKT;

    // Tab Ky Luat
    private JTextField txtSoQDKL, txtNgayKL, txtNoiDungKL, txtMucPhat, txtMaNV_KL, txtMaPB_KL, txtSoPhieuThu;
    private JButton btnLuuKL, btnTroveKL;

    private QuyetDinhDAO quyetDinhDAO;

    public FrmQuyetDinh() {
        quyetDinhDAO = new QuyetDinhDAO();

        setTitle("QUẢN LÝ QUYẾT ĐỊNH KHEN THƯỞNG / KỶ LUẬT");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // ==================== TAB 1: KHEN THƯỞNG ====================
        JPanel pnlKT = new JPanel(new GridLayout(8, 2, 8, 8));
        pnlKT.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlKT.add(new JLabel("Số QĐ Khen thưởng:"));
        txtSoQDKT = new JTextField();
        pnlKT.add(txtSoQDKT);

        pnlKT.add(new JLabel("Ngày QĐ (yyyy-MM-dd):"));
        txtNgayKT = new JTextField("2026-05-10");
        pnlKT.add(txtNgayKT);

        pnlKT.add(new JLabel("Nội dung thưởng:"));
        txtNoiDungKT = new JTextField();
        pnlKT.add(txtNoiDungKT);

        pnlKT.add(new JLabel("Mức thưởng (VNĐ):"));
        txtMucThuong = new JTextField("1000000");
        pnlKT.add(txtMucThuong);

        pnlKT.add(new JLabel("Mã Nhân viên (nếu khen cá nhân):"));
        txtMaNV_KT = new JTextField();
        pnlKT.add(txtMaNV_KT);

        pnlKT.add(new JLabel("Mã Phòng ban (nếu khen tập thể):"));
        txtMaPB_KT = new JTextField();
        pnlKT.add(txtMaPB_KT);

        pnlKT.add(new JLabel("Số phiếu chi tương ứng:"));
        txtSoPhieuChi = new JTextField();
        pnlKT.add(txtSoPhieuChi);

        btnLuuKT = new JButton("Lập QĐ & Phiếu Chi");
        btnTroveKT = new JButton("Trở về Menu chính");
        pnlKT.add(btnLuuKT);
        pnlKT.add(btnTroveKT);

        btnLuuKT.addActionListener(e -> xuLyKhenThuong());
        btnTroveKT.addActionListener(e -> troVeMenu());

        tabbedPane.addTab("Quyết định Khen Thưởng", pnlKT);

        // ==================== TAB 2: KỶ LUẬT ====================
        JPanel pnlKL = new JPanel(new GridLayout(8, 2, 8, 8));
        pnlKL.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlKL.add(new JLabel("Số QĐ Kỷ luật:"));
        txtSoQDKL = new JTextField();
        pnlKL.add(txtSoQDKL);

        pnlKL.add(new JLabel("Ngày QĐ (yyyy-MM-dd):"));
        txtNgayKL = new JTextField("2026-05-15");
        pnlKL.add(txtNgayKL);

        pnlKL.add(new JLabel("Nội dung phạt:"));
        txtNoiDungKL = new JTextField();
        pnlKL.add(txtNoiDungKL);

        pnlKL.add(new JLabel("Mức phạt (VNĐ):"));
        txtMucPhat = new JTextField("200000");
        pnlKL.add(txtMucPhat);

        pnlKL.add(new JLabel("Mã Nhân viên (nếu phạt cá nhân):"));
        txtMaNV_KL = new JTextField();
        pnlKL.add(txtMaNV_KL);

        pnlKL.add(new JLabel("Mã Phòng ban (nếu phạt tập thể):"));
        txtMaPB_KL = new JTextField();
        pnlKL.add(txtMaPB_KL);

        pnlKL.add(new JLabel("Số phiếu thu phạt tương ứng:"));
        txtSoPhieuThu = new JTextField();
        pnlKL.add(txtSoPhieuThu);

        btnLuuKL = new JButton("Lập QĐ & Phiếu Thu");
        btnTroveKL = new JButton("Trở về Menu chính");
        pnlKL.add(btnLuuKL);
        pnlKL.add(btnTroveKL);

        btnLuuKL.addActionListener(e -> xuLyKyLuat());
        btnTroveKL.addActionListener(e -> troVeMenu());

        tabbedPane.addTab("Quyết định Kỷ Luật", pnlKL);

        add(tabbedPane);
    }

    private void xuLyKhenThuong() {
        try {
            boolean ok = quyetDinhDAO.lapKhenThuong(
                    txtSoQDKT.getText().trim(),
                    txtNgayKT.getText().trim(),
                    txtNoiDungKT.getText().trim(),
                    Double.parseDouble(txtMucThuong.getText().trim()),
                    txtMaNV_KT.getText().trim(),
                    txtMaPB_KT.getText().trim(),
                    txtSoPhieuChi.getText().trim()
            );
            if (ok) {
                JOptionPane.showMessageDialog(this, "Đã lập quyết định khen thưởng và sinh phiếu chi thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi lập quyết định khen thưởng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuLyKyLuat() {
        try {
            boolean ok = quyetDinhDAO.lapKyLuat(
                    txtSoQDKL.getText().trim(),
                    txtNgayKL.getText().trim(),
                    txtNoiDungKL.getText().trim(),
                    Double.parseDouble(txtMucPhat.getText().trim()),
                    txtMaNV_KL.getText().trim(),
                    txtMaPB_KL.getText().trim(),
                    txtSoPhieuThu.getText().trim()
            );
            if (ok) {
                JOptionPane.showMessageDialog(this, "Đã lập quyết định kỷ luật và sinh phiếu thu thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi lập quyết định kỷ luật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void troVeMenu() {
        this.dispose();
        new FrmMain().setVisible(true);
    }
}