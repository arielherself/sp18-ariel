public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        int n = word.length();
        Deque<Character> result = new ArrayDeque<>();
        for (int i = 0; i < n; ++i) {
            result.addLast(word.charAt(i));
        }
        return result;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        Character a = deque.removeFirst(), b = deque.removeLast();
        while (a != null && b != null) {
            if (a != b) {
                return false;
            } else {
                a = deque.removeFirst();
                b = deque.removeLast();
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        Character a = deque.removeFirst(), b = deque.removeLast();
        while (a != null && b != null) {
            if (!cc.equalChars(a, b)) {
                return false;
            } else {
                a = deque.removeFirst();
                b = deque.removeLast();
            }
        }
        return true;
    }
}
