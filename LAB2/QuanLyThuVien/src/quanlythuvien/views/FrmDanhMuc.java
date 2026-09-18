package quanlythuvien.views;

import quanlythuvien.models.Models.*;
import quanlythuvien.services.DanhMucService;

import javax.swing.*;
import java.awt.*;

public class FrmDanhMuc extends JDialog {

    private DanhMucService service = new DanhMucService();

    // Controls Tab Nhân viên
    private JTextField txtNVMa, txtNVHo, txtNVTen, txtNVChucVu, txtNVSDT;
    private JComboBox<String> cboNVPhai;
    private JTable tblNhanVien;
    private JButton btnNVThem, btnNVCapNhat, btnNVXoa, btnNVMoi;

    // Controls Tab Thể loại
    private JTextField txtTLMa, txtTLTen;
    private JTable tblTheLoai;
    private JButton btnTLThem, btnTLCapNhat, btnTLXoa, btnTLMoi;

    // Controls Tab NXB
    private JTextField txtNXBMa, txtNXBDiaChi, txtNXBSDT;
    private JTable tblNXB;
    private JButton btnNXBThem, btnNXBCapNhat, btnNXBXoa, btnNXBMoi;

    public FrmDanhMuc(Frame parent) {
        super(parent, "Danh mục và nhân viên", true);
        initComponents();
        loadAllData();
    }

    private void initComponents() {
        setSize(950, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. TAB NHÂN VIÊN
        JPanel panelNV = new JPanel(new BorderLayout(10, 10));
        
        JPanel pnlNVForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlNVForm.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));

        txtNVMa = new JTextField();
        txtNVHo = new JTextField();
        txtNVTen = new JTextField();
        cboNVPhai = new JComboBox<>(new String[]{"Nam", "Nữ"});
        txtNVChucVu = new JTextField();
        txtNVSDT = new JTextField();

        pnlNVForm.add(new JLabel("Mã nhân viên:")); 
        pnlNVForm.add(txtNVMa);
        pnlNVForm.add(new JLabel("Phái:")); 
        pnlNVForm.add(cboNVPhai);

        pnlNVForm.add(new JLabel("Họ:")); 
        pnlNVForm.add(txtNVHo);
        pnlNVForm.add(new JLabel("Chức vụ:")); 
        pnlNVForm.add(txtNVChucVu);

        pnlNVForm.add(new JLabel("Tên:")); 
        pnlNVForm.add(txtNVTen);
        pnlNVForm.add(new JLabel("Số điện thoại:")); 
        pnlNVForm.add(txtNVSDT);

        JPanel pnlNVBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNVThem = new JButton("Thêm");
        btnNVCapNhat = new JButton("Cập nhật");
        btnNVXoa = new JButton("Xóa");
        btnNVMoi = new JButton("Làm mới");
        pnlNVBtns.add(btnNVThem); pnlNVBtns.add(btnNVCapNhat); pnlNVBtns.add(btnNVXoa); pnlNVBtns.add(btnNVMoi);

        JPanel pnlNVNorth = new JPanel(new BorderLayout());
        pnlNVNorth.add(pnlNVForm, BorderLayout.CENTER);
        pnlNVNorth.add(pnlNVBtns, BorderLayout.SOUTH);

