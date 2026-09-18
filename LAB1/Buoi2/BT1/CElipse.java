package Buoi2.BT1;
public class CElipse extends CHinhVe {
    protected float floatA, floatB; 

    public CElipse(int maLoaiHinhVe, float a, float b) {
        super(maLoaiHinhVe);
        this.floatA = a;
        this.floatB = b;
    }

    @Override
    public float DienTich() {
        return (float) (Math.PI * floatA * floatB);
    }

    @Override
    public float ChuVi() {
        return (float) (2 * Math.PI * Math.sqrt((floatA * floatA + floatB * floatB) / 2));
    }

    @Override
    public void Ve() {
        System.out.println("Vẽ Ellipse");
    }
}