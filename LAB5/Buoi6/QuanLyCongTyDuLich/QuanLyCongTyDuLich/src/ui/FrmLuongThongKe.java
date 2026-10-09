package ui;

import services.ThongKeService;

import javax.swing.*;
import java.awt.*;

public class FrmLuongThongKe extends JFrame {
    private JTextField txtMaHDV, txtThang, txtNam;
    private JLabel lblKetQua;
    private ThongKeService service = new ThongKeService();

    public FrmLuongThongKe() {
        setTitle("Tính Lương Hướng Dẫn Viên");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JPanel p1 = new JPanel(new FlowLayout()); p1.add(new JLabel("Mã HDV:")); txtMaHDV = new JTextField(10); p1.add(txtMaHDV); add(p1);
        JPanel p2 = new JPanel(new FlowLayout()); p2.add(new JLabel("Tháng:")); txtThang = new JTextField(4); p2.add(txtThang); p2.add(new JLabel("Năm:")); txtNam = new JTextField(6); p2.add(txtNam); add(p2);

        JButton btnTinh = new JButton("Tính Lương"); add(btnTinh);
        lblKetQua = new JLabel("Tổng Lương: 0 VNĐ", JLabel.CENTER);
        lblKetQua.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblKetQua);

        btnTinh.addActionListener(e -> {
            try {
                double luong = service.tinhLuongHDV(txtMaHDV.getText(), Integer.parseInt(txtThang.getText()), Integer.parseInt(txtNam.getText()));
                lblKetQua.setText("Tổng Lương: " + String.format("%,.0f VNĐ", luong));
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Nhập đầy đủ thông tin tháng năm!"); }
        });
    }
}