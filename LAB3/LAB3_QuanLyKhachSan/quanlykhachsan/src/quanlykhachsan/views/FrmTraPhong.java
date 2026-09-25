package quanlykhachsan.views;

import quanlykhachsan.services.TraPhongService;
import javax.swing.*;
import java.awt.*;

public class FrmTraPhong extends JDialog {
    private TraPhongService service = new TraPhongService();
    private JTable tblPhong = new JTable();
    private JTable tblTN = new JTable();
    private JTable tblTNDenBu = new JTable();
    private JTable tblHoaDon = new JTable();

    private JTextField txtPhieu = new JTextField("DP001", 8);
    private JTextField txtSoDB = new JTextField("DB001", 8);
    private JTextField txtMucDo = new JTextField("Hư hỏng nhẹ", 8);
    private JTextField txtSoTienDB = new JTextField("500000", 8);

    private JTextField txtSoHD = new JTextField("HD001", 8);
    private JTextField txtSoNgay = new JTextField("2", 5);

    private JTextField txtHT = new JTextField("Thẻ", 8);
    private JTextField txtTienTT = new JTextField("1200000", 8);

    public FrmTraPhong(Frame parent) {
        super(parent, "Trả phòng - Đền bù - Hóa đơn - Thanh toán", true);
        setSize(950, 620);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5, 5));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTop.add(new JLabel("Phiếu đang ở:")); pnlTop.add(txtPhieu);
        add(pnlTop, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel pnlTopTables = new JPanel(new GridLayout(1, 3, 5, 0));
        pnlTopTables.add(new JScrollPane(tblPhong));
        pnlTopTables.add(new JScrollPane(tblTN));
        pnlTopTables.add(new JScrollPane(tblTNDenBu));

        JPanel pnlMiddleControls = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel pnlLine1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLine1.add(new JLabel("Số phiếu đền bù:")); pnlLine1.add(txtSoDB);
        pnlLine1.add(new JLabel("Mức độ:")); pnlLine1.add(txtMucDo);
        pnlLine1.add(new JLabel("Số tiền:")); pnlLine1.add(txtSoTienDB);
        JButton btnLapDB = new JButton("Lập phiếu đền bù");
        pnlLine1.add(btnLapDB);

        JPanel pnlLine2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLine2.add(new JLabel("Số hóa đơn:")); pnlLine2.add(txtSoHD);
        pnlLine2.add(new JLabel("Số ngày tính tiền:")); pnlLine2.add(txtSoNgay);
        JButton btnLapHD = new JButton("Lập hóa đơn");
        pnlLine2.add(btnLapHD);

        pnlMiddleControls.add(pnlLine1);
        pnlMiddleControls.add(pnlLine2);

        JPanel pnlUpper = new JPanel(new BorderLayout());
        pnlUpper.add(pnlTopTables, BorderLayout.CENTER);
        pnlUpper.add(pnlMiddleControls, BorderLayout.SOUTH);

        JPanel pnlLower = new JPanel(new BorderLayout());
        pnlLower.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        JPanel pnlBottomControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBottomControls.add(new JLabel("Hình thức:")); pnlBottomControls.add(txtHT);
        pnlBottomControls.add(new JLabel("Số tiền:")); pnlBottomControls.add(txtTienTT);
        JButton btnThanhToan = new JButton("Thanh toán");
        JButton btnHoanTat = new JButton("Hoàn tất trả phòng");
        JButton btnTroLai = new JButton("Trở lại");

        pnlBottomControls.add(btnThanhToan);
        pnlBottomControls.add(btnHoanTat);
        pnlBottomControls.add(btnTroLai);
        pnlLower.add(pnlBottomControls, BorderLayout.SOUTH);

        pnlCenter.add(pnlUpper);
        pnlCenter.add(pnlLower);
        add(pnlCenter, BorderLayout.CENTER);

        btnTroLai.addActionListener(e -> dispose());

        btnLapHD.addActionListener(e -> {
            int songay = Integer.parseInt(txtSoNgay.getText());
            if (service.lapHoaDon(txtSoHD.getText(), txtPhieu.getText(), songay, "NV03")) {
                JOptionPane.showMessageDialog(this, "Lập hóa đơn thành công!");
                taiDuLieuTuDatabase();
            }
        });

        btnHoanTat.addActionListener(e -> {
            String res = service.hoanTatTraPhong(txtPhieu.getText());
            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Hoàn tất trả phòng thành công!");
                taiDuLieuTuDatabase();
            } else {
                JOptionPane.showMessageDialog(this, res, "Từ chối", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nạp tất cả bảng từ CSDL SQL Server
        taiDuLieuTuDatabase();
    }

    private void taiDuLieuTuDatabase() {
        tblPhong.setModel(service.layDanhSachPhongDangO());
        tblTN.setModel(service.layDanhSachTienNghi());
        tblTNDenBu.setModel(service.layDanhSachTienNghiDenBu());
        tblHoaDon.setModel(service.layDanhSachHoaDon());
    }
}