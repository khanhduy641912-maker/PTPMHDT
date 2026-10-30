import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JPanel;

public class FrmNhanVien extends JPanel {
    public FrmNhanVien() {
        setLayout(new BorderLayout());
        setBackground(FormUi.BACKGROUND);
        add(new DataTableForm(
                "Nhân viên công ty", "Tiếp nhận nhân viên mới, hình ảnh, công ty và phòng làm việc.",
                "Thêm nhân viên", "nhân viên",
                new String[] {"Mã nhân viên", "Họ tên", "Công ty", "Phòng", "Chức vụ", "Điện thoại", "Hình ảnh"},
                Arrays.asList(DataTableForm.Field.required("Mã nhân viên"),
                        DataTableForm.Field.required("Họ tên"),
                        DataTableForm.Field.choice("Công ty", "Công ty Minh Phát", "Công ty An Bình", "Công ty Sao Việt"),
                        DataTableForm.Field.choice("Phòng", "P-101", "P-201", "P-102"),
                        DataTableForm.Field.optional("Chức vụ"), DataTableForm.Field.optional("Điện thoại"),
                        DataTableForm.Field.optional("Hình ảnh")),
                new String[][] {
                    {"NV-001", "Nguyễn Hoàng Nam", "Công ty Minh Phát", "P-101", "Trưởng phòng", "0901234567", "nam.jpg"},
                    {"NV-002", "Trần Ngọc Mai", "Công ty An Bình", "P-201", "Kế toán", "0912345678", "mai.jpg"},
                    {"NV-003", "Lê Minh Tuấn", "Công ty Minh Phát", "P-101", "Nhân viên", "0987654321", "tuan.jpg"}
                }, null), BorderLayout.CENTER);
    }
}
