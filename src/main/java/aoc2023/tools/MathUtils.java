package aoc2023.tools;

public class MathUtils {

    /** @return greatest common divisor */
    public static long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    /** @return least common multiple */
    public static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }
}
