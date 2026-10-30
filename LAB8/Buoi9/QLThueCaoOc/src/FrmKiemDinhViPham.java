import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JPanel;

public class FrmKiemDinhViPham extends JPanel {
    public FrmKiemDinhViPham() {
        setLayout(new BorderLayout());
        setBackground(FormUi.BACKGROUND);
        add(new DataTableForm(
                "Kiểm định & vi phạm", "Kiểm tra tình trạng phòng định kỳ 365 ngày/lần; lập biên bản nếu tự ý sửa kết cấu.",
                "Lập phiếu kiểm định", "phiếu kiểm định",
                new String[] {"Mã phiếu", "Phòng", "Ngày kiểm tra", "Tình trạng phòng", "Vi phạm", "Hành vi vi phạm", "Số tiền phạt", "Nộp phạt"},
                Arrays.asList(DataTableForm.Field.required("Mã phiếu"),
                        DataTableForm.Field.choice("Phòng", "P-101", "P-102", "P-201", "P-301"),
                        DataTableForm.Field.required("Ngày kiểm tra"),
                        DataTableForm.Field.required("Tình trạng phòng"),
                        DataTableForm.Field.choice("Vi phạm", "Không", "Có"),
                        DataTableForm.Field.optional("Hành vi vi phạm"),
                        DataTableForm.Field.optional("Số tiền phạt"),
                        DataTableForm.Field.choice("Nộp phạt", "Chưa nộp", "Đã nộp")),
                new String[][] {
                    {"KD-2026-011", "P-101", "03/10/2026", "Tường và hệ thống điện bình thường", "Không", "", "0", "Chưa nộp"},
                    {"KD-2026-010", "P-201", "18/09/2026", "Phát hiện thay đổi vách ngăn", "Có", "Tự ý thay đổi vách ngăn", "5.000.000", "Chưa nộp"}
                }, null), BorderLayout.CENTER);
    }
}
