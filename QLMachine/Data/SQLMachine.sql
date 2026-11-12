USE [master];
GO

IF DB_ID(N'QLMachine') IS NOT NULL
BEGIN
    ALTER DATABASE [QLMachine] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [QLMachine];
END
GO

CREATE DATABASE [QLMachine];
GO
USE [QLMachine];
GO

-- 1.1. Bảng USERS
IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL DROP TABLE dbo.Users;
CREATE TABLE Users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'FARMER' CHECK (role IN ('FARMER', 'OWNER', 'OPERATOR', 'ADMIN')),
    created_at DATETIME DEFAULT GETDATE()
);
GO

-- 1.2. Bảng OPERATORS (Người vận hành)
IF OBJECT_ID('dbo.Operators', 'U') IS NOT NULL DROP TABLE dbo.Operators;
CREATE TABLE Operators (
    operator_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    license_no VARCHAR(50) NOT NULL,
    is_eligible BIT DEFAULT 1, -- 1 = TRUE (Hợp lệ), 0 = FALSE (Không hợp lệ)
    hourly_rate DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT FK_Operators_User FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
GO

-- 1.3. Bảng MACHINES (Danh mục máy)
IF OBJECT_ID('dbo.Machines', 'U') IS NOT NULL DROP TABLE dbo.Machines;
CREATE TABLE Machines (
    machine_id INT IDENTITY(1,1) PRIMARY KEY,
    owner_id INT NOT NULL,
    name NVARCHAR(100) NOT NULL,
    type NVARCHAR(50) NOT NULL,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'IN_MAINTENANCE', 'RESERVED')),
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Machines_Owner FOREIGN KEY (owner_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
GO

-- 1.4. Bảng MAINTENANCES (Lịch bảo trì)
IF OBJECT_ID('dbo.Maintenances', 'U') IS NOT NULL DROP TABLE dbo.Maintenances;
CREATE TABLE Maintenances (
    maintenance_id INT IDENTITY(1,1) PRIMARY KEY,
    machine_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    description NVARCHAR(MAX),
    status VARCHAR(20) DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED')),
    CONSTRAINT FK_Maintenances_Machine FOREIGN KEY (machine_id) REFERENCES Machines(machine_id) ON DELETE CASCADE,
    CONSTRAINT CHK_Maintenance_Time CHECK (end_time > start_time)
);
GO

-- 1.5. Bảng BOOKINGS (Đơn đặt máy)
IF OBJECT_ID('dbo.Bookings', 'U') IS NOT NULL DROP TABLE dbo.Bookings;
CREATE TABLE Bookings (
    booking_id INT IDENTITY(1,1) PRIMARY KEY,
    farmer_id INT NOT NULL,
    machine_id INT NOT NULL,
    operator_id INT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    booking_time DATETIME DEFAULT GETDATE(),
    total_fee DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
	field_address NVARCHAR(255),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'IN_PROGRESS', 'COMPLETED')),
    CONSTRAINT FK_Bookings_Farmer FOREIGN KEY (farmer_id) REFERENCES Users(user_id),
    CONSTRAINT FK_Bookings_Machine FOREIGN KEY (machine_id) REFERENCES Machines(machine_id),
    CONSTRAINT FK_Bookings_Operator FOREIGN KEY (operator_id) REFERENCES Operators(operator_id),
    CONSTRAINT CHK_Booking_Time CHECK (end_time > start_time)
);
GO

