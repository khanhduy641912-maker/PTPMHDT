package Buoi2.BT1;
public class CTuGiac extends CHinhVe {
    protected float CDiemP1, CDiemP2, CDiemP3, CDiemP4; 

    public CTuGiac(int maLoaiHinhVe, float p1, float p2, float p3, float p4) {
        super(maLoaiHinhVe);
        this.CDiemP1 = p1;
        this.CDiemP2 = p2;
        this.CDiemP3 = p3;
        this.CDiemP4 = p4;
    }

    @Override
    public float ChuVi() {
        return CDiemP1 + CDiemP2 + CDiemP3 + CDiemP4;
    }

    @Override
    public float DienTich() {
       float p = ChuVi() / 2; 
        return (float) Math.sqrt((p - CDiemP1) * (p - CDiemP2) * (p - CDiemP3) * (p - CDiemP4));
    }

    @Override
    public void Ve() {
        System.out.println("Vẽ Tứ Giác");
    }
}