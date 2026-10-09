/* =====================================================================
   QUẢN LÝ CÔNG TY DU LỊCH VĂN HÓA VIỆT
   Script SQL Server đầy đủ 17 bảng theo đúng Biểu đồ lớp & ERD (Hình P.21)
   ===================================================================== */

IF DB_ID(N'QuanLyCongTyDuLich') IS NULL 
    CREATE DATABASE QuanLyCongTyDuLich;
GO
USE QuanLyCongTyDuLich;
GO

-- Xóa bảng cũ nếu đã tồn tại (theo thứ tự phụ thuộc)
IF OBJECT_ID('KhaoSat','U') IS NOT NULL DROP TABLE KhaoSat;
IF OBJECT_ID('ThanhToanDoan','U') IS NOT NULL DROP TABLE ThanhToanDoan;
IF OBJECT_ID('PhanCongHDV','U') IS NOT NULL DROP TABLE PhanCongHDV;
IF OBJECT_ID('DangKyLe','U') IS NOT NULL DROP TABLE DangKyLe;
IF OBJECT_ID('ThanhVienDoan','U') IS NOT NULL DROP TABLE ThanhVienDoan;
IF OBJECT_ID('DangKyDoan','U') IS NOT NULL DROP TABLE DangKyDoan;
IF OBJECT_ID('DoanKhach','U') IS NOT NULL DROP TABLE DoanKhach;
IF OBJECT_ID('ChuyenLe','U') IS NOT NULL DROP TABLE ChuyenLe;
IF OBJECT_ID('TourDiemThamQuan','U') IS NOT NULL DROP TABLE TourDiemThamQuan;
IF OBJECT_ID('TourPhuongTien','U') IS NOT NULL DROP TABLE TourPhuongTien;
IF OBJECT_ID('TourDiemDung','U') IS NOT NULL DROP TABLE TourDiemDung;
IF OBJECT_ID('HuongDanVien','U') IS NOT NULL DROP TABLE HuongDanVien;
IF OBJECT_ID('DiemBanVe','U') IS NOT NULL DROP TABLE DiemBanVe;
IF OBJECT_ID('DiemThamQuan','U') IS NOT NULL DROP TABLE DiemThamQuan;
IF OBJECT_ID('PhuongTien','U') IS NOT NULL DROP TABLE PhuongTien;
IF OBJECT_ID('Tour','U') IS NOT NULL DROP TABLE Tour;
GO

-- 1. Bảng Tour
CREATE TABLE Tour (
    MaTour VARCHAR(20) NOT NULL PRIMARY KEY,
    TenTour NVARCHAR(180) NOT NULL,
    SoNgay INT NOT NULL CHECK (SoNgay > 0),
    SoDem INT NOT NULL CHECK (SoDem >= 0),
    DonGiaKhach DECIMAL(18,2) NOT NULL CHECK (DonGiaKhach >= 0),
    MoTa NVARCHAR(1000) NULL,
    DangMoBan BIT NOT NULL DEFAULT 1
);

-- 2. Bảng PhuongTien
CREATE TABLE PhuongTien (
    MaPT VARCHAR(20) NOT NULL PRIMARY KEY,
    TenPT NVARCHAR(120) NOT NULL UNIQUE,
    GhiChu NVARCHAR(300) NULL
);

-- 3. Bảng DiemThamQuan
CREATE TABLE DiemThamQuan (
    MaDiemTQ VARCHAR(20) NOT NULL PRIMARY KEY,
    TenDiemTQ NVARCHAR(180) NOT NULL,
    DiaDiem NVARCHAR(250) NOT NULL,
    NoiDung NVARCHAR(1000) NULL,
    YNghia NVARCHAR(1000) NULL
);

-- 4. Bảng DiemBanVe
CREATE TABLE DiemBanVe (
    MaDiemBan VARCHAR(20) NOT NULL PRIMARY KEY,
    TenDiemBan NVARCHAR(150) NOT NULL,
    DiaChi NVARCHAR(250) NOT NULL,
    DienThoai VARCHAR(20) NULL
);

