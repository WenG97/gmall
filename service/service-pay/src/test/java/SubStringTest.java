
public class SubStringTest {
    public static void test01(){
        String a = "123_456_789";

        String substring = a.substring(a.lastIndexOf("_") + 1);
        System.out.println("substring = " + substring);
    }

    public static void main(String[] args) {
        test01();
    }
}
