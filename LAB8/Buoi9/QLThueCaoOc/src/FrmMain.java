import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class FrmMain extends JFrame {
    private final JPanel cards = new JPanel(new CardLayout());
    private final JPanel sidebar = new JPanel();
    private final JLabel pageTitle = new JLabel("Tổng quan");
    private final Map<String, JButton> menuButtons = new LinkedHashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmMain().setVisible(true));
    }

    public FrmMain() {
        super("Cao Ốc | Quản lý cho thuê văn phòng");
        applyLookAndFeel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 700));
        setSize(1380, 860);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(createSidebar(), BorderLayout.WEST);
        add(createWorkspace(), BorderLayout.CENTER);
        addPages();
        showPage("home", "Tổng quan");
    }

    private void applyLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("Không thể khởi tạo giao diện Nimbus: " + ex.getMessage());
        }
    }

    private JPanel createSidebar() {
        sidebar.setBackground(FormUi.NAVY);
        sidebar.setPreferredSize(new Dimension(248, 0));
        sidebar.setBorder(new EmptyBorder(22, 14, 18, 14));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        JLabel brand = new JLabel("  CAO ỐC");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 21));
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(brand);
        JLabel subtitle = new JLabel("  OFFICE MANAGEMENT");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        subtitle.setForeground(new Color(164, 179, 205));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(30));

        addMenuItem("home", "OV", "Tổng quan", "home");
        addSection("NGHIỆP VỤ");
        addMenuItem("rooms", "TP", "Tầng & phòng", "rooms");
        addMenuItem("contracts", "HD", "Hợp đồng thuê", "contracts");
        addMenuItem("house-bills", "NH", "Hóa đơn tiền nhà", "house-bills");
        addMenuItem("service-bills", "DV", "Hóa đơn dịch vụ", "service-bills");
        addMenuItem("extensions", "GH", "Gia hạn hợp đồng", "extensions");
        addSection("AN NINH & KỸ THUẬT");
        addMenuItem("employees", "NV", "Nhân viên công ty", "employees");
        addMenuItem("parking", "XE", "Thẻ giữ xe", "parking");
        addMenuItem("inspections", "KT", "Kiểm định & vi phạm", "inspections");
        sidebar.add(Box.createVerticalGlue());
        JLabel footer = new JLabel("  QUẢN LÝ TÒA NHÀ • BÀI 4");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footer.setForeground(new Color(164, 179, 205));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(footer);
        return sidebar;
    }

    private void addSection(String title) {
        sidebar.add(Box.createVerticalStrut(19));
        JLabel label = new JLabel("  " + title);
        label.setForeground(new Color(135, 153, 182));
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(label);
        sidebar.add(Box.createVerticalStrut(7));
    }

    private void addMenuItem(String key, String badge, String label, String cardName) {
        JButton button = new JButton("<html><span style='color:#9FB2D1;font-size:10px'><b>"
                + badge + "</b></span>&nbsp;&nbsp;&nbsp;" + label + "</html>");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(new Color(218, 226, 239));
        button.setBackground(FormUi.NAVY);
        button.setBorder(new EmptyBorder(11, 12, 11, 8));
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(event -> showPage(cardName, label));
        sidebar.add(button);
        menuButtons.put(key, button);
    }

    private JPanel createWorkspace() {
        JPanel workspace = new JPanel(new BorderLayout());
        workspace.setBackground(FormUi.BACKGROUND);
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, FormUi.BORDER),
                new EmptyBorder(17, 30, 17, 30)));
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 19));
        pageTitle.setForeground(FormUi.TEXT);
        topBar.add(pageTitle, BorderLayout.WEST);
        JLabel user = new JLabel("BAN QUẢN LÝ   •   QUẢN TRỊ VIÊN");
        user.setFont(new Font("Segoe UI", Font.BOLD, 10));
        user.setForeground(FormUi.MUTED);
        topBar.add(user, BorderLayout.EAST);
        workspace.add(topBar, BorderLayout.NORTH);

        cards.setBackground(FormUi.BACKGROUND);
        cards.setBorder(new EmptyBorder(24, 28, 16, 28));
        workspace.add(cards, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(FormUi.BACKGROUND);
        bottom.setBorder(new EmptyBorder(0, 30, 12, 30));
        JLabel status = new JLabel("Bản giao diện • dữ liệu minh họa, chưa kết nối SQL Server");
        status.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        status.setForeground(FormUi.MUTED);
        bottom.add(status, BorderLayout.WEST);
        workspace.add(bottom, BorderLayout.SOUTH);
        return workspace;
    }

    private void addPages() {
        cards.add(createDashboard(), "home");
        cards.add(new FrmTangPhong(), "rooms");
        cards.add(new FrmHopDongThuePhong(), "contracts");
        cards.add(new FrmHoaDonTienNha(), "house-bills");
        cards.add(new FrmHoaDonDichVu(), "service-bills");
        cards.add(new FrmGiaHanHopDong(), "extensions");
        cards.add(new FrmNhanVien(), "employees");
        cards.add(new FrmTheXe(), "parking");
        cards.add(new FrmKiemDinhViPham(), "inspections");
    }

    private JPanel createDashboard() {
        JPanel page = new JPanel(new BorderLayout(0, 20));
        page.setOpaque(false);
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Xin chào, Ban quản lý");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(FormUi.TEXT);
        JLabel description = new JLabel("Chọn một màn hình nghiệp vụ từ danh sách bên trái để bắt đầu.");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        description.setForeground(FormUi.MUTED);
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(description);
        page.add(heading, BorderLayout.NORTH);

        JPanel dashboard = new JPanel(new BorderLayout(0, 18));
        dashboard.setOpaque(false);
        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setOpaque(false);
        stats.add(statCard("PHÒNG ĐANG CHO THUÊ", "24", "3 phòng trống"));
        stats.add(statCard("HỢP ĐỒNG HIỆU LỰC", "18", "2 hợp đồng sắp hết hạn"));
        stats.add(statCard("HÓA ĐƠN CHỜ THU", "06", "Tiền nhà và dịch vụ"));
        stats.add(statCard("NHÂN VIÊN TẠI TÒA NHÀ", "86", "Đã đăng ký hồ sơ"));
        dashboard.add(stats, BorderLayout.NORTH);

        JPanel shortcuts = FormUi.cardPanel();
        shortcuts.setLayout(new BorderLayout(0, 14));
        JLabel shortcutTitle = new JLabel("MÀN HÌNH NGHIỆP VỤ");
        shortcutTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        shortcutTitle.setForeground(FormUi.MUTED);
        shortcuts.add(shortcutTitle, BorderLayout.NORTH);
        JPanel buttons = new JPanel(new GridLayout(0, 2, 12, 12));
        buttons.setOpaque(false);
        addShortcut(buttons, "Quản lý tầng & phòng", "rooms");
        addShortcut(buttons, "Lập hợp đồng thuê", "contracts");
        addShortcut(buttons, "Hóa đơn tiền nhà", "house-bills");
        addShortcut(buttons, "Hóa đơn dịch vụ", "service-bills");
        addShortcut(buttons, "Gia hạn hợp đồng", "extensions");
        addShortcut(buttons, "Quản lý nhân viên", "employees");
        addShortcut(buttons, "Cấp thẻ giữ xe", "parking");
        addShortcut(buttons, "Kiểm định & vi phạm", "inspections");
        shortcuts.add(buttons, BorderLayout.CENTER);
        dashboard.add(shortcuts, BorderLayout.CENTER);
        page.add(dashboard, BorderLayout.CENTER);
        return page;
    }

    private JPanel statCard(String title, String value, String detail) {
        JPanel card = FormUi.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(FormUi.MUTED);
        JLabel number = new JLabel(value);
        number.setFont(new Font("Segoe UI", Font.BOLD, 28));
        number.setForeground(FormUi.BLUE);
        JLabel caption = new JLabel(detail);
        caption.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        caption.setForeground(FormUi.MUTED);
        card.add(label);
        card.add(Box.createVerticalStrut(8));
        card.add(number);
        card.add(Box.createVerticalStrut(4));
        card.add(caption);
        return card;
    }

    private void addShortcut(JPanel parent, String label, String page) {
        JButton button = FormUi.secondaryButton(label + "  →");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(220, 48));
        ActionListener action = event -> showPage(page, label);
        button.addActionListener(action);
        parent.add(button);
    }

    private void showPage(String key, String title) {
        ((CardLayout) cards.getLayout()).show(cards, key);
        pageTitle.setText(title);
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            boolean selected = entry.getKey().equals(key);
            entry.getValue().setBackground(selected ? new Color(43, 62, 96) : FormUi.NAVY);
            entry.getValue().setForeground(selected ? Color.WHITE : new Color(218, 226, 239));
        }
    }
}
