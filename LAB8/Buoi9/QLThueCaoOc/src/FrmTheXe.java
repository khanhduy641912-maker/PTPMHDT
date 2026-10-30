import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JPanel;

public class FrmTheXe extends JPanel {
    public FrmTheXe() {
        setLayout(new BorderLayout());
        setBackground(FormUi.BACKGROUND);
        add(new DataTableForm(
                "Thẻ giữ xe", "Cấp thẻ xe cho nhân viên; mỗi nhân viên chỉ được cấp tối đa một thẻ.",
                "Cấp thẻ xe", "thẻ giữ xe",
                new String[] {"Mã thẻ", "Mã nhân viên", "Họ tên", "Biển số xe", "Loại xe", "Ngày cấp"},
                Arrays.asList(DataTableForm.Field.required("Mã thẻ"),
                        DataTableForm.Field.choice("Mã nhân viên", "NV-001", "NV-002", "NV-003"),
                        DataTableForm.Field.required("Họ tên"), DataTableForm.Field.required("Biển số xe"),
                        DataTableForm.Field.choice("Loại xe", "Xe máy", "Ô tô", "Xe điện"),
                        DataTableForm.Field.required("Ngày cấp")),
                new String[][] {
                    {"THE-001", "NV-001", "Nguyễn Hoàng Nam", "59-X1 123.45", "Xe máy", "03/10/2026"},
                    {"THE-002", "NV-002", "Trần Ngọc Mai", "51H 456.78", "Ô tô", "04/10/2026"}
                }, null), BorderLayout.CENTER);
    }
}
