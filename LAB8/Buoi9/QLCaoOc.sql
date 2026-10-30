
/* =====================================================================
   BÀI 4 - QUẢN LÝ CHO THUÊ VĂN PHÒNG Ở CAO ỐC
   Script tạo CSDL QuanLyChoThueCaoOc (SQL Server / LocalDB)
   ===================================================================== */
IF DB_ID(N'QuanLyChoThueCaoOc') IS NULL CREATE DATABASE QuanLyChoThueCaoOc;
GO
USE QuanLyChoThueCaoOc;
GO

IF OBJECT_ID('BienBanViPham','U') IS NOT NULL DROP TABLE BienBanViPham;
IF OBJECT_ID('PhieuKiemDinh','U') IS NOT NULL DROP TABLE PhieuKiemDinh;
IF OBJECT_ID('TheGiuXe','U') IS NOT NULL DROP TABLE TheGiuXe;
IF OBJECT_ID('NhanVienCongTy','U') IS NOT NULL DROP TABLE NhanVienCongTy;
IF OBJECT_ID('HoaDonDichVu','U') IS NOT NULL DROP TABLE HoaDonDichVu;
IF OBJECT_ID('DichVu','U') IS NOT NULL DROP TABLE DichVu;
IF OBJECT_ID('HoaDonTienNha','U') IS NOT NULL DROP TABLE HoaDonTienNha;
IF OBJECT_ID('PhieuGiaHan','U') IS NOT NULL DROP TABLE PhieuGiaHan;
IF OBJECT_ID('ChiTietHopDong','U') IS NOT NULL DROP TABLE ChiTietHopDong;
IF OBJECT_ID('HopDongThue','U') IS NOT NULL DROP TABLE HopDongThue;
IF OBJECT_ID('KhachHang','U') IS NOT NULL DROP TABLE KhachHang;
IF OBJECT_ID('Phong','U') IS NOT NULL DROP TABLE Phong;
IF OBJECT_ID('Tang','U') IS NOT NULL DROP TABLE Tang;
GO

CREATE TABLE Tang (
    MaTang varchar(20) NOT NULL PRIMARY KEY,
    TenTang nvarchar(50) NOT NULL,
    ViTri nvarchar(100) NULL
);

CREATE TABLE Phong (
    MaPhong varchar(20) NOT NULL PRIMARY KEY,
    MaTang varchar(20) NOT NULL FOREIGN KEY REFERENCES Tang(MaTang),
    TenPhong nvarchar(100) NOT NULL,
    DienTich decimal(18,2) NOT NULL CHECK(DienTich > 0),
    HuongPhong nvarchar(50) NULL,
    DonGiaThue decimal(18,2) NOT NULL CHECK(DonGiaThue >= 0),
    TrangThai nvarchar(40) NOT NULL DEFAULT N'Trống'
);

CREATE TABLE KhachHang (
    MaKH varchar(20) NOT NULL PRIMARY KEY,
    TenCongTy nvarchar(180) NOT NULL,
    NguoiDaiDien nvarchar(120) NOT NULL,
    DienThoai varchar(20) NOT NULL,
    MaSoThue varchar(30) NULL,
    DiaChi nvarchar(250) NULL
);

CREATE TABLE HopDongThue (
    SoHopDong varchar(20) NOT NULL PRIMARY KEY,
    MaKH varchar(20) NOT NULL FOREIGN KEY REFERENCES KhachHang(MaKH),
    NgayLap datetime2 NOT NULL DEFAULT SYSDATETIME(),
    NgayBatDau date NOT NULL,
    SoThangThue int NOT NULL CHECK(SoThangThue >= 6), -- BR01
    NgayKetHan date NOT NULL,
    TienCoc decimal(18,2) NOT NULL DEFAULT 0,
    TrangThai nvarchar(40) NOT NULL DEFAULT N'Còn hiệu lực'
);

CREATE TABLE ChiTietHopDong (
    SoHopDong varchar(20) NOT NULL FOREIGN KEY REFERENCES HopDongThue(SoHopDong),
    MaPhong varchar(20) NOT NULL FOREIGN KEY REFERENCES Phong(MaPhong),
    DonGiaThueTaiThoiDiem decimal(18,2) NOT NULL CHECK(DonGiaThueTaiThoiDiem >= 0),
    PRIMARY KEY (SoHopDong, MaPhong)
);

