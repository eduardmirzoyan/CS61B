public class Main {
    public static void main(String[] args) {
        System.out.println((byte) (-118 >> 1));
        System.out.println((byte) (-118 >>> 1));
    }

    public static int lastBit(int x) {
        return x ^ (-x & 1);
    }

    public static boolean powerOfTwo(int x) {
        return x != 0 && (x ^ (x - 1)) == 0;
    }

    public static int absolute(int x) {
        int mask1 = x >> 31;
        int mask2 = -1 >>> 1;
        return ((x + mask2) ^ mask2);
    }
}
