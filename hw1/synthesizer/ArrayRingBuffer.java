package synthesizer;

import java.util.Iterator;
import java.util.Objects;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    private static class ArrayRingBufferIterator<T> implements Iterator<T> {
        private final T[] data;
        private final int size, first, last;
        private int currentPosition;
        public ArrayRingBufferIterator(T[] array, int size, int first, int last) {
            data = array;
            this.size = size;
            this.first = first;
            this.last = last;
            currentPosition = first;
        }

        public boolean hasNext() {
            return currentPosition == last;
        }

        public T next() {
            if (!hasNext()) {
                throw new ArrayIndexOutOfBoundsException("StopIteration");
            }
            currentPosition = (currentPosition + 1) % size;
            return data[currentPosition];
        }
    }

    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private final T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (fillCount == capacity) {
            throw new RuntimeException("Ring Buffer Overflow");
        }
        ++fillCount;
        if (rb[first] == null) {
            rb[first] = x;
        } else {
            last = (last + 1) % capacity;
            rb[last] = x;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        --fillCount;
        T result = rb[first];
        first = (first + 1) % capacity;
        return result;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (fillCount == 0) {
            throw new RuntimeException("Trying to peek an empty array");
        }
        return rb[first];
    }

    public int index(T toFind) {
        for (int i = 0; i < fillCount; ++i) {
            if (Objects.equals(rb[(first + i) % capacity], toFind)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(T toFind) {
        return !(index(toFind) == -1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName() + "[");
        for (int i = 0; i < fillCount; ++i) {
            sb.append(rb[(first + i) % capacity])
                    .append(i == fillCount - 1 ? "]" : ", ");
        }
        return sb.toString();
    }

    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator<>(rb, capacity, first, last);
    }
}
