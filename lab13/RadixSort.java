/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        asciis = asciis.clone();

        int maxLength = 0;
        for (String s : asciis) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }

        for (int i = maxLength - 1; i >= 0; --i) {
            sortHelperLSD(asciis, i);
        }

        return asciis;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int[] start = new int[256], count = new int[256];
        String[] result = new String[asciis.length];
        for (String a : asciis) {
            ++(count[charAt(a, index)]);
        }
        start[0] = 0;
        for (int j = 1; j < 256; ++j) {
            start[j] = start[j - 1] + count[j - 1];
        }
        for (String a : asciis) {
            result[start[charAt(a, index)]++] = a;
        }
        System.arraycopy(result, 0, asciis, 0, asciis.length);
    }

    private static char charAt(String s, int i) {
        if (s.length() <= i) {
            return 0;
        } else {
            return s.charAt(i);
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] a = {"Hello", "I'm", "Ariel"};
        a = sort(a);
        for (String s : a) {
            System.out.print(s);
            System.out.print(" ");
        }
        System.out.println();
    }
}
