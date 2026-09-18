package quanlythuvien.views;

import quanlythuvien.models.Models.DocGia;
import quanlythuvien.services.DocGiaService;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class FrmDocGia extends JDialog {

    private DocGiaService service = new DocGiaService();

    private JTextField txtMa, txtHo, txtTen, txtSDT, txtDiaChi, txtEmail;
    private JComboBox<String> cboPhai;
    private JTable tblDocGia;
    private JButton btnThem, btnCapNhat, btnXoa, btnCapThe, btnLamMoi;

    public FrmDocGia(Frame parent) {
        super(parent, "Độc giả và thẻ thư viện", true);
        initComponents();
        loadData();
    }

    private void initComponents() {
        setSize(950, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // 1. Form nhập liệu chuẩn 2 cột
        JPanel pnlForm = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin Độc giả"));

        txtMa = new JTextField(); txtHo = new JTextField(); txtTen = new JTextField();
        txtSDT = new JTextField(); txtDiaChi = new JTextField(); txtEmail = new JTextField();
        cboPhai = new JComboBox<>(new String[]{"Nam", "Nữ"});

        pnlForm.add(new JLabel("Mã độc giả:")); pnlForm.add(txtMa);
        pnlForm.add(new JLabel("Phái:")); pnlForm.add(cboPhai);

        pnlForm.add(new JLabel("Họ:")); pnlForm.add(txtHo);
        pnlForm.add(new JLabel("Số điện thoại:")); pnlForm.add(txtSDT);

        pnlForm.add(new JLabel("Tên:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Địa chỉ:")); pnlForm.add(txtDiaChi);

        pnlForm.add(new JLabel("Email:")); pnlForm.add(txtEmail);
        pnlForm.add(new JLabel("")); pnlForm.add(new JLabel(""));

        // Nút công cụ
        JPanel pnlTool = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnThem = new JButton("Thêm");
        btnCapNhat = new JButton("Cập nhật");
        btnXoa = new JButton("Xóa");
        btnCapThe = new JButton("Cấp/Gia hạn thẻ");
        btnLamMoi = new JButton("Làm mới");

        pnlTool.add(btnThem); pnlTool.add(btnCapNhat); pnlTool.add(btnXoa);
        pnlTool.add(btnCapThe); pnlTool.add(btnLamMoi);

        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(pnlForm, BorderLayout.CENTER);
        pnlNorth.add(pnlTool, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        // 2. Bảng ở giữa
        tblDocGia = new JTable();
        add(new JScrollPane(tblDocGia), BorderLayout.CENTER);

        // 3. Nút Đóng bên dưới
        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBottom.add(btnDong);
        add(panelBottom, BorderLayout.SOUTH);

        initEvents();
    }

    private void loadData() {
        tblDocGia.setModel(service.layDanhSachDocGia());
        resetForm();
    }

    private void initEvents() {
        btnThem.addActionListener(e -> {
            DocGia dg = new DocGia(txtMa.getText().trim(), txtHo.getText().trim(), txtTen.getText().trim(),
                    new Date(), cboPhai.getSelectedItem().toString(), txtSDT.getText().trim(),
                    txtDiaChi.getText().trim(), txtEmail.getText().trim(), null);
            if (service.themDocGia(dg)) { JOptionPane.showMessageDialog(this, "Thêm độc giả thành công!"); loadData(); }
        });

        btnCapNhat.addActionListener(e -> {
            DocGia dg = new DocGia(txtMa.getText().trim(), txtHo.getText().trim(), txtTen.getText().trim(),
                    new Date(), cboPhai.getSelectedItem().toString(), txtSDT.getText().trim(),
                    txtDiaChi.getText().trim(), txtEmail.getText().trim(), null);
            if (service.capNhatDocGia(dg)) { JOptionPane.showMessageDialog(this, "Cập nhật thành công!"); loadData(); }
        });

        btnXoa.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Xóa độc giả này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (service.xoaDocGia(txtMa.getText().trim())) { JOptionPane.showMessageDialog(this, "Xóa thành công!"); loadData(); }
            }
        });

        btnCapThe.addActionListener(e -> {
            if (txtMa.getText().trim().isEmpty()) return;
            if (service.capTheMoi(txtMa.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Cấp/Gia hạn thẻ thư viện thành công (Thời hạn 1 năm)!");
                loadData();
            }
        });

        btnLamMoi.addActionListener(e -> resetForm());

        tblDocGia.getSelectionModel().addListSelectionListener(e -> {
            int row = tblDocGia.getSelectedRow();
            if (row >= 0) {
                txtMa.setText(tblDocGia.getValueAt(row, 0).toString());
                txtHo.setText(tblDocGia.getValueAt(row, 1).toString());
                txtTen.setText(tblDocGia.getValueAt(row, 2).toString());
                cboPhai.setSelectedItem(tblDocGia.getValueAt(row, 3).toString());
                txtSDT.setText(tblDocGia.getValueAt(row, 4) != null ? tblDocGia.getValueAt(row, 4).toString() : "");
                txtDiaChi.setText(tblDocGia.getValueAt(row, 5) != null ? tblDocGia.getValueAt(row, 5).toString() : "");
                txtEmail.setText(tblDocGia.getValueAt(row, 6) != null ? tblDocGia.getValueAt(row, 6).toString() : "");

                txtMa.setEditable(false);
                btnThem.setEnabled(false);
                btnCapNhat.setEnabled(true);
                btnXoa.setEnabled(true);
                btnCapThe.setEnabled(true);
            }
        });
    }

    private void resetForm() {
        txtMa.setText(""); txtHo.setText(""); txtTen.setText("");
        txtSDT.setText(""); txtDiaChi.setText(""); txtEmail.setText("");
        txtMa.setEditable(true);
        btnThem.setEnabled(true); btnCapNhat.setEnabled(false);
        btnXoa.setEnabled(false); btnCapThe.setEnabled(false);
    }
}