-- 5. Bảng HuongDanVien
CREATE TABLE HuongDanVien (
    MaHDV VARCHAR(20) NOT NULL PRIMARY KEY,
    HoTen NVARCHAR(120) NOT NULL,
    DienThoai VARCHAR(20) NULL,
    LuongCoBan DECIMAL(18,2) NOT NULL CHECK (LuongCoBan >= 0),
    DangLamViec BIT NOT NULL DEFAULT 1
);

-- 6. Bảng TourDiemDung (Nơi dừng chân theo thứ tự)
CREATE TABLE TourDiemDung (
    MaTour VARCHAR(20) NOT NULL,
    ThuTu INT NOT NULL CHECK (ThuTu > 0),
    TenDiemDung NVARCHAR(180) NOT NULL,
    DoiPhuongTien BIT NOT NULL DEFAULT 0,
    CoNoiAn BIT NOT NULL DEFAULT 0,
    CoKhachSan BIT NOT NULL DEFAULT 0,
    HangSaoKhachSan INT NULL CHECK (HangSaoKhachSan BETWEEN 2 AND 5),
    GhiChu NVARCHAR(500) NULL,
    PRIMARY KEY (MaTour, ThuTu),
    CONSTRAINT FK_TDD_Tour FOREIGN KEY (MaTour) REFERENCES Tour(MaTour),
    CONSTRAINT CK_TDD_KhachSan CHECK (
        (CoKhachSan = 0 AND HangSaoKhachSan IS NULL) OR 
        (CoKhachSan = 1 AND HangSaoKhachSan BETWEEN 2 AND 5)
    )
);

-- 7. Bảng TourPhuongTien (Phương tiện theo chặng)
CREATE TABLE TourPhuongTien (
    MaTour VARCHAR(20) NOT NULL,
    ThuTuChang INT NOT NULL CHECK (ThuTuChang > 0),
    MaPT VARCHAR(20) NOT NULL,
    GhiChu NVARCHAR(300) NULL,
    PRIMARY KEY (MaTour, ThuTuChang, MaPT),
    CONSTRAINT FK_TPT_Tour FOREIGN KEY (MaTour) REFERENCES Tour(MaTour),
    CONSTRAINT FK_TPT_PT FOREIGN KEY (MaPT) REFERENCES PhuongTien(MaPT)
);

-- 8. Bảng TourDiemThamQuan (Điểm tham quan của tour)
CREATE TABLE TourDiemThamQuan (
    MaTour VARCHAR(20) NOT NULL,
    MaDiemTQ VARCHAR(20) NOT NULL,
    ThuTu INT NOT NULL CHECK (ThuTu > 0),
    PRIMARY KEY (MaTour, MaDiemTQ),
    CONSTRAINT UQ_TDTQ UNIQUE (MaTour, ThuTu),
    CONSTRAINT FK_TDTQ_Tour FOREIGN KEY (MaTour) REFERENCES Tour(MaTour),
    CONSTRAINT FK_TDTQ_Diem FOREIGN KEY (MaDiemTQ) REFERENCES DiemThamQuan(MaDiemTQ)
);

-- 9. Bảng ChuyenLe (Lịch chuyến cho khách lẻ)
CREATE TABLE ChuyenLe (
    MaChuyen VARCHAR(20) NOT NULL PRIMARY KEY,
    MaTour VARCHAR(20) NOT NULL,
    NgayDi DATE NOT NULL,
    NgayVe DATE NOT NULL,
    DiaDiemDon NVARCHAR(250) NOT NULL,
    TrangThai NVARCHAR(40) NOT NULL DEFAULT N'Mở đăng ký',
    CONSTRAINT CK_Chuyen_TrangThai CHECK (TrangThai IN (N'Mở đăng ký', N'Đóng đăng ký')),
    CONSTRAINT FK_Chuyen_Tour FOREIGN KEY (MaTour) REFERENCES Tour(MaTour),
    CONSTRAINT CK_Chuyen_Ngay CHECK (NgayVe >= NgayDi)
);

-- 10. Bảng DoanKhach (Thông tin cơ quan/đại diện đoàn)
CREATE TABLE DoanKhach (
    MaDoan VARCHAR(20) NOT NULL PRIMARY KEY,
    TenCoQuanDaiDien NVARCHAR(180) NOT NULL,
    DiaChi NVARCHAR(250) NOT NULL,
    DienThoai VARCHAR(20) NOT NULL,
    NguoiDaiDien NVARCHAR(120) NOT NULL
);

