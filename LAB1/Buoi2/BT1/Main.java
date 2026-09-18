public class Main {
    public static void main(String[] args) {
        ChinhVe tamGiac = new CTamGiac(1, 3, 4, 5);
        ChinhVe tuGiac = new CTuGiac(2, 4, 5, 6, 7);
        ChinhVe elipse = new CElipse(3, 5, 3);

        System.out.println("--- KIỂM TRA HÌNH TAM GIÁC ---");
        tamGiac.Ve();
        System.out.println("Chu vi: " + tamGiac.ChuVi());
        System.out.println("Diện tích: " + tamGiac.DienTich());

        System.out.println("\n--- KIỂM TRA HÌNH TỨ GIÁC ---");
        tuGiac.Ve();
        System.out.println("Chu vi: " + tuGiac.ChuVi());
        System.out.println("Diện tích: " + tuGiac.DienTich());

        System.out.println("\n--- KIỂM TRA HÌNH ELLIPSE ---");
        elipse.Ve();
        System.out.println("Chu vi: " + elipse.ChuVi());
        System.out.println("Diện tích: " + elipse.DienTich());
    }
}
