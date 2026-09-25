package quanlykhachsan.views;

import quanlykhachsan.services.DichVuService;
import javax.swing.*;
import java.awt.*;

public class FrmDichVu extends JDialog {
    private DichVuService service = new DichVuService();
    private JTable table = new JTable();

    private JTextField txtPhieu = new JTextField("DP001", 8);
    private JTextField txtPhong = new JTextField("A101", 8);
    private JTextField txtDV = new JTextField("DV01", 10);
    private JTextField txtNgay = new JTextField("2026-09-12", 8);
    private JTextField txtSL = new JTextField("2", 5);

    public FrmDichVu(Frame parent) {
        super(parent, "Sử dụng dịch vụ", true);
        setSize(850, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5, 5));

        JPanel pnlInput = new JPanel(new GridLayout(2, 3, 10, 10));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        pnlInput.add(new JLabel("Phiếu lưu trú:")); pnlInput.add(txtPhieu);
        pnlInput.add(new JLabel("Phòng:")); pnlInput.add(txtPhong);
        pnlInput.add(new JLabel("Mã dịch vụ:")); pnlInput.add(txtDV);
        pnlInput.add(new JLabel("Ngày sử dụng:")); pnlInput.add(txtNgay);
        pnlInput.add(new JLabel("Số lượng:")); pnlInput.add(txtSL);

        JButton btnGhi = new JButton("Ghi nhận");
        pnlInput.add(btnGhi);
        add(pnlInput, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTroLai = new JButton("Trở lại");
        pnlBottom.add(btnTroLai);
        add(pnlBottom, BorderLayout.SOUTH);

        btnTroLai.addActionListener(e -> dispose());

        btnGhi.addActionListener(e -> {
            try {
                int sl = Integer.parseInt(txtSL.getText().trim());
                if (service.ghiNhanDichVu(txtPhieu.getText().trim(), txtPhong.getText().trim(), txtDV.getText().trim(), sl, "NV01")) {
                    JOptionPane.showMessageDialog(this, "Ghi nhận thành công! (Số lượng đã tự động cộng dồn nếu trùng ngày)");
                    taiBangDatabase();
                } else {
                    JOptionPane.showMessageDialog(this, "Ghi nhận thất bại! Vui lòng kiểm tra lại thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nạp tất cả dữ liệu thực tế từ SQL Server
        taiBangDatabase();
    }

    private void taiBangDatabase() {
        table.setModel(service.layDanhSachSuDungDV());
    }
}