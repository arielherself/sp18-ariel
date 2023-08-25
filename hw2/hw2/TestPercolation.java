package hw2;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestPercolation {
    @Test
    public void testPercolates() {
        Percolation instance = new Percolation(8);
        final int[] openSequenceX = {0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 5, 6, 6, 6, 6, 7};
        final int[] openSequenceY = {2, 3, 4, 3, 4, 5, 6, 7, 5, 6, 5, 6, 7, 5, 6, 6, 7, 4, 5, 6, 7, 5};
        for (int i = 0; i < openSequenceX.length; ++i) {
            instance.open(openSequenceX[i], openSequenceY[i]);
        }
        assertTrue(instance.percolates());
    }

    @Test
    public void testNotPercolates() {
        Percolation instance = new Percolation(8);
        final int[] openSequenceX = {0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4};
        final int[] openSequenceY = {2, 3, 5, 0, 1, 2, 3, 4, 0, 1, 4, 5, 2, 3, 4, 5, 6, 6, 7};
        for (int i = 0; i < openSequenceX.length; ++i) {
            instance.open(openSequenceX[i], openSequenceY[i]);
        }
        assertFalse(instance.percolates());
    }

    @Test
    public void testEdgeCase() {
        Percolation instance = new Percolation(1);
        assertFalse(instance.percolates());
        instance.open(0, 0);
        assertTrue(instance.percolates());
    }
}