        tblNhanVien = new JTable();
        panelNV.add(pnlNVNorth, BorderLayout.NORTH);
        panelNV.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);
        tabbedPane.addTab("Nhân viên", panelNV);

        // 2. TAB THỂ LOẠI
        JPanel panelTL = new JPanel(new BorderLayout(10, 10));
        JPanel pnlTLForm = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlTLForm.setBorder(BorderFactory.createTitledBorder("Thông tin Thể loại"));
        
        txtTLMa = new JTextField();
        txtTLTen = new JTextField();
        pnlTLForm.add(new JLabel("Mã thể loại:")); pnlTLForm.add(txtTLMa);
        pnlTLForm.add(new JLabel("Tên thể loại:")); pnlTLForm.add(txtTLTen);

        JPanel pnlTLBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnTLThem = new JButton("Thêm"); btnTLCapNhat = new JButton("Cập nhật");
        btnTLXoa = new JButton("Xóa"); btnTLMoi = new JButton("Làm mới");
        pnlTLBtns.add(btnTLThem); pnlTLBtns.add(btnTLCapNhat); pnlTLBtns.add(btnTLXoa); pnlTLBtns.add(btnTLMoi);

        JPanel pnlTLNorth = new JPanel(new BorderLayout());
        pnlTLNorth.add(pnlTLForm, BorderLayout.CENTER);
        pnlTLNorth.add(pnlTLBtns, BorderLayout.SOUTH);

        tblTheLoai = new JTable();
        panelTL.add(pnlTLNorth, BorderLayout.NORTH);
        panelTL.add(new JScrollPane(tblTheLoai), BorderLayout.CENTER);
        tabbedPane.addTab("Thể loại", panelTL);

        // 3. TAB NHÀ XUẤT BẢN
        JPanel panelNXB = new JPanel(new BorderLayout(10, 10));
        JPanel pnlNXBForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlNXBForm.setBorder(BorderFactory.createTitledBorder("Thông tin Nhà xuất bản"));

        txtNXBMa = new JTextField(); txtNXBDiaChi = new JTextField(); txtNXBSDT = new JTextField();
        pnlNXBForm.add(new JLabel("Mã NXB:")); pnlNXBForm.add(txtNXBMa);
        pnlNXBForm.add(new JLabel("Địa chỉ:")); pnlNXBForm.add(txtNXBDiaChi);
        pnlNXBForm.add(new JLabel("Điện thoại:")); pnlNXBForm.add(txtNXBSDT);

        JPanel pnlNXBBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnNXBThem = new JButton("Thêm"); btnNXBCapNhat = new JButton("Cập nhật");
        btnNXBXoa = new JButton("Xóa"); btnNXBMoi = new JButton("Làm mới");
        pnlNXBBtns.add(btnNXBThem); pnlNXBBtns.add(btnNXBCapNhat); pnlNXBBtns.add(btnNXBXoa); pnlNXBBtns.add(btnNXBMoi);

        JPanel pnlNXBNorth = new JPanel(new BorderLayout());
        pnlNXBNorth.add(pnlNXBForm, BorderLayout.CENTER);
        pnlNXBNorth.add(pnlNXBBtns, BorderLayout.SOUTH);

        tblNXB = new JTable();
        panelNXB.add(pnlNXBNorth, BorderLayout.NORTH);
        panelNXB.add(new JScrollPane(tblNXB), BorderLayout.CENTER);
        tabbedPane.addTab("Nhà xuất bản", panelNXB);

        add(tabbedPane, BorderLayout.CENTER);

        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBottom.add(btnDong);
        add(panelBottom, BorderLayout.SOUTH);

        initEvents();
    }

    private void loadAllData() {
        tblNhanVien.setModel(service.layDanhSachNhanVien());
        tblTheLoai.setModel(service.layDanhSachTheLoai());
        tblNXB.setModel(service.layDanhSachNXB());
        resetNVForm(); resetTLForm(); resetNXBForm();
    }

    private void initEvents() {
        // Event Nhân viên
        btnNVThem.addActionListener(e -> {
            NhanVien nv = new NhanVien(txtNVMa.getText().trim(), txtNVHo.getText().trim(), txtNVTen.getText().trim(),
                    cboNVPhai.getSelectedItem().toString(), new java.util.Date(), txtNVChucVu.getText().trim(), txtNVSDT.getText().trim());
            if (service.themNhanVien(nv)) { JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!"); loadAllData(); }
        });

        btnNVCapNhat.addActionListener(e -> {
            NhanVien nv = new NhanVien(txtNVMa.getText().trim(), txtNVHo.getText().trim(), txtNVTen.getText().trim(),
                    cboNVPhai.getSelectedItem().toString(), new java.util.Date(), txtNVChucVu.getText().trim(), txtNVSDT.getText().trim());
            if (service.capNhatNhanVien(nv)) { JOptionPane.showMessageDialog(this, "Cập nhật thành công!"); loadAllData(); }
        });

        btnNVXoa.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (service.xoaNhanVien(txtNVMa.getText().trim())) { JOptionPane.showMessageDialog(this, "Xóa thành công!"); loadAllData(); }
            }
        });

        btnNVMoi.addActionListener(e -> resetNVForm());

        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            int row = tblNhanVien.getSelectedRow();
            if (row >= 0) {
                txtNVMa.setText(tblNhanVien.getValueAt(row, 0).toString());
                txtNVHo.setText(tblNhanVien.getValueAt(row, 1).toString());
                txtNVTen.setText(tblNhanVien.getValueAt(row, 2).toString());
                cboNVPhai.setSelectedItem(tblNhanVien.getValueAt(row, 3).toString());
                txtNVChucVu.setText(tblNhanVien.getValueAt(row, 5).toString());
                txtNVSDT.setText(tblNhanVien.getValueAt(row, 6) != null ? tblNhanVien.getValueAt(row, 6).toString() : "");
                
                txtNVMa.setEditable(false);
                btnNVThem.setEnabled(false);
                btnNVCapNhat.setEnabled(true);
                btnNVXoa.setEnabled(true);
            }
        });

        // Event Thể loại
        btnTLThem.addActionListener(e -> {
            TheLoai tl = new TheLoai(txtTLMa.getText().trim(), txtTLTen.getText().trim());
            if (service.themTheLoai(tl)) { JOptionPane.showMessageDialog(this, "Thêm thể loại thành công!"); loadAllData(); }
        });

        btnTLCapNhat.addActionListener(e -> {
            TheLoai tl = new TheLoai(txtTLMa.getText().trim(), txtTLTen.getText().trim());
            if (service.capNhatTheLoai(tl)) { JOptionPane.showMessageDialog(this, "Cập nhật thể loại thành công!"); loadAllData(); }
        });

        btnTLXoa.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa thể loại này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (service.xoaTheLoai(txtTLMa.getText().trim())) { JOptionPane.showMessageDialog(this, "Xóa thành công!"); loadAllData(); }
            }
        });

        btnTLMoi.addActionListener(e -> resetTLForm());

        tblTheLoai.getSelectionModel().addListSelectionListener(e -> {
            int row = tblTheLoai.getSelectedRow();
            if (row >= 0) {
                txtTLMa.setText(tblTheLoai.getValueAt(row, 0).toString());
                txtTLTen.setText(tblTheLoai.getValueAt(row, 1).toString());
                txtTLMa.setEditable(false);
                btnTLThem.setEnabled(false);
                btnTLCapNhat.setEnabled(true);
                btnTLXoa.setEnabled(true);
            }
        });

        // Event NXB
        btnNXBThem.addActionListener(e -> {
            NhaXuatBan nxb = new NhaXuatBan(txtNXBMa.getText().trim(), txtNXBDiaChi.getText().trim(), txtNXBSDT.getText().trim());
            if (service.themNXB(nxb)) { JOptionPane.showMessageDialog(this, "Thêm NXB thành công!"); loadAllData(); }
        });

        btnNXBCapNhat.addActionListener(e -> {
            NhaXuatBan nxb = new NhaXuatBan(txtNXBMa.getText().trim(), txtNXBDiaChi.getText().trim(), txtNXBSDT.getText().trim());
            if (service.capNhatNXB(nxb)) { JOptionPane.showMessageDialog(this, "Cập nhật NXB thành công!"); loadAllData(); }
        });

        btnNXBXoa.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa NXB này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (service.xoaNXB(txtNXBMa.getText().trim())) { JOptionPane.showMessageDialog(this, "Xóa thành công!"); loadAllData(); }
            }
        });

        btnNXBMoi.addActionListener(e -> resetNXBForm());

        tblNXB.getSelectionModel().addListSelectionListener(e -> {
            int row = tblNXB.getSelectedRow();
            if (row >= 0) {
                txtNXBMa.setText(tblNXB.getValueAt(row, 0).toString());
                txtNXBDiaChi.setText(tblNXB.getValueAt(row, 1) != null ? tblNXB.getValueAt(row, 1).toString() : "");
                txtNXBSDT.setText(tblNXB.getValueAt(row, 2) != null ? tblNXB.getValueAt(row, 2).toString() : "");
                txtNXBMa.setEditable(false);
                btnNXBThem.setEnabled(false);
                btnNXBCapNhat.setEnabled(true);
                btnNXBXoa.setEnabled(true);
            }
        });
    }

    private void resetNVForm() {
        txtNVMa.setText(""); txtNVHo.setText(""); txtNVTen.setText("");
        txtNVChucVu.setText(""); txtNVSDT.setText("");
        txtNVMa.setEditable(true);
        btnNVThem.setEnabled(true); btnNVCapNhat.setEnabled(false); btnNVXoa.setEnabled(false);
    }

    private void resetTLForm() {
        txtTLMa.setText(""); txtTLTen.setText("");
        txtTLMa.setEditable(true);
        btnTLThem.setEnabled(true); btnTLCapNhat.setEnabled(false); btnTLXoa.setEnabled(false);
    }

    private void resetNXBForm() {
        txtNXBMa.setText(""); txtNXBDiaChi.setText(""); txtNXBSDT.setText("");
        txtNXBMa.setEditable(true);
        btnNXBThem.setEnabled(true); btnNXBCapNhat.setEnabled(false); btnNXBXoa.setEnabled(false);
    }
}