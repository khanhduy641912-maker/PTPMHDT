package com.quanlynhansu.model;

import java.util.Date;

public class HopDong {
    private String soHD;
    private String loaiHD;
    private Date ngayKy;
    private Date ngayHetHan;
    private String maNV;

    public HopDong() {}

    public HopDong(String soHD, String loaiHD, Date ngayKy, Date ngayHetHan, String maNV) {
        this.soHD = soHD;
        this.loaiHD = loaiHD;
        this.ngayKy = ngayKy;
        this.ngayHetHan = ngayHetHan;
        this.maNV = maNV;
    }

    public String getSoHD() { return soHD; }
    public void setSoHD(String soHD) { this.soHD = soHD; }
    public String getLoaiHD() { return loaiHD; }
    public void setLoaiHD(String loaiHD) { this.loaiHD = loaiHD; }
    public Date getNgayKy() { return ngayKy; }
    public void setNgayKy(Date ngayKy) { this.ngayKy = ngayKy; }
    public Date getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(Date ngayHetHan) { this.ngayHetHan = ngayHetHan; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
}