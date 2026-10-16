# LAB 6: QUẢN LÝ HỒ SƠ BỆNH ÁN CỦA BỆNH NHÂN

> **Môn học:** Phát triển Phần mềm Hướng đối tượng (PTPMHDT)  
> **Sinh viên thực hiện:** Nguyễn Khánh Duy  
> **MSSV:** 1250080038  
> **Lớp:** 12_ĐH_CNPM1 

---

## 📌 Giới Thiệu Đồ Án
Dự án được xây dựng nhằm tin học hóa toàn bộ quy trình nghiệp vụ **Quản lý Hồ sơ Bệnh án Bệnh nhân** cho Bệnh viện Đa khoa Quốc tế. Hệ thống hỗ trợ xử lý toàn bộ luồng nghiệp vụ khép kín:
- **Tiếp nhận & Cấp hồ sơ:** Đăng ký bệnh nhân lần đầu, đóng lệ phí hằng năm và tự động cấp Mã hồ sơ bệnh án duy nhất.
- **Khám định kỳ quý:** Đặt lịch hẹn khám sức khỏe định kỳ theo Quý hoàn toàn miễn phí cho bệnh nhân có hồ sơ hợp lệ.
- **Thăm khám & Chẩn đoán:** Bác sĩ tra cứu lý lịch bệnh án, ghi nhận chỉ số triệu chứng lâm sàng và chẩn đoán bệnh.
- **Xét nghiệm & Kê đơn:** Chỉ định xét nghiệm, cập nhật kết quả cận lâm sàng và kê toa thuốc điều trị (có kiểm tra cảnh báo chống chỉ định).
- **Thanh toán Viện phí:** Thu ngân tính tổng viện phí, phát hành hóa đơn và chốt trạng thái hoàn tất đợt điều trị.

---

## 🛠 Công Nghệ & Thư Viện Sử Dụng

- **Ngôn ngữ lập trình:** Java (JDK 11 / JDK 17)
- **Thư viện Giao diện:** Java Swing & AWT (`UIStyle.java` thiết kế giao diện phẳng, hiện đại)
- **Hệ quản trị CSDL:** Microsoft SQL Server
- **Trình kết nối CSDL:** `mssql-jdbc-12.4.2.jre11.jar` (JDBC Driver)
- **Công cụ thiết kế UML/ERD:** Draw.io (`Benh_an.drawio`) / StarUML

---

## 📁 Cấu Trúc Thư Mục Thật (`Buoi7`)

```text
Buoi7/
├── QL_BenhAn/hosobenhan/
│   ├── lib/
│   │   └── mssql-jdbc-12.4.2.jre11.jar     # Thư viện JDBC kết nối SQL Server
│   ├── src/
│   │   ├── Database.java                   # Kết nối SQL Server (DriverManager)
│   │   ├── UIStyle.java                    # Định dạng màu sắc, font chữ & style JTable/JButton
│   │   ├── MainFrame.java                  # Giao diện Menu chính điều hướng hệ thống
│   │   ├── FrmDangKyHoSo.java              # Tiếp nhận bệnh nhân & cấp hồ sơ năm (Transaction)
│   │   ├── FrmDatLichDinhKy.java           # Đặt lịch hẹn khám định kỳ quý (Ràng buộc Unique)
│   │   ├── FrmKhamBenh.java                # Lập phiếu khám & ghi nhận triệu chứng
│   │   ├── FrmXetNhiem.java                # Chỉ định & nhập kết quả xét nghiệm
│   │   ├── FrmLapToaThuoc.java             # Kê toa thuốc điều trị (Batch Processing & Transaction)
│   │   └── FrmThanhToanVienPhi.cs          # Thanh toán viện phí & chốt hoàn tất đợt điều trị
│   └── README.md                           # Tài liệu hướng dẫn đồ án
├── BenhVien_Lab6.sql                       # Script khởi tạo CSDL & dữ liệu mẫu SQL Server
├── Benh_an.drawio                          # File thiết kế sơ đồ UML & ERD (Draw.io)
└── LAB6_Bai_5_Quan_ly_ho_so_benh_an.docx  # Báo cáo Word chi tiết (50-70 trang)
