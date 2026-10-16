
IF DB_ID(N'QuanLyHoSoBenhAn') IS NULL 
    CREATE DATABASE QuanLyHoSoBenhAn;
GO

USE QuanLyHoSoBenhAn;
GO

IF OBJECT_ID('HoaDonVienPhi', 'U') IS NOT NULL DROP TABLE HoaDonVienPhi;
IF OBJECT_ID('ChiTietToaThuoc', 'U') IS NOT NULL DROP TABLE ChiTietToaThuoc;
IF OBJECT_ID('ToaThuoc', 'U') IS NOT NULL DROP TABLE ToaThuoc;
IF OBJECT_ID('Thuoc', 'U') IS NOT NULL DROP TABLE Thuoc;
IF OBJECT_ID('XetNhiem', 'U') IS NOT NULL DROP TABLE XetNhiem;
IF OBJECT_ID('PhieuKhamBenh', 'U') IS NOT NULL DROP TABLE PhieuKhamBenh;
IF OBJECT_ID('LichHenKham', 'U') IS NOT NULL DROP TABLE LichHenKham;
IF OBJECT_ID('LePhiNam', 'U') IS NOT NULL DROP TABLE LePhiNam;
IF OBJECT_ID('HoSoBenhAn', 'U') IS NOT NULL DROP TABLE HoSoBenhAn;
IF OBJECT_ID('BenhNhan', 'U') IS NOT NULL DROP TABLE BenhNhan;
IF OBJECT_ID('BacSi', 'U') IS NOT NULL DROP TABLE BacSi;
IF OBJECT_ID('KhoaDieuTri', 'U') IS NOT NULL DROP TABLE KhoaDieuTri;
GO

CREATE TABLE BenhNhan (
    MaBenhNhan VARCHAR(20) NOT NULL PRIMARY KEY,
    HoTen NVARCHAR(120) NOT NULL,
    NgaySinh DATE NOT NULL,
    DiaChi NVARCHAR(250) NOT NULL,
    SoDienThoai VARCHAR(20) NOT NULL CONSTRAINT UQ_BenhNhan_SDT UNIQUE,
    Anh VARCHAR(250) NULL
);

CREATE TABLE HoSoBenhAn (
    MaHoSo VARCHAR(20) NOT NULL PRIMARY KEY,
    MaBenhNhan VARCHAR(20) NOT NULL CONSTRAINT UQ_HSBA_BenhNhan UNIQUE,
    NgayLap DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    NgayHetHanLePhi DATE NOT NULL,
    TrangThai NVARCHAR(50) NOT NULL DEFAULT N'Đang hoạt động',
    CONSTRAINT FK_HSBA_BenhNhan FOREIGN KEY (MaBenhNhan) REFERENCES BenhNhan(MaBenhNhan) ON DELETE CASCADE
);

CREATE TABLE LePhiNam (
    MaLePhi VARCHAR(20) NOT NULL PRIMARY KEY,
    MaHoSo VARCHAR(20) NOT NULL,
    Nam INT NOT NULL CHECK(Nam >= 2020),
    SoTien DECIMAL(18,2) NOT NULL CHECK(SoTien > 0),
    NgayDong DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT FK_LPN_HoSo FOREIGN KEY (MaHoSo) REFERENCES HoSoBenhAn(MaHoSo),
    CONSTRAINT UQ_HoSo_Nam UNIQUE (MaHoSo, Nam)
);

CREATE TABLE LichHenKham (
    MaLichHen VARCHAR(20) NOT NULL PRIMARY KEY,
    MaHoSo VARCHAR(20) NOT NULL,
    NgayGioHen DATETIME2 NOT NULL,
    Quy INT NOT NULL CHECK(Quy BETWEEN 1 AND 4),
    Nam INT NOT NULL CHECK(Nam >= 2020),
    TrangThai NVARCHAR(50) NOT NULL DEFAULT N'Đã đặt lịch'
        CHECK (TrangThai IN (N'Đã đặt lịch', N'Đã khám', N'Đã hủy')),
    CONSTRAINT FK_LHK_HoSo FOREIGN KEY (MaHoSo) REFERENCES HoSoBenhAn(MaHoSo),
    CONSTRAINT UQ_HoSo_Quy_Nam UNIQUE (MaHoSo, Quy, Nam)
);

