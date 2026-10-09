package services;

public class Model {

    public static class KetQuaXuLy {
        private boolean thanhCong;
        private String thongBao;

        public KetQuaXuLy(boolean thanhCong, String thongBao) {
            this.thanhCong = thanhCong;
            this.thongBao = thongBao;
        }

        public boolean isThanhCong() { return thanhCong; }
        public String getThongBao() { return thongBao; }
    }

    public static class ComboItem {
        private String value;
        private String display;

        public ComboItem(String value, String display) {
            this.value = value;
            this.display = display;
        }

        public String getValue() { return value; }
        public String getDisplay() { return display; }

        @Override
        public String toString() { return display; }
    }

    public static class TourItem {
        private String maTour, tenTour;
        private int soNgay, soDem;
        private double donGiaKhach;

        public TourItem(String maTour, String tenTour, int soNgay, int soDem, double donGiaKhach) {
            this.maTour = maTour;
            this.tenTour = tenTour;
            this.soNgay = soNgay;
            this.soDem = soDem;
            this.donGiaKhach = donGiaKhach;
        }

        public String getMaTour() { return maTour; }
        public String getTenTour() { return tenTour; }
        public int getSoNgay() { return soNgay; }
        public int getSoDem() { return soDem; }
        public double getDonGiaKhach() { return donGiaKhach; }
    }

    public static class ChuyenLeItem {
        private String maChuyen, tenTour, ngayDi, ngayVe, diaDiemDon, trangThai;

        public ChuyenLeItem(String maChuyen, String tenTour, String ngayDi, String ngayVe, String diaDiemDon, String trangThai) {
            this.maChuyen = maChuyen;
            this.tenTour = tenTour;
            this.ngayDi = ngayDi;
            this.ngayVe = ngayVe;
            this.diaDiemDon = diaDiemDon;
            this.trangThai = trangThai;
        }

        public String getMaChuyen() { return maChuyen; }
        public String getTenTour() { return tenTour; }
        public String getNgayDi() { return ngayDi; }
        public String getNgayVe() { return ngayVe; }
        public String getDiaDiemDon() { return diaDiemDon; }
        public String getTrangThai() { return trangThai; }
    }

    public static class DangKyLeItem {
        private String soDKLe, maChuyen, tenNguoiDangKy, dienThoai, trangThai;
        private int soNguoi;
        private double thanhTien;

        public DangKyLeItem(String soDKLe, String maChuyen, String tenNguoiDangKy, String dienThoai, int soNguoi, double thanhTien, String trangThai) {
            this.soDKLe = soDKLe;
            this.maChuyen = maChuyen;
            this.tenNguoiDangKy = tenNguoiDangKy;
            this.dienThoai = dienThoai;
            this.soNguoi = soNguoi;
            this.thanhTien = thanhTien;
            this.trangThai = trangThai;
        }

        public String getSoDKLe() { return soDKLe; }
        public String getMaChuyen() { return maChuyen; }
        public String getTenNguoiDangKy() { return tenNguoiDangKy; }
        public String getDienThoai() { return dienThoai; }
        public int getSoNguoi() { return soNguoi; }
        public double getThanhTien() { return thanhTien; }
        public String getTrangThai() { return trangThai; }
    }

    public static class DangKyDoanItem {
        private String soDKDoan, tenCoQuan, tenTour, ngayDi, ngayKetThuc, trangThai;
        private int soNguoi;
        private double tienCoc, tongTienDuKien;

        public DangKyDoanItem(String soDKDoan, String tenCoQuan, String tenTour, String ngayDi, String ngayKetThuc, int soNguoi, double tienCoc, double tongTienDuKien, String trangThai) {
            this.soDKDoan = soDKDoan;
            this.tenCoQuan = tenCoQuan;
            this.tenTour = tenTour;
            this.ngayDi = ngayDi;
            this.ngayKetThuc = ngayKetThuc;
            this.soNguoi = soNguoi;
            this.tienCoc = tienCoc;
            this.tongTienDuKien = tongTienDuKien;
            this.trangThai = trangThai;
        }

        public String getSoDKDoan() { return soDKDoan; }
        public String getTenCoQuan() { return tenCoQuan; }
        public String getTenTour() { return tenTour; }
        public String getNgayDi() { return ngayDi; }
        public String getNgayKetThuc() { return ngayKetThuc; }
        public int getSoNguoi() { return soNguoi; }
        public double getTienCoc() { return tienCoc; }
        public double getTongTienDuKien() { return tongTienDuKien; }
        public String getTrangThai() { return trangThai; }
    }
}