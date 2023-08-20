package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    /* The number of stored elements in the array. */
    protected int fillCount;
    /* The maximum of fillCount. */
    protected int capacity;
}
