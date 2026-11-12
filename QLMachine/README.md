# Hệ thống quản lý cho thuê máy nông nghiệp

## Thông tin đồ án

| Nội dung |
|---|---|
| Trường | Trường Đại học Tài nguyên và Môi trường TP. Hồ Chí Minh (HCMUNRE) |
| Môn học | Phân tích và thiết kế hệ thống thông tin (OOSD) |
| Sinh viên | Nguyễn Khánh Duy |
| MSSV | 1250080038 |
| Tên đề tài | Phân tích, thiết kế và xây dựng hệ thống cho thuê máy nông nghiệp (AGR-04)|

## Giới thiệu

Project xây dựng website hỗ trợ kết nối nông dân có nhu cầu thuê máy với chủ sở hữu máy nông nghiệp và người vận hành. Hệ thống tập trung quản lý danh mục máy, lịch đặt, tài xế, bảo trì và biên bản bàn giao.

Báo cáo đồ án trình bày quá trình khảo sát và phân tích nghiệp vụ, xác định tác nhân và chức năng, mô hình hóa quy trình, thiết kế dữ liệu và giao diện, sau đó hiện thực một ứng dụng web theo các nội dung thiết kế. Các tài liệu thiết kế kèm theo gồm báo cáo Word và sơ đồ draw.io trong thư mục `Data`.

### Vai trò và nghiệp vụ chính

- **Nông dân (FARMER):** tìm máy, kiểm tra khung giờ, tạo và theo dõi đơn thuê, hủy đơn đang chờ, lập/xác nhận biên bản bàn giao.
- **Chủ máy (OWNER):** quản lý máy và giá thuê, duyệt hoặc từ chối đơn liên quan đến máy của mình, lập lịch bảo trì.
- **Tài xế/người vận hành (OPERATOR):** cập nhật hồ sơ, xem các đơn được phân công và tham gia bàn giao.
- **Quản trị viên (ADMIN):** giám sát dữ liệu toàn hệ thống, quản lý trạng thái hồ sơ tài xế và theo dõi đơn/bảo trì.

Ứng dụng kiểm tra quyền ở API dựa trên phiên đăng nhập phía máy chủ. Trạng thái máy được tính từ đơn đang có hiệu lực và lịch bảo trì; phí thuê được tính ở backend.

## Công nghệ sử dụng

- Java 17
- Spring Boot 3.1.5, Spring Web, Spring Data JPA
- Microsoft SQL Server
- Maven
- HTML, CSS, JavaScript, Bootstrap

## Cấu trúc thư mục

```text
QLMachine/
├── Data/
│   ├── BaoCao_demo1.docx
│   ├── PTPMHDT.drawio
│   └── SQLMachine.sql
└── QLMay/
    └── qlmay/
        ├── pom.xml
        └── src/
            └── main/
                ├── java/com/agr04/
                └── resources/
                    ├── application.properties
                    └── static/
```

## Yêu cầu cài đặt

1. Cài **JDK 17**.
2. Cài **Apache Maven**.
3. Cài **Microsoft SQL Server** và SQL Server Management Studio (SSMS).
4. Đảm bảo SQL Server đang chạy và có thể kết nối tại `localhost:1433`, hoặc cập nhật cấu hình kết nối trong `QLMay/qlmay/src/main/resources/application.properties`.

## Cài đặt cơ sở dữ liệu

Mở file `Data/SQLMachine.sql` bằng SSMS và chạy script. Script tạo database `QLMachine`, các bảng, ràng buộc, thủ tục, trigger, view và dữ liệu demo.

> **Cảnh báo:** script bắt đầu bằng thao tác xóa database `QLMachine` nếu database đã tồn tại rồi tạo lại. Chạy lại script sẽ mất toàn bộ dữ liệu hiện có trong database đó. Hãy sao lưu trước nếu cần giữ dữ liệu.

Ứng dụng dùng `spring.jpa.hibernate.ddl-auto=validate`: Hibernate chỉ kiểm tra mapping với schema, không tự tạo hay sửa bảng. Giữ nguyên các bảng/cột đã được tạo bởi script.

### Cấu hình kết nối SQL Server

Mở `QLMay/qlmay/src/main/resources/application.properties` và chỉnh các giá trị phù hợp với máy của bạn:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=QLMachine;encrypt=true;trustServerCertificate=true;
spring.datasource.username=sa
spring.datasource.password=${DB_PASSWORD}
```

Không chia sẻ hoặc commit mật khẩu SQL Server lên GitHub. Nếu đã thay mật khẩu tài khoản demo trong database, đăng nhập bằng mật khẩu hiện tại thay vì mật khẩu demo.

## Chạy ứng dụng

Mở PowerShell tại thư mục project Maven:

```powershell
cd QLMay\qlmay
$env:DB_PASSWORD="mat-khau-SQL-Server-cua-ban"
```

Chạy ứng dụng bình thường:

```powershell
mvn spring-boot:run
```

Nếu muốn xóa kết quả biên dịch cũ và build lại từ đầu (ví dụ sau khi gặp lỗi build), chạy:

```powershell
mvn clean spring-boot:run
```

Lệnh `clean` chỉ xóa file build cũ trong thư mục `target`; không xóa database hay dữ liệu SQL Server.

Khi Spring Boot khởi động thành công, mở:

- Website đăng nhập: <http://localhost:8080/login.html>
- Trang chính: <http://localhost:8080/>

Giữ cửa sổ terminal đang chạy ứng dụng. Nhấn `Ctrl+C` trong terminal để dừng.

## Tài khoản demo

Nếu dữ liệu mẫu trong `SQLMachine.sql` chưa bị thay đổi, các tài khoản demo dùng mật khẩu `123456`:

| Vai trò | Số điện thoại |
|---|---|
| Admin | `0900000001` |
| Chủ máy | `0901234561` |
| Tài xế | `0901234563` |
| Nông dân | `0901234565` |

Chỉ tài khoản có vai trò phù hợp mới truy cập được các chức năng tương ứng. Tài khoản đăng ký mới không thể tự đăng ký vai trò ADMIN.

## Kiểm thử

Chạy các kiểm thử và biên dịch:

```powershell
cd QLMay\qlmay
mvn clean test
```

## API

API gốc: `http://localhost:8080`. Các nhóm endpoint chính:

- `/api/auth` — đăng nhập, đăng ký, phiên hiện tại, đăng xuất.
- `/api/machines` — danh sách máy, kiểm tra khả dụng, tạo và cập nhật máy.
- `/api/operators` — hồ sơ tài xế, danh sách và duyệt trạng thái.
- `/api/bookings` — tạo, xem, duyệt, từ chối và hủy đơn.
- `/api/maintenances` — lập, xem, hoàn tất và hủy lịch bảo trì.
- `/api/bookings/{bookingId}/handovers` — biên bản nhận/trả máy và xác nhận của bên còn lại.

Đăng nhập trước khi gọi API cần xác thực. Giữ cookie phiên `JSESSIONID` để các request tiếp theo được nhận diện cùng phiên.