-- 11. Bảng DangKyDoan (Phiếu đăng ký đoàn > 12 người)
CREATE TABLE DangKyDoan (
    SoDKDoan VARCHAR(20) NOT NULL PRIMARY KEY,
    MaDoan VARCHAR(20) NOT NULL,
    MaTour VARCHAR(20) NOT NULL,
    NgayDangKy DATETIME2 NOT NULL,
    NgayDi DATE NOT NULL,
    NgayKetThucDuKien DATE NOT NULL,
    SoNguoi INT NOT NULL CHECK (SoNguoi > 12),
    DiaDiemDon NVARCHAR(250) NOT NULL,
    MuaBaoHiem BIT NOT NULL DEFAULT 0,
    TienCoc DECIMAL(18,2) NOT NULL CHECK (TienCoc > 0),
    DaThanhToanCoc BIT NOT NULL DEFAULT 1,
    TongTienDuKien DECIMAL(18,2) NOT NULL CHECK (TongTienDuKien >= 0),
    TrangThai NVARCHAR(40) NOT NULL DEFAULT N'Đã đăng ký',
    CONSTRAINT CK_DKDoan_TrangThai CHECK (TrangThai IN (N'Đã đăng ký', N'Hủy - mất cọc', N'Đã hoàn tất thanh toán')),
    CONSTRAINT CK_DKDoan_Coc CHECK (TienCoc <= TongTienDuKien),
    CONSTRAINT FK_DKDoan_Doan FOREIGN KEY (MaDoan) REFERENCES DoanKhach(MaDoan),
    CONSTRAINT FK_DKDoan_Tour FOREIGN KEY (MaTour) REFERENCES Tour(MaTour),
    CONSTRAINT CK_DKDoan_Ngay CHECK (NgayKetThucDuKien >= NgayDi)
);

-- 12. Bảng ThanhVienDoan (Danh sách người đi khi mua bảo hiểm)
CREATE TABLE ThanhVienDoan (
    SoDKDoan VARCHAR(20) NOT NULL,
    STT INT NOT NULL CHECK (STT > 0),
    HoTen NVARCHAR(120) NOT NULL,
    NgaySinh DATE NULL,
    SoGiayTo NVARCHAR(40) NULL,
    PRIMARY KEY (SoDKDoan, STT),
    CONSTRAINT FK_TVDoan_DK FOREIGN KEY (SoDKDoan) REFERENCES DangKyDoan(SoDKDoan)
);

-- 13. Bảng DangKyLe (Phiếu đăng ký khách lẻ < 12 người)
CREATE TABLE DangKyLe (
    SoDKLe VARCHAR(20) NOT NULL PRIMARY KEY,
    MaChuyen VARCHAR(20) NOT NULL,
    MaDiemBan VARCHAR(20) NOT NULL,
    NgayDangKy DATETIME2 NOT NULL,
    TenNguoiDangKy NVARCHAR(120) NOT NULL,
    DienThoai VARCHAR(20) NOT NULL,
    SoNguoi INT NOT NULL CHECK (SoNguoi BETWEEN 1 AND 11),
    ThanhTien DECIMAL(18,2) NOT NULL CHECK (ThanhTien >= 0),
    DaThanhToan BIT NOT NULL DEFAULT 1 CHECK (DaThanhToan = 1),
    TrangThai NVARCHAR(40) NOT NULL DEFAULT N'Đã đăng ký',
    CONSTRAINT FK_DKLe_Chuyen FOREIGN KEY (MaChuyen) REFERENCES ChuyenLe(MaChuyen),
    CONSTRAINT FK_DKLe_DiemBan FOREIGN KEY (MaDiemBan) REFERENCES DiemBanVe(MaDiemBan)
);

