package aoc2023;

import aoc2023.tools.Input;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day24.class);
        System.out.println("part 1: " + getPart1(input, new BigDecimal("200000000000000"), new BigDecimal("400000000000000")));
        System.out.println("part 2: " + getPart2(input));
    }

    record Hailstone(BigInteger px, BigInteger py, BigInteger pz, BigInteger vx, BigInteger vy, BigInteger vz) {

        @Override
        public String toString() {
            return px + ", " + py + ", " + pz + " @ " + vx + ", " + vy + ", " + vz;
        }
    }

    public static long getPart1(List<String> lines, BigDecimal min, BigDecimal max) {
        List<Hailstone> hailstones = parse(lines);
        long count = 0;
        for (int s1 = 0; s1 < hailstones.size() - 1; s1++) {
            for (int s2 = s1 + 1; s2 < hailstones.size(); s2++) {
                Hailstone hs1 = hailstones.get(s1);
                Hailstone hs2 = hailstones.get(s2);
                BigInteger crossProduct = hs1.vx.multiply(hs2.vy).subtract(hs2.vx.multiply(hs1.vy));

                if (crossProduct.compareTo(BigInteger.ZERO) == 0) {
                    // parallel
                    continue;
                }
                BigInteger dividendT1 = hs2.vy.multiply(hs1.px.subtract(hs2.px)).subtract(hs2.vx.multiply(hs1.py.subtract(hs2.py)));
                BigInteger dividentT2 = hs1.vy.multiply(hs2.px.subtract(hs1.px)).subtract(hs1.vx.multiply(hs2.py.subtract(hs1.py)));
                BigDecimal t1 = new BigDecimal(dividendT1).divide(new BigDecimal(crossProduct.negate()), 10, RoundingMode.HALF_UP);
                BigDecimal t2 = new BigDecimal(dividentT2).divide(new BigDecimal(crossProduct), 10, RoundingMode.HALF_UP);
                if (t1.compareTo(BigDecimal.ZERO) < 0 || t2.compareTo(BigDecimal.ZERO) < 0) {
                    // in the past
                    continue;
                }
                BigDecimal cx = new BigDecimal(hs1.px).add(t1.multiply(new BigDecimal(hs1.vx)));
                BigDecimal cy = new BigDecimal(hs1.py).add(t1.multiply(new BigDecimal(hs1.vy)));
                if (cx.compareTo(min) >= 0 && cx.compareTo(max) <= 0 && cy.compareTo(min) >= 0 && cy.compareTo(max) <= 0) {
                    // in test area
                    count++;
                }
            }
        }
        return count;
    }

    public static BigInteger getPart2(List<String> lines) {
        List<Hailstone> hailstones = parse(lines);
        BigInteger finalX = null;
        BigInteger finalY = null;
        BigInteger finalZ = null;
        BigInteger finalVX = null;
        BigInteger finalVY = null;
        BigInteger finalVZ = null;

        for (int vxInt = -1000; vxInt <= 1000; vxInt++) {
            if (vxInt == 0) {
                continue;
            }
            // two-dimensional solution for x and y
            BigInteger vx = BigInteger.valueOf(vxInt);
            for (int vyInt = -1000; vyInt <= 1000; vyInt++) {
                if (vyInt == 0) {
                    continue;
                }
                BigInteger vy = BigInteger.valueOf(vyInt);
                // find x and y for these vx and vy and the first two hailstones
                Hailstone s1 = hailstones.get(0);
                Hailstone s2 = hailstones.get(1);
                BigInteger x = calcX(vx, vy, s1.px, s1.py, s1.vx, s1.vy, s2.px, s2.py, s2.vx, s2.vy);
                if (x == null) {
                    continue;
                }
                BigInteger y = calcY(vx, vy, x, s1.px, s1.py, s1.vx, s1.vy);
                if (y == null) {
                    continue;
                }
                for (int index = 2; index < hailstones.size(); index++) {
                    Hailstone s3 = hailstones.get(index);
                    BigInteger newX = calcX(vx, vy, s1.px, s1.py, s1.vx, s1.vy, s3.px, s3.py, s3.vx, s3.vy);
                    BigInteger newY = calcY(vx, vy, x, s1.px, s1.py, s1.vx, s1.vy);
                    if (x.equals(newX) && y.equals(newY)) {
                        finalX = x;
                        finalVX = vx;
                        finalY = y;
                        finalVY = vy;
                        break;
                    }
                }
            }

            // two-dimensional solution for x and z
            for (int vzInt = -1000; vzInt <= 1000; vzInt++) {
                if (vzInt == 0) {
                    continue;
                }
                BigInteger vz = BigInteger.valueOf(vzInt);
                // find x and z for these vx and vz and the first two hailstones
                Hailstone s1 = hailstones.get(0);
                Hailstone s2 = hailstones.get(1);
                BigInteger x = calcX(vx, vz, s1.px, s1.pz, s1.vx, s1.vz, s2.px, s2.pz, s2.vx, s2.vz);
                if (x == null) {
                    continue;
                }
                BigInteger z = calcY(vx, vz, x, s1.px, s1.pz, s1.vx, s1.vz);
                if (z == null) {
                    continue;
                }
                for (int index = 2; index < hailstones.size(); index++) {
                    Hailstone s3 = hailstones.get(index);
                    BigInteger newX = calcX(vx, vz, s1.px, s1.pz, s1.vx, s1.vz, s3.px, s3.pz, s3.vx, s3.vz);
                    BigInteger newZ = calcY(vx, vz, x, s1.px, s1.pz, s1.vx, s1.vz);
                    if (x.equals(newX) && z.equals(newZ)) {
                        finalZ = z;
                        finalVZ = vz;
                    }
                }
            }
        }
        System.out.println("Thrown from " + finalX + ", " + finalY + ", " + finalZ + " @ " + finalVX + ", " + finalVY + ", " + finalVZ);
        return finalX.add(finalY).add(finalZ);
    }

    private static BigInteger calcY(BigInteger vx, BigInteger vy, BigInteger x, BigInteger s1x, BigInteger s1y, BigInteger v1x, BigInteger v1y) {
        BigInteger dividend = x.subtract(s1x).multiply(vy.subtract(v1y));
        BigInteger divisor = vx.subtract(v1x);
        if (divisor.compareTo(BigInteger.ZERO) != 0 && dividend.remainder(divisor).compareTo(BigInteger.ZERO) == 0) {
            return dividend.divide(divisor).add(s1y);
        }
        return null;
    }

    private static BigInteger calcX(BigInteger vx, BigInteger vy,
                                    BigInteger s1x, BigInteger s1y, BigInteger v1x, BigInteger v1y,
                                    BigInteger s2x, BigInteger s2y, BigInteger v2x, BigInteger v2y) {
        BigInteger a = vy.subtract(v1y).multiply(vx.subtract(v2x));
        BigInteger b = vy.subtract(v2y).multiply(vx.subtract(v1x));
        BigInteger c = s2y.subtract(s1y).multiply(vx.subtract(v1x)).multiply(vx.subtract(v2x));
        BigInteger dividend = c.add(s1x.multiply(a)).subtract(s2x.multiply(b));
        BigInteger divisor = a.subtract(b);
        if (divisor.compareTo(BigInteger.ZERO) != 0 && dividend.remainder(divisor).compareTo(BigInteger.ZERO) == 0) {
            return dividend.divide(divisor);
        }
        return null;
    }

    private static List<Hailstone> parse(List<String> lines) {
        List<Hailstone> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d+), +(\\d+), +(\\d+) +@ +(-?\\d+), +(-?\\d+), +(-?\\d+)");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) {
                System.out.println("error parsing " + line);
            }
            result.add(new Hailstone(new BigInteger(matcher.group(1)),
                    new BigInteger(matcher.group(2)),
                    new BigInteger(matcher.group(3)),
                    new BigInteger(matcher.group(4)),
                    new BigInteger(matcher.group(5)),
                    new BigInteger(matcher.group(6))));
        }
        return result;
    }

}
