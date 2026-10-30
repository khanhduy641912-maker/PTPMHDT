import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JPanel;

public class FrmHopDongThuePhong extends JPanel {
    public FrmHopDongThuePhong() {
        setLayout(new BorderLayout());
        setBackground(FormUi.BACKGROUND);
        add(new DataTableForm(
                "Hợp đồng thuê", "Lập hợp đồng nhiều phòng, tối thiểu 6 tháng; thu trước tiền thuê 6 tháng đầu.",
                "Lập hợp đồng", "hợp đồng",
                new String[] {"Số hợp đồng", "Công ty thuê", "Ngày bắt đầu", "Số tháng thuê", "Ngày kết hạn", "Phòng thuê", "Tiền cọc", "Trạng thái"},
                Arrays.asList(DataTableForm.Field.required("Số hợp đồng"),
                        DataTableForm.Field.choice("Công ty thuê", "Công ty Minh Phát", "Công ty An Bình", "Công ty Sao Việt"),
                        DataTableForm.Field.required("Ngày bắt đầu"),
                        DataTableForm.Field.required("Số tháng thuê"),
                        DataTableForm.Field.required("Ngày kết hạn"),
                        DataTableForm.Field.required("Phòng thuê"),
                        DataTableForm.Field.required("Tiền cọc"),
                        DataTableForm.Field.choice("Trạng thái", "Còn hiệu lực", "Chờ thanh toán", "Đã kết thúc")),
                new String[][] {
                    {"HD-2026-018", "Công ty Minh Phát", "01/10/2026", "12", "01/10/2027", "P-101", "20.000.000", "Còn hiệu lực"},
                    {"HD-2026-017", "Công ty An Bình", "15/09/2026", "24", "15/09/2028", "P-201", "30.000.000", "Còn hiệu lực"},
                    {"HD-2026-016", "Công ty Sao Việt", "01/09/2026", "6", "01/03/2027", "P-102", "15.000.000", "Chờ thanh toán"}
                }, values -> {
                    try {
                        if (Integer.parseInt(values[3]) < 6) {
                            return "Thời hạn thuê tối thiểu là 6 tháng theo quy định BR01.";
                        }
                    } catch (NumberFormatException ex) {
                        return "Thời hạn thuê phải là số tháng hợp lệ.";
                    }
                    return null;
                }), BorderLayout.CENTER);
    }
}
