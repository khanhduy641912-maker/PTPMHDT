package quanlykhachsan.views;

import quanlykhachsan.services.DanhMucService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmDanhMuc extends JDialog {
    private DanhMucService service = new DanhMucService();
    private JTable table = new JTable();
    private JTextField txtMa = new JTextField("DV01", 10), txtTen = new JTextField("Ăn sáng", 15), txtDonVi = new JTextField("Suất", 10);

    public FrmDanhMuc(Frame parent) {
        super(parent, "Danh mục khách sạn", true);
        setSize(800, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5, 5));

        JLabel lblTabs = new JLabel("  [ Khu vực ] [ Nhân viên ] [ Loại tiện nghi ] [ Dịch vụ ] [ Quy định đền bù ]");
        lblTabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel pnlInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlInput.add(new JLabel("Mã:")); pnlInput.add(txtMa);
        pnlInput.add(new JLabel("Tên:")); pnlInput.add(txtTen);
        pnlInput.add(new JLabel("Đơn vị / Vai trò:")); pnlInput.add(txtDonVi);
        JButton btnThem = new JButton("Tìm kiếm");
        pnlInput.add(btnThem);

        JPanel pnlTop = new JPanel(new GridLayout(2, 1));
        pnlTop.add(lblTabs);
        pnlTop.add(pnlInput);
        add(pnlTop, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTroLai = new JButton("Trở lại");
        pnlBottom.add(btnTroLai);
        add(pnlBottom, BorderLayout.SOUTH);

        btnTroLai.addActionListener(e -> dispose());

        btnThem.addActionListener(e -> {
            DefaultTableModel model = service.timKiemDichVu(txtTen.getText().trim());
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy danh mục phù hợp!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            } else {
                table.setModel(model);
            }
        });

        // Tải dữ liệu mặc định từ SQL Server
        table.setModel(service.timKiemDichVu(""));
    }
}