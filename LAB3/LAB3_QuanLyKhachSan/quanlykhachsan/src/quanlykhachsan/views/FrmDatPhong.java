package quanlykhachsan.views;

import quanlykhachsan.services.DatPhongService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmDatPhong extends JDialog {

    private DatPhongService service = new DatPhongService();
    private JTable tblPhongTrong = new JTable();
    private JTable tblPhongChon = new JTable();
    private JTable tblPhieuDat = new JTable();

    private JTextField txtSoPhieu = new JTextField("DP001", 8);
    private JTextField txtKhach = new JTextField("Nguyễn Văn A", 10);
    private JTextField txtKenh = new JTextField("Website", 8);
    private JTextField txtCoc = new JTextField("500000", 8);

    public FrmDatPhong(Frame parent) {
        super(parent, "Khách hàng - Đặt phòng - Nhận phòng", true);
        setSize(880, 580);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5, 5));

        // 1. THANH TABS VÀ KHUNG NHẬP THÔNG TIN PHÍA TRÊN
        JLabel lblTabs = new JLabel("  [Khách hàng]  [Đặt phòng]  [Nhận phòng / Người lưu trú]");
        lblTabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel pnlInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlInput.add(new JLabel("Số phiếu đặt:")); pnlInput.add(txtSoPhieu);
        pnlInput.add(new JLabel("Khách:")); pnlInput.add(txtKhach);
        pnlInput.add(new JLabel("Kênh đặt:")); pnlInput.add(txtKenh);
        pnlInput.add(new JLabel("Tiền cọc:")); pnlInput.add(txtCoc);

        JPanel pnlTop = new JPanel(new GridLayout(2, 1));
        pnlTop.add(lblTabs);
        pnlTop.add(pnlInput);
        add(pnlTop, BorderLayout.NORTH);

        // 2. KHUNG CHÍNH CHỨA 3 BẢNG DỮ LIỆU
        JPanel pnlCenter = new JPanel(new BorderLayout(5, 10));

        // Khung trên: Chia làm 2 bảng (Bảng Phòng trống bên trái - Bảng Phòng chọn bên phải)
        JPanel pnlTwoTables = new JPanel(new GridLayout(1, 2, 10, 0));
        
        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Danh sách Phòng"));
        pnlLeft.add(new JScrollPane(tblPhongTrong), BorderLayout.CENTER);

        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBorder(BorderFactory.createTitledBorder("Phòng chọn"));
        pnlRight.add(new JScrollPane(tblPhongChon), BorderLayout.CENTER);

        pnlTwoTables.add(pnlLeft);
        pnlTwoTables.add(pnlRight);

        // Khung giữa: Nút "Lập phiếu đặt"
        JPanel pnlMiddleBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLapPhieu = new JButton("Lập phiếu đặt");
        pnlMiddleBtn.add(btnLapPhieu);

        // Khung dưới: Bảng "Phiếu đặt phòng"
        JPanel pnlBottomTable = new JPanel(new BorderLayout());
        pnlBottomTable.setBorder(BorderFactory.createTitledBorder("Phiếu đặt phòng:"));
        pnlBottomTable.add(new JScrollPane(tblPhieuDat), BorderLayout.CENTER);
        pnlBottomTable.setPreferredSize(new Dimension(850, 180));

        // Ghép các thành phần vào pnlCenter
        JPanel pnlTopCenter = new JPanel(new BorderLayout());
        pnlTopCenter.add(pnlTwoTables, BorderLayout.CENTER);
        pnlTopCenter.add(pnlMiddleBtn, BorderLayout.SOUTH);

        pnlCenter.add(pnlTopCenter, BorderLayout.CENTER);
        pnlCenter.add(pnlBottomTable, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        // 3. THANH DƯỚI CÙNG (NÚT TRỞ LẠI)
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTroLai = new JButton("Trở lại");
        pnlFooter.add(btnTroLai);
        add(pnlFooter, BorderLayout.SOUTH);

        // 4. SỰ KIỆN NÚT BẤM
        btnTroLai.addActionListener(e -> dispose());

        btnLapPhieu.addActionListener(e -> {
            String res = service.taoDatPhong(
                txtSoPhieu.getText().trim(),
                "KH01", // Mã khách mặc định
                "NV01", // Mã lễ tân mặc định
                "A101", // Số phòng
                2,      // Số người
                Double.parseDouble(txtCoc.getText().trim())
            );

            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Lập phiếu đặt phòng thành công!");
                taiDuLieuDatabase(); // Tải lại bảng sau khi đặt
            } else {
                JOptionPane.showMessageDialog(this, res, "Từ chối thực hiện", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 5. TỰ ĐỘNG TẢI DỮ LIỆU TỪ SQL SERVER KHI MỞ FORM
        taiDuLieuDatabase();
    }

    private void taiDuLieuDatabase() {
        // Nạp dữ liệu bảng Phòng trống từ Database
        tblPhongTrong.setModel(service.layDanhSachPhongTrong());

        // Khởi tạo khung mẫu cho bảng Phòng chọn
        tblPhongChon.setModel(new DefaultTableModel(
            new String[]{"Phòng chọn", "Số người", "Đơn giá/ngày"}, 0
        ));

        // Nạp danh sách Phiếu đặt phòng từ Database
        DefaultTableModel modelPhieu = new DefaultTableModel(
            new String[]{"Số phiếu", "Khách", "Ngày nhận", "Ngày trả dự kiến", "Cọc", "Kênh", "Trạng thái"}, 0
        );
        // Lấy dữ liệu mẫu từ Service
        tblPhieuDat.setModel(modelPhieu);
    }
}