-- 14. Bảng PhanCongHDV (Phân công HDV cho khách lẻ hoặc đoàn)
CREATE TABLE PhanCongHDV (
    MaPC VARCHAR(20) NOT NULL PRIMARY KEY,
    MaHDV VARCHAR(20) NOT NULL,
    LoaiDoiTuong VARCHAR(10) NOT NULL CHECK (LoaiDoiTuong IN ('LE', 'DOAN')),
    MaChuyen VARCHAR(20) NULL,
    SoDKDoan VARCHAR(20) NULL,
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    ThuLaoTour DECIMAL(18,2) NOT NULL CHECK (ThuLaoTour >= 0),
    CONSTRAINT FK_PC_HDV FOREIGN KEY (MaHDV) REFERENCES HuongDanVien(MaHDV),
    CONSTRAINT FK_PC_Chuyen FOREIGN KEY (MaChuyen) REFERENCES ChuyenLe(MaChuyen),
    CONSTRAINT FK_PC_Doan FOREIGN KEY (SoDKDoan) REFERENCES DangKyDoan(SoDKDoan),
    CONSTRAINT CK_PC_Target CHECK (
        (LoaiDoiTuong = 'LE' AND MaChuyen IS NOT NULL AND SoDKDoan IS NULL) OR 
        (LoaiDoiTuong = 'DOAN' AND SoDKDoan IS NOT NULL AND MaChuyen IS NULL)
    ),
    CONSTRAINT CK_PC_Ngay CHECK (NgayKetThuc >= NgayBatDau)
);

-- Mỗi chuyến khách lẻ chỉ có duy nhất 1 HDV
CREATE UNIQUE INDEX UX_PC_ChuyenLe ON PhanCongHDV(MaChuyen) WHERE MaChuyen IS NOT NULL;

-- 15. Bảng ThanhToanDoan (Thanh toán đợt sau tour của khách đoàn)
CREATE TABLE ThanhToanDoan (
    SoTT VARCHAR(20) NOT NULL PRIMARY KEY,
    SoDKDoan VARCHAR(20) NOT NULL,
    NgayThanhToan DATETIME2 NOT NULL,
    SoTien DECIMAL(18,2) NOT NULL CHECK (SoTien > 0),
    GhiChu NVARCHAR(300) NULL,
    CONSTRAINT FK_TTDoan_DK FOREIGN KEY (SoDKDoan) REFERENCES DangKyDoan(SoDKDoan)
);

-- 16. Bảng KhaoSat (Phiếu khảo sát ý kiến khách hàng)
CREATE TABLE KhaoSat (
    MaKhaoSat VARCHAR(20) NOT NULL PRIMARY KEY,
    LoaiKhach VARCHAR(10) NOT NULL CHECK (LoaiKhach IN ('LE', 'DOAN')),
    SoDKLe VARCHAR(20) NULL,
    SoDKDoan VARCHAR(20) NULL,
    NgayGui DATE NOT NULL,
    NgayPhanHoi DATE NULL,
    DiemDanhGia INT NULL CHECK (DiemDanhGia BETWEEN 1 AND 5),
    GopY NVARCHAR(1500) NULL,
    CONSTRAINT FK_KS_Le FOREIGN KEY (SoDKLe) REFERENCES DangKyLe(SoDKLe),
    CONSTRAINT FK_KS_Doan FOREIGN KEY (SoDKDoan) REFERENCES DangKyDoan(SoDKDoan),
    CONSTRAINT CK_KS_PhanHoi CHECK (NgayPhanHoi IS NULL OR NgayPhanHoi >= NgayGui),
    CONSTRAINT CK_KS_Target CHECK (
        (LoaiKhach = 'LE' AND SoDKLe IS NOT NULL AND SoDKDoan IS NULL) OR 
        (LoaiKhach = 'DOAN' AND SoDKDoan IS NOT NULL AND SoDKLe IS NULL)
    )
);

-- Đảm bảo mỗi phiếu đăng ký chỉ nhận tối đa 1 phiếu khảo sát
CREATE UNIQUE INDEX UX_KS_Le ON KhaoSat(SoDKLe) WHERE SoDKLe IS NOT NULL;
CREATE UNIQUE INDEX UX_KS_Doan ON KhaoSat(SoDKDoan) WHERE SoDKDoan IS NOT NULL;

-- 17. Index hỗ trợ truy vấn tính lương HDV nhanh hơn
CREATE INDEX IX_PC_HDV_Ngay ON PhanCongHDV(MaHDV, NgayBatDau, NgayKetThuc);
GO