CREATE TABLE KhoaDieuTri (
    MaKhoa VARCHAR(20) NOT NULL PRIMARY KEY,
    TenKhoa NVARCHAR(150) NOT NULL CONSTRAINT UQ_Khoa_Ten UNIQUE,
    ViTri NVARCHAR(200) NULL
);

CREATE TABLE BacSi (
    MaBacSi VARCHAR(20) NOT NULL PRIMARY KEY,
    HoTen NVARCHAR(120) NOT NULL,
    SoDienThoai VARCHAR(20) NULL,
    ChuyenKhoa NVARCHAR(100) NOT NULL
);

CREATE TABLE PhieuKhamBenh (
    MaPhieuKham VARCHAR(20) NOT NULL PRIMARY KEY,
    MaHoSo VARCHAR(20) NOT NULL,
    MaBacSi VARCHAR(20) NOT NULL,
    MaKhoa VARCHAR(20) NOT NULL,
    NgayGioKham DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    LoaiKham NVARCHAR(50) NOT NULL CHECK(LoaiKham IN (N'Khám bệnh', N'Khám định kỳ')),
    TrieuChung NVARCHAR(1000) NULL, 
    KetLuanBenh NVARCHAR(1000) NULL,
    TrangThaiThanhToan BIT NOT NULL DEFAULT 0, 
    TrangThaiKham NVARCHAR(50) NOT NULL DEFAULT N'Đang khám'
        CHECK (TrangThaiKham IN (N'Đang khám', N'Chờ xét nghiệm', N'Chờ thanh toán', N'Hoàn tất')),
    CONSTRAINT FK_PKB_HoSo FOREIGN KEY (MaHoSo) REFERENCES HoSoBenhAn(MaHoSo),
    CONSTRAINT FK_PKB_BacSi FOREIGN KEY (MaBacSi) REFERENCES BacSi(MaBacSi),
    CONSTRAINT FK_PKB_Khoa FOREIGN KEY (MaKhoa) REFERENCES KhoaDieuTri(MaKhoa)
);

CREATE TABLE XetNhiem (
    MaXetNhiem VARCHAR(20) NOT NULL PRIMARY KEY,
    MaPhieuKham VARCHAR(20) NOT NULL,
    TenXetNhiem NVARCHAR(200) NOT NULL,
    KetQua NVARCHAR(1000) NULL,
    ChiPhi DECIMAL(18,2) NOT NULL CHECK(ChiPhi >= 0),
    CONSTRAINT FK_XN_PhieuKham FOREIGN KEY (MaPhieuKham) REFERENCES PhieuKhamBenh(MaPhieuKham)
);

CREATE TABLE Thuoc (
    MaThuoc VARCHAR(20) NOT NULL PRIMARY KEY,
    TenThuoc NVARCHAR(180) NOT NULL CONSTRAINT UQ_Thuoc_Ten UNIQUE,
    DonViTinh NVARCHAR(30) NOT NULL,
    DonGia DECIMAL(18,2) NOT NULL CHECK(DonGia >= 0),
    ChongChiDinh NVARCHAR(500) NULL
);

CREATE TABLE ToaThuoc (
    MaToa VARCHAR(20) NOT NULL PRIMARY KEY,
    MaPhieuKham VARCHAR(20) NOT NULL CONSTRAINT UQ_Toa_PhieuKham UNIQUE,
    NgayLap DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    GhiChu NVARCHAR(500) NULL,
    CONSTRAINT FK_TT_PhieuKham FOREIGN KEY (MaPhieuKham) REFERENCES PhieuKhamBenh(MaPhieuKham)
);

