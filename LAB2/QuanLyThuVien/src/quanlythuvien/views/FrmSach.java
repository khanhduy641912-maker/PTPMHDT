package quanlythuvien.views;

import quanlythuvien.models.Models.DauSach;
import quanlythuvien.services.DanhMucService;
import quanlythuvien.services.SachService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmSach extends JDialog {

    private SachService service = new SachService();
    private DanhMucService danhMucService = new DanhMucService();

    private JTextField txtMa, txtTen, txtNamXB, txtSoLuong, txtTim;
    private JComboBox<String> cboTheLoai, cboNXB;
    private JTable tblSach;
    private JButton btnThem, btnCapNhat, btnXoa, btnLamMoi, btnTim;

    public FrmSach(Frame parent) {
        super(parent, "Quản lý đầu sách", true);
        initComponents();
        loadData("");
        loadComboboxData();
    }

    private void initComponents() {
        setSize(950, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // 1. Form nhập liệu phía trên
        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin Đầu sách"));

        txtMa = new JTextField();
        txtTen = new JTextField();
        txtNamXB = new JTextField();
        txtSoLuong = new JTextField();
        cboTheLoai = new JComboBox<>();
        cboNXB = new JComboBox<>();

        pnlForm.add(new JLabel("Mã đầu sách:")); pnlForm.add(txtMa);
        pnlForm.add(new JLabel("Thể loại:")); pnlForm.add(cboTheLoai);

        pnlForm.add(new JLabel("Tên sách:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Nhà xuất bản:")); pnlForm.add(cboNXB);

        pnlForm.add(new JLabel("Năm xuất bản:")); pnlForm.add(txtNamXB);
        pnlForm.add(new JLabel("Số lượng hiện có:")); pnlForm.add(txtSoLuong);

        // Thanh công cụ Nút bấm + Tìm kiếm
        JPanel pnlTool = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem = new JButton("Thêm");
        btnCapNhat = new JButton("Cập nhật");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        txtTim = new JTextField(15);
        btnTim = new JButton("Tìm kiếm");

        pnlTool.add(btnThem); pnlTool.add(btnCapNhat); pnlTool.add(btnXoa); pnlTool.add(btnLamMoi);
        pnlTool.add(new JLabel("  | Từ khóa:")); pnlTool.add(txtTim); pnlTool.add(btnTim);

        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(pnlForm, BorderLayout.CENTER);
        pnlNorth.add(pnlTool, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        // 2. Bảng hiển thị ở giữa
        tblSach = new JTable();
        add(new JScrollPane(tblSach), BorderLayout.CENTER);

        // 3. Nút Đóng phía dưới
        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBottom.add(btnDong);
        add(panelBottom, BorderLayout.SOUTH);

        initEvents();
    }

    private void loadData(String tuKhoa) {
        tblSach.setModel(service.layDanhSachSach(tuKhoa));
        resetForm();
    }

    private void loadComboboxData() {
        cboTheLoai.removeAllItems();
        cboNXB.removeAllItems();

        DefaultTableModel dtTL = danhMucService.layDanhSachTheLoai();
        for (int i = 0; i < dtTL.getRowCount(); i++) {
            cboTheLoai.addItem(dtTL.getValueAt(i, 0).toString());
        }

        DefaultTableModel dtNXB = danhMucService.layDanhSachNXB();
        for (int i = 0; i < dtNXB.getRowCount(); i++) {
            cboNXB.addItem(dtNXB.getValueAt(i, 0).toString());
        }
    }

    private void initEvents() {
        btnThem.addActionListener(e -> {
            try {
                DauSach s = new DauSach(
                    txtMa.getText().trim(), txtTen.getText().trim(),
                    Integer.parseInt(txtNamXB.getText().trim()),
                    Integer.parseInt(txtSoLuong.getText().trim()),
                    cboTheLoai.getSelectedItem().toString(),
                    cboNXB.getSelectedItem().toString()
                );
                if (service.themSach(s)) { JOptionPane.showMessageDialog(this, "Thêm đầu sách thành công!"); loadData(""); }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho Năm XB và Số lượng!");
            }
        });

        btnCapNhat.addActionListener(e -> {
            try {
                DauSach s = new DauSach(
                    txtMa.getText().trim(), txtTen.getText().trim(),
                    Integer.parseInt(txtNamXB.getText().trim()),
                    Integer.parseInt(txtSoLuong.getText().trim()),
                    cboTheLoai.getSelectedItem().toString(),
                    cboNXB.getSelectedItem().toString()
                );
                if (service.capNhatSach(s)) { JOptionPane.showMessageDialog(this, "Cập nhật thành công!"); loadData(""); }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!");
            }
        });

        btnXoa.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Xóa đầu sách này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (service.xoaSach(txtMa.getText().trim())) { JOptionPane.showMessageDialog(this, "Xóa thành công!"); loadData(""); }
            }
        });

        btnLamMoi.addActionListener(e -> resetForm());
        btnTim.addActionListener(e -> loadData(txtTim.getText().trim()));

        tblSach.getSelectionModel().addListSelectionListener(e -> {
            int row = tblSach.getSelectedRow();
            if (row >= 0) {
                txtMa.setText(tblSach.getValueAt(row, 0).toString());
                txtTen.setText(tblSach.getValueAt(row, 1).toString());
                txtNamXB.setText(tblSach.getValueAt(row, 2).toString());
                txtSoLuong.setText(tblSach.getValueAt(row, 3).toString());
                cboTheLoai.setSelectedItem(tblSach.getValueAt(row, 4).toString());
                cboNXB.setSelectedItem(tblSach.getValueAt(row, 5).toString());

                txtMa.setEditable(false);
                btnThem.setEnabled(false);
                btnCapNhat.setEnabled(true);
                btnXoa.setEnabled(true);
            }
        });
    }

    private void resetForm() {
        txtMa.setText(""); txtTen.setText(""); txtNamXB.setText("2026"); txtSoLuong.setText("1");
        txtMa.setEditable(true);
        btnThem.setEnabled(true); btnCapNhat.setEnabled(false); btnXoa.setEnabled(false);
    }
}