-- 1. Tour
INSERT INTO Tour(MaTour, TenTour, SoNgay, SoDem, DonGiaKhach, MoTa, DangMoBan) VALUES
('T001', N'Miền Tây 3 ngày 2 đêm', 3, 2, 2500000, N'TP.HCM - Mỹ Tho - Cần Thơ - TP.HCM', 1),
('T002', N'Đà Lạt 4 ngày 3 đêm', 4, 3, 3200000, N'TP.HCM - Đà Lạt - TP.HCM', 1),
('T003', N'Hà Nội - Hạ Long 5 ngày 4 đêm', 5, 4, 8900000, N'TP.HCM - Hà Nội - Hạ Long - TP.HCM', 1);

-- 2. Phương tiện
INSERT INTO PhuongTien(MaPT, TenPT, GhiChu) VALUES 
('PT01', N'Xe du lịch', NULL),
('PT02', N'Máy bay', NULL),
('PT03', N'Tàu hỏa', NULL),
('PT04', N'Tàu thủy', NULL);

-- 3. Điểm bán vé
INSERT INTO DiemBanVe(MaDiemBan, TenDiemBan, DiaChi, DienThoai) VALUES
('DB01', N'Điểm bán Quận 1', N'12 Lê Lợi, Quận 1, TP.HCM', '0281000001'),
('DB02', N'Điểm bán Thủ Đức', N'5 Võ Văn Ngân, TP. Thủ Đức', '0281000002');

-- 4. Hướng dẫn viên
INSERT INTO HuongDanVien(MaHDV, HoTen, DienThoai, LuongCoBan, DangLamViec) VALUES
('HDV01', N'Nguyễn Minh Anh', '0903000001', 9000000, 1),
('HDV02', N'Trần Quốc Bình', '0903000002', 9500000, 1),
('HDV03', N'Lê Thu Cúc', '0903000003', 8500000, 1);

-- 5. Điểm tham quan
INSERT INTO DiemThamQuan(MaDiemTQ, TenDiemTQ, DiaDiem, NoiDung, YNghia) VALUES
('DTQ01', N'Chợ nổi Cái Răng', N'Cần Thơ', N'Tham quan chợ trên sông', N'Nét văn hóa sông nước miền Tây'),
('DTQ02', N'Chùa Vĩnh Tràng', N'Mỹ Tho, Tiền Giang', N'Tham quan kiến trúc chùa', N'Di tích kiến trúc nghệ thuật cấp quốc gia'),
('DTQ03', N'Hồ Xuân Hương', N'Đà Lạt', N'Dạo quanh hồ trung tâm', N'Biểu tượng thành phố Đà Lạt'),
('DTQ04', N'Vịnh Hạ Long', N'Quảng Ninh', N'Du thuyền tham quan vịnh', N'Di sản thiên nhiên thế giới'),
('DTQ05', N'Văn Miếu - Quốc Tử Giám', N'Hà Nội', N'Tham quan di tích', N'Trường đại học đầu tiên của Việt Nam');

-- 6. Điểm dừng (Nơi đến quan trọng & nối tiếp chặng, điểm cuối là TP.HCM)
INSERT INTO TourDiemDung(MaTour, ThuTu, TenDiemDung, DoiPhuongTien, CoNoiAn, CoKhachSan, HangSaoKhachSan, GhiChu) VALUES
('T001', 1, N'Mỹ Tho', 0, 1, 0, NULL, NULL),
('T001', 2, N'Cần Thơ', 0, 1, 1, 3, NULL),
('T001', 3, N'TP.HCM', 0, 0, 0, NULL, N'Kết thúc tour'),
('T003', 1, N'Hà Nội', 1, 1, 1, 4, N'Đổi sang xe du lịch'),
('T003', 2, N'Hạ Long', 1, 1, 1, 5, N'Đi tàu thủy trên vịnh'),
('T003', 3, N'TP.HCM', 0, 0, 0, NULL, N'Kết thúc tour');

-- 7. Phương tiện theo chặng
INSERT INTO TourPhuongTien(MaTour, ThuTuChang, MaPT, GhiChu) VALUES
('T001', 1, 'PT01', NULL),
('T001', 2, 'PT01', NULL),
('T001', 3, 'PT01', NULL),
('T003', 1, 'PT02', N'TP.HCM - Hà Nội'),
('T003', 2, 'PT01', N'Hà Nội - Hạ Long'),
('T003', 2, 'PT04', N'Tham quan vịnh'),
('T003', 3, 'PT02', N'Hà Nội - TP.HCM');

