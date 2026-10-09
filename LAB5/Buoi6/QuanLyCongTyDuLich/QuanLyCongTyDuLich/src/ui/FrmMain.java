package ui;

import javax.swing.*;
import java.awt.*;

public class FrmMain extends JFrame {

    public FrmMain() {
        setTitle("CÔNG TY DU LỊCH VĂN HÓA VIỆT");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("CÔNG TY DU LỊCH VĂN HÓA VIỆT", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(20, 50, 120));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new GridLayout(4, 2, 15, 15));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JButton btnTour = new JButton("Tour - Hành trình");
        JButton btnChuyenLe = new JButton("Lịch chuyến khách lẻ");
        JButton btnDangKyLe = new JButton("Đăng ký khách lẻ");
        JButton btnDangKyDoan = new JButton("Đăng ký theo đoàn");
        JButton btnPhanCong = new JButton("Phân công HDV");
        JButton btnKetThuc = new JButton("Kết thúc tour - Khảo sát");
        JButton btnLuong = new JButton("Lương - Thống kê");
        JButton btnThoat = new JButton("Thoát");

        pnlCenter.add(btnTour); pnlCenter.add(btnChuyenLe);
        pnlCenter.add(btnDangKyLe); pnlCenter.add(btnDangKyDoan);
        pnlCenter.add(btnPhanCong); pnlCenter.add(btnKetThuc);
        pnlCenter.add(btnLuong); pnlCenter.add(btnThoat);

        add(pnlCenter, BorderLayout.CENTER);

        // Events
        btnTour.addActionListener(e -> new FrmTour().setVisible(true));
        btnChuyenLe.addActionListener(e -> new FrmChuyenLe().setVisible(true));
        btnDangKyLe.addActionListener(e -> new FrmDangKyLe().setVisible(true));
        btnDangKyDoan.addActionListener(e -> new FrmDangKyDoan().setVisible(true));
        btnPhanCong.addActionListener(e -> new FrmPhanCongHDV().setVisible(true));
        btnKetThuc.addActionListener(e -> new FrmKetThucKhaoSat().setVisible(true));
        btnLuong.addActionListener(e -> new FrmLuongThongKe().setVisible(true));

        btnThoat.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmMain().setVisible(true));
    }
}