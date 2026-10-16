import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrmKhamBenh extends JFrame {
    private JTextField txtMaPK, txtMaHoSo, txtTrieuChung, txtKetLuan;
    private JComboBox<String> cboBacSi, cboKhoa, cboLoaiKham;
    private JTable tablePK;
    private DefaultTableModel model;
    private MainFrame mainFrame;

    public FrmKhamBenh(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setTitle("PHÂN HỆ BÁC SĨ - KHÁM BỆNH VÀ CHẨN ĐOÁN");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIStyle.BG_COLOR);
        setLayout(new BorderLayout(15, 15));

        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBackground(UIStyle.PRIMARY_COLOR);
        JLabel lblTitle = new JLabel("  LẬP PHIẾU KHÁM BỆNH VÀ GHI NHẬN TRIỆU CHỨNG LÂM SÀNG");
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

        pInput.add(new JLabel("Mã Phiếu Khám:")); txtMaPK = new JTextField(); pInput.add(txtMaPK);
        pInput.add(new JLabel("Mã Hồ Sơ BN:")); txtMaHoSo = new JTextField(); pInput.add(txtMaHoSo);

        pInput.add(new JLabel("Bác Sĩ Khám:")); cboBacSi = new JComboBox<>(new String[]{"BS01", "BS02", "BS03"}); pInput.add(cboBacSi);
        pInput.add(new JLabel("Khoa Điều Trị:")); cboKhoa = new JComboBox<>(new String[]{"K01", "K02", "K03"}); pInput.add(cboKhoa);

        pInput.add(new JLabel("Loại Khám:")); cboLoaiKham = new JComboBox<>(new String[]{"Khám bệnh", "Khám định kỳ"}); pInput.add(cboLoaiKham);
        pInput.add(new JLabel("Triệu Chứng Lâm Sàng:")); txtTrieuChung = new JTextField(); pInput.add(txtTrieuChung);

        pInput.add(new JLabel("Chẩn Đoán / Kết Luận:")); txtKetLuan = new JTextField(); pInput.add(txtKetLuan);

        pCenterContainer.add(pInput, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Mã PK", "Mã Hồ Sơ", "Bác Sĩ", "Khoa", "Loại Khám", "Triệu Chứng", "Kết Luận", "Trạng Thái"}, 0);
        tablePK = new JTable(model);
        UIStyle.styleTable(tablePK);

        JScrollPane scrollPane = new JScrollPane(tablePK);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pCenterContainer.add(scrollPane, BorderLayout.CENTER);

        add(pCenterContainer, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pBtn.setBackground(UIStyle.BG_COLOR);

        JButton btnTaoPK = UIStyle.createButton("Tạo Phiếu Khám", UIStyle.ACCENT_COLOR);
        JButton btnTroLai = UIStyle.createButton("Trở lại Menu", UIStyle.PRIMARY_COLOR);

        pBtn.add(btnTaoPK); pBtn.add(btnTroLai);
        add(pBtn, BorderLayout.SOUTH);

        btnTaoPK.addActionListener(e -> taoPhieuKham());
        btnTroLai.addActionListener(e -> {
            this.dispose();
            if (mainFrame != null) mainFrame.setVisible(true);
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        model.setRowCount(0);
        String sql = "SELECT MaPhieuKham, MaHoSo, MaBacSi, MaKhoa, LoaiKham, TrieuChung, KetLuanBenh, TrangThaiKham FROM PhieuKhamBenh ORDER BY NgayGioKham DESC";
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)});
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void taoPhieuKham() {
        String sql = "INSERT INTO PhieuKhamBenh(MaPhieuKham, MaHoSo, MaBacSi, MaKhoa, NgayGioKham, LoaiKham, TrieuChung, KetLuanBenh, TrangThaiKham) " +
                     "VALUES(?, ?, ?, ?, GETDATE(), ?, ?, ?, N'Đang khám')";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtMaPK.getText().trim()); ps.setString(2, txtMaHoSo.getText().trim());
            ps.setString(3, cboBacSi.getSelectedItem().toString()); ps.setString(4, cboKhoa.getSelectedItem().toString());
            ps.setString(5, cboLoaiKham.getSelectedItem().toString()); ps.setString(6, txtTrieuChung.getText().trim());
            ps.setString(7, txtKetLuan.getText().trim()); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Tạo Phiếu khám thành công!");
            taiDuLieu();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
    }
}