-- 8. Điểm tham quan theo tour
INSERT INTO TourDiemThamQuan(MaTour, MaDiemTQ, ThuTu) VALUES 
('T001', 'DTQ02', 1),
('T001', 'DTQ01', 2),
('T002', 'DTQ03', 1),
('T003', 'DTQ05', 1),
('T003', 'DTQ04', 2);

-- 9. Chuyến khách lẻ (NgayVe = NgayDi + SoNgay - 1)
INSERT INTO ChuyenLe(MaChuyen, MaTour, NgayDi, NgayVe, DiaDiemDon, TrangThai) VALUES
('CL001', 'T001', '20260905', '20260907', N'Nhà Văn hóa Thanh Niên, Quận 1', N'Đóng đăng ký'),
('CL002', 'T001', '20261115', '20261117', N'Nhà Văn hóa Thanh Niên, Quận 1', N'Mở đăng ký'),
('CL003', 'T002', '20261120', '20261123', N'Công viên 23/9, Quận 1', N'Mở đăng ký');

-- 10. Đăng ký khách lẻ
INSERT INTO DangKyLe(SoDKLe, MaChuyen, MaDiemBan, NgayDangKy, TenNguoiDangKy, DienThoai, SoNguoi, ThanhTien, DaThanhToan, TrangThai) VALUES
('DKL001', 'CL001', 'DB01', '20260820 09:00', N'Phạm Văn Long', '0912000001', 2, 5000000, 1, N'Đã đăng ký'),
('DKL002', 'CL002', 'DB02', '20261001 10:00', N'Võ Thị Mai', '0912000002', 3, 7500000, 1, N'Đã đăng ký');

-- 11. Đoàn khách (Cơ quan / Gia đình)
INSERT INTO DoanKhach(MaDoan, TenCoQuanDaiDien, DiaChi, DienThoai, NguoiDaiDien) VALUES
('DK01', N'Công ty CP Phần mềm Sao Việt', N'25 Nguyễn Thị Minh Khai, Quận 3, TP.HCM', '0283900001', N'Lê Văn Hải'),
('DK02', N'Gia đình ông Trần Văn Nam', N'8 Phan Xích Long, Phú Nhuận, TP.HCM', '0909111222', N'Trần Văn Nam');

-- 12. Đăng ký đoàn
INSERT INTO DangKyDoan(SoDKDoan, MaDoan, MaTour, NgayDangKy, NgayDi, NgayKetThucDuKien, SoNguoi, DiaDiemDon, MuaBaoHiem, TienCoc, DaThanhToanCoc, TongTienDuKien, TrangThai) VALUES
('DD001', 'DK01', 'T001', '20260801 08:30', '20260910', '20260912', 20, N'25 Nguyễn Thị Minh Khai, Quận 3', 0, 10000000, 1, 50000000, N'Đã đăng ký'),
('DD002', 'DK02', 'T002', '20260925 14:00', '20261210', '20261213', 15, N'8 Phan Xích Long, Phú Nhuận', 0, 12000000, 1, 48000000, N'Đã đăng ký');

-- 13. Phân công hướng dẫn viên
INSERT INTO PhanCongHDV(MaPC, MaHDV, LoaiDoiTuong, MaChuyen, SoDKDoan, NgayBatDau, NgayKetThuc, ThuLaoTour) VALUES
('PC001', 'HDV01', 'LE', 'CL001', NULL, '20260905', '20260907', 1500000),
('PC002', 'HDV02', 'DOAN', NULL, 'DD001', '20260910', '20260912', 2000000),
('PC003', 'HDV03', 'DOAN', NULL, 'DD001', '20260910', '20260912', 2000000);

-- 14. Khảo sát ý kiến khách hàng
INSERT INTO KhaoSat(MaKhaoSat, LoaiKhach, SoDKLe, SoDKDoan, NgayGui, NgayPhanHoi, DiemDanhGia, GopY) VALUES
('KS001', 'LE', 'DKL001', NULL, '20260908', '20260910', 5, N'Hướng dẫn viên nhiệt tình');