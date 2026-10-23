
IF DB_ID(N'QuanLyNhanSu') IS NULL CREATE DATABASE QuanLyNhanSu;
GO
USE QuanLyNhanSu;
GO

-- Xóa bảng theo thứ tự ngược lại để tránh lỗi khóa ngoại khi chạy lại script
IF OBJECT_ID('BangLuong','U') IS NOT NULL DROP TABLE BangLuong;
IF OBJECT_ID('ChiTietChamCong','U') IS NOT NULL DROP TABLE ChiTietChamCong;
IF OBJECT_ID('BangChamCong','U') IS NOT NULL DROP TABLE BangChamCong;
IF OBJECT_ID('ChiTietPhuTroi','U') IS NOT NULL DROP TABLE ChiTietPhuTroi;
IF OBJECT_ID('PhieuPhuTroi','U') IS NOT NULL DROP TABLE PhieuPhuTroi;
IF OBJECT_ID('KeHoachPhanCong','U') IS NOT NULL DROP TABLE KeHoachPhanCong;
IF OBJECT_ID('PhieuThuNopPhat','U') IS NOT NULL DROP TABLE PhieuThuNopPhat;
IF OBJECT_ID('QuyetDinhKyLuat','U') IS NOT NULL DROP TABLE QuyetDinhKyLuat;
IF OBJECT_ID('PhieuChiKhenThuong','U') IS NOT NULL DROP TABLE PhieuChiKhenThuong;
IF OBJECT_ID('QuyetDinhKhenThuong','U') IS NOT NULL DROP TABLE QuyetDinhKhenThuong;
IF OBJECT_ID('QuyetDinhLuong','U') IS NOT NULL DROP TABLE QuyetDinhLuong;
IF OBJECT_ID('QuyetDinhChucVu','U') IS NOT NULL DROP TABLE QuyetDinhChucVu;
IF OBJECT_ID('HoSoBaoHiem','U') IS NOT NULL DROP TABLE HoSoBaoHiem;
IF OBJECT_ID('HopDong','U') IS NOT NULL DROP TABLE HopDong;
IF OBJECT_ID('QuyetDinhTuyenDung','U') IS NOT NULL DROP TABLE QuyetDinhTuyenDung;
IF OBJECT_ID('NhanVien','U') IS NOT NULL DROP TABLE NhanVien;
IF OBJECT_ID('CaLamViec','U') IS NOT NULL DROP TABLE CaLamViec;
IF OBJECT_ID('ChucVu','U') IS NOT NULL DROP TABLE ChucVu;
IF OBJECT_ID('PhongBan','U') IS NOT NULL DROP TABLE PhongBan;
GO

-- 1. Nhóm Danh mục
CREATE TABLE PhongBan(
    MaPB varchar(20) NOT NULL PRIMARY KEY,
    TenPB nvarchar(150) NOT NULL
);

CREATE TABLE ChucVu(
    MaCV varchar(20) NOT NULL PRIMARY KEY,
    TenCV nvarchar(150) NOT NULL,
    MucPhuCap decimal(18,2) NOT NULL CHECK(MucPhuCap >= 0)
);

CREATE TABLE CaLamViec(
    MaCa varchar(20) NOT NULL PRIMARY KEY,
    TenCa nvarchar(100) NOT NULL,
    GioBatDau time NOT NULL,
    GioKetThuc time NOT NULL
);

-- 2. Nhóm Nhân sự & Hợp đồng
CREATE TABLE NhanVien(
    MaNV varchar(20) NOT NULL PRIMARY KEY,
    HoTen nvarchar(120) NOT NULL,
    NgaySinh date NULL,
    DiaChi nvarchar(250) NULL,
    MaPB varchar(20) NOT NULL,
    CONSTRAINT FK_NV_PhongBan FOREIGN KEY(MaPB) REFERENCES PhongBan(MaPB)
);

CREATE TABLE QuyetDinhTuyenDung(
    SoQDTD varchar(20) NOT NULL PRIMARY KEY,
    NgayQD date NOT NULL,
    ThoiGianThuViec int NOT NULL CHECK(ThoiGianThuViec > 0), -- tính bằng tháng hoặc tuần
    MucLuongThuViec decimal(18,2) NOT NULL CHECK(MucLuongThuViec >= 0),
    DieuKhoan nvarchar(1000) NULL,
    MaNV varchar(20) NOT NULL,
    CONSTRAINT FK_QDTD_NhanVien FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV)
);

CREATE TABLE HopDong(
    SoHD varchar(20) NOT NULL PRIMARY KEY,
    LoaiHD nvarchar(50) NOT NULL, -- Dài hạn / Ngắn hạn
    NgayKy date NOT NULL,
    NgayHetHan date NULL,
    MaNV varchar(20) NOT NULL,
    CONSTRAINT FK_HopDong_NhanVien FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV)
);

