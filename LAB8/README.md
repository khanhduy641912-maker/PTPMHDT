# Quản lý cho thuê văn phòng cao ốc

Ứng dụng Java Swing minh họa giao diện cho Bài 4. `App.java` là điểm chạy và mở `FrmMain`, từ đó chọn màn hình nghiệp vụ.

## Các form (theo Chương 3)

- `FrmMain.java`: trang chính và điều hướng.
- `FrmTangPhong.java`: danh mục tầng và phòng.
- `FrmHopDongThuePhong.java`: hợp đồng thuê.
- `FrmHoaDonTienNha.java`: hóa đơn tiền nhà.
- `FrmHoaDonDichVu.java`: hóa đơn dịch vụ.
- `FrmGiaHanHopDong.java`: phiếu gia hạn.
- `FrmNhanVien.java`: nhân viên công ty.
- `FrmTheXe.java`: thẻ giữ xe.
- `FrmKiemDinhViPham.java`: kiểm định và vi phạm.

`DataTableForm.java` và `FormUi.java` là phần dùng chung cho bảng, form nhập liệu và kiểu giao diện; mỗi màn hình nghiệp vụ vẫn nằm trong file riêng.

## Chạy ứng dụng

Mở thư mục `QLThueCaoOc` trong VS Code và chạy `src/FrmMain.java` (hoặc `src/App.java`); hoặc chạy từ thư mục `QLThueCaoOc` bằng PowerShell:

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
javac -encoding UTF-8 -d bin src\*.java
java -cp bin App
```

Các màn hình hiện dùng dữ liệu mẫu trong bộ nhớ để trình bày và thử nhập liệu. Thêm, sửa, xóa chưa ghi vào SQL Server; cần bổ sung lớp truy cập dữ liệu/kết nối JDBC trước khi dùng dữ liệu thật. Script `../QLCaoOc.sql` tạo schema SQL Server, chưa được kết nối với giao diện Java.

