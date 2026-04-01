package utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class A04_ResourceBundle {
    static void main() {
        System.out.println("A04_ResourceBundle");

        ResourceBundle bundle = ResourceBundle.getBundle("messages"); // Default
        System.out.println(bundle.getString("hi"));
        System.out.println(bundle.getString("bye"));
        System.out.println(bundle.getString("good.morning"));
        System.out.println("\n");

        bundle = ResourceBundle.getBundle("messages", Locale.of("en", "US"));
        System.out.println(bundle.getString("hi"));
        System.out.println(bundle.getString("bye"));
        System.out.println(bundle.getString("good.morning"));
    }
}
