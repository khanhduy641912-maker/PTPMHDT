
# LAB 4: Thiết kế CSDL
# PHÂN TÍCH, THIẾT KẾ VÀ THI CÔNG HỆ THỐNG e-SHOPPING

📌 **Thông Tin Sinh Viên**

* **Họ và tên:** Nguyễn Khánh Duy
* **MSSV:** 1250080038
* **Lớp:** 12_ĐH_CNPM1
* **Tên bài Lab:** LAB 4 - Thiết Kế Sơ Đồ Lớp & Prototype Hệ Thống e-SHOPPING

---

🛠 **Môi Trường & Phiên Bản**

* **IDE:** Visual Studio Code (Extension Pack for Java)
* **Ngôn ngữ & Framework:** Java 11 (Maven, Java Swing UI)
* **Database:** Microsoft SQL Server (Driver `mssql-jdbc`)

---

🎯 **Nội Dung Đã Thực Hiện**

* **Module 1 (`b1_anhClass` - Mô hình hóa Lớp):**
  * Khảo sát đối tượng, trích xuất Thuộc tính, Phương thức và thiết lập đóng gói (Access Modifiers).
  * Xây dựng Biểu đồ lớp (Class Diagram) thể hiện chuẩn xác các mối quan hệ Hướng đối tượng (Association, Aggregation, Composition, Generalization).

* **Module 2 (`b2_shopping` - Hệ Thống e-SHOPPING):**
  * **Phân tích & Thiết kế UML:**
    * Xác định Ranh giới hệ thống (System Boundary), phân định rõ Core System và 3 Dịch vụ bên ngoài (PIM/ERP Sản phẩm, Cổng thanh toán, Email Service).
    * Lập bảng 07 Use Cases (`UC01` - `UC07`), xây dựng Biểu đồ Hoạt động (Activity Diagram) và Biểu đồ Trình tự (Sequence Diagram) chi tiết cho 2 chức năng trọng tâm (`UC03` Đặt hàng, `UC05` Thanh toán).
    * Thiết kế Biểu đồ Lớp Phân tích (BCE Pattern), Biểu đồ Trạng thái Đơn hàng (State Machine) và Biểu đồ Lớp Chi tiết áp dụng Service/Adapter Pattern.
  * **Thiết kế CSDL SQL Server (`EShopDB`):**
    * Tạo 5 bảng dữ liệu chính (`Customers`, `Products`, `Orders`, `OrderItems`, `PaymentTransactions`).
    * Thiết lập đầy đủ Khóa chính, Khóa ngoại, Identity và các ràng buộc dữ liệu.
  * **Lập trình Java Swing (Mô hình Phân Lớp UI → Service/Adapter → Data):**
    * `config/DBConnection.java`: Quản lý kết nối JDBC SQL Server (`LAPTOP-6VL0VPIP`, user `sa` / pass `123`, CSDL `EShopDB`).
    * `repository`: Thực thi truy vấn CSDL qua JDBC PreparedStatement (`OrderRepositoryImpl`).
    * `adapter`: Giả lập tích hợp 3 hệ thống bên ngoài tách biệt hoàn toàn (`ExternalPIMAdapter`, `MoMoPaymentAdapter`, `SendGridEmailAdapter`).
    * `service`: Xử lý logic nghiệp vụ đặt hàng và thanh toán (`OrderService`, `PaymentService`).
    * `forms`: Hoàn thiện 3 Form giao diện Swing Prototype (`CartAndCheckoutForm`, `PaymentGatewayForm`, `OrderManagementForm`).

---

✅ **Kết Quả & Kiểm Thử Quy Tắc Nghiệp Vụ**

* **Giữ kho & Kiểm tra tồn:** Tự động gọi PIM kiểm tra và khóa số lượng tồn khả dụng khi bấm Đặt hàng; chặn tạo đơn nếu PIM báo hết hàng.
* **Thanh toán & Callback IPN:** Sinh liên kết thanh toán an toàn, tiếp nhận thông báo IPN Server-to-Server từ Cổng thanh toán để tự động cập nhật trạng thái đơn hàng sang `Paid`.
* **Tự động gửi Email thông báo:** Kích hoạt sự kiện gửi email xác nhận đơn hàng và hóa đơn thanh toán tự động qua Dịch vụ Email ngoài.
* **Quản lý đơn hàng Admin:** Nhân viên xác nhận hoàn tất đơn hàng tự động kích hoạt lệnh trừ kho thực tế trên PIM và gửi email thông báo hành trình cho khách.

---

⚠️ **Lỗi Gặp Phải & Cách Khắc Phục**

* **Lỗi `ClassNotFoundException` khi Run:** Do VS Code mở thư mục cha thay vì đúng thư mục root của Maven.
  * *Cách khắc phục:* Mở trực tiếp thư mục gốc `eshoppingprototype` bằng VS Code và thực thi lệnh `mvn clean compile`.
* **Lỗi lệch phiên bản Java (JDK 1.7 vs Java 11):** Cấu hình compiler mặc định trong `pom.xml` chưa đồng bộ với JDK máy tính.
  * *Cách khắc phục:* Cập nhật thẻ `<maven.compiler.source>11</maven.compiler.source>` trong `pom.xml` và chạy lệnh `Java: Update Project Configuration`.
* **Lỗi hiển thị Font Tiếng Việt trên Swing:**
  * *Cách khắc phục:* Thêm cấu hình encoding UTF-8 trong file `.vscode/settings.json` và thiết lập VM options.

---

🚀 **Hướng Dẫn Kiểm Trợ / Chạy Lại Project**

1. Mở SQL Server Management Studio (SSMS), mở và chạy file script T-SQL để tạo CSDL `EShopDB`.
2. Mở VS Code, chọn `File -> Open Folder` và chọn đúng thư mục `b2_shopping/eshoppingprototype`.
3. Mở Terminal (`Ctrl + ~`) và chạy lệnh build:
   ```bash
   mvn clean compile
