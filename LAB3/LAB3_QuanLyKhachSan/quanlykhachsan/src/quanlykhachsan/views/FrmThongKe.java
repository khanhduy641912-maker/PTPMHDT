package quanlykhachsan.views;

import quanlykhachsan.services.ThongKeService;
import javax.swing.*;
import java.awt.*;

public class FrmThongKe extends JDialog {
    private ThongKeService service = new ThongKeService();
    private JTable table = new JTable();

    public FrmThongKe(Frame parent) {
        super(parent, "Thống kê khách sạn", true);
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5, 5));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.add(new JLabel("Từ ngày:")); pnlTop.add(new JTextField("01/09/2026", 8));
        pnlTop.add(new JLabel("Đến ngày:")); pnlTop.add(new JTextField("30/09/2026", 8));
        JButton btnTK = new JButton("Thống kê");
        pnlTop.add(btnTK);
        add(pnlTop, BorderLayout.NORTH);

        JPanel pnlStats = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlStats.setBorder(BorderFactory.createTitledBorder("Thống kê tổng quan"));
        pnlStats.add(new JLabel(" Phiếu đặt: 28"));
        pnlStats.add(new JLabel(" Đang ở: 7"));
        pnlStats.add(new JLabel(" Hóa đơn: 21"));
        pnlStats.add(new JLabel(" Doanh thu HĐ: 52.600.000 đ"));
        pnlStats.add(new JLabel(" Tổng đền bù: 2.100.000 đ"));

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlStats, BorderLayout.NORTH);
        
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBorder(BorderFactory.createTitledBorder("Dịch vụ sử dụng:"));
        pnlTable.add(new JScrollPane(table), BorderLayout.CENTER);
        pnlCenter.add(pnlTable, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTroLai = new JButton("Trở lại");
        pnlBottom.add(btnTroLai);
        add(pnlBottom, BorderLayout.SOUTH);

        btnTroLai.addActionListener(e -> dispose());
        table.setModel(service.layThongKeTongHop());
    }
}