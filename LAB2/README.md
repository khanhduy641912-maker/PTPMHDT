# ĐỒ ÁN PHÁT TRIỂN PHẦN MỀM HƯỚNG ĐỐI TƯỢNG - LAB 2

## 📌 Thông Tin Sinh Viên
* **Họ và tên:** Nguyễn Khánh Duy
* **MSSV:** 1250080038
* **Lớp:** 12_ĐH_CNPM1

---

## 🚀 Giới Thiệu Dự Án
Hệ thống Quản lý Thư viện được xây dựng bằng ngôn ngữ **Java (Swing UI)** kết nối với cơ sở dữ liệu **SQL Server (JDBC)** nhằm tự động hóa các hoạt động quản lý sách, độc giả và mượn trả tại thư viện.

---

## 🛠 Công Nghệ Sử Dụng
* **Ngôn ngữ:** Java (JDK 17+)
* **Giao diện:** Java Swing, AWT
* **Cơ sở dữ liệu:** Microsoft SQL Server
* **Thư viện kết nối:** JDBC (`mssql-jdbc-12.2.0.jre11.jar`)

---

## 📋 Các Chức Năng Chính (Lab 2)
1. **Quản lý Danh mục & Nhân viên (`FrmDanhMuc`):**
   * Quản lý thông tin Nhân viên, Thể loại sách và Nhà xuất bản.
   * Cài đặt đầy đủ các thao tác CRUD (Thêm, Cập nhật, Xóa, Làm mới).

2. **Quản lý Đầu sách (`FrmSach`):**
   * Quản lý danh mục sách, cập nhật số lượng tồn kho.
   * Tìm kiếm sách theo mã hoặc tên.

3. **Quản lý Độc giả & Thẻ thư viện (`FrmDocGia`):**
   * Quản lý thông tin độc giả.
   * Nghiệp vụ Cấp thẻ / Gia hạn thẻ thư viện (Tự động kiểm tra thẻ cũ còn hạn).

4. **Nghiệp vụ Mượn - Trả sách (`FrmMuonTra`):**
   * Lập phiếu mượn sách, kiểm tra quy tắc mượn (tối đa 3 cuốn, thẻ hợp lệ).
   * Tự động trừ/cộng lại số lượng tồn kho trong database.
   * Trả sách, tự động tính số ngày quá hạn và tiền phạt (5.000 VNĐ/ngày).

5. **Thống kê Báo cáo (`FrmThongKe`):**
   * Thống kê tổng lượt mượn, số lượng sách đang quá hạn.
   * Tổng hợp phí phạt thực tế và chi tiết danh sách các phiếu phạt.

---

## ⚙️ Hướng Dẫn Chạy Chương Trình
1. **Cơ sở dữ liệu:** 
   * Mở SQL Server Management Studio (SSMS).
   * Chạy file script tạo database `QuanLyThuVienDB` trong thư mục `LAB2/database`.
2. **Cấu hình kết nối:**
   * Mở file `DbConnection.java`.
   * Chỉnh sửa lại `user`, `password` và `port` SQL Server cho phù hợp với máy của bạn.
3. **Thực thi:**
   * Mở dự án trong Visual Studio Code hoặc NetBeans / Eclipse.
   * Đảm bảo đã add thư viện `mssql-jdbc` vào Referenced Libraries.
   * Chạy file `FrmMain.java` để bắt đầu trải nghiệm ứng dụng.
