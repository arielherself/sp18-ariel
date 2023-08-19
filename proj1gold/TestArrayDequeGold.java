import org.junit.Test;

import static org.junit.Assert.assertEquals;

class FuncCallbackRecord {
    private final String functionName;
    private final int argument;

    public FuncCallbackRecord(String functionName, int argument) {
        this.functionName = functionName;
        this.argument = argument;
    }

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
    private static class ListNode<T>  {
        public T item;
        public ListNode<T> prev;

        public ListNode(T item, ListNode<T> prev) {
            this.item = item;
            this.prev = prev;
        }
    }

    ListNode<T> last;

    public void push(T i) {
        last = new ListNode<>(i, last);
    }

    public T pop() {
        T result = last.item;
        last = last.prev;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListNode<T> ref = last;
        while (ref != null) {
            sb.append(ref.item).append("\n");
            ref = ref.prev;
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
        Stack<FuncCallbackRecord> history = new Stack<>();
        int choice, argument, dequeLen = 0;
        Integer a, b;
        for (int i = 0; i < rounds; ++i) {
            argument = StdRandom.uniform(10000);
            if (dequeLen > 0) {
                choice = StdRandom.uniform(4);
                switch (choice) {
                    case 0:
                        history.push(new FuncCallbackRecord("addFirst", argument));
                        toTest.addFirst(argument);
                        provenCorrect.addFirst(argument);
                        ++dequeLen;
                        break;
                    case 1:
                        history.push(new FuncCallbackRecord("addLast", argument));
                        toTest.addLast(argument);
                        provenCorrect.addLast(argument);
                        ++dequeLen;
                        break;
                    case 2:
                        a = provenCorrect.removeFirst();
                        history.push(new FuncCallbackRecord("removeFirst", -1));
                        b = toTest.removeFirst();
                        assertEquals(history.toString(), a, b);
                        --dequeLen;
                        break;
                    case 3:
                        a = provenCorrect.removeLast();
                        history.push(new FuncCallbackRecord("removeLast", -1));
                        b = toTest.removeLast();
                        assertEquals(history.toString(), a, b);
                        --dequeLen;
                        break;
                    default:
                }
            } else {
                choice = Boolean.compare(StdRandom.bernoulli(), false);
                switch (choice) {
                    case 0:
                        history.push(new FuncCallbackRecord("addFirst", argument));
                        toTest.addFirst(argument);
                        provenCorrect.addFirst(argument);
                        ++dequeLen;
                        break;
                    case 1:
                        history.push(new FuncCallbackRecord("addLast", argument));
                        toTest.addLast(argument);
                        provenCorrect.addLast(argument);
                        ++dequeLen;
                        break;
                    default:
                }
            }
            assertEquals(history.toString(), provenCorrect.size(), toTest.size());
        }
    }
}
