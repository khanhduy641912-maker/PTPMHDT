package quanlythuvien.views;

import quanlythuvien.services.ThongKeService;
import javax.swing.*;
import java.awt.*;

public class FrmThongKe extends JDialog {

    private ThongKeService service = new ThongKeService();
    private JTable tblPhat;
    private JLabel lblLuotMuon, lblQuaHan, lblTongPhat;

    public FrmThongKe(Frame parent) {
        super(parent, "Thống kê hoạt động thư viện", true);
        initComponents();
        loadData();
    }

    private void initComponents() {
        setSize(1000, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("BÁO CÁO THỐNG KÊ THƯ VIỆN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Khung các chỉ số báo cáo tổng quan
        JPanel panelSummary = new JPanel(new GridLayout(1, 3, 15, 15));
        panelSummary.setBorder(BorderFactory.createTitledBorder("Tổng quan dữ liệu hệ thống"));

        lblLuotMuon = new JLabel("Tổng lượt mượn: 0", SwingConstants.CENTER);
        lblLuotMuon.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblQuaHan = new JLabel("Sách đang quá hạn: 0", SwingConstants.CENTER);
        lblQuaHan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblQuaHan.setForeground(Color.BLUE);

        lblTongPhat = new JLabel("Tổng tiền phạt thu được: 0 VNĐ", SwingConstants.CENTER);
        lblTongPhat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongPhat.setForeground(Color.RED);

        panelSummary.add(lblLuotMuon);
        panelSummary.add(lblQuaHan);
        panelSummary.add(lblTongPhat);

        // 3. Khung chứa bảng chi tiết các phiếu phạt
        JPanel panelCenter = new JPanel(new BorderLayout(10, 10));
        panelCenter.add(panelSummary, BorderLayout.NORTH);

        tblPhat = new JTable();
        panelCenter.add(new JScrollPane(tblPhat), BorderLayout.CENTER);
        add(panelCenter, BorderLayout.CENTER);

        // 4. Nút bấm phía dưới
        JButton btnLamMoi = new JButton("Tải lại dữ liệu");
        btnLamMoi.addActionListener(e -> loadData());

        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelBottom.add(btnLamMoi);
        panelBottom.add(btnDong);
        add(panelBottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        // Tải chỉ số KPI
        int[] kpi = service.layThongKeTongQuan();
        lblLuotMuon.setText("Tổng lượt mượn: " + kpi[0]);
        lblQuaHan.setText("Sách đang quá hạn: " + kpi[1]);
        lblTongPhat.setText(String.format("Tổng phí phạt: %,d VNĐ", kpi[2]));

        // Tải bảng danh sách phạt
        tblPhat.setModel(service.layChiTietPhat());
    }
}