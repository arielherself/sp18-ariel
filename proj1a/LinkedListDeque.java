public class LinkedListDeque<T> {
    private class ListNode {
        public T value;
        public ListNode prev;
        public ListNode next;

        public ListNode(T v, ListNode p, ListNode n) {
            value = v;
            prev = p;
            next = n;
        }
    }

    private final ListNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new ListNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        sentinel.next = new ListNode(item, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        ++size;
    }

    public void addLast(T item) {
        sentinel.prev = new ListNode(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
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
        StringBuilder sb = new StringBuilder(String.valueOf(sentinel.next.value));
        ListNode ref = sentinel;
        for (int i = 1; i < size; ++i) {
            ref = ref.next;
            sb.append(" ").append(ref.value);
        }
        System.out.println(sb);
    }

    public T removeFirst() {
        T result = sentinel.next.value;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        if (size > 0) {
            --size;
        }
        return result;
    }

    public T removeLast() {
        T result = sentinel.prev.value;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        if (size > 0) {
            --size;
        }
        return result;
    }

    public T get(int index) {
        /* TODO: edge case */
        ListNode ref = sentinel;
        for (int i = 0; i <= index; ++i) {
            ref = ref.next;
        }
        return ref.value;
    }

    private T getRecursiveCore(ListNode node, int count) {
        if (count == 0) {
            return node.value;
        } else {
            return getRecursiveCore(node.next, count - 1);
        }
    }

    public T getRecursive(int index) {
        /* TODO: edge case */
        return getRecursiveCore(sentinel, index + 1);
    }
}
