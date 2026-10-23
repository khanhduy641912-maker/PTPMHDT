package com.quanlynhansu.gui;

import com.quanlynhansu.dao.HopDongDAO;
import com.quanlynhansu.model.HopDong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FrmHopDongBaoHiem extends JFrame {

    private JTextField txtSoHD, txtMaNV, txtNgayKy, txtNgayHetHan;
    private JComboBox<String> cboLoaiHD;
    private JTextField txtMaHS, txtBHYT, txtBHXH;
    private JTable tblHopDong;
    private DefaultTableModel tableModel;
    private JButton btnKyHD, btnTrove;

    private HopDongDAO hopDongDAO;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public FrmHopDongBaoHiem() {
        hopDongDAO = new HopDongDAO();

        setTitle("QUẢN LÝ HỢP ĐỒNG VÀ BẢO HIỂM");
        setSize(850, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("KÝ HỢP ĐỒNG LAO ĐỘNG & THIẾT LẬP BẢO HIỂM", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridLayout(8, 2, 8, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin Hợp đồng & Hồ sơ Bảo hiểm"));

        pnlForm.add(new JLabel("Số hợp đồng:"));
        txtSoHD = new JTextField();
        pnlForm.add(txtSoHD);

        pnlForm.add(new JLabel("Mã nhân viên:"));
        txtMaNV = new JTextField();
        pnlForm.add(txtMaNV);

        pnlForm.add(new JLabel("Loại hợp đồng:"));
        cboLoaiHD = new JComboBox<>(new String[]{"Dài hạn", "Ngắn hạn"});
        pnlForm.add(cboLoaiHD);

        pnlForm.add(new JLabel("Ngày ký (yyyy-MM-dd):"));
        txtNgayKy = new JTextField(sdf.format(new Date()));
        pnlForm.add(txtNgayKy);

        pnlForm.add(new JLabel("Ngày hết hạn (yyyy-MM-dd, để trống nếu dài hạn):"));
        txtNgayHetHan = new JTextField();
        pnlForm.add(txtNgayHetHan);

        pnlForm.add(new JLabel("Mã hồ sơ BHYT/BHXH (Nếu HD dài hạn):"));
        txtMaHS = new JTextField();
        pnlForm.add(txtMaHS);

        pnlForm.add(new JLabel("Mức đóng BHYT (VNĐ):"));
        txtBHYT = new JTextField("180000");
        pnlForm.add(txtBHYT);

        pnlForm.add(new JLabel("Mức đóng BHXH (VNĐ):"));
        txtBHXH = new JTextField("800000");
        pnlForm.add(txtBHXH);

        btnKyHD = new JButton("Ký Hợp Đồng");
        btnTrove = new JButton("Trở về Menu chính");
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlButton.add(btnKyHD);
        pnlButton.add(btnTrove);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(pnlForm, BorderLayout.CENTER);
        pnlTop.add(pnlButton, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Số HĐ", "Mã NV", "Loại HĐ", "Ngày Ký", "Ngày Hết Hạn"}, 0);
        tblHopDong = new JTable(tableModel);
        add(new JScrollPane(tblHopDong), BorderLayout.CENTER);

        btnKyHD.addActionListener(e -> xuLyKyHopDong());
        btnTrove.addActionListener(e -> {
            this.dispose();
            new FrmMain().setVisible(true);
        });

        cboLoaiHD.addActionListener(e -> {
            boolean isDaiHan = "Dài hạn".equalsIgnoreCase((String) cboLoaiHD.getSelectedItem());
            txtMaHS.setEnabled(isDaiHan);
            txtBHYT.setEnabled(isDaiHan);
            txtBHXH.setEnabled(isDaiHan);
        });

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HopDong> ds = hopDongDAO.layDanhSachHopDong();
        for (HopDong hd : ds) {
            tableModel.addRow(new Object[]{
                    hd.getSoHD(),
                    hd.getMaNV(),
                    hd.getLoaiHD(),
                    sdf.format(hd.getNgayKy()),
                    hd.getNgayHetHan() != null ? sdf.format(hd.getNgayHetHan()) : "Không"
            });
        }
    }

    private void xuLyKyHopDong() {
        try {
            String soHD = txtSoHD.getText().trim();
            String maNV = txtMaNV.getText().trim();
            String loaiHD = (String) cboLoaiHD.getSelectedItem();
            Date ngayKy = sdf.parse(txtNgayKy.getText().trim());
            Date ngayHetHan = txtNgayHetHan.getText().trim().isEmpty() ? null : sdf.parse(txtNgayHetHan.getText().trim());

            HopDong hd = new HopDong(soHD, loaiHD, ngayKy, ngayHetHan, maNV);

            double bhyt = Double.parseDouble(txtBHYT.getText().trim());
            double bhxh = Double.parseDouble(txtBHXH.getText().trim());

            if (hopDongDAO.kyHopDong(hd, txtMaHS.getText().trim(), bhyt, bhxh)) {
                JOptionPane.showMessageDialog(this, "Ký hợp đồng thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu hợp đồng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày tháng không đúng định dạng yyyy-MM-dd!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tiền bảo hiểm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}