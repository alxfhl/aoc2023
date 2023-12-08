package aoc2023.tools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MathUtilsTest {

    @Test
    public void gcd() {
        assertThat(MathUtils.gcd(1, 2)).isEqualTo(1);
        assertThat(MathUtils.gcd(12, 18)).isEqualTo(6);
        assertThat(MathUtils.gcd(100, 60)).isEqualTo(20);
        assertThat(MathUtils.gcd(97, 91)).isEqualTo(1);
    }

    @Test
    public void lcm() {
        assertThat(MathUtils.lcm(1, 2)).isEqualTo(2);
        assertThat(MathUtils.lcm(12, 18)).isEqualTo(36);
        assertThat(MathUtils.lcm(100, 60)).isEqualTo(300);
        assertThat(MathUtils.lcm(97, 91)).isEqualTo(97 * 91);
    }
}