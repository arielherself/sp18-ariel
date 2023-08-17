public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        int n = word.length();
        Deque<Character> result = new ArrayDeque<>();
        for (int i = 0; i < n; ++i) {
            result.addLast(word.charAt(i));
        }
        return result;
    }
}
