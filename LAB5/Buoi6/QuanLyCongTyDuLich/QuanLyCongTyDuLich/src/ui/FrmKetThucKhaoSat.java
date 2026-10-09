package ui;

import services.KetThucService;
import services.Model;

import javax.swing.*;
import java.awt.*;

public class FrmKetThucKhaoSat extends JFrame {
    private JTextField txtSoTT, txtSoDK, txtSoTien, txtGhiChu;
    private KetThucService service = new KetThucService();

    public FrmKetThucKhaoSat() {
        setTitle("Thanh Toán Sau Tour & Khảo Sát");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Số Thanh Toán:")); txtSoTT = new JTextField(); add(txtSoTT);
        add(new JLabel("Số Phiếu Đoàn:")); txtSoDK = new JTextField(); add(txtSoDK);
        add(new JLabel("Số Tiền Thanh Toán:")); txtSoTien = new JTextField(); add(txtSoTien);
        add(new JLabel("Ghi Chú:")); txtGhiChu = new JTextField(); add(txtGhiChu);

        JButton btnThanhToan = new JButton("Ghi Nhận Thanh Toán");
        add(btnThanhToan);

        btnThanhToan.addActionListener(e -> {
            try {
                Model.KetQuaXuLy kq = service.thanhToanDoan(txtSoTT.getText(), txtSoDK.getText(), Double.parseDouble(txtSoTien.getText()), txtGhiChu.getText());
                JOptionPane.showMessageDialog(this, kq.getThongBao());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Nhập số tiền hợp lệ!"); }
        });
    }
}