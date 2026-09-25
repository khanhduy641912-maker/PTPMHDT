package quanlykhachsan.views;

import quanlykhachsan.services.PhongTienNghiService;
import quanlykhachsan.services.DanhMucService;
import javax.swing.*;
import java.awt.*;

public class FrmPhongTienNghi extends JDialog {
    private PhongTienNghiService service = new PhongTienNghiService();
    private DanhMucService dmService = new DanhMucService();
    private JTable table = new JTable();

    private JTextField txtPhong = new JTextField("A101", 8);
    private JTextField txtKhu = new JTextField("Khu A", 8);
    private JTextField txtMax = new JTextField("2", 5);
    private JTextField txtGia = new JTextField("600000", 8);

    private JTextField txtSoLD = new JTextField("LD001", 8);
    private JTextField txtTN = new JTextField("TV01", 8);
    private JTextField txtPhongLD = new JTextField("A101", 8);
    private JTextField txtTT = new JTextField("Tốt", 8);

    public FrmPhongTienNghi(Frame parent) {
        super(parent, "Phòng - Tiện nghi - Phiếu lắp đặt", true);
        setSize(850, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5, 5));

        JLabel lblTabs = new JLabel("  [Phòng]  [Tiện nghi]  [Lắp đặt / luân chuyển]");
        lblTabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel pnlInputTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlInputTop.add(new JLabel("Số phòng:")); pnlInputTop.add(txtPhong);
        pnlInputTop.add(new JLabel("Khu vực:")); pnlInputTop.add(txtKhu);
        pnlInputTop.add(new JLabel("Số người tối đa:")); pnlInputTop.add(txtMax);
        pnlInputTop.add(new JLabel("Đơn giá/ngày:")); pnlInputTop.add(txtGia);

        JPanel pnlTop = new JPanel(new GridLayout(2, 1));
        pnlTop.add(lblTabs);
        pnlTop.add(pnlInputTop);
        add(pnlTop, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new BorderLayout());
        JPanel pnlLapDat = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlLapDat.add(new JLabel("Phiếu lắp đặt:")); pnlLapDat.add(txtSoLD);
        pnlLapDat.add(new JLabel("Tiện nghi:")); pnlLapDat.add(txtTN);
        pnlLapDat.add(new JLabel("Phòng:")); pnlLapDat.add(txtPhongLD);
        pnlLapDat.add(new JLabel("Tình trạng:")); pnlLapDat.add(txtTT);

        JButton btnLapPhieu = new JButton("Lập phiếu");
        JButton btnTroLai = new JButton("Trở lại");

        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtns.add(btnLapPhieu);
        pnlBtns.add(btnTroLai);

        pnlBottom.add(pnlLapDat, BorderLayout.WEST);
        pnlBottom.add(pnlBtns, BorderLayout.SOUTH);
        add(pnlBottom, BorderLayout.SOUTH);

        btnTroLai.addActionListener(e -> dispose());

        btnLapPhieu.addActionListener(e -> {
            String res = service.lapPhieuLapDat(txtSoLD.getText(), txtTN.getText(), txtPhongLD.getText(), "NV02");
            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Lập phiếu lắp đặt thành công!");
            } else {
                JOptionPane.showMessageDialog(this, res, "Từ chối thực hiện", JOptionPane.ERROR_MESSAGE);
            }
        });

        table.setModel(dmService.layDanhSachPhong());
    }
}