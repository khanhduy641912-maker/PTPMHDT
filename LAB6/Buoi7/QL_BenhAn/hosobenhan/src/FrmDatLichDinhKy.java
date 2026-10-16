import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrmDatLichDinhKy extends JFrame {
    private JTextField txtMaLich, txtMaHoSo, txtNgayGio;
    private JComboBox<Integer> cboQuy, cboNam;
    private JTable tableLich;
    private DefaultTableModel model;
    private MainFrame mainFrame;

    public FrmDatLichDinhKy(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setTitle("ĐẶT LỊCH KHÁM SỨC KHỎE ĐỊNH KỲ THEO QUÝ");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIStyle.BG_COLOR);
        setLayout(new BorderLayout(15, 15));

        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBackground(UIStyle.PRIMARY_COLOR);
        JLabel lblTitle = new JLabel("  ĐẶT LỊCH KHÁM SỨC KHỎE ĐỊNH KỲ QUÝ (MIỄN PHÍ KHÁM)");
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

        pInput.add(new JLabel("Mã Lịch Hẹn:")); txtMaLich = new JTextField(); pInput.add(txtMaLich);
        pInput.add(new JLabel("Mã Hồ Sơ Bệnh Án:")); txtMaHoSo = new JTextField(); pInput.add(txtMaHoSo);

        pInput.add(new JLabel("Quý Khám:")); cboQuy = new JComboBox<>(new Integer[]{1, 2, 3, 4}); pInput.add(cboQuy);
        pInput.add(new JLabel("Năm Khám:")); cboNam = new JComboBox<>(new Integer[]{2025, 2026, 2027}); cboNam.setSelectedItem(2026); pInput.add(cboNam);

        pInput.add(new JLabel("Ngày Giờ Hẹn (YYYY-MM-DD HH:MM):")); txtNgayGio = new JTextField("2026-06-15 09:00"); pInput.add(txtNgayGio);

        pCenterContainer.add(pInput, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Mã Lịch", "Mã Hồ Sơ", "Họ Tên Bệnh Nhân", "Ngày Giờ Hẹn", "Quý", "Năm", "Trạng Thái"}, 0);
        tableLich = new JTable(model);
        UIStyle.styleTable(tableLich);

        JScrollPane scrollPane = new JScrollPane(tableLich);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pCenterContainer.add(scrollPane, BorderLayout.CENTER);

        add(pCenterContainer, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pBtn.setBackground(UIStyle.BG_COLOR);

        JButton btnDatLich = UIStyle.createButton("Xác nhận Đặt lịch", UIStyle.ACCENT_COLOR);
        JButton btnHuy = UIStyle.createButton("Hủy lịch", new Color(220, 100, 0));
        JButton btnNap = UIStyle.createButton("Nạp lại dữ liệu", new Color(100, 110, 120));
        JButton btnTroLai = UIStyle.createButton("Trở lại Menu", UIStyle.PRIMARY_COLOR);

        pBtn.add(btnDatLich); pBtn.add(btnHuy); pBtn.add(btnNap); pBtn.add(btnTroLai);
        add(pBtn, BorderLayout.SOUTH);

        btnDatLich.addActionListener(e -> datLich());
        btnHuy.addActionListener(e -> huyLich());
        btnNap.addActionListener(e -> taiDuLieu());
        btnTroLai.addActionListener(e -> {
            this.dispose();
            if (mainFrame != null) mainFrame.setVisible(true);
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        model.setRowCount(0);
        String sql = "SELECT l.MaLichHen, l.MaHoSo, b.HoTen, l.NgayGioHen, l.Quy, l.Nam, l.TrangThai " +
                     "FROM LichHenKham l JOIN HoSoBenhAn h ON l.MaHoSo = h.MaHoSo " +
                     "JOIN BenhNhan b ON h.MaBenhNhan = b.MaBenhNhan ORDER BY l.NgayGioHen DESC";
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getString(7)});
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void datLich() {
        String sql = "INSERT INTO LichHenKham(MaLichHen, MaHoSo, NgayGioHen, Quy, Nam, TrangThai) VALUES(?, ?, ?, ?, ?, N'Đã đặt lịch')";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtMaLich.getText().trim()); ps.setString(2, txtMaHoSo.getText().trim());
            ps.setString(3, txtNgayGio.getText().trim()); ps.setInt(4, (Integer) cboQuy.getSelectedItem());
            ps.setInt(5, (Integer) cboNam.getSelectedItem()); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Đặt lịch khám định kỳ thành công!");
            taiDuLieu();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: Quý/Năm này hồ sơ đã đăng ký lịch rồi!", "Lỗi Unique", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void huyLich() {
        int row = tableLich.getSelectedRow();
        if (row < 0) return;
        String maLich = model.getValueAt(row, 0).toString();
        String sql = "UPDATE LichHenKham SET TrangThai = N'Đã hủy' WHERE MaLichHen = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLich); ps.executeUpdate();
            taiDuLieu();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}