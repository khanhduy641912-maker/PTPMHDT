# BÁO CÁO THỰC HÀNH PHÂN TÍCH VÀ THIẾT KẾ HƯỚNG ĐỐI TƯỢNG (OOSD)

## Thông tin sinh viên

- Họ và tên: Nguyễn Khánh Duy
- Mã số sinh viên (MSSV): 1250080038
- Giảng viên: Ths. Phạm Trọng Huynh
- Môn học: Phân tích & Thiết kế Hướng đối tượng (Object-Oriented Software Design)
- Repository: HTPMHDT

## Tổng quan nội dung Repository

Repository lưu trữ toàn bộ các bài tập thực hành theo tiến trình môn học từ LAB 1 đến LAB 8, bao gồm:

- Phân tích nghiệp vụ
- Mô hình hóa phần mềm bằng UML
- Thiết kế cơ sở dữ liệu SQL Server
- Hiện thực phần mềm ứng dụng theo kiến trúc 3 tầng (UI → Service → Data)
- Bài tập thực hành Java/Swing và quản lý dữ liệu liên quan

Dự án này được tổ chức theo từng buổi học và từng hệ thống thông tin riêng biệt, nhằm minh họa cách áp dụng các nguyên tắc OOP trong thực tế.

## Cấu trúc thư mục chính

```text
PTPMHDT/
├── IMAGES/
├── LAB1/
│   ├── Buoi2/
│   │    │ BT1/
│   │    └─BT2/
│   └── README.md/
├── LAB2/
│   ├── Buoi3/
│   │    └── QuanLyThuVien/
│   └── README.md/
├── LAB3/
│   ├── Buoi4/
│   │    └── LAB3_QuanLyKhachSan/
│   └── README.md/
├── LAB4/
│   ├── Buoi5/
│   ├── Buoi2/
│   │    │ b1_anhClass/
│   │    └─b2_shopping/
│   └── README.md/
├── LAB5/
│   ├── Buoi6/
│   │    └── QuanLyCongTyDuLich/
│   └── README.md/
├── LAB6/
│   ├── Buoi7/
│   │    └── QL_BenhAn/
│   └── README.md/
├── LAB7/
│   ├── Buoi8/
│   │    └── QLNhanSu/
│   └── README.md/
├── LAB8/
│   ├── Buoi9/
│   │    └── QLThueCaoOc/
│   └── README.md/
└── README.md
```

## Danh sách bài tập thực hành

### LAB 1 - Bài tập cơ bản về UML và OOP
- Mô tả yêu cầu hệ thống
- Phân tích use case
- Lập sơ đồ activity

### LAB 2 - Hệ thống Quản lý Thư viện
- Quản lý sách, độc giả, mượn/trả sách
- Lập sơ đồ class, sơ đồ sequence
- Thiết kế cơ sở dữ liệu SQL Server
- Xây dựng giao diện và xử lý nghiệp vụ quản lý thư viện

### LAB 3 - Hệ thống Quản lý Khách sạn
- Quản lý phòng, khách hàng, đặt phòng, thanh toán
- Tích hợp dữ liệu khách sạn và báo cáo thống kê

### LAB 4 - Hệ thống E-Shopping Prototype
- Quản lý sản phẩm, giỏ hàng, khách hàng, đơn hàng
- Mô hình hóa hệ thống thương mại điện tử

### LAB 5 - Hệ thống Quản lý Công ty Du lịch
- Quản lý tour du lịch, khách hàng, đặt tour, hóa đơn
- Thiết kế theo mô hình doanh nghiệp du lịch

### LAB 6 - Hệ thống Quản lý Hồ sơ bệnh án
- Quản lý bệnh nhân, bác sĩ, hồ sơ bệnh án, lịch hẹn khám
- Xử lý dữ liệu y tế và hồ sơ bệnh nhân

### LAB 7 - Hệ thống Quản lý Nhân sự
- Quản lý nhân viên, phòng ban, hợp đồng, lương
- Các nghiệp vụ nhân sự cơ bản trong doanh nghiệp

### LAB 8 - Hệ thống Quản lý Cho thuê văn phòng cao ốc
- Quản lý văn phòng, khách thuê, hợp đồng thuê, thuế, thanh toán
- Hệ thống quản lý tài sản và không gian cho thuê

## Công nghệ và kỹ thuật sử dụng

- Ngôn ngữ lập trình: Java
- Giao diện: Swing / Java GUI
- Cơ sở dữ liệu: SQL Server
- Công cụ mô hình hóa: Draw, UML, sơ đồ use case, class diagram,...
- Cấu trúc hệ thống: hướng đối tượng, tách tầng, xử lý nghiệp vụ rõ ràng

## Hướng dẫn sử dụng

1. Mở thư mục tương ứng với bài tập cần chạy.
2. Kiểm tra file `.sql` để tạo database và dữ liệu mẫu.
3. Import các thư viện cần thiết trong thư mục `lib` nếu project Java yêu cầu.
4. Chạy file `App.java` hoặc `FrmMain.java` hoặc class chứa phương thức `main` để thực thi chương trình.
5. Đối với các dự án Java Swing, có thể sử dụng VS Code để build và chạy.

## Mục tiêu của repository

Repository này nhằm:

- Hệ thống hóa toàn bộ bài tập thực hành môn Phân tích & Thiết kế Hướng đối tượng.
- Làm tài liệu tham khảo cho sinh viên trong quá trình học và ôn tập.
- Minh họa rõ ràng quy trình từ phân tích nghiệp vụ đến thiết kế và triển khai hệ thống.
- Tạo nền tảng để phát triển các dự án quản lý dữ liệu theo mô hình OOP thực tế.

## Ghi chú

- Một số thư mục chứa file tài liệu Word/PDF mô tả yêu cầu bài tập và sơ đồ hệ thống.
- Một số dự án được lưu dưới dạng source code Java kèm SQL script để tạo cơ sở dữ liệu mẫu.
- Đây là repository học tập, phục vụ mục đích nghiên cứu và thực hành cá nhân.

