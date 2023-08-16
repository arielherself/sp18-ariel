public class LinkedListDeque<T> {
    private class ListNode {
        private T value;
        private ListNode prev;
        private ListNode next;

        public ListNode(T v, ListNode p, ListNode n) {
            value = v;
            prev = p;
            next = n;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T n) {
            value = n;
        }

        public ListNode getPrev() {
            return prev;
        }

        public void setPrev(ListNode n) {
            prev = n;
        }

        public ListNode getNext() {
            return next;
        }

        public void setNext(ListNode n) {
            next = n;
        }
    }

    private final ListNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new ListNode(null, null, null);
        sentinel.setPrev(sentinel);
        sentinel.setNext(sentinel);
        size = 0;
    }

    public void addFirst(T item) {
        sentinel.setNext(new ListNode(item, sentinel, sentinel.getNext()));
        sentinel.next.next.prev = sentinel.next;
        ++size;
    }

    public void addLast(T item) {
        sentinel.setPrev(new ListNode(item, sentinel.getPrev(), sentinel));
        sentinel.getPrev().getPrev().setNext(sentinel.getPrev());
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
        StringBuilder sb = new StringBuilder(String.valueOf(sentinel.getNext().getValue()));
        ListNode ref = sentinel;
        for (int i = 1; i < size; ++i) {
            ref = ref.getNext();
            sb.append(" ").append(ref.getValue());
        }
        System.out.println(sb);
    }

    public T removeFirst() {
        T result = sentinel.getNext().getValue();
        sentinel.getNext().getNext().setPrev(sentinel);
        sentinel.setNext(sentinel.getNext().getNext());
        if (size > 0) {
            --size;
        }
        return result;
    }

    public T removeLast() {
        T result = sentinel.getPrev().getValue();
        sentinel.getPrev().getPrev().setNext(sentinel);
        sentinel.setPrev(sentinel.getPrev().getPrev());
        if (size > 0) {
            --size;
        }
        return result;
    }

    public T get(int index) {
        if (size == 0) {
            return null;
        }
        ListNode ref = sentinel;
        for (int i = 0; i <= index; ++i) {
            ref = ref.getNext();
        }
        return ref.getValue();
    }

    private T getRecursiveCore(ListNode node, int count) {
        if (count == 0) {
            return node.getValue();
        } else {
            return getRecursiveCore(node.getNext(), count - 1);
        }
    }

    public T getRecursive(int index) {
        if (size == 0) {
            return null;
        }
        return getRecursiveCore(sentinel, index + 1);
    }
}
