package aoc2023.tools;

import java.math.BigInteger;

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

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        while (b.compareTo(BigInteger.ZERO) != 0) {
            BigInteger t = b;
            b = a.remainder(b);
            a = t;
        }
        return a;
    }

    /** @return least common multiple */
    public static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    public static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.divide(gcd(a, b)).multiply(b);
    }
}
