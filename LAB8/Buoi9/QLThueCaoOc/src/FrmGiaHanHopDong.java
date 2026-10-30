import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JPanel;

public class FrmGiaHanHopDong extends JPanel {
    public FrmGiaHanHopDong() {
        setLayout(new BorderLayout());
        setBackground(FormUi.BACKGROUND);
        add(new DataTableForm(
                "Gia hạn hợp đồng", "Chỉ gia hạn khi đã thanh toán hết tiền nhà và dịch vụ; thời hạn không quá một nửa hợp đồng ban đầu.",
                "Lập phiếu gia hạn", "phiếu gia hạn",
                new String[] {"Số phiếu", "Số hợp đồng", "Phòng", "Ngày lập", "Số tháng", "Ngày kết hạn mới", "Công nợ"},
                Arrays.asList(DataTableForm.Field.required("Số phiếu"),
                        DataTableForm.Field.choice("Số hợp đồng", "HD-2026-018", "HD-2026-017"),
                        DataTableForm.Field.choice("Phòng", "P-101", "P-201"),
                        DataTableForm.Field.required("Ngày lập"), DataTableForm.Field.required("Số tháng"),
                        DataTableForm.Field.required("Ngày kết hạn mới"),
                        DataTableForm.Field.choice("Công nợ", "Đã thanh toán", "Còn nợ")),
                new String[][] {
                    {"GH-2026-004", "HD-2026-018", "P-101", "15/10/2026", "3", "01/01/2028", "Đã thanh toán"},
                    {"GH-2026-003", "HD-2026-017", "P-201", "20/09/2026", "6", "15/03/2029", "Đã thanh toán"}
                }, values -> {
                    try {
                        int months = Integer.parseInt(values[4]);
                        int originalMonths = "HD-2026-018".equals(values[1]) ? 12 : 24;
                        if (months <= 0 || months > originalMonths / 2) {
                            return "Số tháng gia hạn phải lớn hơn 0 và không vượt quá 1/2 thời hạn thuê ban đầu.";
                        }
                    } catch (NumberFormatException ex) {
                        return "Số tháng gia hạn phải là số nguyên hợp lệ.";
                    }
                    if (!"Đã thanh toán".equals(values[6])) {
                        return "Chỉ được gia hạn khi đã thanh toán hết tiền nhà và phí dịch vụ.";
                    }
                    return null;
                }), BorderLayout.CENTER);
    }
}
