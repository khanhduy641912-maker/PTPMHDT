# LAB 6: QUẢN LÝ HỒ SƠ BỆNH ÁN CỦA BỆNH NHÂN

> **Môn học:** Phát triển Phần mềm Hướng đối tượng (PTPMHDT)  
> **Sinh viên thực hiện:** Nguyễn Khánh Duy  
> **MSSV:** 1250080038  
> **Lớp:** 12_ĐH_CNPM1 — ĐH Công nghệ TP.HCM (HUTECH)

---

## 📌 Giới Thiệu Đồ Án
Dự án nhằm xây dựng và hoàn thiện ứng dụng **Quản lý Hồ sơ Bệnh án Bệnh nhân** cho Bệnh viện Đa khoa Quốc tế. Phần mềm hỗ trợ tin học hóa toàn bộ quy trình nghiệp vụ từ khâu tiếp nhận bệnh nhân lần đầu, cấp hồ sơ bệnh án, quản lý lệ phí năm, đặt lịch hẹn khám sức khỏe định kỳ theo quý, cho đến quy trình khám chữa bệnh, chỉ định xét nghiệm, kê toa thuốc và thanh toán viện phí xuất viện.

---

## 🛠 Công Nghệ & Kiến Trúc Sử Dụng

- **Ngôn ngữ lập trình:** C# / .NET Framework 4.7.2
- **Giao diện (UI):** Windows Forms (WinForms)
- **Hệ quản trị CSDL:** Microsoft SQL Server
- **Kiến trúc ứng dụng:** Kiến trúc 3 lớp (3-Layer Architecture)
  - `UI (Forms)`: Giao diện người dùng, không truy vấn SQL trực tiếp.
  - `Services`: Xử lý các quy tắc nghiệp vụ (Business Logic) & kiểm soát Transaction.
  - `Data (Db.cs)`: Tầng truy cập và thao tác CSDL thông qua ADO.NET (`SqlConnection`, `SqlCommand`, `SqlTransaction`).

---

## 📁 Cấu Trúc Thư Mục

```text
LAB6/
├── Buoi7/
│   ├── Database/
│   │   └── QuanLyHoSoBenhAn.sql     # Script khởi tạo CSDL & dữ liệu mẫu
│   ├── Data/
│   │   └── Db.cs                    # Lớp thực thi kết nối & câu lệnh SQL
│   ├── Services/
│   │   ├── Models.cs                # Lớp chứa cấu trúc dữ liệu trả về (KetQuaXuLy)
│   │   ├── HoSoService.cs           # Nghiệp vụ Cấp hồ sơ & Lệ phí năm
│   │   ├── KhamBenhService.cs       # Nghiệp vụ Phiếu khám & Triệu chứng
│   │   └── VienPhiService.cs        # Nghiệp vụ Hóa đơn & Chốt đợt điều trị
│   ├── Forms/
│   │   ├── FrmMain.cs               # Giao diện chính điều hướng hệ thống
│   │   ├── FrmDangKyHoSo.cs         # Tiếp nhận bệnh nhân & cấp hồ sơ năm
│   │   ├── FrmDatLichDinhKy.cs      # Đặt lịch hẹn khám định kỳ quý
│   │   ├── FrmKhamBenh.cs           # Lập phiếu khám & ghi nhận triệu chứng
│   │   ├── FrmXetNhiem.cs           # Chỉ định & nhập kết quả xét nghiệm
│   │   ├── FrmLapToaThuoc.cs        # Kê toa thuốc (Xử lý Batch/Transaction)
│   │   └── FrmThanhToanVienPhi.cs   # Thanh toán viện phí & hoàn tất xuất viện
│   └── App.config                   # Cấu hình chuỗi kết nối Database
└── README.md                        # Tài liệu hướng dẫn đồ án
