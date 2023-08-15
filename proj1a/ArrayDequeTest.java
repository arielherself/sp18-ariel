import org.junit.Test;

import java.lang.reflect.Array;

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
}
