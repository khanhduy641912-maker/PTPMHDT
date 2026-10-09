package ui;

import services.Model;
import services.PhanCongService;

import javax.swing.*;
import java.awt.*;

public class FrmPhanCongHDV extends JFrame {
    private JTextField txtMaPC, txtMaHDV, txtTargetId, txtBD, txtKT, txtThuLao;
    private JComboBox<String> cboLoai;
    private PhanCongService service = new PhanCongService();

    public FrmPhanCongHDV() {
        setTitle("Phân Công Hướng Dẫn Viên");
        setSize(500, 380);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 10, 10));

        add(new JLabel("Mã Phân Công:")); txtMaPC = new JTextField(); add(txtMaPC);
        add(new JLabel("Mã HDV:")); txtMaHDV = new JTextField(); add(txtMaHDV);
        add(new JLabel("Loại (LE/DOAN):")); cboLoai = new JComboBox<>(new String[]{"LE", "DOAN"}); add(cboLoai);
        add(new JLabel("Mã Chuyến / Số DK Đoàn:")); txtTargetId = new JTextField(); add(txtTargetId);
        add(new JLabel("Ngày Bắt Đầu (YYYY-MM-DD):")); txtBD = new JTextField(); add(txtBD);
        add(new JLabel("Ngày Kết Thúc (YYYY-MM-DD):")); txtKT = new JTextField(); add(txtKT);
        add(new JLabel("Thù Lao Tour:")); txtThuLao = new JTextField(); add(txtThuLao);

        JButton btnPhanCong = new JButton("Phân Công");
        add(btnPhanCong);

        btnPhanCong.addActionListener(e -> {
            try {
                Model.KetQuaXuLy kq = service.phanCong(txtMaPC.getText(), txtMaHDV.getText(), cboLoai.getSelectedItem().toString(), txtTargetId.getText(), txtBD.getText(), txtKT.getText(), Double.parseDouble(txtThuLao.getText()));
                JOptionPane.showMessageDialog(this, kq.getThongBao());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Số liệu nhập vào không hợp lệ!"); }
        });
    }
}