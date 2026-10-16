import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("HỆ THỐNG QUẢN LÝ HỒ SƠ BỆNH ÁN BỆNH NHÂN");
        setSize(700, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.BG_COLOR);
        setLayout(new BorderLayout());

        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(UIStyle.PRIMARY_COLOR);
        pHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("BỆNH VIỆN ĐA KHOA QUỐC TẾ", SwingConstants.CENTER);
        lblTitle.setFont(UIStyle.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Hệ thống Quản lý Hồ sơ Bệnh án & Khám chữa bệnh", SwingConstants.CENTER);
        lblSub.setFont(UIStyle.FONT_BODY);
        lblSub.setForeground(new Color(220, 240, 255));

        pHeader.add(lblTitle, BorderLayout.CENTER);
        pHeader.add(lblSub, BorderLayout.SOUTH);
        add(pHeader, BorderLayout.NORTH);

        JPanel pCenter = new JPanel(new GridLayout(6, 1, 10, 10));
        pCenter.setBackground(UIStyle.BG_COLOR);
        pCenter.setBorder(new EmptyBorder(20, 50, 20, 50));

        String[] btnLabels = {
            "1. Đăng ký Bệnh nhân & Cấp Hồ sơ (Lệ phí năm)",
            "2. Đặt lịch Khám sức khỏe Định kỳ Quý",
            "3. Khám bệnh, Tra cứu & Ghi nhận Triệu chứng",
            "4. Chỉ định & Nhập Kết quả Xét nghiệm",
            "5. Kê Toa Thuốc Điều Trị",
            "6. Thanh toán Viện phí & Chốt Hoàn tất"
        };

        for (int i = 0; i < btnLabels.length; i++) {
            int index = i + 1;
            JButton btn = UIStyle.createButton(btnLabels[i], UIStyle.ACCENT_COLOR);
            btn.addActionListener(e -> moModule(index));
            pCenter.add(btn);
        }

        add(pCenter, BorderLayout.CENTER);

        JPanel pFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pFooter.setBackground(UIStyle.BG_COLOR);
        pFooter.setBorder(new EmptyBorder(0, 0, 10, 20));

        JButton btnThoat = UIStyle.createButton("Thoát Hệ Thống", new Color(204, 51, 0));
        btnThoat.addActionListener(e -> System.exit(0));
        pFooter.add(btnThoat);

        add(pFooter, BorderLayout.SOUTH);
    }

    private void moModule(int index) {
        this.setVisible(false);
        try {
            switch (index) {
                case 1: new FrmDangKyHoSo(this).setVisible(true); break;
                case 2: new FrmDatLichDinhKy(this).setVisible(true); break;
                case 3: new FrmKhamBenh(this).setVisible(true); break;
                case 4: new FrmXetNhiem(this).setVisible(true); break;
                case 5: new FrmLapToaThuoc(this).setVisible(true); break;
                case 6: new FrmThanhToanVienPhi(this).setVisible(true); break;
            }
        } catch (Throwable ex) {
            this.setVisible(true);
            JOptionPane.showMessageDialog(this, "Lỗi khởi chạy giao diện: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}