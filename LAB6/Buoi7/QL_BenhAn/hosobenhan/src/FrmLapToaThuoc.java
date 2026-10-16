import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FrmLapToaThuoc extends JFrame {
    private JTextField txtMaToa, txtMaPK, txtLieuDung, txtSoLuong;
    private JComboBox<String> cboThuoc;
    private JTable tableCT;
    private DefaultTableModel model;
    private MainFrame mainFrame;

    public FrmLapToaThuoc(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setTitle("LẬP TOA THUỐC ĐIỀU TRỊ BH/ĐỊNH KỲ");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIStyle.BG_COLOR);
        setLayout(new BorderLayout(15, 15));

        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBackground(UIStyle.PRIMARY_COLOR);
        JLabel lblTitle = new JLabel("  KÊ TOA THUỐC ĐIỀU TRỊ TRIỆU CHỨNG LÂM SÀNG");
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

        pInput.add(new JLabel("Mã Toa Thuốc:")); txtMaToa = new JTextField(); pInput.add(txtMaToa);
        pInput.add(new JLabel("Mã Phiếu Khám:")); txtMaPK = new JTextField(); pInput.add(txtMaPK);

        pInput.add(new JLabel("Chọn Thuốc:")); cboThuoc = new JComboBox<>(new String[]{"T01", "T02", "T03", "T04"}); pInput.add(cboThuoc);
        pInput.add(new JLabel("Số Lượng:")); txtSoLuong = new JTextField("10"); pInput.add(txtSoLuong);

        pInput.add(new JLabel("Liều Dùng / Cách Dùng:")); txtLieuDung = new JTextField("Sáng 1v, Tối 1v"); pInput.add(txtLieuDung);

        pCenterContainer.add(pInput, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Mã Thuốc", "Tên Thuốc", "Số Lượng", "Liều Dùng", "Đơn Giá"}, 0);
        tableCT = new JTable(model);
        UIStyle.styleTable(tableCT);

        JScrollPane scrollPane = new JScrollPane(tableCT);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pCenterContainer.add(scrollPane, BorderLayout.CENTER);

        add(pCenterContainer, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pBtn.setBackground(UIStyle.BG_COLOR);

        JButton btnThemThuoc = UIStyle.createButton("Thêm Thuốc Về Dòng", new Color(0, 153, 102));
        JButton btnLuuToa = UIStyle.createButton("Lưu Toa Thuốc", UIStyle.ACCENT_COLOR);
        JButton btnTroLai = UIStyle.createButton("Trở lại Menu", UIStyle.PRIMARY_COLOR);

        pBtn.add(btnThemThuoc); pBtn.add(btnLuuToa); pBtn.add(btnTroLai);
        add(pBtn, BorderLayout.SOUTH);

        btnThemThuoc.addActionListener(e -> {
            model.addRow(new Object[]{cboThuoc.getSelectedItem(), "Thuốc " + cboThuoc.getSelectedItem(), txtSoLuong.getText(), txtLieuDung.getText(), "2000"});
        });

        btnLuuToa.addActionListener(e -> luuToaThuoc());
        btnTroLai.addActionListener(e -> {
            this.dispose();
            if (mainFrame != null) mainFrame.setVisible(true);
        });
    }

    private void luuToaThuoc() {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            String sqlToa = "INSERT INTO ToaThuoc(MaToa, MaPhieuKham, NgayLap) VALUES(?, ?, GETDATE())";
            try (PreparedStatement ps = conn.prepareStatement(sqlToa)) {
                ps.setString(1, txtMaToa.getText().trim());
                ps.setString(2, txtMaPK.getText().trim());
                ps.executeUpdate();
            }

            String sqlCT = "INSERT INTO ChiTietToaThuoc(MaToa, MaThuoc, SoLuong, LieuDung, DonGia) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement psCT = conn.prepareStatement(sqlCT)) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    psCT.setString(1, txtMaToa.getText().trim());
                    psCT.setString(2, model.getValueAt(i, 0).toString());
                    psCT.setInt(3, Integer.parseInt(model.getValueAt(i, 2).toString()));
                    psCT.setString(4, model.getValueAt(i, 3).toString());
                    psCT.setDouble(5, Double.parseDouble(model.getValueAt(i, 4).toString()));
                    psCT.addBatch();
                }
                psCT.executeBatch();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Đã lưu toa thuốc thành công trong Transaction!");
        } catch (Exception ex) {
            try { if (conn != null) conn.rollback(); } catch (Exception e) {}
            JOptionPane.showMessageDialog(this, "Lỗi kê toa: " + ex.getMessage());
        }
    }
}