import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class FrmTangPhong extends JPanel {
    public FrmTangPhong() {
        setLayout(new BorderLayout());
        setBackground(FormUi.BACKGROUND);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Danh mục tầng", new DataTableForm(
                "Danh mục tầng", "Quản lý các tầng và vị trí của tầng trong cao ốc.",
                "Thêm tầng", "tầng",
                new String[] {"Mã tầng", "Tên tầng", "Vị trí"},
                Arrays.asList(DataTableForm.Field.required("Mã tầng"),
                        DataTableForm.Field.required("Tên tầng"), DataTableForm.Field.optional("Vị trí")),
                new String[][] {
                    {"T01", "Tầng 1", "Khối văn phòng A"},
                    {"T02", "Tầng 2", "Khối văn phòng A"},
                    {"T03", "Tầng 3", "Khối văn phòng B"}
                }, null));
        tabs.addTab("Danh mục phòng", new DataTableForm(
                "Danh mục phòng", "Quản lý diện tích, hướng, đơn giá và trạng thái cho thuê.",
                "Thêm phòng", "phòng",
                new String[] {"Mã phòng", "Mã tầng", "Tên phòng", "Diện tích (m²)", "Hướng", "Đơn giá/tháng", "Trạng thái"},
                Arrays.asList(DataTableForm.Field.required("Mã phòng"),
                        DataTableForm.Field.required("Mã tầng"), DataTableForm.Field.required("Tên phòng"),
                        DataTableForm.Field.required("Diện tích (m²)"), DataTableForm.Field.optional("Hướng"),
                        DataTableForm.Field.required("Đơn giá/tháng"),
                        DataTableForm.Field.choice("Trạng thái", "Trống", "Đang thuê", "Bảo trì")),
                new String[][] {
                    {"P-101", "T01", "Văn phòng 101", "85", "Đông", "18.000.000", "Đang thuê"},
                    {"P-102", "T01", "Văn phòng 102", "72", "Nam", "15.500.000", "Trống"},
                    {"P-201", "T02", "Văn phòng 201", "120", "Tây", "28.000.000", "Đang thuê"},
                    {"P-301", "T03", "Văn phòng 301", "96", "Bắc", "22.000.000", "Bảo trì"}
                }, values -> {
                    try {
                        if (Double.parseDouble(values[3]) <= 0 || Double.parseDouble(values[5]) < 0) {
                            return "Diện tích phải lớn hơn 0 và đơn giá không được âm.";
                        }
                    } catch (NumberFormatException ex) {
                        return "Diện tích và đơn giá phải là số hợp lệ.";
                    }
                    return null;
                }));
        add(tabs, BorderLayout.CENTER);
    }
}
