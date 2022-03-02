package pl.alyx.range;

import static org.junit.jupiter.api.Assertions.*;

class RangeTest {

    @org.junit.jupiter.api.Test
    void normalize() {
        String source;
        String target;
        String expect;
        Range range;

        source = " [  :  ] ";
        assertNotNull(Range.match(source));
        assertTrue(Range.match(source).isEmpty());

        source = " [ 1 : 1 ] ";
        expect = "1";
        range = Range.match(source).get(0);
        assertNotNull(range);
        target = range.normalize().toString();
        assertEquals(expect, target);

        source = " ( 0 : 0 ) ";
        expect = "";
        assertNotNull(range);
        range = Range.match(source).get(0);
        target = range.normalize().toString();
        assertEquals(expect, target);

        source = " [ 0 : 0 ) ";
        expect = "";
        assertNotNull(range);
        range = Range.match(source).get(0);
        target = range.normalize().toString();
        assertEquals(expect, target);

        source = " ( 0 : 0 ] ";
        expect = "";
        assertNotNull(range);
        range = Range.match(source).get(0);
        target = range.normalize().toString();
        assertEquals(expect, target);

        source = " ( 5 : 2 ] ";
        expect = "[2:5)";
        assertNotNull(range);
        range = Range.match(source).get(0);
        target = range.normalize().toString();
        assertEquals(expect, target);
    }

    @org.junit.jupiter.api.Test
    void match() {
        String source;
        String target;
        String expect;

        Range range;
        Range.Array array;

        source = " [ 1 : 2 ] ";
        expect = "[1:2]";
        target = Range.match(source).get(0).toString();
        assertEquals(expect, target);

        source = " [  : 2 ] ";
        expect = "[:2]";
        target = Range.match(source).get(0).toString();
        assertEquals(expect, target);

        source = "[1:5] 1 5 ( 2.3 : )  [:-5) -0 -0.1  (1.:3.) (0.0000001:4.00000005] 3.1415926535897932384626";
        array = Range.match(source);
        assertNotNull(array);
    }
}