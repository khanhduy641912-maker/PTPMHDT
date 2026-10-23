package com.quanlynhansu.gui;

import com.quanlynhansu.dao.PhuTroiDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FrmPhanCongPhuTroi extends JFrame {

    private JTextField txtSoPhieu, txtNgayPT, txtMaNV, txtSoGio;
    private JComboBox<String> cboHinhThuc;
    private JTable tblChiTiet, tblLichSu;
    private DefaultTableModel modelChiTiet, modelLichSu;
    private JButton btnThemNV, btnXoaNV, btnLuuPhieu, btnTrove;

    private PhuTroiDAO phuTroiDAO;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public FrmPhanCongPhuTroi() {
        phuTroiDAO = new PhuTroiDAO();

        setTitle("QUẢN LÝ LẬP PHIẾU PHỤ TRỘI VÀ TĂNG CA");
        setSize(850, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. TOP PANEL: Thông tin chung phiếu phụ trội
        JPanel pnlHeader = new JPanel(new GridLayout(2, 4, 10, 8));
        pnlHeader.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu phụ trội"));

        pnlHeader.add(new JLabel("Số Phiếu Phụ Trội:"));
        txtSoPhieu = new JTextField("PPT01");
        pnlHeader.add(txtSoPhieu);

        pnlHeader.add(new JLabel("Ngày Phụ Trội:"));
        txtNgayPT = new JTextField(sdf.format(new Date()));
        pnlHeader.add(txtNgayPT);

        pnlHeader.add(new JLabel("Hình Thức:"));
        cboHinhThuc = new JComboBox<>(new String[]{"Tăng ca", "Ngày lễ", "Ngày nghỉ"});
        pnlHeader.add(cboHinhThuc);

        add(pnlHeader, BorderLayout.NORTH);

        // 2. CENTER PANEL: Thêm danh sách nhân viên tăng ca
        JPanel pnlCenter = new JPanel(new BorderLayout(5, 5));

        JPanel pnlInputNV = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlInputNV.setBorder(BorderFactory.createTitledBorder("Thêm nhân viên làm phụ trội"));

        pnlInputNV.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField(8);
        pnlInputNV.add(txtMaNV);

        pnlInputNV.add(new JLabel("Số giờ thực tế:"));
        txtSoGio = new JTextField("4.0", 5);
        pnlInputNV.add(txtSoGio);

        btnThemNV = new JButton("Thêm vào danh sách");
        btnXoaNV = new JButton("Xóa khỏi danh sách");
        pnlInputNV.add(btnThemNV);
        pnlInputNV.add(btnXoaNV);

        pnlCenter.add(pnlInputNV, BorderLayout.NORTH);

        // Bảng danh sách nhân viên sẽ lập trong phiếu hiện tại
        modelChiTiet = new DefaultTableModel(new String[]{"Mã NV", "Số Giờ Thực Làm", "Giờ Quy Đổi (x1.5)"}, 0);
        tblChiTiet = new JTable(modelChiTiet);
        JScrollPane scrollChiTiet = new JScrollPane(tblChiTiet);
        scrollChiTiet.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên trong phiếu hiện tại"));
        
        // Bảng lịch sử các phiếu phụ trội
        modelLichSu = new DefaultTableModel(new String[]{"Số Phếu", "Ngày PT", "Hình Thức", "Mã NV", "Họ Tên", "Số Giờ", "Giờ Quy Đổi"}, 0);
        tblLichSu = new JTable(modelLichSu);
        JScrollPane scrollLichSu = new JScrollPane(tblLichSu);
        scrollLichSu.setBorder(BorderFactory.createTitledBorder("Lịch sử phiếu phụ trội đã lưu"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollChiTiet, scrollLichSu);
        splitPane.setDividerLocation(150);
        pnlCenter.add(splitPane, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // 3. BOTTOM PANEL: Nút bấm thao tác
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnLuuPhieu = new JButton("Lưu Phiếu Phụ Trội");
        btnLuuPhieu.setBackground(new Color(0, 153, 76));
        btnLuuPhieu.setForeground(Color.WHITE);

        btnTrove = new JButton("Trở về Menu chính");

        pnlBottom.add(btnLuuPhieu);
        pnlBottom.add(btnTrove);
        add(pnlBottom, BorderLayout.SOUTH);

        // Sự kiện
        btnThemNV.addActionListener(e -> themNhanVienVaoBang());
        btnXoaNV.addActionListener(e -> xoaNhanVienKhoiBang());
        btnLuuPhieu.addActionListener(e -> luuPhieuPhuTroi());
        btnTrove.addActionListener(e -> {
            this.dispose();
            new FrmMain().setVisible(true);
        });

        loadLichSu();
    }

    private void themNhanVienVaoBang() {
        String maNV = txtMaNV.getText().trim();
        String soGioStr = txtSoGio.getText().trim();

        if (maNV.isEmpty() || soGioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã NV và Số giờ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double soGio = Double.parseDouble(soGioStr);
            double gioQuyDoi = soGio * 1.5;

            modelChiTiet.addRow(new Object[]{maNV, soGio, gioQuyDoi});
            txtMaNV.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số giờ phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaNhanVienKhoiBang() {
        int row = tblChiTiet.getSelectedRow();
        if (row >= 0) {
            modelChiTiet.removeRow(row);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa trong danh sách!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void luuPhieuPhuTroi() {
        if (modelChiTiet.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Danh sách nhân viên tăng ca đang trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Object[]> chiTiet = new ArrayList<>();
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            chiTiet.add(new Object[]{
                    modelChiTiet.getValueAt(i, 0),
                    modelChiTiet.getValueAt(i, 1)
            });
        }

        boolean ok = phuTroiDAO.luuPhieuPhuTroiDayDu(
                txtSoPhieu.getText().trim(),
                txtNgayPT.getText().trim(),
                (String) cboHinhThuc.getSelectedItem(),
                chiTiet
        );

        if (ok) {
            JOptionPane.showMessageDialog(this, "Đã lưu phiếu phụ trội thành công!");
            modelChiTiet.setRowCount(0);
            loadLichSu();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu phụ trội!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLichSu() {
        modelLichSu.setRowCount(0);
        List<Object[]> ds = phuTroiDAO.layDanhSachPhieuPhuTroi();
        for (Object[] row : ds) {
            modelLichSu.addRow(row);
        }
    }
}