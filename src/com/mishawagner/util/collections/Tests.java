package com.mishawagner.util.collections;

public class Tests {
    public static void main(String[] args) {
//        for (int i = 0; i < 20; i++) {
//            System.out.println(String.format("%03d or %8h: %d", i, i, msf(i)));
//            System.out.println(String.format("0x%8h", i).replace(" ", "0"));
//        }

        System.out.println("return " + tryTest());

//        System.out.println("Post finally");
    }

    public static int tryTest() {
        try {
            System.out.println("try");
            return getInt(1 / 0);
        } catch (Exception e) {
            System.out.println("catch");
            return getInt(2);
        } finally {
            System.out.println("finally");
            return getInt(3);
        }
    }

    public static int getInt(int n) {
        System.out.println("getInt");
        return n;
    }

    public static int msf(int x) {
        if (x == 0) {
            return 0;
        } else {
            return 1 + msf(x >> 1);
        }
    }
}