CREATE TABLE PhieuGiaHan (
    SoPhieuGH varchar(20) NOT NULL PRIMARY KEY,
    SoHopDong varchar(20) NOT NULL FOREIGN KEY REFERENCES HopDongThue(SoHopDong),
    MaPhong varchar(20) NOT NULL FOREIGN KEY REFERENCES Phong(MaPhong),
    NgayLap date NOT NULL DEFAULT CAST(GETDATE() AS date),
    SoThangGiaHan int NOT NULL CHECK(SoThangGiaHan > 0),
    NgayKetHanMoi date NOT NULL,
    GhiChu nvarchar(300) NULL
);

CREATE TABLE HoaDonTienNha (
    SoHDNha varchar(20) NOT NULL PRIMARY KEY,
    SoHopDong varchar(20) NOT NULL FOREIGN KEY REFERENCES HopDongThue(SoHopDong),
    ThangNam varchar(7) NOT NULL, -- Format: MM/YYYY
    NgayLap date NOT NULL,
    SoTien decimal(18,2) NOT NULL CHECK(SoTien >= 0),
    DaThanhToan bit NOT NULL DEFAULT 0
);

CREATE TABLE DichVu (
    MaDV varchar(20) NOT NULL PRIMARY KEY,
    TenDichVu nvarchar(100) NOT NULL,
    DonViTinh nvarchar(30) NOT NULL,
    DonGia decimal(18,2) NOT NULL CHECK(DonGia >= 0)
);

CREATE TABLE HoaDonDichVu (
    SoHDDV varchar(20) NOT NULL PRIMARY KEY,
    SoHopDong varchar(20) NOT NULL FOREIGN KEY REFERENCES HopDongThue(SoHopDong),
    MaDV varchar(20) NOT NULL FOREIGN KEY REFERENCES DichVu(MaDV),
    ThangNam varchar(7) NOT NULL,
    NgayLap date NOT NULL,
    ChiSoCu decimal(18,2) NOT NULL DEFAULT 0,
    ChiSoMoi decimal(18,2) NOT NULL DEFAULT 0,
    TongTien decimal(18,2) NOT NULL CHECK(TongTien >= 0),
    DaThanhToan bit NOT NULL DEFAULT 0
);

CREATE TABLE NhanVienCongTy (
    MaNV varchar(20) NOT NULL PRIMARY KEY,
    MaKH varchar(20) NOT NULL FOREIGN KEY REFERENCES KhachHang(MaKH),
    MaPhong varchar(20) NOT NULL FOREIGN KEY REFERENCES Phong(MaPhong),
    HoTen nvarchar(120) NOT NULL,
    HinhAnh nvarchar(500) NULL,
    ChucVu nvarchar(100) NULL,
    DienThoai varchar(20) NULL
);

CREATE TABLE TheGiuXe (
    MaThe varchar(20) NOT NULL PRIMARY KEY,
    MaNV varchar(20) NOT NULL UNIQUE FOREIGN KEY REFERENCES NhanVienCongTy(MaNV), -- BR07
    BienSoXe varchar(30) NOT NULL,
    LoaiXe nvarchar(50) NOT NULL,
    NgayCap date NOT NULL DEFAULT CAST(GETDATE() AS date)
);

CREATE TABLE PhieuKiemDinh (
    MaPhieuKD varchar(20) NOT NULL PRIMARY KEY,
    MaPhong varchar(20) NOT NULL FOREIGN KEY REFERENCES Phong(MaPhong),
    NgayKiemTra date NOT NULL,
    TinhTrangPhong nvarchar(500) NOT NULL,
    CoViPham bit NOT NULL DEFAULT 0
);

CREATE TABLE BienBanViPham (
    MaBB varchar(20) NOT NULL PRIMARY KEY,
    MaPhieuKD varchar(20) NOT NULL FOREIGN KEY REFERENCES PhieuKiemDinh(MaPhieuKD),
    NgayLap date NOT NULL DEFAULT CAST(GETDATE() AS date),
    HanhViViPham nvarchar(500) NOT NULL,
    SoTienPhat decimal(18,2) NOT NULL CHECK(SoTienPhat >= 0),
    DaNopPhat bit NOT NULL DEFAULT 0
);
GO
