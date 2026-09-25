package quanlykhachsan.views;

import javax.swing.*;
import java.awt.*;

public class FrmMain extends JFrame {

    public FrmMain() {
        setTitle("Quản lý khách sạn");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 20));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ KHÁCH SẠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(10, 45, 110));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlGrid = new JPanel(new GridLayout(3, 2, 20, 15));
        pnlGrid.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JButton btnDanhMuc = new JButton("Danh mục");
        JButton btnPhongTN = new JButton("Phòng - Tiện nghi");
        JButton btnDatPhong = new JButton("Đặt / Nhận phòng");
        JButton btnDichVu = new JButton("Sử dụng dịch vụ");
        JButton btnTraPhong = new JButton("Trả phòng - Thanh toán");
        JButton btnThongKe = new JButton("Thống kê");

        Font fontBtn = new Font("Segoe UI", Font.PLAIN, 16);
        for (JButton b : new JButton[]{btnDanhMuc, btnPhongTN, btnDatPhong, btnDichVu, btnTraPhong, btnThongKe}) {
            b.setFont(fontBtn);
            pnlGrid.add(b);
        }
        add(pnlGrid, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        JButton btnThoat = new JButton("Thoát");
        btnThoat.setFont(fontBtn);
        btnThoat.setPreferredSize(new Dimension(250, 45));
        pnlBottom.add(btnThoat);
        add(pnlBottom, BorderLayout.SOUTH);

        btnDanhMuc.addActionListener(e -> moForm(new FrmDanhMuc(this)));
        btnPhongTN.addActionListener(e -> moForm(new FrmPhongTienNghi(this)));
        btnDatPhong.addActionListener(e -> moForm(new FrmDatPhong(this)));
        btnDichVu.addActionListener(e -> moForm(new FrmDichVu(this)));
        btnTraPhong.addActionListener(e -> moForm(new FrmTraPhong(this)));
        btnThongKe.addActionListener(e -> moForm(new FrmThongKe(this)));

        btnThoat.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát ứng dụng?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) System.exit(0);
        });
    }

    private void moForm(JDialog dialog) {
        this.setVisible(false);
        dialog.setVisible(true);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmMain().setVisible(true));
    }
}