CREATE TABLE HoSoBaoHiem(
    MaHS varchar(20) NOT NULL PRIMARY KEY,
    SoHD varchar(20) NOT NULL,
    MucDongBHYT decimal(18,2) NOT NULL CHECK(MucDongBHYT >= 0),
    MucDongBHXH decimal(18,2) NOT NULL CHECK(MucDongBHXH >= 0),
    CONSTRAINT FK_HSBH_HopDong FOREIGN KEY(SoHD) REFERENCES HopDong(SoHD)
);

CREATE TABLE QuyetDinhChucVu(
    SoQDCV varchar(20) NOT NULL PRIMARY KEY,
    NgayHieuLuc date NOT NULL,
    MaCV varchar(20) NOT NULL,
    MaNV varchar(20) NOT NULL,
    CONSTRAINT FK_QDCV_ChucVu FOREIGN KEY(MaCV) REFERENCES ChucVu(MaCV),
    CONSTRAINT FK_QDCV_NhanVien FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV)
);

CREATE TABLE QuyetDinhLuong(
    SoQDL varchar(20) NOT NULL PRIMARY KEY,
    NgayHieuLuc date NOT NULL,
    MucLuongCoBan decimal(18,2) NOT NULL CHECK(MucLuongCoBan >= 0),
    PhuCapLuong decimal(18,2) NOT NULL DEFAULT 0 CHECK(PhuCapLuong >= 0),
    PhuCapDocHai decimal(18,2) NOT NULL DEFAULT 0 CHECK(PhuCapDocHai >= 0),
    MaNV varchar(20) NOT NULL,
    CONSTRAINT FK_QDL_NhanVien FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV)
);

-- 3. Nhóm Khen thưởng & Kỷ luật
CREATE TABLE QuyetDinhKhenThuong(
    SoQDKT varchar(20) NOT NULL PRIMARY KEY,
    NgayQD date NOT NULL,
    NoiDung nvarchar(500) NOT NULL,
    MucThuong decimal(18,2) NOT NULL CHECK(MucThuong >= 0),
    MaNV varchar(20) NULL, -- Áp dụng cho cá nhân (nếu có)
    MaPB varchar(20) NULL, -- Hoặc áp dụng cho tập thể phòng ban (nếu có)
    CONSTRAINT FK_QDKT_NV FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV),
    CONSTRAINT FK_QDKT_PB FOREIGN KEY(MaPB) REFERENCES PhongBan(MaPB)
);

CREATE TABLE PhieuChiKhenThuong(
    SoPhieuChi varchar(20) NOT NULL PRIMARY KEY,
    NgayChi date NOT NULL,
    SoTien decimal(18,2) NOT NULL CHECK(SoTien > 0),
    SoQDKT varchar(20) NOT NULL,
    CONSTRAINT FK_PCKT_QDKT FOREIGN KEY(SoQDKT) REFERENCES QuyetDinhKhenThuong(SoQDKT)
);

CREATE TABLE QuyetDinhKyLuat(
    SoQDKL varchar(20) NOT NULL PRIMARY KEY,
    NgayQD date NOT NULL,
    NoiDung nvarchar(500) NOT NULL,
    MucPhat decimal(18,2) NOT NULL CHECK(MucPhat >= 0),
    MaNV varchar(20) NULL,
    MaPB varchar(20) NULL,
    CONSTRAINT FK_QDKL_NV FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV),
    CONSTRAINT FK_QDKL_PB FOREIGN KEY(MaPB) REFERENCES PhongBan(MaPB)
);

CREATE TABLE PhieuThuNopPhat(
    SoPhieuThu varchar(20) NOT NULL PRIMARY KEY,
    NgayThu date NOT NULL,
    SoTien decimal(18,2) NOT NULL CHECK(SoTien > 0),
    SoQDKL varchar(20) NOT NULL,
    CONSTRAINT FK_PTNP_QDKL FOREIGN KEY(SoQDKL) REFERENCES QuyetDinhKyLuat(SoQDKL)
);

-- 4. Nhóm Chấm công & Lương
CREATE TABLE KeHoachPhanCong(
    MaKHC varchar(20) NOT NULL PRIMARY KEY,
    Tuan int NOT NULL,
    Nam int NOT NULL,
    MaCa varchar(20) NOT NULL,
    MaNV varchar(20) NOT NULL,
    MaPB varchar(20) NOT NULL,
    CONSTRAINT FK_KHPC_Ca FOREIGN KEY(MaCa) REFERENCES CaLamViec(MaCa),
    CONSTRAINT FK_KHPC_NV FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV),
    CONSTRAINT FK_KHPC_PB FOREIGN KEY(MaPB) REFERENCES PhongBan(MaPB)
);

