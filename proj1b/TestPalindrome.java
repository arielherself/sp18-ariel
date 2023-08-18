import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    private void checkIsPalindrome(String s, boolean expected) {
        if (expected) {
            assertTrue(palindrome.isPalindrome(s));
        } else {
            assertFalse(palindrome.isPalindrome(s));
        }
    }

    @Test
    public void testIsPalindrome() {
        checkIsPalindrome("", true);
        checkIsPalindrome("a", true);
        checkIsPalindrome("ab", false);
        checkIsPalindrome("aba", true);
        checkIsPalindrome("Aba", false);
        checkIsPalindrome("noon", true);
        checkIsPalindrome("horse", false);
        checkIsPalindrome("rancor", false);
        checkIsPalindrome("aaaaab", false);
        checkIsPalindrome("racecar", true);
        checkIsPalindrome("Racecar", false);
    }
}