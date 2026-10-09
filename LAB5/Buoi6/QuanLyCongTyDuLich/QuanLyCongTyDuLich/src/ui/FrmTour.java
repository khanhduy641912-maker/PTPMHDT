package ui;

import services.Model;
import services.TourService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmTour extends JFrame {
    private JTextField txtMaTour, txtTenTour, txtSoNgay, txtSoDem, txtDonGia;
    private JTable tblTour;
    private DefaultTableModel tableModel;
    private TourService tourService = new TourService();

    public FrmTour() {
        setTitle("Quản Lý Tour - Hành Trình");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin Tour"));

        pnlInput.add(new JLabel("Mã Tour:")); txtMaTour = new JTextField(); pnlInput.add(txtMaTour);
        pnlInput.add(new JLabel("Tên Tour:")); txtTenTour = new JTextField(); pnlInput.add(txtTenTour);
        pnlInput.add(new JLabel("Số Ngày:")); txtSoNgay = new JTextField(); pnlInput.add(txtSoNgay);
        pnlInput.add(new JLabel("Số Đêm:")); txtSoDem = new JTextField(); pnlInput.add(txtSoDem);
        pnlInput.add(new JLabel("Đơn Giá:")); txtDonGia = new JTextField(); pnlInput.add(txtDonGia);

        JButton btnThem = new JButton("Thêm Tour");
        pnlInput.add(btnThem);

        add(pnlInput, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Mã Tour", "Tên Tour", "Số Ngày", "Số Đêm", "Đơn Giá"}, 0);
        tblTour = new JTable(tableModel);
        add(new JScrollPane(tblTour), BorderLayout.CENTER);

        napDuLieu();

        btnThem.addActionListener(e -> {
            try {
                Model.KetQuaXuLy kq = tourService.themTour(
                    txtMaTour.getText(), txtTenTour.getText(),
                    Integer.parseInt(txtSoNgay.getText()),
                    Integer.parseInt(txtSoDem.getText()),
                    Double.parseDouble(txtDonGia.getText())
                );
                JOptionPane.showMessageDialog(this, kq.getThongBao());
                if (kq.isThanhCong()) napDuLieu();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Số liệu nhập vào không hợp lệ!");
            }
        });
    }

    private void napDuLieu() {
        tableModel.setRowCount(0);
        List<Model.TourItem> list = tourService.layDanhSachTour();
        for (Model.TourItem i : list) {
            tableModel.addRow(new Object[]{i.getMaTour(), i.getTenTour(), i.getSoNgay(), i.getSoDem(), String.format("%,.0f VNĐ", i.getDonGiaKhach())});
        }
    }
}