CREATE TABLE ChiTietToaThuoc (
    MaToa VARCHAR(20) NOT NULL,
    MaThuoc VARCHAR(20) NOT NULL,
    SoLuong INT NOT NULL CHECK(SoLuong > 0),
    LieuDung NVARCHAR(250) NOT NULL,
    DonGia DECIMAL(18,2) NOT NULL CHECK(DonGia >= 0),
    PRIMARY KEY (MaToa, MaThuoc),
    CONSTRAINT FK_CTTT_Toa FOREIGN KEY (MaToa) REFERENCES ToaThuoc(MaToa),
    CONSTRAINT FK_CTTT_Thuoc FOREIGN KEY (MaThuoc) REFERENCES Thuoc(MaThuoc)
);

CREATE TABLE HoaDonVienPhi (
    MaHoaDon VARCHAR(20) NOT NULL PRIMARY KEY,
    MaPhieuKham VARCHAR(20) NOT NULL CONSTRAINT UQ_HD_PhieuKham UNIQUE,
    NgayThanhToan DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    TienXetNhiem DECIMAL(18,2) NOT NULL CHECK(TienXetNhiem >= 0),
    TienThuoc DECIMAL(18,2) NOT NULL CHECK(TienThuoc >= 0),
    TienKham DECIMAL(18,2) NOT NULL DEFAULT 0 CHECK(TienKham >= 0),
    TongTien AS (TienKham + TienXetNhiem + TienThuoc), -- Cột tính toán tự động
    TrangThai NVARCHAR(50) NOT NULL DEFAULT N'Đã thanh toán',
    CONSTRAINT FK_HDVP_PhieuKham FOREIGN KEY (MaPhieuKham) REFERENCES PhieuKhamBenh(MaPhieuKham)
);
GO

INSERT INTO BenhNhan (MaBenhNhan, HoTen, NgaySinh, DiaChi, SoDienThoai, Anh) VALUES
('BN001', N'Nguyễn Văn An', '1985-05-12', N'123 Nguyễn Trãi, Q.5, TP.HCM', '0908123456', 'bn001.jpg'),
('BN002', N'Trần Thị Bích', '1990-10-20', N'456 Lê Hồng Phong, Q.10, TP.HCM', '0918654321', 'bn002.jpg'),
('BN003', N'Lê Hoàng Nam', '1978-03-15', N'789 Võ Văn Tần, Q.3, TP.HCM', '0937888999', 'bn003.jpg');

INSERT INTO HoSoBenhAn (MaHoSo, MaBenhNhan, NgayLap, NgayHetHanLePhi, TrangThai) VALUES
('HS001', 'BN001', '2025-01-10', '2027-01-10', N'Đang hoạt động'),
('HS002', 'BN002', '2025-06-15', '2026-06-15', N'Đang hoạt động'),
('HS003', 'BN003', '2026-02-01', '2027-02-01', N'Đang hoạt động');

INSERT INTO LePhiNam (MaLePhi, MaHoSo, Nam, SoTien, NgayDong) VALUES
('LP001', 'HS001', 2025, 500000, '2025-01-10 08:30:00'),
('LP002', 'HS001', 2026, 500000, '2026-01-05 09:00:00'),
('LP003', 'HS002', 2025, 500000, '2025-06-15 10:15:00'),
('LP004', 'HS003', 2026, 500000, '2026-02-01 14:00:00');

INSERT INTO KhoaDieuTri (MaKhoa, TenKhoa, ViTri) VALUES
('K01', N'Khoa Nội Tổng hợp', N'Tầng 2 - Khu A'),
('K02', N'Khoa Tim mạch', N'Tầng 3 - Khu A'),
('K03', N'Khoa Khám bệnh Định kỳ', N'Tầng 1 - Khu B');

INSERT INTO BacSi (MaBacSi, HoTen, SoDienThoai, ChuyenKhoa) VALUES
('BS01', N'BS. Phạm Minh Tuấn', '0903111222', N'Nội khoa'),
('BS02', N'BS. Đỗ Thu Hà', '0903333444', N'Tim mạch'),
('BS03', N'BS. Nguyễn Thanh Tùng', '0903555666', N'Đa khoa');

