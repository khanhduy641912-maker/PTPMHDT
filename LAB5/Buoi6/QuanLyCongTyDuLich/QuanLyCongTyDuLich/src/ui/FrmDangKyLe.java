package ui;

import services.ChuyenLeService;
import services.DangKyLeService;
import services.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmDangKyLe extends JFrame {
    private JTextField txtSoDK, txtTenKhach, txtSDT, txtSoNguoi, txtMaDiemBan;
    private JComboBox<Model.ComboItem> cboChuyen;
    private JTable tblDK;
    private DefaultTableModel tableModel;
    private DangKyLeService dangKyService = new DangKyLeService();

    public FrmDangKyLe() {
        setTitle("Đăng Ký Khách Lẻ");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlInput = new JPanel(new GridLayout(4, 4, 8, 8));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Đăng ký khách lẻ (< 12 người)"));

        pnlInput.add(new JLabel("Số Phiếu DK:")); txtSoDK = new JTextField(); pnlInput.add(txtSoDK);
        pnlInput.add(new JLabel("Chọn Chuyến:")); cboChuyen = new JComboBox<>(); pnlInput.add(cboChuyen);
        pnlInput.add(new JLabel("Mã Điểm Bán:")); txtMaDiemBan = new JTextField("DB01"); pnlInput.add(txtMaDiemBan);
        pnlInput.add(new JLabel("Tên Khách:")); txtTenKhach = new JTextField(); pnlInput.add(txtTenKhach);
        pnlInput.add(new JLabel("SĐT:")); txtSDT = new JTextField(); pnlInput.add(txtSDT);
        pnlInput.add(new JLabel("Số Người:")); txtSoNguoi = new JTextField("1"); pnlInput.add(txtSoNguoi);

        JButton btnDangKy = new JButton("Đăng ký & Thanh toán");
        pnlInput.add(btnDangKy);

        add(pnlInput, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Số Phiếu", "Mã Chuyến", "Tên Khách", "SĐT", "Số Người", "Thành Tiền", "Trạng Thái"}, 0);
        tblDK = new JTable(tableModel);
        add(new JScrollPane(tblDK), BorderLayout.CENTER);

        loadCombo(); napDuLieu();

        btnDangKy.addActionListener(e -> {
            try {
                Model.ComboItem chuyen = (Model.ComboItem) cboChuyen.getSelectedItem();
                Model.KetQuaXuLy kq = dangKyService.dangKy(txtSoDK.getText(), chuyen.getValue(), txtMaDiemBan.getText(), txtTenKhach.getText(), txtSDT.getText(), Integer.parseInt(txtSoNguoi.getText()));
                JOptionPane.showMessageDialog(this, kq.getThongBao());
                if (kq.isThanhCong()) napDuLieu();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Số người phải là số nguyên!"); }
        });
    }

    private void loadCombo() {
        cboChuyen.removeAllItems();
        for (Model.ComboItem item : new ChuyenLeService().layChuyenMoCombo()) cboChuyen.addItem(item);
    }

    private void napDuLieu() {
        tableModel.setRowCount(0);
        for (Model.DangKyLeItem i : dangKyService.layDanhSach()) {
            tableModel.addRow(new Object[]{i.getSoDKLe(), i.getMaChuyen(), i.getTenNguoiDangKy(), i.getDienThoai(), i.getSoNguoi(), String.format("%,.0f VNĐ", i.getThanhTien()), i.getTrangThai()});
        }
    }
}