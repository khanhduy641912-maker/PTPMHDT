package ui;

import services.DangKyDoanService;
import services.Model;
import services.TourService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmDangKyDoan extends JFrame {
    private JTextField txtSoDK, txtMaDoan, txtTenCQ, txtDiaChi, txtSDT, txtDaiDien, txtNgayDi, txtSoNguoi, txtDon, txtTienCoc;
    private JCheckBox chkBaoHiem;
    private JComboBox<Model.ComboItem> cboTour;
    private JTable tblDoan;
    private DefaultTableModel tableModel;
    private DangKyDoanService doanService = new DangKyDoanService();

    public FrmDangKyDoan() {
        setTitle("Đăng Ký Theo Đoàn (> 12 người)");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlInput = new JPanel(new GridLayout(6, 4, 8, 8));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin đăng ký đoàn"));

        pnlInput.add(new JLabel("Mã Đoàn:")); txtMaDoan = new JTextField(); pnlInput.add(txtMaDoan);
        pnlInput.add(new JLabel("Tên Cơ Quan:")); txtTenCQ = new JTextField(); pnlInput.add(txtTenCQ);
        pnlInput.add(new JLabel("Địa Chỉ:")); txtDiaChi = new JTextField(); pnlInput.add(txtDiaChi);
        pnlInput.add(new JLabel("SĐT:")); txtSDT = new JTextField(); pnlInput.add(txtSDT);
        pnlInput.add(new JLabel("Người Đại Diện:")); txtDaiDien = new JTextField(); pnlInput.add(txtDaiDien);

        pnlInput.add(new JLabel("Số Phiếu DK:")); txtSoDK = new JTextField(); pnlInput.add(txtSoDK);
        pnlInput.add(new JLabel("Chọn Tour:")); cboTour = new JComboBox<>(); pnlInput.add(cboTour);
        pnlInput.add(new JLabel("Ngày Đi (YYYY-MM-DD):")); txtNgayDi = new JTextField(); pnlInput.add(txtNgayDi);
        pnlInput.add(new JLabel("Số Người (> 12):")); txtSoNguoi = new JTextField(); pnlInput.add(txtSoNguoi);
        pnlInput.add(new JLabel("Nơi Đón:")); txtDon = new JTextField(); pnlInput.add(txtDon);
        pnlInput.add(new JLabel("Tiền Cọc:")); txtTienCoc = new JTextField(); pnlInput.add(txtTienCoc);

        chkBaoHiem = new JCheckBox("Mua bảo hiểm"); pnlInput.add(chkBaoHiem);

        JButton btnLap = new JButton("Lập phiếu đoàn");
        JButton btnHuy = new JButton("Hủy phiếu (mất cọc)");
        pnlInput.add(btnLap); pnlInput.add(btnHuy);

        add(pnlInput, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Số Phiếu", "Đoàn Khách", "Tour", "Ngày Đi", "Ngày KT", "Số Người", "Cọc", "Tổng Tiền", "Trạng Thái"}, 0);
        tblDoan = new JTable(tableModel);
        add(new JScrollPane(tblDoan), BorderLayout.CENTER);

        loadCombo(); napDuLieu();

        btnLap.addActionListener(e -> {
            try {
                Model.ComboItem tour = (Model.ComboItem) cboTour.getSelectedItem();
                Model.KetQuaXuLy kq = doanService.dangKy(txtSoDK.getText(), txtMaDoan.getText(), txtTenCQ.getText(), txtDiaChi.getText(), txtSDT.getText(), txtDaiDien.getText(), tour.getValue(), txtNgayDi.getText(), Integer.parseInt(txtSoNguoi.getText()), txtDon.getText(), chkBaoHiem.isSelected(), Double.parseDouble(txtTienCoc.getText()));
                JOptionPane.showMessageDialog(this, kq.getThongBao());
                if (kq.isThanhCong()) napDuLieu();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Số liệu nhập vào không hợp lệ!"); }
        });

        btnHuy.addActionListener(e -> {
            int r = tblDoan.getSelectedRow();
            if (r >= 0) {
                String so = tableModel.getValueAt(r, 0).toString();
                Model.KetQuaXuLy kq = doanService.huyDangKy(so);
                JOptionPane.showMessageDialog(this, kq.getThongBao());
                if (kq.isThanhCong()) napDuLieu();
            }
        });
    }

    private void loadCombo() {
        cboTour.removeAllItems();
        for (Model.ComboItem item : new TourService().layTourCombo()) cboTour.addItem(item);
    }

    private void napDuLieu() {
        tableModel.setRowCount(0);
        for (Model.DangKyDoanItem i : doanService.layDanhSach()) {
            tableModel.addRow(new Object[]{i.getSoDKDoan(), i.getTenCoQuan(), i.getTenTour(), i.getNgayDi(), i.getNgayKetThuc(), i.getSoNguoi(), String.format("%,.0f", i.getTienCoc()), String.format("%,.0f", i.getTongTienDuKien()), i.getTrangThai()});
        }
    }
}