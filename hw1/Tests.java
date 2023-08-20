import org.junit.Test;
import synthesizer.*;

import static org.junit.Assert.*;

public class Tests {
    @Test
    public void testArrayRingBuffer() {
        ArrayRingBuffer<Integer> a = new ArrayRingBuffer<>(10);

        for (int t = 0; t < 20; ++t) {
            a.enqueue(1);
            a.enqueue(2);
            a.enqueue(3);
            assertEquals(3, a.fillCount());
            assertEquals(Integer.valueOf(1), a.dequeue());
            assertEquals(Integer.valueOf(2), a.dequeue());
            assertEquals(1, a.fillCount());

            for (int i = 0; i < 9; ++i) {
                a.enqueue(i);
            }
            int[] expected = {3, 0, 1, 2, 3, 4, 5, 6, 7, 8};
            for (int i = 0; i < 10; ++i) {
                assertEquals(Integer.valueOf(expected[i]), a.dequeue());
            }

            assertEquals(0, a.fillCount());
        }
    }
}
