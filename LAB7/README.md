# BÀI 7: HỆ THỐNG QUẢN LÝ NHÂN SỰ CÔNG TY SƠN ĐÔNG Á
**Môn học:** Phân tích thiết kế hướng đối tượng (PTPMHDT)  
**Sinh viên thực hiện:** Nguyễn Khánh Duy  
**MSSV:** 1250080038  
**Lớp:** 12_ĐH_CNPM1  

---

## 1. GIỚI THIỆU DỰ ÁN
Hệ thống tin học hóa quy trình Quản lý Nhân sự tại Công ty Sơn Đông Á, bao gồm toàn bộ các nghiệp vụ từ tuyển dụng thử việc, ký hợp đồng lao động, thiết lập hồ sơ bảo hiểm, phân công ca làm việc, chấm công, ghi nhận giờ phụ trội (tăng ca x1.5), khen thưởng, kỷ luật, đến tính toán bảng lương hằng tháng.

---

## 2. CẤU TRÚC THƯ MỤC THƯỜNG TRỰC
```text
Buoi8/
├── LAB7.drawio                  # Sơ đồ thiết kế UML (Use Case, Class, Activity, Sequence Diagram)
├── LAB7_Bai_7_Quan_ly_nhan_su.docx # Báo cáo chi tiết đồ án Bài 7
├── QLNhanSu.sql                 # Script SQL khởi tạo 19 bảng và dữ liệu mẫu
└── QLNhanSu/                    # Mã nguồn ứng dụng Java
    ├── .vscode/                 # Cấu hình môi trường VS Code
    ├── bin/                     # Thư mục chứa các file .class biên dịch
    ├── lib/                     # Thư mục chứa thư viện mssql-jdbc-12.4.2.jre8.jar
    ├── src/
    │   └── com/
    │       └── quanlynhansu/
    │           ├── data/
    │           │   └── Database.java                 # Lớp kết nối JDBC tới SQL Server
    │           ├── model/
    │           │   ├── NhanVien.java                 # Model Nhân viên
    │           │   └── HopDong.java                  # Model Hợp đồng lao động
    │           ├── dao/
    │           │   ├── NhanVienDAO.java              # Xử lý CSDL Hồ sơ nhân viên
    │           │   ├── HopDongDAO.java               # Xử lý CSDL Hợp đồng & Bảo hiểm
    │           │   ├── QuyetDinhDAO.java             # Xử lý CSDL Khen thưởng & Kỷ luật
    │           │   ├── QuyetDinhLuongChucVuDAO.java # Xử lý CSDL Lương & Chức vụ
    │           │   ├── ChamCongDAO.java              # Xử lý CSDL Bảng chấm công
    │           │   ├── PhuTroiDAO.java               # Xử lý CSDL Phiếu phụ trội & Tăng ca
    │           │   └── LuongDAO.java                 # Xử lý Thuật toán Tính lương tự động
    │           └── gui/
    │               ├── FrmMain.java                  # Giao diện Menu trung tâm
    │               ├── FrmNhanVien.java              # Giao diện Hồ sơ nhân viên & Tuyển dụng
    │               ├── FrmHopDongBaoHiem.java        # Giao diện Hợp đồng & Hồ sơ bảo hiểm
    │               ├── FrmQuyetDinhLuongChucVu.java # Giao diện QĐ Lương & Chức vụ
    │               ├── FrmQuyetDinh.java             # Giao diện QĐ Khen thưởng & Kỷ luật
    │               ├── FrmPhanCongPhuTroi.java       # Giao diện Lập phiếu phụ trội & Tăng ca
    │               ├── FrmChamCong.java              # Giao diện Bảng chấm công theo phòng ban
    │               └── FrmTinhLuong.java             # Giao diện Bảng tính lương tự động
    └── README.md

---

## 3. CÁC TÍNH NĂNG CHÍNH

1. **Quản lý Hồ sơ Nhân viên & Tuyển dụng (`FrmNhanVien`):**
   - Lập quyết định tuyển dụng thử việc (mức lương thử việc, thời gian thử việc, phòng ban tiếp nhận).
   - Thêm, xóa, cập nhật thông tin hồ sơ nhân viên.

2. **Quản lý Hợp đồng & Hồ sơ Bảo hiểm (`FrmHopDongBaoHiem`):**
   - Ký kết hợp đồng lao động ngắn hạn / dài hạn.
   - Tự động kích hoạt lập hồ sơ BHYT và BHXH nếu nhân viên ký hợp đồng dài hạn.

3. **Quản lý Quyết định Lương & Chức vụ (`FrmQuyetDinhLuongChucVu`):**
   - Thiết lập quyết định lương cơ bản, phụ cấp lương, phụ cấp độc hại.
   - Lập quyết định bổ nhiệm chức vụ kèm phụ cấp chức vụ.

4. **Quản lý Khen thưởng, Kỷ luật & Thu/Chi (`FrmQuyetDinh`):**
   - Lập quyết định khen thưởng cá nhân/tập thể, tự động sinh Phiếu chi khen thưởng.
   - Lập quyết định kỷ luật cá nhân/tập thể, tự động sinh Phiếu thu nộp phạt.

5. **Quản lý Phụ trội & Tăng ca (`FrmPhanCongPhuTroi`):**
   - Lập phiếu phụ trội áp dụng cho nhiều nhân viên cùng lúc.
   - Tự động quy đổi số giờ phụ trội theo hệ số 1.5.

6. **Chấm công theo Phòng ban (`FrmChamCong`):**
   - Tải danh sách nhân viên theo phòng ban để nhập ngày công thực tế hằng tháng.

7. **Tính & Chốt lương hằng tháng (`FrmTinhLuong`):**
   - Quét CSDL tổng hợp: Lương cơ bản, phụ cấp, ngày công, giờ phụ trội (x1.5), trích trừ BHYT/BHXH, thưởng/phạt.
   - Tự động tính toán Tổng lương, Thực lĩnh và chốt lưu vào SQL Server.

---

## 4. HƯỚNG DẪN CÀI ĐẶT & CHẠY ỨNG DỤNG

### Bước 1: Khởi tạo Cơ sở dữ liệu SQL Server
1. Mở **SQL Server Management Studio (SSMS)**.
2. Mở file `QLNhanSu.sql` và nhấn **Execute (F5)** để tạo CSDL `QuanLyNhanSu` (gồm 19 bảng chuẩn hóa và dữ liệu mẫu).

### Bước 2: Cấu hình Chuỗi kết nối Java
1. Mở file `src/com/quanlynhansu/data/Database.java`.
2. Thay đổi thông tin `SERVER_NAME` và `PASSWORD` cho phù hợp với máy cá nhân:
   ```java
   private static final String SERVER_NAME = "Server_name";
   private static final String DATABASE_NAME = "QuanLyNhanSu";
   private static final String USER = "sa";
   private static final String PASSWORD = "mật_khẩu_của_bạn";