CREATE TABLE PhieuPhuTroi(
    SoPhieuPT varchar(20) NOT NULL PRIMARY KEY,
    NgayPhuTroi date NOT NULL,
    HinhThuc nvarchar(50) NOT NULL -- Tăng ca, Ngày lễ, Ngày nghỉ
);

CREATE TABLE ChiTietPhuTroi(
    SoPhieuPT varchar(20) NOT NULL,
    MaNV varchar(20) NOT NULL,
    SoGio decimal(5,2) NOT NULL CHECK(SoGio > 0),
    GioQuyDoi decimal(5,2) NOT NULL CHECK(GioQuyDoi >= 0), -- tính bằng 1.5 lần giờ bình thường
    PRIMARY KEY(SoPhieuPT, MaNV),
    CONSTRAINT FK_CTPT_Phieu FOREIGN KEY(SoPhieuPT) REFERENCES PhieuPhuTroi(SoPhieuPT),
    CONSTRAINT FK_CTPT_NV FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV)
);

CREATE TABLE BangChamCong(
    MaBCC varchar(20) NOT NULL PRIMARY KEY,
    Thang int NOT NULL CHECK(Thang BETWEEN 1 AND 12),
    Nam int NOT NULL,
    MaPB varchar(20) NOT NULL,
    CONSTRAINT FK_BCC_PB FOREIGN KEY(MaPB) REFERENCES PhongBan(MaPB)
);

CREATE TABLE ChiTietChamCong(
    MaBCC varchar(20) NOT NULL,
    MaNV varchar(20) NOT NULL,
    SoNgayLamViec decimal(4,1) NOT NULL CHECK(SoNgayLamViec >= 0),
    PRIMARY KEY(MaBCC, MaNV),
    CONSTRAINT FK_CTCC_BCC FOREIGN KEY(MaBCC) REFERENCES BangChamCong(MaBCC),
    CONSTRAINT FK_CTCC_NV FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV)
);

CREATE TABLE BangLuong(
    MaBL varchar(20) NOT NULL PRIMARY KEY,
    Thang int NOT NULL CHECK(Thang BETWEEN 1 AND 12),
    Nam int NOT NULL,
    TongLuong decimal(18,2) NOT NULL CHECK(TongLuong >= 0),
    ThucLanh decimal(18,2) NOT NULL CHECK(ThucLanh >= 0),
    MaNV varchar(20) NOT NULL,
    CONSTRAINT FK_BL_NV FOREIGN KEY(MaNV) REFERENCES NhanVien(MaNV)
);
GO

PRINT N'Đã tạo thành công cơ sở dữ liệu QuanLyNhanSu với 19 bảng chuẩn hóa!';

-- 1. Thêm dữ liệu Danh mục Phòng ban, Chức vụ, Ca làm việc
INSERT INTO PhongBan (MaPB, TenPB) VALUES 
('PB01', N'Phòng Tài chính Kế toán'),
('PB02', N'Phòng Kỹ thuật'),
('PB03', N'Phòng Sản xuất');

INSERT INTO ChucVu (MaCV, TenCV, MucPhuCap) VALUES 
('CV01', N'Nhân viên', 500000),
('CV02', N'Trưởng phòng', 2000000),
('CV03', N'Phó phòng', 1500000);

INSERT INTO CaLamViec (MaCa, TenCa, GioBatDau, GioKetThuc) VALUES 
('CA01', N'Ca Hành chính', '08:00:00', '17:00:00'),
('CA02', N'Ca Sáng', '06:00:00', '14:00:00'),
('CA03', N'Ca Chiều', '14:00:00', '22:00:00');

-- 2. Thêm dữ liệu Nhân viên
INSERT INTO NhanVien (MaNV, HoTen, NgaySinh, DiaChi, MaPB) VALUES 
('NV01', N'Nguyễn Văn An', '1995-05-12', N'123 Lê Lợi, TP.HCM', 'PB01'),
('NV02', N'Trần Thị Bình', '1998-08-20', N'45 Nguyễn Huệ, TP.HCM', 'PB02'),
('NV03', N'Lê Hoàng Cường', '1990-02-15', N'78 CMT8, TP.HCM', 'PB03');

