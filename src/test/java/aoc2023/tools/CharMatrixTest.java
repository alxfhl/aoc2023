package aoc2023.tools;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CharMatrixTest {

    @Test
    public void initializeAndRead() {
        CharMatrix matrix = CharMatrix.valueOf(List.of("123", "abc"));
        assertThat(matrix.getWidth()).isEqualTo(3);
        assertThat(matrix.getHeight()).isEqualTo(2);
        assertThat(matrix.get(0, 0)).isEqualTo('1');
        assertThat(matrix.get(1, 0)).isEqualTo('2');
        assertThat(matrix.get(2, 0)).isEqualTo('3');
        assertThat(matrix.get(0, 1)).isEqualTo('a');
        assertThat(matrix.get(1, 1)).isEqualTo('b');
        assertThat(matrix.get(2, 1)).isEqualTo('c');
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(-1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(3, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(0, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> matrix.get(2, 2));
    }

    @Test
    public void copyAndTranspose() {
        CharMatrix matrix = CharMatrix.valueOf(List.of("123", "abc"));
        matrix = matrix.transposed();
        CharMatrix copy = new CharMatrix(matrix);

        assertThat(copy.getWidth()).isEqualTo(2);
        assertThat(copy.getHeight()).isEqualTo(3);
        assertThat(copy.get(0, 0)).isEqualTo('1');
        assertThat(copy.get(1, 0)).isEqualTo('a');
        assertThat(copy.get(0, 1)).isEqualTo('2');
        assertThat(copy.get(1, 1)).isEqualTo('b');
        assertThat(copy.get(0, 2)).isEqualTo('3');
        assertThat(copy.get(1, 2)).isEqualTo('c');

        copy.set(0, 0, 'x');
        assertThat(copy.get(0, 0)).isEqualTo('x');
        assertThat(matrix.get(0, 0)).isEqualTo('1');
    }

    @Test
    public void getRowOrColumn() {
        CharMatrix matrix = CharMatrix.valueOf(List.of("123", "abc"));

        assertThat(matrix.getRow(0)).isEqualTo(new char[]{'1', '2', '3'});
        assertThat(matrix.getRow(1)).isEqualTo(new char[]{'a', 'b', 'c'});

        assertThat(matrix.getColumn(0)).isEqualTo(new char[]{'1', 'a'});
        assertThat(matrix.getColumn(1)).isEqualTo(new char[]{'2', 'b'});
        assertThat(matrix.getColumn(2)).isEqualTo(new char[]{'3', 'c'});

        matrix = matrix.transposed();
        assertThat(matrix.getRow(0)).isEqualTo(new char[]{'1', 'a'});
        assertThat(matrix.getColumn(0)).isEqualTo(new char[]{'1', '2', '3'});
    }
}