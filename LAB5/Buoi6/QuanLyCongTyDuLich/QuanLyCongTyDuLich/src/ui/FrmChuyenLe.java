package ui;

import services.ChuyenLeService;
import services.Model;
import services.TourService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmChuyenLe extends JFrame {
    private JTextField txtMaChuyen, txtNgayDi, txtDiaDiemDon;
    private JComboBox<Model.ComboItem> cboTour;
    private JTable tblChuyen;
    private DefaultTableModel tableModel;
    private ChuyenLeService chuyenService = new ChuyenLeService();

    public FrmChuyenLe() {
        setTitle("Lịch Chuyến Khách Lẻ");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin chuyến khách lẻ"));

        pnlInput.add(new JLabel("Mã Chuyến:")); txtMaChuyen = new JTextField(); pnlInput.add(txtMaChuyen);
        pnlInput.add(new JLabel("Chọn Tour:")); cboTour = new JComboBox<>(); pnlInput.add(cboTour);
        pnlInput.add(new JLabel("Ngày Đi (YYYY-MM-DD):")); txtNgayDi = new JTextField(); pnlInput.add(txtNgayDi);
        pnlInput.add(new JLabel("Địa Điểm Đón:")); txtDiaDiemDon = new JTextField(); pnlInput.add(txtDiaDiemDon);

        JButton btnTao = new JButton("Tạo chuyến");
        JButton btnDong = new JButton("Đóng đăng ký");
        pnlInput.add(btnTao); pnlInput.add(btnDong);

        add(pnlInput, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Mã Chuyến", "Tên Tour", "Ngày Đi", "Ngày Về", "Địa Điểm Đón", "Trạng Thái"}, 0);
        tblChuyen = new JTable(tableModel);
        add(new JScrollPane(tblChuyen), BorderLayout.CENTER);

        loadCombo(); napDuLieu();

        btnTao.addActionListener(e -> {
            Model.ComboItem tour = (Model.ComboItem) cboTour.getSelectedItem();
            if (tour == null) return;
            Model.KetQuaXuLy kq = chuyenService.themChuyen(txtMaChuyen.getText(), tour.getValue(), txtNgayDi.getText(), txtDiaDiemDon.getText());
            JOptionPane.showMessageDialog(this, kq.getThongBao());
            if (kq.isThanhCong()) napDuLieu();
        });

        btnDong.addActionListener(e -> {
            int row = tblChuyen.getSelectedRow();
            if (row >= 0) {
                String ma = tableModel.getValueAt(row, 0).toString();
                Model.KetQuaXuLy kq = chuyenService.dongDangKy(ma);
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
        for (Model.ChuyenLeItem i : chuyenService.layDanhSachChuyen()) {
            tableModel.addRow(new Object[]{i.getMaChuyen(), i.getTenTour(), i.getNgayDi(), i.getNgayVe(), i.getDiaDiemDon(), i.getTrangThai()});
        }
    }
}