package synthesizer;

import java.util.Iterator;
import java.util.Objects;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

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
            ++last;
            if (last == capacity) {
                last = 0;
            }
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
        ++first;
        if (first == capacity) {
            first = 0;
        }
        return result;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        return rb[first];
    }

    public int capacity() {
        return capacity;
    }

    public int fillCount() {
        return fillCount;
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

    // TODO: When you get to part 5, implement the needed code to support iteration.
}
