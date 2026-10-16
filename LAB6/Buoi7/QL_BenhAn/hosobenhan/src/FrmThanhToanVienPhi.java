import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrmThanhToanVienPhi extends JFrame {
    private JTextField txtMaHD, txtMaPK, txtTienKham, txtTienXN, txtTienThuoc;
    private JTable tableHD;
    private DefaultTableModel model;
    private MainFrame mainFrame;

    public FrmThanhToanVienPhi(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setTitle("THANH TOÁN VIỆN PHÍ & CHỐT HOÀN TẤT ĐỢT ĐIỀU TRỊ");
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIStyle.BG_COLOR);
        setLayout(new BorderLayout(15, 15));

        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBackground(UIStyle.PRIMARY_COLOR);
        JLabel lblTitle = new JLabel("  TÍNH VIỆN PHÍ VÀ GHI NHẬN HOÀN TẤT ĐỢT ĐIỀU TRỊ KHÁM");
        lblTitle.setFont(UIStyle.FONT_HEADER);
        lblTitle.setForeground(Color.WHITE);
        pTop.add(lblTitle);
        add(pTop, BorderLayout.NORTH);

        JPanel pCenterContainer = new JPanel(new BorderLayout(10, 15));
        pCenterContainer.setBackground(UIStyle.BG_COLOR);
        pCenterContainer.setBorder(new EmptyBorder(0, 15, 0, 15));

        JPanel pInput = new JPanel(new GridLayout(3, 4, 10, 10));
        pInput.setBackground(UIStyle.CARD_BG);
        pInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(12, 15, 12, 15)
        ));

        pInput.add(new JLabel("Mã Hóa Đơn:")); txtMaHD = new JTextField(); pInput.add(txtMaHD);
        pInput.add(new JLabel("Mã Phiếu Khám:")); txtMaPK = new JTextField(); pInput.add(txtMaPK);

        pInput.add(new JLabel("Tiền Khám (VNĐ):")); txtTienKham = new JTextField("0"); pInput.add(txtTienKham);
        pInput.add(new JLabel("Tiền Xét Nghiệm:")); txtTienXN = new JTextField("150000"); pInput.add(txtTienXN);

        pInput.add(new JLabel("Tiền Thuốc:")); txtTienThuoc = new JTextField("92500"); pInput.add(txtTienThuoc);

        pCenterContainer.add(pInput, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Mã Hóa Đơn", "Mã PK", "Ngày Thanh Toán", "Tiền XN", "Tiền Thuốc", "Tổng Viện Phí"}, 0);
        tableHD = new JTable(model);
        UIStyle.styleTable(tableHD);

        JScrollPane scrollPane = new JScrollPane(tableHD);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pCenterContainer.add(scrollPane, BorderLayout.CENTER);

        add(pCenterContainer, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pBtn.setBackground(UIStyle.BG_COLOR);

        JButton btnThanhToan = UIStyle.createButton("Thanh Toán & Hoàn Tất", UIStyle.ACCENT_COLOR);
        JButton btnTroLai = UIStyle.createButton("Trở lại Menu", UIStyle.PRIMARY_COLOR);

        pBtn.add(btnThanhToan); pBtn.add(btnTroLai);
        add(pBtn, BorderLayout.SOUTH);

        btnThanhToan.addActionListener(e -> thanhToan());
        btnTroLai.addActionListener(e -> {
            this.dispose();
            if (mainFrame != null) mainFrame.setVisible(true);
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        model.setRowCount(0);
        String sql = "SELECT MaHoaDon, MaPhieuKham, NgayThanhToan, TienXetNhiem, TienThuoc, TongTien FROM HoaDonVienPhi";
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6)});
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void thanhToan() {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            String sqlHD = "INSERT INTO HoaDonVienPhi(MaHoaDon, MaPhieuKham, NgayThanhToan, TienKham, TienXetNhiem, TienThuoc) VALUES(?, ?, GETDATE(), ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlHD)) {
                ps.setString(1, txtMaHD.getText().trim());
                ps.setString(2, txtMaPK.getText().trim());
                ps.setDouble(3, Double.parseDouble(txtTienKham.getText().trim()));
                ps.setDouble(4, Double.parseDouble(txtTienXN.getText().trim()));
                ps.setDouble(5, Double.parseDouble(txtTienThuoc.getText().trim()));
                ps.executeUpdate();
            }

            String sqlPK = "UPDATE PhieuKhamBenh SET TrangThaiThanhToan = 1, TrangThaiKham = N'Hoàn tất' WHERE MaPhieuKham = ?";
            try (PreparedStatement psPK = conn.prepareStatement(sqlPK)) {
                psPK.setString(1, txtMaPK.getText().trim());
                psPK.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Đã thanh toán viện phí và chốt hoàn tất đợt điều trị!");
            taiDuLieu();
        } catch (Exception ex) {
            try { if (conn != null) conn.rollback(); } catch (Exception e) {}
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
}