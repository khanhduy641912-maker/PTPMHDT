package com.quanlynhansu.gui;

import com.quanlynhansu.dao.NhanVienDAO;
import com.quanlynhansu.model.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FrmNhanVien extends JFrame {

    private JTextField txtMaNV, txtHoTen, txtNgaySinh, txtDiaChi, txtMaPB;
    private JTable tblNhanVien;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnLamMoi, btnTrove;

    private NhanVienDAO nhanVienDAO;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public FrmNhanVien() {
        nhanVienDAO = new NhanVienDAO();

        setTitle("QUẢN LÝ NHÂN SỰ - CÔNG TY SƠN ĐÔNG Á");
        setSize(800, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ HỒ SƠ NHÂN VIÊN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        pnlForm.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        pnlForm.add(txtMaNV);

        pnlForm.add(new JLabel("Họ và tên:"));
        txtHoTen = new JTextField();
        pnlForm.add(txtHoTen);

        pnlForm.add(new JLabel("Ngày sinh (yyyy-MM-dd):"));
        txtNgaySinh = new JTextField();
        pnlForm.add(txtNgaySinh);

        pnlForm.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        pnlForm.add(txtDiaChi);

        pnlForm.add(new JLabel("Mã phòng ban (PB01/PB02/PB03):"));
        txtMaPB = new JTextField();
        pnlForm.add(txtMaPB);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnThem = new JButton("Thêm Nhân Viên");
        btnLamMoi = new JButton("Làm Mới");
        btnTrove = new JButton("Trở về Menu chính");

        pnlButtons.add(btnThem);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnTrove);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(pnlForm, BorderLayout.CENTER);
        pnlTop.add(pnlButtons, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Mã NV", "Họ và Tên", "Ngày Sinh", "Địa Chỉ", "Mã PB"});
        tblNhanVien = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên hiện có"));
        add(scrollPane, BorderLayout.CENTER);

        btnThem.addActionListener(e -> themNhanVien());
        btnLamMoi.addActionListener(e -> {
            xoaTrangForm();
            hienThiDanhSach();
        });

        // Xử lý sự kiện nút Trở về
        btnTrove.addActionListener(e -> {
            this.dispose();
            new FrmMain().setVisible(true);
        });

        hienThiDanhSach();
    }

    private void hienThiDanhSach() {
        tableModel.setRowCount(0);
        List<NhanVien> ds = nhanVienDAO.layDanhSachNhanVien();
        for (NhanVien nv : ds) {
            String ngaySinhStr = (nv.getNgaySinh() != null) ? sdf.format(nv.getNgaySinh()) : "";
            tableModel.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getHoTen(),
                    ngaySinhStr,
                    nv.getDiaChi(),
                    nv.getMaPB()
            });
        }
    }

    private void themNhanVien() {
        String maNV = txtMaNV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String ngaySinhStr = txtNgaySinh.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String maPB = txtMaPB.getText().trim();

        if (maNV.isEmpty() || hoTen.isEmpty() || maPB.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã NV, Họ Tên và Mã PB!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date ngaySinh = null;
        if (!ngaySinhStr.isEmpty()) {
            try {
                ngaySinh = sdf.parse(ngaySinhStr);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Định dạng ngày sinh không hợp lệ (đúng dạng yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        NhanVien nv = new NhanVien(maNV, hoTen, ngaySinh, diaChi, maPB);
        if (nhanVienDAO.themNhanVien(nv)) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            xoaTrangForm();
            hienThiDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại! Kiểm tra trùng Mã NV hoặc Mã PB không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaTrangForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtDiaChi.setText("");
        txtMaPB.setText("");
    }
}