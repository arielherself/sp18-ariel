import edu.princeton.cs.algs4.Queue;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class TestMergeSort {
    private static <T> void writeQueue(Queue<T> q, T[] s) {
        for (T i : s) {
            q.enqueue(i);
        }
    }

    @Test
    public void testMakeSingleItemQueues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        Queue<Queue<Integer>> expected = new Queue<>();
        for (int i : q) {
            Queue<Integer> t = new Queue<>();
            t.enqueue(i);
            expected.enqueue(t);
        }

        MergeSort ms = new MergeSort();
        Method makeSingleItemQueues = ms.getClass().getDeclaredMethod("makeSingleItemQueues", Queue.class);
        makeSingleItemQueues.setAccessible(true);
        Queue<Queue<Integer>> qs = (Queue<Queue<Integer>>) makeSingleItemQueues.invoke(null, q);
        for (Queue<Integer> qi : qs) {
            assertEquals(expected.dequeue().peek(), qi.peek());
        }
    }

    @Test
    public void testMergeSortedQueues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Queue<Integer> qa = new Queue<>(), qb = new Queue<>(), qc = new Queue<>();
        Integer[] aa = {1, 3, 5, 7, 9}, ab = {2, 4, 6, 8}, ac = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        writeQueue(qa, aa);
        writeQueue(qb, ab);
        writeQueue(qc, ac);

        MergeSort ms = new MergeSort();
        Method mergeSortedQueues = ms.getClass().getDeclaredMethod("mergeSortedQueues", Queue.class, Queue.class);
        mergeSortedQueues.setAccessible(true);
        Queue<Integer> result = (Queue<Integer>) mergeSortedQueues.invoke(null, qa, qb);
        for (int i : result) {
            assertEquals(qc.dequeue(), Integer.valueOf(i));
        }
    }

    @Test
    public void testMergeSort() {
        Queue<Integer> qa = new Queue<>(), qb = new Queue<>();
        Integer[] aa = {1, 9, 8, 7, 8, 2, 5}, ab = {1, 2, 5, 7, 8, 8, 9};
        writeQueue(qa, aa);
        writeQueue(qb, ab);

        Queue<Integer> result = MergeSort.mergeSort(qa);
        for (int i : result) {
            assertEquals(qb.dequeue(), Integer.valueOf(i));
        }
    }
}
