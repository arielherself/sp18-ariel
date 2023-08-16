public class ArrayDeque<T> {
    private T[] data;
    private int size, capacity;
    private int firstPos;

    public ArrayDeque() {
        size = 0;
        capacity = 8;
        firstPos = 0;
        data = (T[]) new Object[8];
    }

    private void expandLeft() {
        firstPos = capacity;
        capacity *= 2;
        T[] temp = (T[]) new Object[capacity];
        System.arraycopy(data, 0, temp, firstPos, size);
        data = temp;
    }

    private void expand() {
        capacity *= 2;
        T[] temp = (T[]) new Object[capacity];
        System.arraycopy(data, 0, temp, 0, firstPos + size);
        data = temp;
    }

    private void shrink() {
        assert (double) size / capacity < 0.25;
        while ((double) size / capacity < 0.25) {
            T[] temp = (T[]) new Object[capacity / 2];
            System.arraycopy(data, firstPos / 2, temp, 0, (size + capacity) / 2 - firstPos / 2);
            firstPos -= firstPos / 2;
            capacity /= 2;
            data = temp;
        }
    }

    private void permissiveShrink() {
        if ((double) size / capacity < 0.25 && size > 10) {
            shrink();
        }
    }

    public void addFirst(T item) {
        if (firstPos == 0) {
            expandLeft();
        }
        --firstPos;
        data[firstPos] = item;
        ++size;
    }

    public void addLast(T item) {
        if (firstPos + size >= capacity) {
            expand();
        }
        data[firstPos + size] = item;
        ++size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if (size == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder(String.valueOf(data[0]));
        for (int i = 1; i < size; ++i) {
            sb.append(" ").append(data[i]);
        }
        System.out.println(sb);
    }

    public T removeFirst() {
        /* TODO: edge case */
        if (size == 0) {
            return null;
        }
        T result = data[firstPos];
        ++firstPos;
        --size;
        permissiveShrink();
        return result;
    }

    public T removeLast() {
        /* TODO: edge case */
        if (size == 0) {
            return null;
        }
        T result = data[firstPos + size - 1];
        --size;
        permissiveShrink();
        return result;
    }

    public T get(int index) {
        return data[firstPos + index];
    }


}
