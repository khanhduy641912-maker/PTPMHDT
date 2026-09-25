# LAB 3: HỆ THỐNG QUẢN LÝ KHÁCH SẠN (JAVA SWING)

## 📌 Thông Tin Sinh Viên
* **Họ và tên:** Nguyễn Khánh Duy
* **MSSV:** 1250080038
* **Lớp:** 12_ĐH_CNPM1

---

## 🛠 Môi Trường Phát Triển
* **Ngôn ngữ:** Java (JDK 17+)
* **Giao diện:** Java Swing UI
* **Cơ sở dữ liệu:** Microsoft SQL Server
* **Thư viện kết nối:** JDBC Driver (`mssql-jdbc`)

---

## 🚀 Các Chức Năng Đã Thực Hiện
1. **Quản lý Danh mục & Phòng (`FrmDanhMuc`):**
   * Quản lý thông tin Phòng, Khu vực, Đơn giá và Sức chứa.
2. **Nghiệp vụ Đặt - Nhận phòng (`FrmDatPhong`):**
   * Lập phiếu đặt phòng, nhận tiền cọc, tự động cập nhật trạng thái phòng sang "Đã đặt".
3. **Nghiệp vụ Dịch vụ & Thanh toán (`FrmTraPhong`):**
   * Ghi nhận dịch vụ phát sinh, cộng dồn dịch vụ trong ngày, tính tổng hóa đơn (Tiền phòng + Tiền dịch vụ).