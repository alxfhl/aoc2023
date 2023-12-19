package aoc2023.tools;

import java.util.List;

public record Range(long start /* included */, long end /* excluded */) implements Comparable<Range> {

    public Range {
        if (start > end) {
            throw new IllegalArgumentException("Start of range must be before end, but " + start + " > " + end);
        }
    }

    public Range plus(long shift) {
        return new Range(start + shift, end + shift);
    }

    public boolean contains(long value) {
        return start <= value && value < end;
    }

    public boolean contains(Range range) {
        return start <= range.start && end >= range.end;
    }

    /**
     * @return if there are long values that are contained in both ranges.
     */
    public boolean overlaps(Range range) {
        return start < range.end && range.start < end && range.start < range.end && start < end;
    }

    public Range and(Range range) {
        if (!overlaps(range)) {
            return null;
        }
        if (start == range.start) {
            return end <= range.end ? this : range;
        }
        if (end == range.end) {
            return start >= range.start ? this : range;
        }
        if (start < range.start) {
            return end > range.end ? range : new Range(range.start, end);
        } else {
            return end < range.end ? this : new Range(start, range.end);
        }
    }

    public List<Range> or(Range range) {
        if (!overlaps(range)) {
            if (end == range.start) {
                return List.of(new Range(start, range.end));
            } else if (start == range.end) {
                return List.of(new Range(range.start, end));
            }
            return this.compareTo(range) < 0 ? List.of(this, range) : List.of(range, this);
        }
        if (start == range.start) {
            return List.of(end >= range.end ? this : range);
        }
        if (end == range.end) {
            return List.of(start <= range.start ? this : range);
        }
        if (start < range.start) {
            return List.of(end > range.end ? this : new Range(start, range.end));
        } else {
            return List.of(end < range.end ? range : new Range(range.start, end));
        }
    }

    public List<Range> minus(Range range) {
        if (!overlaps(range)) {
            return List.of(this);
        }
        if (start == range.start) {
            return end <= range.end ? List.of() : List.of(new Range(range.end, end));
        }
        if (end == range.end) {
            return start < range.start ? List.of(new Range(start, range.start)) : List.of();
        }
        if (start <= range.start) {
            if (end <= range.end) {
                return List.of(new Range(start, range.start));
            }
            return List.of(new Range(start, range.start), new Range(range.end, end));
        }
        if (end <= range.end) {
            return List.of();
        }
        return List.of(new Range(range.end, end));
    }

    public long size() {
        return end - start;
    }

    @Override
    public int compareTo(Range o) {
        int c = Long.compare(start, o.start);
        return c != 0 ? c : Long.compare(end, o.end);
    }
}
