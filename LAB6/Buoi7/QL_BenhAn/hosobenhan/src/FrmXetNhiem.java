import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrmXetNhiem extends JFrame {
    private JTextField txtMaXN, txtMaPK, txtTenXN, txtKetQua, txtChiPhi;
    private JTable tableXN;
    private DefaultTableModel model;
    private MainFrame mainFrame;

    public FrmXetNhiem(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setTitle("QUẢN LÝ CHỈ ĐỊNH VÀ KẾT QUẢ XÉT NGHIỆM");
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIStyle.BG_COLOR);
        setLayout(new BorderLayout(15, 15));

        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBackground(UIStyle.PRIMARY_COLOR);
        JLabel lblTitle = new JLabel("  CHỈ ĐỊNH VÀ CẬP NHẬT KẾT QUẢ XÉT NGHIỆM");
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

        pInput.add(new JLabel("Mã Xét Nghiệm:")); txtMaXN = new JTextField(); pInput.add(txtMaXN);
        pInput.add(new JLabel("Mã Phiếu Khám:")); txtMaPK = new JTextField(); pInput.add(txtMaPK);

        pInput.add(new JLabel("Tên Xét Nghiệm:")); txtTenXN = new JTextField("Xét nghiệm máu"); pInput.add(txtTenXN);
        pInput.add(new JLabel("Chi Phí (VNĐ):")); txtChiPhi = new JTextField("150000"); pInput.add(txtChiPhi);

        pInput.add(new JLabel("Kết Quả Xét Nghiệm:")); txtKetQua = new JTextField(); pInput.add(txtKetQua);

        pCenterContainer.add(pInput, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Mã XN", "Mã Phiếu Khám", "Tên Xét Nghiệm", "Kết Quả", "Chi Phí"}, 0);
        tableXN = new JTable(model);
        UIStyle.styleTable(tableXN);

        JScrollPane scrollPane = new JScrollPane(tableXN);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pCenterContainer.add(scrollPane, BorderLayout.CENTER);

        add(pCenterContainer, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pBtn.setBackground(UIStyle.BG_COLOR);

        JButton btnLuu = UIStyle.createButton("Lưu Chỉ Định / KQ", UIStyle.ACCENT_COLOR);
        JButton btnTroLai = UIStyle.createButton("Trở lại Menu", UIStyle.PRIMARY_COLOR);

        pBtn.add(btnLuu); pBtn.add(btnTroLai);
        add(pBtn, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> luuXetNhiem());
        btnTroLai.addActionListener(e -> {
            this.dispose();
            if (mainFrame != null) mainFrame.setVisible(true);
        });

        taiDuLieu();
    }

    private void taiDuLieu() {
        model.setRowCount(0);
        String sql = "SELECT MaXetNhiem, MaPhieuKham, TenXetNhiem, KetQua, ChiPhi FROM XetNhiem";
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5)});
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void luuXetNhiem() {
        String sql = "INSERT INTO XetNhiem(MaXetNhiem, MaPhieuKham, TenXetNhiem, KetQua, ChiPhi) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtMaXN.getText().trim()); ps.setString(2, txtMaPK.getText().trim());
            ps.setString(3, txtTenXN.getText().trim()); ps.setString(4, txtKetQua.getText().trim());
            ps.setDouble(5, Double.parseDouble(txtChiPhi.getText().trim())); ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Đã lưu thông tin xét nghiệm!");
            taiDuLieu();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
    }
}