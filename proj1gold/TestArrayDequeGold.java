import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Objects;

public class TestArrayDequeGold {
    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> toTest = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> provenCorrect = new ArrayDequeSolution<>();

        final int rounds = 1000;
        int choice, argument, dequeLen = 0;
        for (int i = 0; i < rounds; ++i) {
            argument = StdRandom.uniform(10000);
            if (dequeLen > 0) {
                choice = StdRandom.uniform(4);
                switch (choice) {
                    case 0 -> {
                        toTest.addFirst(argument);
                        provenCorrect.addFirst(argument);
                        ++dequeLen;
                    }
                    case 1 -> {
                        toTest.addLast(argument);
                        provenCorrect.addLast(argument);
                        ++dequeLen;
                    }
                    case 2 -> {
                        assertTrue(Objects.equals(provenCorrect.removeFirst(), toTest.removeFirst()));
                        --dequeLen;
                    }
                    case 3 -> {
                        assertTrue(Objects.equals(provenCorrect.removeLast(), toTest.removeLast()));
                        --dequeLen;
                    }
                    default -> {
                    }
                }
            } else {
                choice = Boolean.compare(StdRandom.bernoulli(), false);
                switch (choice) {
                    case 0 -> {
                        toTest.addFirst(argument);
                        provenCorrect.addFirst(argument);
                        ++dequeLen;
                    }
                    case 1 -> {
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