-- 3. Thêm Quyết định tuyển dụng (thời gian thử việc)
INSERT INTO QuyetDinhTuyenDung (SoQDTD, NgayQD, ThoiGianThuViec, MucLuongThuViec, DieuKhoan, MaNV) VALUES 
('QDTD01', '2026-01-01', 2, 6000000, N'Thử việc 2 tháng tại phòng Tài chính Kế toán', 'NV01'),
('QDTD02', '2026-02-01', 2, 7000000, N'Thử việc 2 tháng tại phòng Kỹ thuật', 'NV02'),
('QDTD03', '2026-03-01', 1, 5500000, N'Thử việc 1 tháng tại phòng Sản xuất', 'NV03');

-- 4. Thêm Hợp đồng lao động và Hồ sơ bảo hiểm
INSERT INTO HopDong (SoHD, LoaiHD, NgayKy, NgayHetHan, MaNV) VALUES 
('HD01', N'Dài hạn', '2026-03-01', NULL, 'NV01'),
('HD02', N'Dài hạn', '2026-04-01', NULL, 'NV02'),
('HD03', N'Ngắn hạn', '2026-04-01', '2027-04-01', 'NV03');

INSERT INTO HoSoBaoHiem (MaHS, SoHD, MucDongBHYT, MucDongBHXH) VALUES 
('HSBH01', 'HD01', 180000, 800000),
('HSBH02', 'HD02', 210000, 950000);

-- 5. Thêm Quyết định chức vụ và Quyết định lương chính thức
INSERT INTO QuyetDinhChucVu (SoQDCV, NgayHieuLuc, MaCV, MaNV) VALUES 
('QDCV01', '2026-03-01', 'CV01', 'NV01'),
('QDCV02', '2026-04-01', 'CV02', 'NV02');

INSERT INTO QuyetDinhLuong (SoQDL, NgayHieuLuc, MucLuongCoBan, PhuCapLuong, PhuCapDocHai, MaNV) VALUES 
('QDL01', '2026-03-01', 9000000, 500000, 0, 'NV01'),
('QDL02', '2026-04-01', 12000000, 1000000, 500000, 'NV02'),
('QDL03', '2026-04-01', 8000000, 300000, 800000, 'NV03');

-- 6. Thêm Quyết định khen thưởng, kỷ luật và phiếu thu/chi
INSERT INTO QuyetDinhKhenThuong (SoQDKT, NgayQD, NoiDung, MucThuong, MaNV, MaPB) VALUES 
('QDKT01', '2026-05-10', N'Thưởng hoàn thành xuất sắc nhiệm vụ tháng 4', 1000000, 'NV01', NULL);

INSERT INTO PhieuChiKhenThuong (SoPhieuChi, NgayChi, SoTien, SoQDKT) VALUES 
('PCKT01', '2026-05-12', 1000000, 'QDKT01');

INSERT INTO QuyetDinhKyLuat (SoQDKL, NgayQD, NoiDung, MucPhat, MaNV, MaPB) VALUES 
('QDKL01', '2026-05-15', N'Đi làm muộn quá quy định', 200000, 'NV03', NULL);

INSERT INTO PhieuThuNopPhat (SoPhieuThu, NgayThu, SoTien, SoQDKL) VALUES 
('PTNP01', '2026-05-18', 200000, 'QDKL01');

-- 7. Thêm Kế hoạch phân công, Phụ trội và Chấm công tháng
INSERT INTO KeHoachPhanCong (MaKHC, Tuan, Nam, MaCa, MaNV, MaPB) VALUES 
('KH01', 1, 2026, 'CA01', 'NV01', 'PB01'),
('KH02', 1, 2026, 'CA02', 'NV02', 'PB02');

INSERT INTO PhieuPhuTroi (SoPhieuPT, NgayPhuTroi, HinhThuc) VALUES 
('PPT01', '2026-05-20', N'Tăng ca');

INSERT INTO ChiTietPhuTroi (SoPhieuPT, MaNV, SoGio, GioQuyDoi) VALUES 
('PPT01', 'NV01', 4.0, 6.0); -- 4 giờ nhân hệ số 1.5 = 6 giờ quy đổi

INSERT INTO BangChamCong (MaBCC, Thang, Nam, MaPB) VALUES 
('BCC01', 5, 2026, 'PB01'),
('BCC02', 5, 2026, 'PB02');

INSERT INTO ChiTietChamCong (MaBCC, MaNV, SoNgayLamViec) VALUES 
('BCC01', 'NV01', 22.0),
('BCC02', 'NV02', 22.0);

INSERT INTO BangLuong (MaBL, Thang, Nam, TongLuong, ThucLanh, MaNV) VALUES 
('BL01', 5, 2026, 10500000, 9520000, 'NV01'),
('BL02', 5, 2026, 14500000, 13340000, 'NV02');

GO
PRINT N'Đã chèn dữ liệu mẫu thành công!';