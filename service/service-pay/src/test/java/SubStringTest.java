import java.util.*;

public class SubStringTest {
    public static void test01() {
        String a = "123_456_789";

        String substring = a.substring(a.lastIndexOf("_") + 1);
        System.out.println("substring = " + substring);
    }


    public static void test02() {
        // List<Integer> a = new ArrayList<>();
        // a.add(6);
        // a.add(6);
        // a.add(6);
        // a.add(6);
        // a.add(6);
        // a.forEach(o -> {
        //     a.add(5);
        // });
        Map<String,String> map = new HashMap<>();
        map.put("Aa","aaaaa");
        map.put("Bb","bbbbb");
        Collection<String> values = map.values();
        System.out.println(values);



    }

    public static void main(String[] args) {
        test02();
    }
}
