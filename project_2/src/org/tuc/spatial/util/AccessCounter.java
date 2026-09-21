package org.tuc.spatial.util;

public class AccessCounter {

    private static int count = 0;

    public static void reset() {
        count = 0;
    }

    public static void increment() {
        count++;
    }

    public static void add(int n) {
        count += n;
    }

    public static int get() {
        return count;
    }
}
