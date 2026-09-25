# LAB 3: THIẾT KẾ VÀ THI CÔNG HỆ THỐNG QUẢN LÝ KHÁCH SẠN

## 📌 Thông Tin Sinh Viên
* **Họ và tên:** Nguyễn Khánh Duy
* **MSSV:** 1250080038
* **Lớp:** 12_ĐH_CNPM1
* **Tên bài Lab:** LAB 3 - Hệ Thống Quản Lý Khách Sạn

---

## 🛠 Môi Trường & Phiên Bản
* **IDE:** Visual Studio 2022 (C# WinForms)
* **Framework:** .NET Framework 4.7.2
* **Database:** Microsoft SQL Server / LocalDB (ADO.NET)

---

## 🎯 Nội Dung Đã Thực Hiện
1. **Phân tích & Thiết kế UML:**
   * Khảo sát nghiệp vụ, lập Bảng thuật ngữ, quy tắc nghiệp vụ (BR01 - BR10).
   * Xây dựng Use Case tổng quát, Use Case phân rã, Activity Diagram, Sequence Diagram cho từng chức năng.
   * Thiết kế Biểu đồ lớp phân tích, Biểu đồ trạng thái và Biểu đồ lớp chi tiết.
2. **Thiết kế CSDL SQL Server:**
   * Tạo 17 bảng dữ liệu (`KhuVuc`, `Phong`, `TienNghi`, `KhachHang`, `PhieuDatPhong`, `DichVu`, `HoaDon`, `ThanhToan`,...).
   * Thiết lập đầy đủ khóa chính, khóa ngoại, Unique Index (chặn 1 thiết bị ở 2 phòng/ngày, gom dịch vụ/ngày) và các Check Constraint.
3. **Lập trình WinForms (Mô hình Phân Lớp):**
   * **`Data/Db.cs`**: Quản lý kết nối ADO.NET và thực thi Query/Execute/Scalar với Transaction.
   * **`Services`**: Xử lý logic nghiệp vụ tách biệt hoàn toàn với UI (`DatPhongService`, `TraPhongService`, `DichVuService`,...).
   * **`Forms`**: Hoàn thiện 7 Form giao diện (`FrmMain`, `FrmDanhMuc`, `FrmPhongTienNghi`, `FrmDatPhong`, `FrmDichVu`, `FrmTraPhong`, `FrmThongKe`).

---

## ✅ Kết Quả & Kiểm Thử Quy Tắc Nghiệp Vụ
* **Kiểm tra sức chứa & trùng lịch:** Từ chối đặt phòng nếu số người > sức chứa hoặc phòng bị đặt trùng thời gian.
* **Quy tắc thiết bị:** Chặn lắp đặt 1 thiết bị tiện nghi cho 2 phòng khác nhau trong cùng 1 ngày.
* **Cộng dồn dịch vụ:** Sử dụng cùng 1 dịch vụ nhiều lần trong ngày sẽ tự động cộng dồn số lượng.
* **Trả phòng & Thanh toán:** Tự động tính tiền phòng + dịch vụ, hỗ trợ nhiều giao dịch/hình thức thanh toán (Tiền mặt, Thẻ, CK, Ví), chỉ giải phóng phòng khi đã thanh toán đủ hóa đơn.

---

## ⚠️ Lỗi Gặp Phải & Cách Khắc Phục
* **Lỗi kết nối SQL Server:** Do chuỗi `ConnectionString` khác nhau giữa các máy.
  * *Cách khắc phục:* Đã cấu hình chuỗi kết nối linh hoạt trong file `App.config` để dễ dàng đổi tên Server/LocalDB.
* **Lỗi trùng khóa ngoại khi xóa dữ liệu:**
  * *Cách khắc phục:* Áp dụng ràng buộc ON DELETE/nhắc nhở lỗi bằng Messagebox thông qua gói kết quả `KetQuaXuLy`.

---

## 🚀 Hướng Dẫn Kiểm Trợ / Chạy Lại Project
1. Mở SQL Server Management Studio (SSMS), mở và chạy file `QuanLyKhachSan.sql` trong thư mục `LAB3/Database`.
2. Mở Solution `QuanLyKhachSan.sln` bằng **Visual Studio 2022**.
3. Mở file `App.config` và cập nhật chuỗi `ConnectionString` cho phù hợp với máy cá nhân.
4. Nhấn `F5` để Build và Run ứng dụng từ `FrmMain`.
