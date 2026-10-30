import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JPanel;

public class FrmHoaDonTienNha extends JPanel {
    public FrmHoaDonTienNha() {
        setLayout(new BorderLayout());
        setBackground(FormUi.BACKGROUND);
        add(new DataTableForm(
                "Hóa đơn tiền nhà", "Theo dõi tiền thuê mặt bằng từ tháng thứ 7; sáu tháng đầu thu khi ký hợp đồng.",
                "Tạo hóa đơn", "hóa đơn tiền nhà",
                new String[] {"Số hóa đơn", "Số hợp đồng", "Tháng/Năm", "Ngày lập", "Số tiền", "Thanh toán"},
                Arrays.asList(DataTableForm.Field.required("Số hóa đơn"),
                        DataTableForm.Field.required("Số hợp đồng"), DataTableForm.Field.required("Tháng/Năm"),
                        DataTableForm.Field.required("Ngày lập"), DataTableForm.Field.required("Số tiền"),
                        DataTableForm.Field.choice("Thanh toán", "Chưa thanh toán", "Đã thanh toán")),
                new String[][] {
                    {"HN-2026-007", "HD-2026-018", "10/2026", "01/10/2026", "18.000.000", "Đã thanh toán"},
                    {"HN-2026-008", "HD-2026-017", "10/2026", "01/10/2026", "28.000.000", "Chưa thanh toán"},
                    {"HN-2026-009", "HD-2026-016", "10/2026", "01/10/2026", "15.500.000", "Đã thanh toán"}
                }, null), BorderLayout.CENTER);
    }
}
