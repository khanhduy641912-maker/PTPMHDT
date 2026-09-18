package quanlythuvien.views;

import javax.swing.*;
import java.awt.*;

public class FrmMain extends JFrame {

    public FrmMain() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Quản lý thư viện - Nguyễn Khánh Duy (1250080038)");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Tiêu đề chính
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ THƯ VIỆN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Khung chứa 6 nút chức năng
        JPanel panelButtons = new JPanel(new GridLayout(3, 2, 20, 20));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        JButton btnDanhMuc = new JButton("Danh mục / Nhân viên");
        JButton btnSach = new JButton("Quản lý đầu sách");
        JButton btnDocGia = new JButton("Độc giả và thẻ");
        JButton btnMuonTra = new JButton("Mượn - Trả sách");
        JButton btnThongKe = new JButton("Thống kê");
        JButton btnThoat = new JButton("Thoát");

        // Chỉnh font cho nút
        Font btnFont = new Font("Segoe UI", Font.PLAIN, 14);
        // Mở Form Danh mục khi bấm nút
        btnDanhMuc.addActionListener(e -> {
            FrmDanhMuc frm = new FrmDanhMuc(this);
            frm.setVisible(true);
        });
        btnSach.addActionListener(e -> {
            FrmSach frm = new FrmSach(this);
            frm.setVisible(true);
        });
        btnDocGia.addActionListener(e -> {
            FrmDocGia frm = new FrmDocGia(this);
            frm.setVisible(true);
        });
        btnMuonTra.addActionListener(e -> {
            FrmMuonTra frm = new FrmMuonTra(this);
            frm.setVisible(true);
        });
        btnThongKe.addActionListener(e -> {
            FrmThongKe frm = new FrmThongKe(this);
            frm.setVisible(true);
        });
        btnThoat.setFont(btnFont);

        panelButtons.add(btnDanhMuc);
        panelButtons.add(btnSach);
        panelButtons.add(btnDocGia);
        panelButtons.add(btnMuonTra);
        panelButtons.add(btnThongKe);
        panelButtons.add(btnThoat);

        add(panelButtons, BorderLayout.CENTER);

        // 3. Khung hiển thị thông tin Sinh viên (Góc dưới giao diện theo mẫu Lab 2)
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panelInfo.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JLabel lblHoTen = new JLabel("Họ và tên: Nguyễn Khánh Duy");
        JLabel lblMSSV = new JLabel("MSSV: 1250080038");
        JLabel lblLop = new JLabel("Lớp: 12_ĐH_CNPM1");

        Font infoFont = new Font("Segoe UI", Font.BOLD, 13);
        lblHoTen.setFont(infoFont);
        lblMSSV.setFont(infoFont);
        lblLop.setFont(infoFont);

        lblHoTen.setForeground(new Color(0, 102, 204));
        lblMSSV.setForeground(new Color(0, 102, 204));
        lblLop.setForeground(new Color(0, 102, 204));

        panelInfo.add(lblHoTen);
        panelInfo.add(lblMSSV);
        panelInfo.add(lblLop);

        add(panelInfo, BorderLayout.SOUTH);

        // Sự kiện nút Thoát
        btnThoat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Bạn có thực sự muốn thoát chương trình?", 
                "Xác nhận", 
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new FrmMain().setVisible(true));
    }
}