-- 1.6. Bảng HANDOVERS (Biên bản bàn giao)
IF OBJECT_ID('dbo.Handovers', 'U') IS NOT NULL DROP TABLE dbo.Handovers;
CREATE TABLE Handovers (
    handover_id INT IDENTITY(1,1) PRIMARY KEY,
    booking_id INT NOT NULL,
    handover_time DATETIME DEFAULT GETDATE(),
    machine_condition NVARCHAR(MAX) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('CHECK_IN', 'CHECK_OUT')),
    remarks NVARCHAR(MAX),
    CONSTRAINT FK_Handovers_Booking FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id) ON DELETE CASCADE
);
GO
-- 1.7. Bảng SESSIONS (Lưu trữ Tạm thời - Tự xóa/hết hạn theo phiên)
IF OBJECT_ID('dbo.UserSessions', 'U') IS NOT NULL DROP TABLE dbo.UserSessions;
CREATE TABLE UserSessions (
    session_id VARCHAR(100) PRIMARY KEY,
    user_id INT NOT NULL,
    ip_address VARCHAR(50),
    created_at DATETIME DEFAULT GETDATE(),
    expires_at DATETIME NOT NULL, -- Sau thời gian này Session hết giá trị (Garbage Collection sẽ xóa)
    CONSTRAINT FK_Sessions_User FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
GO

-- 2.1. Thêm 15 Users (1 Admin, 4 Owner, 5 Operator, 5 Farmer, Mk: 123456)
INSERT INTO Users (full_name, phone, password_hash, role) VALUES
(N'Nguyễn Khánh Duy (Admin)', '0900000001', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'ADMIN'),
(N'Nguyễn Văn Chủ', '0901234561', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'OWNER'),
(N'Trần Thị Loan', '0901234562', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'OWNER'),
(N'Lê Hoàng Nam', '0901234570', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'OWNER'),
(N'Phạm Văn Hợp Tác Xã', '0901234571', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'OWNER'),
(N'Lê Văn Tài Xế', '0901234563', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'OPERATOR'),
(N'Phạm Quốc Lái', '0901234564', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'OPERATOR'),
(N'Hoàng Văn Bằng', '0901234567', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'OPERATOR'),
(N'Ngô Thanh Lái', '0901234568', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'OPERATOR'),
(N'Đỗ Văn Chạy', '0901234569', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'OPERATOR'),
(N'Võ Văn Nông', '0901234565', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'FARMER'),
(N'Đặng Thị Ruộng', '0901234566', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'FARMER'),
(N'Trần Văn Lúa', '0901234572', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'FARMER'),
(N'Nguyễn Thị Đồng', '0901234573', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'FARMER'),
(N'Bùi Văn Cày', '0901234574', '$2a$12$EPNuPQzhSBWMUCem9kll/Ov5Sxgy7cFXdIpaSBF2Ee6OKgpNcS7w6', 'FARMER');

-- 2.2. Thêm 5 Operators (Tài xế)
INSERT INTO Operators (user_id, license_no, is_eligible, hourly_rate) VALUES
(6, 'GPLX-MAY-1001', 1, 50000.00), 
(7, 'GPLX-MAY-1002', 0, 45000.00),  -- Không đủ điều kiện (is_eligible = 0)
(8, 'GPLX-MAY-1003', 1, 60000.00),   
(9, 'GPLX-MAY-1004', 1, 55000.00),  
(10, 'GPLX-MAY-1005', 0, 40000.00); -- Không đủ điều kiện (is_eligible = 0)

-- 2.3. Thêm 12 Machines (Máy nông nghiệp)
INSERT INTO Machines (owner_id, name, type, hourly_rate, status) VALUES
(2, N'Máy Gặt Đập Kubota DC70', N'Máy gặt', 250000.00, 'AVAILABLE'),
(2, N'Máy Cày Yanmar YM351A', N'Máy cày', 180000.00, 'IN_MAINTENANCE'), 
(2, N'Máy Phun Thuốc DJI T40', N'Máy bay phun thuốc', 300000.00, 'AVAILABLE'),
(3, N'Máy Cày John Deere 5045D', N'Máy cày', 200000.00, 'AVAILABLE'),
(3, N'Máy Cấy Lúa Hamco 2 hàng', N'Máy cấy', 120000.00, 'RESERVED'),
(3, N'Máy Gặt Đập Yanmar AW70V', N'Máy gặt', 260000.00, 'AVAILABLE'),
(4, N'Máy Gặt Đập Kubota DC105X', N'Máy gặt', 320000.00, 'AVAILABLE'),
(4, N'Máy Cày New Holland TT45', N'Máy cày', 220000.00, 'AVAILABLE'),
(4, N'Máy Phun Thuốc DJI T30', N'Máy bay phun thuốc', 280000.00, 'IN_MAINTENANCE'),
(5, N'Máy Cấy Lúa Kubota SPW-48C', N'Máy cấy', 150000.00, 'AVAILABLE'),
(5, N'Máy Trộn Phân Bón Công Nghiệp', N'Máy hỗ trợ', 100000.00, 'AVAILABLE'),
(5, N'Máy Cày Iseki NT540', N'Máy cày', 190000.00, 'AVAILABLE');

-- 2.4. Thêm 10 Maintenances (Lịch bảo trì)
INSERT INTO Maintenances (machine_id, start_time, end_time, description, status) VALUES
(2, '2026-10-01 08:00:00', '2026-10-03 17:00:00', N'Bảo dưỡng động cơ và thay dầu định kỳ', 'COMPLETED'),
(9, '2026-10-20 08:00:00', '2026-10-25 17:00:00', N'Thay cánh quạt Flycam và bảo dưỡng pin', 'IN_PROGRESS'),
(1, '2026-11-01 07:00:00', '2026-11-01 12:00:00', N'Thay xích xe gặt', 'SCHEDULED'),
(4, '2026-11-05 08:00:00', '2026-11-06 17:00:00', N'Sửa hệ thống truyền động', 'SCHEDULED'),
(5, '2026-09-20 13:00:00', '2026-09-20 17:00:00', N'Thay xích máy cấy', 'COMPLETED'),
(7, '2026-11-10 08:00:00', '2026-11-12 17:00:00', N'Thay xích gặt và bảo trì hệ thống điều khiển', 'SCHEDULED'),
(8, '2026-09-10 08:00:00', '2026-09-10 12:00:00', N'Thay lốp xe cày', 'COMPLETED'),
(3, '2026-10-15 08:00:00', '2026-10-15 11:00:00', N'Kiểm tra định kỳ ra đa quét', 'COMPLETED'),
(10, '2026-11-15 08:00:00', '2026-11-15 17:00:00', N'Tra mỡ khay cấy', 'SCHEDULED'),
(12, '2026-11-20 08:00:00', '2026-11-21 17:00:00', N'Đổi bộ lọc dầu', 'SCHEDULED');

-- 2.5. Thêm 12 Bookings (Đơn đặt máy)
INSERT INTO Bookings (farmer_id, machine_id, operator_id, start_time, end_time, total_fee, status) VALUES
(11, 1, 1, '2026-10-05 07:00:00', '2026-10-05 11:00:00', 1200000.00, 'COMPLETED'),
(11, 3, 3, '2026-10-06 08:00:00', '2026-10-06 10:00:00', 720000.00, 'COMPLETED'),
(12, 4, NULL, '2026-10-07 07:00:00', '2026-10-07 17:00:00', 2000000.00, 'CONFIRMED'),
(12, 5, 4, '2026-10-08 07:00:00', '2026-10-08 12:00:00', 875000.00, 'COMPLETED'),
(11, 6, 1, '2026-09-25 07:00:00', '2026-09-25 15:00:00', 2480000.00, 'COMPLETED'),
(13, 7, 3, '2026-10-22 08:00:00', '2026-10-22 12:00:00', 1520000.00, 'CONFIRMED'),
(13, 8, NULL, '2026-10-23 07:00:00', '2026-10-23 11:00:00', 880000.00, 'PENDING'),
(14, 10, 4, '2026-10-24 08:00:00', '2026-10-24 16:00:00', 1640000.00, 'CONFIRMED'),
(14, 11, NULL, '2026-10-25 07:00:00', '2026-10-25 11:00:00', 400000.00, 'PENDING'),
(15, 12, 1, '2026-10-26 07:00:00', '2026-10-26 15:00:00', 1920000.00, 'PENDING'),
(15, 1, 3, '2026-10-27 08:00:00', '2026-10-27 12:00:00', 1240000.00, 'PENDING'),
(12, 3, 4, '2026-10-28 08:00:00', '2026-10-28 10:00:00', 710000.00, 'PENDING');

-- 2.6. Thêm 10 Handovers (Biên bản bàn giao)
INSERT INTO Handovers (booking_id, handover_time, machine_condition, type, remarks) VALUES
(1, '2026-10-05 06:50:00', N'Máy gặt đủ nhiên liệu, hoạt động tốt', 'CHECK_IN', N'Bàn giao đúng giờ'),
(1, '2026-10-05 11:05:00', N'Máy hoàn tất ruộng 5 công, không hỏng hóc', 'CHECK_OUT', N'Nông dân ký nghiệm thu'),
(2, '2026-10-06 07:55:00', N'Pin đầy, thuốc nạp sẵn', 'CHECK_IN', N'Tiến hành phun đợt 1'),
(2, '2026-10-06 10:05:00', N'Máy sạch sẽ, hoàn thành phun 2ha', 'CHECK_OUT', N'Hoàn tất công việc'),
(4, '2026-10-08 06:55:00', N'Máy cấy kiểm tra bình thường', 'CHECK_IN', N'Đã nhận máy'),
(4, '2026-10-08 12:10:00', N'Máy cấy xong 3 ha, lưỡi cấy tốt', 'CHECK_OUT', N'Xong nhiệm vụ'),
(5, '2026-09-25 06:50:00', N'Đầy bình dầu, lưỡi cắt sắc', 'CHECK_IN', N'Bàn giao cho xế Lê Văn Tài Xế'),
(5, '2026-09-25 15:10:00', N'Máy hoạt động tốt, đã vệ sinh xích', 'CHECK_OUT', N'Nông dân nghiệm thu hài lòng'),
(3, '2026-10-07 06:55:00', N'Máy cày bàn giao đủ dầu', 'CHECK_IN', N'Nông dân tự vận hành'),
(6, '2026-10-22 07:50:00', N'Máy gặt Kubota sẵn sàng', 'CHECK_IN', N'Nhận máy tại đồng ruộng');
GO

IF OBJECT_ID('dbo.sp_CreateBooking', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_CreateBooking;
GO
CREATE PROCEDURE sp_CreateBooking
    @farmer_id INT,
    @machine_id INT,
    @operator_id INT = NULL,
    @start_time DATETIME,
    @end_time DATETIME
AS
BEGIN
    SET NOCOUNT ON;

    -- 1. Kiểm tra máy có đang trong lịch Bảo trì trùng giờ không (UC10)
    IF EXISTS (
        SELECT 1 FROM Maintenances 
        WHERE machine_id = @machine_id 
          AND status IN ('SCHEDULED', 'IN_PROGRESS')
          AND (start_time < @end_time AND end_time > @start_time)
    )
    BEGIN
        RAISERROR(N'Lỗi: Máy đang trong lịch bảo trì/sửa chữa trong khoảng thời gian này!', 16, 1);
        RETURN;
    END

    -- 2. Kiểm tra máy có bị đơn Booking khác đặt trùng giờ không
    IF EXISTS (
        SELECT 1 FROM Bookings 
        WHERE machine_id = @machine_id 
          AND status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS')
          AND (start_time < @end_time AND end_time > @start_time)
    )
    BEGIN
        RAISERROR(N'Lỗi: Máy đã được đặt bởi người khác trong khoảng thời gian này!', 16, 1);
        RETURN;
    END

    -- 3. Nếu có chọn Tài xế, kiểm tra cờ is_eligible
    IF @operator_id IS NOT NULL
    BEGIN
        DECLARE @is_eligible BIT;
        SELECT @is_eligible = is_eligible FROM Operators WHERE operator_id = @operator_id;

        IF @is_eligible = 0 OR @is_eligible IS NULL
        BEGIN
            RAISERROR(N'Lỗi: Tài xế được chọn chưa đủ điều kiện vận hành (is_eligible = FALSE)!', 16, 1);
            RETURN;
        END
    END

    -- 4. Tính toán tổng chi phí (total_fee)
    DECLARE @machine_rate DECIMAL(10,2), @operator_rate DECIMAL(10,2) = 0.00;
    DECLARE @hours DECIMAL(10,2);
    
    SELECT @machine_rate = hourly_rate FROM Machines WHERE machine_id = @machine_id;
    IF @operator_id IS NOT NULL
        SELECT @operator_rate = hourly_rate FROM Operators WHERE operator_id = @operator_id;

    SET @hours = DATEDIFF(MINUTE, @start_time, @end_time) / 60.0;
    
    DECLARE @total_fee DECIMAL(12,2) = @hours * (@machine_rate + @operator_rate);

    -- 5. Thêm đơn đặt thành công
    INSERT INTO Bookings (farmer_id, machine_id, operator_id, start_time, end_time, total_fee, status)
    VALUES (@farmer_id, @machine_id, @operator_id, @start_time, @end_time, @total_fee, 'PENDING');

    PRINT N'Tạo đơn đặt máy thành công!';
END;
GO

IF OBJECT_ID('dbo.sp_CleanupTemporaryData', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_CleanupTemporaryData;
GO
CREATE PROCEDURE sp_CleanupTemporaryData
AS
BEGIN
    SET NOCOUNT ON;
    -- Xóa tất cả các phiên đăng nhập hết hạn (Expires_at < Hiện tại)
    DELETE FROM UserSessions WHERE expires_at < GETDATE();
    PRINT N'Đã dọn dẹp sạch toàn bộ dữ liệu phiên làm việc tạm thời hết hạn!';
END;
GO
USE QLMachine;
GO

-- 5.1. TRIGGER: Tự động chặn đặt lịch trùng Bảo trì hoặc Đơn khác ở cấp INSERT/UPDATE (UC10)
IF OBJECT_ID('dbo.trg_CheckBookingOverlap', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_CheckBookingOverlap;
GO
CREATE TRIGGER trg_CheckBookingOverlap
ON Bookings
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    -- A. Kiểm tra nếu thời gian đặt máy bị trùng với Lịch Bảo Trì đang có
    IF EXISTS (
        SELECT 1 
        FROM inserted i
        JOIN Maintenances m ON i.machine_id = m.machine_id
        WHERE m.status IN ('SCHEDULED', 'IN_PROGRESS')
          AND (i.start_time < m.end_time AND i.end_time > m.start_time)
    )
    BEGIN
        RAISERROR (N'Lỗi Trigger CSDL: Máy nông nghiệp này đang trong thời gian BẢO TRÌ/SỬA CHỮA!', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END

    -- B. Kiểm tra trùng lặp với đơn Booking khác đã được Duyệt hoặc đang Chờ
    IF EXISTS (
        SELECT 1 
        FROM inserted i
        JOIN Bookings b ON i.machine_id = b.machine_id AND i.booking_id <> b.booking_id
        WHERE b.status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS')
          AND (i.start_time < b.end_time AND i.end_time > b.start_time)
    )
    BEGIN
        RAISERROR (N'Lỗi Trigger CSDL: Máy nông nghiệp này đã có đơn đặt trùng khung giờ!', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END

    -- C. Kiểm tra cờ is_eligible của Tài xế (nếu có chọn tài xế)
    IF EXISTS (
        SELECT 1 
        FROM inserted i
        JOIN Operators o ON i.operator_id = o.operator_id
        WHERE i.operator_id IS NOT NULL 
          AND (o.is_eligible = 0 OR o.is_eligible IS NULL)
    )
    BEGIN
        RAISERROR (N'Lỗi Trigger CSDL: Tài xế được chọn chưa đủ điều kiện pháp lý/bằng cấp (is_eligible = FALSE)!', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END
END;
GO

-- 5.2. TRIGGER: Tự động đổi trạng thái Booking và Machine khi KÝ BIÊN BẢN BÀN GIAO (UC09)
IF OBJECT_ID('dbo.trg_AutoUpdateOnHandover', 'TR') IS NOT NULL DROP TRIGGER dbo.trg_AutoUpdateOnHandover;
GO
CREATE TRIGGER trg_AutoUpdateOnHandover
ON Handovers
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    -- Nếu là Biên bản Check-in (Nhận máy): Chuyển Booking sang IN_PROGRESS, Machine sang RESERVED/IN_USE
    UPDATE b
    SET b.status = 'IN_PROGRESS'
    FROM Bookings b
    JOIN inserted i ON b.booking_id = i.booking_id
    WHERE i.type = 'CHECK_IN';

    -- Nếu là Biên bản Check-out (Trả máy): Chuyển Booking sang COMPLETED, Mở máy về AVAILABLE
    UPDATE b
    SET b.status = 'COMPLETED'
    FROM Bookings b
    JOIN inserted i ON b.booking_id = i.booking_id
    WHERE i.type = 'CHECK_OUT';

    UPDATE m
    SET m.status = 'AVAILABLE'
    FROM Machines m
    JOIN Bookings b ON m.machine_id = b.machine_id
    JOIN inserted i ON b.booking_id = i.booking_id
    WHERE i.type = 'CHECK_OUT';
END;
GO


-- 6.1. FUNCTION: Hàm tính tổng chi phí thuê dự toán tự động
IF OBJECT_ID('dbo.fn_CalculateTotalFee', 'FN') IS NOT NULL DROP FUNCTION dbo.fn_CalculateTotalFee;
GO
CREATE FUNCTION fn_CalculateTotalFee
(
    @machine_id INT,
    @operator_id INT,
    @start_time DATETIME,
    @end_time DATETIME
)
RETURNS DECIMAL(12,2)
AS
BEGIN
    DECLARE @machine_rate DECIMAL(10,2) = 0.00;
    DECLARE @operator_rate DECIMAL(10,2) = 0.00;
    DECLARE @hours DECIMAL(10,2);
    DECLARE @total DECIMAL(12,2);

    SELECT @machine_rate = ISNULL(hourly_rate, 0.00) FROM Machines WHERE machine_id = @machine_id;
    
    IF @operator_id IS NOT NULL
        SELECT @operator_rate = ISNULL(hourly_rate, 0.00) FROM Operators WHERE operator_id = @operator_id;

    SET @hours = DATEDIFF(MINUTE, @start_time, @end_time) / 60.0;
    IF @hours <= 0 SET @hours = 1; -- Tối thiểu 1 giờ

    SET @total = @hours * (@machine_rate + @operator_rate);
    RETURN @total;
END;
GO

-- 6.2. FUNCTION: Kiểm tra tính sẵn sàng của Máy trong khoảng thời gian (Trả về 1 = Rảnh, 0 = Bận)
IF OBJECT_ID('dbo.fn_IsMachineAvailable', 'FN') IS NOT NULL DROP FUNCTION dbo.fn_IsMachineAvailable;
GO
CREATE FUNCTION fn_IsMachineAvailable
(
    @machine_id INT,
    @start_time DATETIME,
    @end_time DATETIME
)
RETURNS BIT
AS
BEGIN
    -- Kiểm tra vướng lịch bảo trì
    IF EXISTS (
        SELECT 1 FROM Maintenances 
        WHERE machine_id = @machine_id 
          AND status IN ('SCHEDULED', 'IN_PROGRESS')
          AND (start_time < @end_time AND end_time > @start_time)
    )
        RETURN 0;

    -- Kiểm tra vướng đơn Booking khác
    IF EXISTS (
        SELECT 1 FROM Bookings 
        WHERE machine_id = @machine_id 
          AND status IN ('PENDING', 'CONFIRMED', 'IN_PROGRESS')
          AND (start_time < @end_time AND end_time > @start_time)
    )
        RETURN 0;

    RETURN 1;
END;
GO


-- 7.1. VIEW: Danh mục Máy sẵn sàng cho thuê kèm thông tin Chủ máy
IF OBJECT_ID('dbo.vw_AvailableMachines', 'V') IS NOT NULL DROP VIEW dbo.vw_AvailableMachines;
GO
CREATE VIEW vw_AvailableMachines AS
SELECT 
    m.machine_id,
    m.name AS machine_name,
    m.type AS machine_type,
    m.hourly_rate AS machine_hourly_rate,
    m.status AS machine_status,
    u.full_name AS owner_name,
    u.phone AS owner_phone
FROM Machines m
JOIN Users u ON m.owner_id = u.user_id
WHERE m.status = 'AVAILABLE';
GO

-- 7.2. VIEW: Báo cáo Doanh thu và Chi tiết Đơn hàng Tổng hợp
IF OBJECT_ID('dbo.vw_BookingSummaryReport', 'V') IS NOT NULL DROP VIEW dbo.vw_BookingSummaryReport;
GO
CREATE VIEW vw_BookingSummaryReport AS
SELECT 
    b.booking_id,
    f.full_name AS farmer_name,
    f.phone AS farmer_phone,
    m.name AS machine_name,
    o_user.full_name AS operator_name,
    b.start_time,
    b.end_time,
    DATEDIFF(MINUTE, b.start_time, b.end_time) / 60.0 AS total_hours,
    b.total_fee,
    b.status AS booking_status
FROM Bookings b
JOIN Users f ON b.farmer_id = f.user_id
JOIN Machines m ON b.machine_id = m.machine_id
LEFT JOIN Operators o ON b.operator_id = o.operator_id
LEFT JOIN Users o_user ON o.user_id = o_user.user_id;
GO
