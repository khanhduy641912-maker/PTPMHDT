package quanlythuvien.models;

import java.util.Date;

public class Models {
    
    public static class NhanVien {
        public String maNhanVien;
        public String ho;
        public String ten;
        public String phai;
        public Date ngaySinh;
        public String chucVu;
        public String soDienThoai;

        public NhanVien() {}
        public NhanVien(String ma, String ho, String ten, String phai, Date ngaySinh, String chucVu, String sdt) {
            this.maNhanVien = ma;
            this.ho = ho;
            this.ten = ten;
            this.phai = phai;
            this.ngaySinh = ngaySinh;
            this.chucVu = chucVu;
            this.soDienThoai = sdt;
        }
    }

    public static class TheLoai {
        public String maTheLoai;
        public String tenTheLoai;

        public TheLoai() {}
        public TheLoai(String ma, String ten) {
            this.maTheLoai = ma;
            this.tenTheLoai = ten;
        }
    }

    public static class NhaXuatBan {
        public String maNhaXuatBan;
        public String diaChi;
        public String soDienThoai;

        public NhaXuatBan() {}
        public NhaXuatBan(String ma, String diaChi, String sdt) {
            this.maNhaXuatBan = ma;
            this.diaChi = diaChi;
            this.soDienThoai = sdt;
        }
    }

    public static class DauSach {
    public String maDauSach;
    public String tenSach;
    public int namXuatBan;
    public int soLuongHienCo;
    public String maTheLoai;
    public String maNhaXuatBan;

    public DauSach() {}
        public DauSach(String ma, String ten, int nam, int sl, String tl, String nxb) {
            this.maDauSach = ma;
            this.tenSach = ten;
            this.namXuatBan = nam;
            this.soLuongHienCo = sl;
            this.maTheLoai = tl;
            this.maNhaXuatBan = nxb;
        }
    }

    public static class DocGia {
    public String maDocGia;
    public String ho;
    public String ten;
    public Date ngaySinh;
    public String phai;
    public String soDienThoai;
    public String diaChi;
    public String email;
    public String anh3x4;

    public DocGia() {}
        public DocGia(String ma, String ho, String ten, Date ngaySinh, String phai, String sdt, String diaChi, String email, String anh) {
            this.maDocGia = ma;
            this.ho = ho;
            this.ten = ten;
            this.ngaySinh = ngaySinh;
            this.phai = phai;
            this.soDienThoai = sdt;
            this.diaChi = diaChi;
            this.email = email;
            this.anh3x4 = anh;
        }
    }
}