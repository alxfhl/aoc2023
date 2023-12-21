package aoc2023.tools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RangeTest {

    @Test
    void rangeCreation() {
        assertThat(new Range(0, 2)).isNotNull();
        assertThat(new Range(42, 42)).isNotNull();
        assertThrows(IllegalArgumentException.class, () -> new Range(3, 1));
    }

    @Test
    void plus() {
        assertThat(new Range(0, 2).plus(5)).isEqualTo(new Range(5, 7));
    }

    @Test
    void containsWorks() {
        Range emptyRange = new Range(42, 42);
        assertThat(emptyRange.contains(0)).isFalse();
        assertThat(emptyRange.contains(41)).isFalse();
        assertThat(emptyRange.contains(42)).isFalse();
        assertThat(emptyRange.contains(43)).isFalse();

        Range range = new Range(4, 6);
        assertThat(range.contains(3)).isFalse();
        assertThat(range.contains(4)).isTrue();
        assertThat(range.contains(5)).isTrue();
        assertThat(range.contains(6)).isFalse();
    }

    @Test
    void and() {
        // overlap
        Range range1 = new Range(0, 10);
        Range range2 = new Range(5, 15);
        assertThat(range1.and(range2)).isEqualTo(new Range(5, 10));
        assertThat(range2.and(range1)).isEqualTo(new Range(5, 10));

        // no overlap
        Range range3 = new Range(10, 20);
        assertThat(range1.and(range3)).isNull();
        assertThat(range3.and(range1)).isNull();
        Range range4 = new Range(100, 100);
        assertThat(range1.and(range4)).isNull();
        assertThat(range4.and(range1)).isNull();

        // identical
        assertThat(range1.and(range1)).isEqualTo(range1);

        // included
        Range range5 = new Range(0, 20);
        assertThat(range1.and(range5)).isEqualTo(range1);
        assertThat(range2.and(range5)).isEqualTo(range2);
        assertThat(range3.and(range5)).isEqualTo(range3);
        assertThat(range5.and(range1)).isEqualTo(range1);
        assertThat(range5.and(range2)).isEqualTo(range2);
        assertThat(range5.and(range3)).isEqualTo(range3);
    }

    @Test
    void or() {
        // overlap
        Range range1 = new Range(0, 10);
        Range range2 = new Range(5, 15);
        assertThat(range1.or(range2)).containsExactly(new Range(0, 15));
        assertThat(range2.or(range1)).containsExactly(new Range(0, 15));

        // no overlap
        Range range3 = new Range(10, 20);
        assertThat(range1.or(range3)).containsExactly(new Range(0, 20));
        assertThat(range3.or(range1)).containsExactly(new Range(0, 20));
        Range range4 = new Range(100, 100);
        assertThat(range1.or(range4)).containsExactly(range1, range4);
        assertThat(range4.or(range1)).containsExactly(range1, range4);

        // identical
        assertThat(range1.or(range1)).containsExactly(range1);

        // included
        Range range5 = new Range(0, 20);
        assertThat(range1.or(range5)).containsExactly(range5);
        assertThat(range2.or(range5)).containsExactly(range5);
        assertThat(range3.or(range5)).containsExactly(range5);
        assertThat(range5.or(range1)).containsExactly(range5);
        assertThat(range5.or(range2)).containsExactly(range5);
        assertThat(range5.or(range3)).containsExactly(range5);
    }

    @Test
    public void overlaps() {
        Range range1 = new Range(0, 10);
        Range range2 = new Range(5, 15);
        Range range3 = new Range(10, 20);
        assertThat(range1.overlaps(range2)).isTrue();
        assertThat(range2.overlaps(range3)).isTrue();
        assertThat(range1.overlaps(range3)).isFalse();

        assertThat(range2.overlaps(range1)).isTrue();
        assertThat(range3.overlaps(range2)).isTrue();
        assertThat(range3.overlaps(range1)).isFalse();

        Range range4 = new Range(42, 42);
        assertThat(range1.overlaps(range4)).isFalse();
        assertThat(range4.overlaps(range4)).isFalse();
        assertThat(range1.overlaps(range1)).isTrue();

        // no overlap with empty range
        assertThat(new Range(0, 10).overlaps(new Range(5, 5))).isFalse();
        assertThat(new Range(5, 5).overlaps(new Range(0, 10))).isFalse();
    }

    @Test
    public void minus() {
        Range range1 = new Range(0, 10);
        Range range2 = new Range(5, 15);
        Range range3 = new Range(10, 20);
        assertThat(range1.minus(range2)).containsExactly(new Range(0, 5));
        assertThat(range2.minus(range3)).containsExactly(new Range(5, 10));
        assertThat(range1.minus(range3)).containsExactly(range1);

        assertThat(range2.minus(range1)).containsExactly(new Range(10, 15));
        assertThat(range3.minus(range2)).containsExactly(new Range(15, 20));
        assertThat(range3.minus(range1)).containsExactly(range3);

        assertThat(range1.minus(new Range(2, 8))).containsExactly(new Range(0, 2), new Range(8, 10));
        assertThat(new Range(2, 8).minus(range1)).isEmpty();

        assertThat(new Range(2, 8).minus(new Range(5, 5))).containsExactly(new Range(2, 8));

        assertThat(range1.minus(new Range(0, 5))).containsExactly(new Range(5, 10));
        assertThat(range1.minus(new Range(5, 10))).containsExactly(new Range(0, 5));
    }

    @Test
    public void containsRange() {
        Range range = new Range(4, 8);
        assertThat(range.contains(new Range(4, 8))).isTrue();
        assertThat(range.contains(new Range(4, 6))).isTrue();
        assertThat(range.contains(new Range(6, 6))).isTrue();
        assertThat(range.contains(new Range(7, 8))).isTrue();

        assertThat(range.contains(new Range(1, 1))).isFalse();
        assertThat(range.contains(new Range(3, 5))).isFalse();
        assertThat(range.contains(new Range(0, 2))).isFalse();
        assertThat(range.contains(new Range(8, 9))).isFalse();
        assertThat(range.contains(new Range(4, 9))).isFalse();
        assertThat(range.contains(new Range(3, 8))).isFalse();
    }

    @Test
    public void size() {
        assertThat(new Range(4, 8).size()).isEqualTo(4);
        assertThat(new Range(4, 4).size()).isEqualTo(0);
        assertThat(new Range(-3, 5).size()).isEqualTo(8);
    }
}