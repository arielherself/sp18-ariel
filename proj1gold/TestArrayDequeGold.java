import org.junit.Test;

import static org.junit.Assert.assertEquals;

record funcCallbackRecord(String functionName, int argument) {
    @Override
    public String toString() {
        if (argument == -1) {
            return String.format("%s()", functionName);
        } else {
            return String.format("%s(%d)", functionName, argument);
        }
    }
}

class Stack<T> {
    private record ListNode<T> (T item, ListNode<T> prev) {}

    ListNode<T> last;

    public void push(T i) {
        last = new ListNode<>(i, last);
    }

    public T pop() {
        T result = last.item();
        last = last.prev();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListNode<T> ref = last;
        while (ref != null) {
            sb.append(ref.item()).append("\n");
            ref = ref.prev();
        }
        return sb.toString();
    }
}

public class TestArrayDequeGold {
    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> toTest = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> provenCorrect = new ArrayDequeSolution<>();

        final int rounds = 1000;
        Stack<funcCallbackRecord> history = new Stack<>();
        int choice, argument, dequeLen = 0;
        for (int i = 0; i < rounds; ++i) {
            argument = StdRandom.uniform(10000);
            if (dequeLen > 0) {
                choice = StdRandom.uniform(4);
                switch (choice) {
                    case 0 -> {
                        history.push(new funcCallbackRecord("addFirst", argument));
                        toTest.addFirst(argument);
                        provenCorrect.addFirst(argument);
                        ++dequeLen;
                    }
                    case 1 -> {
                        history.push(new funcCallbackRecord("addLast", argument));
                        toTest.addLast(argument);
                        provenCorrect.addLast(argument);
                        ++dequeLen;
                    }
                    case 2 -> {
                        var a = provenCorrect.removeFirst();
                        history.push(new funcCallbackRecord("removeFirst", -1));
                        var b = toTest.removeFirst();
                        assertEquals(history.toString(), a, b);
                        --dequeLen;
                    }
                    case 3 -> {
                        var a = provenCorrect.removeLast();
                        history.push(new funcCallbackRecord("removeLast", -1));
                        var b = toTest.removeLast();
                        assertEquals(history.toString(), a, b);
                        --dequeLen;
                    }
                    default -> {
                    }
                }
            } else {
                choice = Boolean.compare(StdRandom.bernoulli(), false);
                switch (choice) {
                    case 0 -> {
                        history.push(new funcCallbackRecord("addFirst", argument));
                        toTest.addFirst(argument);
                        provenCorrect.addFirst(argument);
                        ++dequeLen;
                    }
                    case 1 -> {
                        history.push(new funcCallbackRecord("addLast", argument));
                        toTest.addLast(argument);
                        provenCorrect.addLast(argument);
                        ++dequeLen;
                    }
                    default -> {
                    }
                }
            }

        }
    }
}