INSERT INTO LichHenKham (MaLichHen, MaHoSo, NgayGioHen, Quy, Nam, TrangThai) VALUES
('LH001', 'HS001', '2026-03-15 09:00:00', 1, 2026, N'Đã khám'),
('LH002', 'HS002', '2026-06-20 10:00:00', 2, 2026, N'Đã đặt lịch');

INSERT INTO PhieuKhamBenh (MaPhieuKham, MaHoSo, MaBacSi, MaKhoa, NgayGioKham, LoaiKham, TrieuChung, KetLuanBenh, TrangThaiThanhToan, TrangThaiKham) VALUES
('PK001', 'HS001', 'BS01', 'K01', '2026-02-10 08:30:00', N'Khám bệnh', N'Thân nhiệt 38.5°C, Huyết áp 120/80 mmHg, nhức đầu, ho dữ dội', N'Viêm họng cấp', 1, N'Hoàn tất'),
('PK002', 'HS001', 'BS03', 'K03', '2026-03-15 09:15:00', N'Khám định kỳ', N'Huyết áp 115/75, Thân nhiệt 36.8°C, bình thường', N'Khỏe mạnh, chưa phát hiện mầm bệnh', 1, N'Hoàn tất'),
('PK003', 'HS002', 'BS02', 'K02', '2026-10-10 14:00:00', N'Khám bệnh', N'Huyết áp 140/90, đau nhói ngực trái, mệt mỏi', N'Tăng huyết áp độ 1', 0, N'Chờ thanh toán');

INSERT INTO XetNhiem (MaXetNhiem, MaPhieuKham, TenXetNhiem, KetQua, ChiPhi) VALUES
('XN001', 'PK001', N'Xét nghiệm Công thức máu', N'Bạch cầu tăng nhẹ (11.2 K/uL)', 150000),
('XN002', 'PK003', N'Điện tâm đồ (ECG)', N'Nhịp xoang nhanh 95 lần/phút', 200000),
('XN003', 'PK003', N'Xét nghiệm Siêu âm tim', N'Chức năng tâm thu thất trái EF 62%', 350000);

INSERT INTO Thuoc (MaThuoc, TenThuoc, DonViTinh, DonGia, ChongChiDinh) VALUES
('T01', N'Paracetamol 500mg', N'Viên', 1500, N'Mẫn cảm với Paracetamol, suy gan nặng'),
('T02', N'Amoxicillin 500mg', N'Viên', 3000, N'Dị ứng nhóm Penicillin'),
('T03', N'Amlodipin 5mg', N'Viên', 2500, N'Hạ huyết áp nặng, hẹp van động mạch chủ'),
('T04', N'Vitamin C 500mg', N'Viên', 1000, N'Sỏi thận oxalat');

INSERT INTO ToaThuoc (MaToa, MaPhieuKham, NgayLap, GhiChu) VALUES
('TT001', 'PK001', '2026-02-10', N'Uống sau khi ăn, nghỉ ngơi đầy đủ'),
('TT002', 'PK003', '2026-10-10', N'Uống buổi sáng, theo dõi huyết áp hằng ngày');

INSERT INTO ChiTietToaThuoc (MaToa, MaThuoc, SoLuong, LieuDung, DonGia) VALUES
('TT001', 'T01', 15, N'Sáng 1v, Trưa 1v, Tối 1v', 1500),
('TT001', 'T02', 20, N'Sáng 2v, Tối 2v', 3000),
('TT001', 'T04', 10, N'Sáng 1v', 1000),
('TT002', 'T03', 30, N'Sáng 1v duy nhất', 2500);

INSERT INTO HoaDonVienPhi (MaHoaDon, MaPhieuKham, NgayThanhToan, TienKham, TienXetNhiem, TienThuoc, TrangThai) VALUES
('HD001', 'PK001', '2026-02-10 10:00:00', 100000, 150000, 92500, N'Đã thanh toán'),
('HD002', 'PK002', '2026-03-15 11:30:00', 0, 0, 0, N'Đã thanh toán'); 
GO

PRINT N'Đã khởi tạo thành công CSDL QuanLyHoSoBenhAn và chèn dữ liệu mẫu!';