package Buoi2.BT1;
public abstract class CHinhVe {
    protected int MaLoaiHinh;

    public CHinhVe(int maLoaiHinh) {
        this.MaLoaiHinh = maLoaiHinh;
    }

    public abstract float DienTich();
    public abstract float ChuVi();
    public abstract void Ve();
}