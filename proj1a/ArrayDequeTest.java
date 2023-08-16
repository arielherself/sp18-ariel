import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void testAddLast() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        ad.addLast("Hello");
        ad.addLast("world");
        assertEquals("Hello", ad.get(0));
        assertEquals("world", ad.get(1));
        assertEquals(2, ad.size());
    }

    @Test
    public void testAddFirst() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        ad.addFirst("world");
        ad.addFirst("Hello");
        assertEquals("Hello", ad.get(0));
        assertEquals("world", ad.get(1));
        assertEquals(2, ad.size());
    }

    @Test
    public void testExpand() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        for (int i = 0; i < 100; ++i) {
            ad.addLast("Hello");
        }
        for (int i = 0; i < 100; ++i) {
            assertEquals("Hello", ad.get(i));
        }
        assertEquals(100, ad.size());
    }

    @Test
    public void testExpandLeft() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        for (int i = 0; i < 100; ++i) {
            ad.addFirst("Hello");
        }
        for (int i = 0; i < 100; ++i) {
            assertEquals("Hello", ad.get(i));
        }
        assertEquals(100, ad.size());
    }

    @Test
    public void testRemove() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        ad.addLast("Hello");
        ad.addLast("world");
        ad.addLast("from");
        ad.addLast("Java");
        assertEquals("Java", ad.removeLast());
        assertEquals("Hello", ad.get(0));
        assertEquals(3, ad.size());
    }

    @Test
    public void testRemoveFirst() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        ad.addFirst("Java");
        ad.addFirst("from");
        ad.addFirst("world");
        ad.addFirst("Hello");
        assertEquals("Hello", ad.removeFirst());
        assertEquals("world", ad.get(0));
        assertEquals(3, ad.size());
    }

    @Test
    public void test1() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addLast(0);
        ad.addLast(1);
        assertEquals(Integer.valueOf(1), ad.get(1));
        ad.addFirst(3);
        ad.addLast(4);
        ad.addFirst(5);
        ad.addLast(6);
        ad.addLast(7);
        ad.addFirst(8);
        ad.addLast(9);
        assertEquals(Integer.valueOf(6), ad.get(6));
        ad.addFirst(11);
        ad.addLast(12);
        ad.addLast(13);
        assertEquals(Integer.valueOf(11), ad.removeFirst());
        ad.addFirst(15);
        ad.addLast(16);
        assertEquals(Integer.valueOf(15), ad.removeFirst());
        assertEquals(Integer.valueOf(7), ad.get(7));
    }

    @Test
    public void test2() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 100; ++i) {
            ad.addFirst(i);
        }
        for (int i = 99; i >= 0; --i) {
            assertEquals(Integer.valueOf(i), ad.removeFirst());
        }
        assertTrue(ad.isEmpty());
        for (int i = 99; i >= 0; --i) {
            ad.addFirst(i);
        }
        for (int i = 0; i < 100; ++i) {
            assertEquals(Integer.valueOf(i), ad.removeFirst());
        }
    }
}
