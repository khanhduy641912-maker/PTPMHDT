import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class FrmDangKyHoSo extends JFrame {
    private JTextField txtMaBN, txtHoTen, txtNgaySinh, txtDiaChi, txtSDT, txtMaHS, txtMaLP, txtLePhi;
    private JTable tableHoSo;
    private DefaultTableModel tableModel;
    private MainFrame mainFrame;

    public FrmDangKyHoSo(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setTitle("ĐĂNG KÝ BỆNH NHÂN VÀ CẤP HỒ SƠ BỆNH ÁN NĂM");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIStyle.BG_COLOR);
        setLayout(new BorderLayout(15, 15));

        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBackground(UIStyle.PRIMARY_COLOR);
        JLabel lblTitle = new JLabel("  TIẾP NHẬN BỆNH NHÂN NĂM ĐẦU & CẤP HỒ SƠ BỆNH ÁN");
        lblTitle.setFont(UIStyle.FONT_HEADER);
        lblTitle.setForeground(Color.WHITE);
        pTop.add(lblTitle);
        add(pTop, BorderLayout.NORTH);

        JPanel pCenterContainer = new JPanel(new BorderLayout(10, 15));
        pCenterContainer.setBackground(UIStyle.BG_COLOR);
        pCenterContainer.setBorder(new EmptyBorder(0, 15, 0, 15));

        JPanel pInput = new JPanel(new GridLayout(4, 4, 10, 10));
        pInput.setBackground(UIStyle.CARD_BG);
        pInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)),
            new EmptyBorder(12, 15, 12, 15)
        ));

        pInput.add(new JLabel("Mã Bệnh Nhân:")); txtMaBN = new JTextField(); pInput.add(txtMaBN);
        pInput.add(new JLabel("Mã Hồ Sơ Bệnh Án:")); txtMaHS = new JTextField(); pInput.add(txtMaHS);

        pInput.add(new JLabel("Họ và Tên:")); txtHoTen = new JTextField(); pInput.add(txtHoTen);
        pInput.add(new JLabel("Mã Lệ Phí Năm:")); txtMaLP = new JTextField(); pInput.add(txtMaLP);

        pInput.add(new JLabel("Ngày Sinh (YYYY-MM-DD):")); txtNgaySinh = new JTextField("1995-05-20"); pInput.add(txtNgaySinh);
        pInput.add(new JLabel("Số Tiền Lệ Phí (VNĐ):")); txtLePhi = new JTextField("500000"); pInput.add(txtLePhi);

        pInput.add(new JLabel("Số Điện Thoại:")); txtSDT = new JTextField(); pInput.add(txtSDT);
        pInput.add(new JLabel("Địa Chỉ:")); txtDiaChi = new JTextField(); pInput.add(txtDiaChi);

        pCenterContainer.add(pInput, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{
            "Mã Hồ Sơ", "Họ Tên Bệnh Nhân", "Ngày Sinh", "Số Điện Thoại", "Ngày Lập", "Hạn Lệ Phí", "Trạng Thái"
        }, 0);
        tableHoSo = new JTable(tableModel);
        UIStyle.styleTable(tableHoSo);

        JScrollPane scrollPane = new JScrollPane(tableHoSo);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pCenterContainer.add(scrollPane, BorderLayout.CENTER);

        add(pCenterContainer, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pBtn.setBackground(UIStyle.BG_COLOR);

        JButton btnDangKy = UIStyle.createButton("Đăng ký & Cấp Hồ sơ", UIStyle.ACCENT_COLOR);
        JButton btnLamMoi = UIStyle.createButton("Nạp lại dữ liệu", new Color(100, 110, 120));
        JButton btnTroLai = UIStyle.createButton("Trở lại Menu", UIStyle.PRIMARY_COLOR);

        pBtn.add(btnDangKy); pBtn.add(btnLamMoi); pBtn.add(btnTroLai);
        add(pBtn, BorderLayout.SOUTH);

        btnDangKy.addActionListener(e -> xuLyDangKy());
        btnLamMoi.addActionListener(e -> taiDuLieu());
        btnTroLai.addActionListener(e -> {
            this.dispose();
            if (mainFrame != null) mainFrame.setVisible(true);
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        tableModel.setRowCount(0);
        String sql = "SELECT h.MaHoSo, b.HoTen, b.NgaySinh, b.SoDienThoai, h.NgayLap, h.NgayHetHanLePhi, h.TrangThai " +
                     "FROM HoSoBenhAn h JOIN BenhNhan b ON h.MaBenhNhan = b.MaBenhNhan ORDER BY h.NgayLap DESC";

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString(1), rs.getString(2), rs.getDate(3), rs.getString(4), rs.getDate(5), rs.getDate(6), rs.getString(7)
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void xuLyDangKy() {
        String maBN = txtMaBN.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String ngaySinh = txtNgaySinh.getText().trim();
        String sdt = txtSDT.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String maHS = txtMaHS.getText().trim();
        String maLP = txtMaLP.getText().trim();
        String strLePhi = txtLePhi.getText().trim();

        if (maBN.isEmpty() || hoTen.isEmpty() || maHS.isEmpty() || maLP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các thông tin bắt buộc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            String sqlBN = "INSERT INTO BenhNhan(MaBenhNhan, HoTen, NgaySinh, DiaChi, SoDienThoai) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlBN)) {
                ps.setString(1, maBN); ps.setString(2, hoTen); ps.setDate(3, Date.valueOf(ngaySinh));
                ps.setString(4, diaChi); ps.setString(5, sdt); ps.executeUpdate();
            }

            LocalDate homNay = LocalDate.now();
            LocalDate hetHan = homNay.plusYears(1);
            String sqlHS = "INSERT INTO HoSoBenhAn(MaHoSo, MaBenhNhan, NgayLap, NgayHetHanLePhi, TrangThai) VALUES(?, ?, ?, ?, N'Đang hoạt động')";
            try (PreparedStatement ps = conn.prepareStatement(sqlHS)) {
                ps.setString(1, maHS); ps.setString(2, maBN); ps.setDate(3, Date.valueOf(homNay));
                ps.setDate(4, Date.valueOf(hetHan)); ps.executeUpdate();
            }

            String sqlLP = "INSERT INTO LePhiNam(MaLePhi, MaHoSo, Nam, SoTien, NgayDong) VALUES(?, ?, ?, ?, GETDATE())";
            try (PreparedStatement ps = conn.prepareStatement(sqlLP)) {
                ps.setString(1, maLP); ps.setString(2, maHS); ps.setInt(3, homNay.getYear());
                ps.setDouble(4, Double.parseDouble(strLePhi)); ps.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Đã cấp Hồ sơ bệnh án: " + maHS, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            taiDuLieu();
        } catch (Exception ex) {
            try { if (conn != null) conn.rollback(); } catch (Exception e) {}
            JOptionPane.showMessageDialog(this, "Lỗi Transaction: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}