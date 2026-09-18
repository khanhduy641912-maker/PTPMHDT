package Buoi2.BT1;
public class CTamGiac extends CHinhVe {
    protected float CDiemP1, CDiemP2, CDiemP3; 

    public CTamGiac(int maLoaiHinhVe, float p1, float p2, float p3) {
        super(maLoaiHinhVe);
        this.CDiemP1 = p1;
        this.CDiemP2 = p2;
        this.CDiemP3 = p3;
    }

    @Override
    public float ChuVi() {
        return CDiemP1 + CDiemP2 + CDiemP3;
    }

    @Override
    public float DienTich() {
        float p = ChuVi() / 2;
        return (float) Math.sqrt(p * (p - CDiemP1) * (p - CDiemP2) * (p - CDiemP3));
    }

    @Override
    public void Ve() {
        System.out.println("Vẽ Tam Giác");
    }
}