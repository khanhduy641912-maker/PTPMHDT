package quanlythuvien.views;

import quanlythuvien.services.MuonTraService;
import quanlythuvien.services.SachService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FrmMuonTra extends JDialog {

    private MuonTraService muonTraService = new MuonTraService();
    private SachService sachService = new SachService();

    private JTextField txtMuonMaDG, txtNgayMuon, txtNgayHenTra, txtTraMaDG;
    private JTable tblSachKho, tblSachMuon, tblDangMuon;
    private DefaultTableModel modelSachMuon;
    private JButton btnThemVaoDanhSach, btnXoaKhoiDanhSach, btnChoMuon, btnTraSach, btnTimTra;

    public FrmMuonTra(Frame parent) {
        super(parent, "Mượn - Trả sách", true);
        initComponents();
        loadAllData();
    }

    private void initComponents() {
        setSize(1100, 700);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // ------------------ 1. TAB MƯỢN SÁCH ------------------
        JPanel panelMuon = new JPanel(new BorderLayout(10, 10));
        
        // Thông tin phiếu mượn
        JPanel pnlMuonTop = new JPanel(new GridLayout(1, 6, 10, 10));
        pnlMuonTop.setBorder(BorderFactory.createTitledBorder("Thông tin độc giả & Ngày mượn"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 14);

        txtMuonMaDG = new JTextField();
        txtNgayMuon = new JTextField(sdf.format(now)); txtNgayMuon.setEditable(false);
        txtNgayHenTra = new JTextField(sdf.format(cal.getTime())); txtNgayHenTra.setEditable(false);

        pnlMuonTop.add(new JLabel("Mã độc giả:")); pnlMuonTop.add(txtMuonMaDG);
        pnlMuonTop.add(new JLabel("Ngày mượn:")); pnlMuonTop.add(txtNgayMuon);
        pnlMuonTop.add(new JLabel("Hẹn trả (+14d):")); pnlMuonTop.add(txtNgayHenTra);

        // Khung ở giữa chia đôi: Bên trái là Sách trong kho, Bên phải là Sách chọn mượn
        JPanel pnlMuonCenter = new JPanel(new GridLayout(1, 2, 10, 10));

        // Kho sách
        tblSachKho = new JTable();
        JPanel pnlKho = new JPanel(new BorderLayout());
        pnlKho.setBorder(BorderFactory.createTitledBorder("1. Kho sách (Chọn sách cần mượn)"));
        pnlKho.add(new JScrollPane(tblSachKho), BorderLayout.CENTER);
        btnThemVaoDanhSach = new JButton("Thêm vào danh sách mượn >>");
        pnlKho.add(btnThemVaoDanhSach, BorderLayout.SOUTH);

        // Giỏ sách
        modelSachMuon = new DefaultTableModel(new String[]{"Mã sách", "Tên sách"}, 0);
        tblSachMuon = new JTable(modelSachMuon);
        JPanel pnlGio = new JPanel(new BorderLayout());
        pnlGio.setBorder(BorderFactory.createTitledBorder("2. Danh sách sách chọn mượn (Tối đa 3 cuốn)"));
        pnlGio.add(new JScrollPane(tblSachMuon), BorderLayout.CENTER);
        
        JPanel pnlGioSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnXoaKhoiDanhSach = new JButton("Xóa khỏi danh sách");
        btnChoMuon = new JButton("Thực hiện mượn tất cả");
        pnlGioSouth.add(btnXoaKhoiDanhSach);
        pnlGioSouth.add(btnChoMuon);
        pnlGio.add(pnlGioSouth, BorderLayout.SOUTH);

        pnlMuonCenter.add(pnlKho);
        pnlMuonCenter.add(pnlGio);

        panelMuon.add(pnlMuonTop, BorderLayout.NORTH);
        panelMuon.add(pnlMuonCenter, BorderLayout.CENTER);
        tabbedPane.addTab("Mượn sách", panelMuon);

        // ------------------ 2. TAB TRẢ SÁCH ------------------
        JPanel panelTra = new JPanel(new BorderLayout(10, 10));
        JPanel pnlTraTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTraTop.setBorder(BorderFactory.createTitledBorder("Tra cứu sách đang mượn để trả"));

        txtTraMaDG = new JTextField(10);
        btnTimTra = new JButton("Tìm danh sách mượn");
        btnTraSach = new JButton("Xác nhận trả các cuốn đã chọn");

        pnlTraTop.add(new JLabel("Mã độc giả:")); pnlTraTop.add(txtTraMaDG);
        pnlTraTop.add(btnTimTra); pnlTraTop.add(btnTraSach);

        tblDangMuon = new JTable();
        panelTra.add(pnlTraTop, BorderLayout.NORTH);
        panelTra.add(new JScrollPane(tblDangMuon), BorderLayout.CENTER);
        tabbedPane.addTab("Trả sách", panelTra);

        add(tabbedPane, BorderLayout.CENTER);

        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBottom.add(btnDong);
        add(panelBottom, BorderLayout.SOUTH);

        initEvents();
    }

    private void loadAllData() {
        tblSachKho.setModel(sachService.layDanhSachSach(""));
    }

    private void initEvents() {
        // Thêm sách từ Kho sang Giỏ
        btnThemVaoDanhSach.addActionListener(e -> {
            int row = tblSachKho.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 cuốn sách trong kho!");
                return;
            }

            if (modelSachMuon.getRowCount() >= 3) {
                JOptionPane.showMessageDialog(this, "Mỗi lần lập phiếu chỉ chọn tối đa 3 cuốn sách!");
                return;
            }

            String maSach = tblSachKho.getValueAt(row, 0).toString();
            String tenSach = tblSachKho.getValueAt(row, 1).toString();

            // Kiểm tra trùng trong giỏ
            for (int i = 0; i < modelSachMuon.getRowCount(); i++) {
                if (modelSachMuon.getValueAt(i, 0).toString().equals(maSach)) {
                    JOptionPane.showMessageDialog(this, "Cuốn sách này đã có trong danh sách chọn mượn!");
                    return;
                }
            }

            modelSachMuon.addRow(new Object[]{maSach, tenSach});
        });

        // Xóa sách khỏi Giỏ
        btnXoaKhoiDanhSach.addActionListener(e -> {
            int row = tblSachMuon.getSelectedRow();
            if (row >= 0) {
                modelSachMuon.removeRow(row);
            }
        });

        // Thực hiện mượn tất cả sách trong giỏ
        btnChoMuon.addActionListener(e -> {
            String maDG = txtMuonMaDG.getText().trim();
            if (maDG.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Độc Giả!");
                return;
            }

            if (modelSachMuon.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 cuốn sách để mượn!");
                return;
            }

            List<String> dsMaSach = new ArrayList<>();
            for (int i = 0; i < modelSachMuon.getRowCount(); i++) {
                dsMaSach.add(modelSachMuon.getValueAt(i, 0).toString());
            }

            if (muonTraService.lapPhieuMuonNhieuSach(maDG, "NV001", dsMaSach)) {
                JOptionPane.showMessageDialog(this, "Mượn thành công " + dsMaSach.size() + " cuốn sách!\n- Ngày mượn: " + txtNgayMuon.getText() + "\n- Hẹn trả: " + txtNgayHenTra.getText());
                modelSachMuon.setRowCount(0);
                loadAllData();
            }
        });

        // Tìm danh sách trả
        btnTimTra.addActionListener(e -> {
            String maDG = txtTraMaDG.getText().trim();
            if (!maDG.isEmpty()) {
                tblDangMuon.setModel(muonTraService.laySachDangMuon(maDG));
            }
        });

        // Xác nhận trả nhiều sách (Cho phép chọn nhiều dòng trên bảng bằng phím Ctrl/Shift)
        btnTraSach.addActionListener(e -> {
            int[] selectedRows = tblDangMuon.getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn các cuốn sách cần trả trên bảng (Giữ Ctrl để chọn nhiều dòng)!");
                return;
            }

            int countSuccess = 0;
            for (int row : selectedRows) {
                String maCT = tblDangMuon.getValueAt(row, 0).toString();
                String maSach = tblDangMuon.getValueAt(row, 2).toString();
                String strTre = tblDangMuon.getValueAt(row, 6).toString();
                
                int soNgayTre = 0;
                if (!strTre.equals("Đúng hạn")) {
                    soNgayTre = Integer.parseInt(strTre.replace(" ngày", ""));
                }

                if (muonTraService.traSach(maCT, maSach, soNgayTre)) {
                    countSuccess++;
                }
            }

            JOptionPane.showMessageDialog(this, "Đã trả thành công " + countSuccess + " cuốn sách!");
            tblDangMuon.setModel(muonTraService.laySachDangMuon(txtTraMaDG.getText().trim()));
            loadAllData();
        });
    }
}