import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JPanel;

public class FrmHoaDonDichVu extends JPanel {
    public FrmHoaDonDichVu() {
        setLayout(new BorderLayout());
        setBackground(FormUi.BACKGROUND);
        add(new DataTableForm(
                "Hóa đơn dịch vụ", "Ghi nhận chỉ số điện, nước, internet và thanh toán vào cuối tháng.",
                "Lập hóa đơn dịch vụ", "hóa đơn dịch vụ",
                new String[] {"Số hóa đơn", "Số hợp đồng", "Dịch vụ", "Tháng/Năm", "Chỉ số cũ", "Chỉ số mới", "Tổng tiền", "Thanh toán"},
                Arrays.asList(DataTableForm.Field.required("Số hóa đơn"),
                        DataTableForm.Field.required("Số hợp đồng"),
                        DataTableForm.Field.choice("Dịch vụ", "Điện", "Nước", "Internet"),
                        DataTableForm.Field.required("Tháng/Năm"), DataTableForm.Field.required("Chỉ số cũ"),
                        DataTableForm.Field.required("Chỉ số mới"), DataTableForm.Field.required("Tổng tiền"),
                        DataTableForm.Field.choice("Thanh toán", "Chưa thanh toán", "Đã thanh toán")),
                new String[][] {
                    {"DV-2026-021", "HD-2026-018", "Điện", "10/2026", "1200", "1480", "980.000", "Chưa thanh toán"},
                    {"DV-2026-022", "HD-2026-018", "Nước", "10/2026", "310", "326", "320.000", "Đã thanh toán"},
                    {"DV-2026-023", "HD-2026-017", "Internet", "10/2026", "0", "0", "450.000", "Đã thanh toán"}
                }, null), BorderLayout.CENTER);
    }
}
